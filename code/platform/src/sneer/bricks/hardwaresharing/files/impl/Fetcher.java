package sneer.bricks.hardwaresharing.files.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.FileContents;
import sneer.bricks.hardwaresharing.files.FolderContents;
import sneer.bricks.hardwaresharing.files.FolderEntry;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;

class Fetcher {

	
	private static Map<Sneer1024, Object> _contentsByHash = new HashMap<Sneer1024, Object>();
	
	@SuppressWarnings("unused") private final static WeakContract _fileContract;
	@SuppressWarnings("unused") private final static WeakContract _folderContract;
	

	static {
		my(TupleSpace.class).keep(FileContents.class);
		_fileContract = my(TupleSpace.class).addSubscription(FileContents.class, new Consumer<FileContents>() { @Override public void consume(FileContents contents) {
			putContentsByHash(Hasher.hashFile(contents), contents);
		}});
		
		my(TupleSpace.class).keep(FolderContents.class);
		_folderContract = my(TupleSpace.class).addSubscription(FolderContents.class, new Consumer<FolderContents>() { @Override public void consume(FolderContents contents) {
			putContentsByHash(Hasher.hashFolder(contents), contents);
		}});
	}


	static void fetchContentsInto(File fileOrFolder, long lastModified, Sneer1024 hash) throws IOException {
		final Object contents = waitForContents(hash);
		
		if (contents instanceof FileContents)
			fetchFileContentsInto(fileOrFolder, lastModified, (FileContents)contents);
		else
			fetchFolderContentsInto(fileOrFolder, lastModified, (FolderContents)contents);
	}


	private static void putContentsByHash(Sneer1024 hash, Object contents) {
		synchronized (_contentsByHash) {
			_contentsByHash.put(hash, contents);
			_contentsByHash.notifyAll();
		}
	}

	
	private static Object waitForContents(Sneer1024 hash) {
		synchronized (_contentsByHash) {
			while (true) {
				Object contents = _contentsByHash.get(hash);
				if (contents != null) return contents;
				my(Threads.class).waitWithoutInterruptions(_contentsByHash);
			}
		}
	}

	
	private static void fetchFolderContentsInto(File folder, long lastModified, FolderContents contents) throws IOException {
		folder.mkdirs();
		
		for (FolderEntry entry : contents.contents)
			fetchFolderEntryInto(folder, entry);
		
		folder.setLastModified(lastModified);
	}

	
	private static void fetchFolderEntryInto(File folder, FolderEntry entry) throws IOException {
		fetchContentsInto(
			new File(folder, entry.name),
			entry.lastModified,
			entry.hashOfContents
		);
	}

	
	private static void fetchFileContentsInto(File destination, long lastModified, final FileContents contents) throws IOException {
		my(IO.class).files().writeByteArrayToFile(destination, contents.bytes.copy());
		destination.setLastModified(lastModified);
	}
}

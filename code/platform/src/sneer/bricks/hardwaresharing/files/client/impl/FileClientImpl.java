package sneer.bricks.hardwaresharing.files.client.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.hardwaresharing.files.hasher.Hasher;
import sneer.bricks.hardwaresharing.files.protocol.FileContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class FileClientImpl implements FileClient {

	@SuppressWarnings("unused") private final WeakContract _fileContract;
	@SuppressWarnings("unused") private final WeakContract _folderContract;
	
	
	{
		_fileContract = my(TupleSpace.class).addSubscription(FileContents.class, new Consumer<FileContents>() { @Override public void consume(FileContents contents) {
			putContentsByHash(hashFile(contents), contents);
		}});
		
		_folderContract = my(TupleSpace.class).addSubscription(FolderContents.class, new Consumer<FolderContents>() { @Override public void consume(FolderContents contents) {
			putContentsByHash(hashFolder(contents), contents);
		}});
	}

	
	@Override
	public void fetch(Sneer1024 hashOfContents) {
		if (my(FileCache.class).getContents(hashOfContents) == null) throw new NotImplementedYet();
	}

	
	
	
	
	
	
	

	





	private void putContentsByHash(@SuppressWarnings("unused") Sneer1024 hash, @SuppressWarnings("unused") Object contents) {
//		synchronized (_contentsBufferByHash) {
//			_contentsBufferByHash.put(hash, contents);
//			_contentsBufferByHash.notifyAll();
//		}
	}


	
	
	//	private Object waitForContents(Sneer1024 hash) {
//		synchronized (_contentsBufferByHash) {
//			while (true) {
//				Object contents = _contentsBufferByHash.get(hash);
//				if (contents != null) return contents;
//				my(Threads.class).waitWithoutInterruptions(_contentsBufferByHash);
//			}
//		}
//	}
//
//	
//	private void fetchFolderContentsInto(File folder, long lastModified, FolderContents contents) throws IOException {
//		folder.mkdirs();
//		
//		for (FolderEntry entry : contents.contents)
//			fetchFolderEntryInto(folder, entry);
//		
//		folder.setLastModified(lastModified);
//	}
//
//	
//	private void fetchFolderEntryInto(File folder, FolderEntry entry) throws IOException {
//		writeTo(
//			new File(folder, entry.name),
//			entry.lastModified,
//			entry.hashOfContents
//		);
//	}
//
//	
//	private void fetchFileContentsInto(File destination, long lastModified, FileContents contents) throws IOException {
//		my(IO.class).files().writeByteArrayToFile(destination, contents.bytes.copy());
//		destination.setLastModified(lastModified);
//	}
//
//	
	private Sneer1024 hashFile(FileContents contents) {
		return my(Hasher.class).hashFile(contents);
	}
	
	private Sneer1024 hashFolder(FolderContents contents) {
		return my(Hasher.class).hashFolder(contents);
	}


	
	
	
	
	
	
	
	
	
}

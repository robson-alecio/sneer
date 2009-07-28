package sneer.bricks.hardwaresharing.files.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.bricks.hardwaresharing.files.FileContents;
import sneer.bricks.hardwaresharing.files.FolderEntry;
import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.hardwaresharing.files.FolderContents;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class FileSpaceImpl implements FileSpace {

	private static final byte[] TRUE_AS_BYTES = new byte[]{1};
	private static final byte[] FALSE_AS_BYTES = new byte[]{0};
	
	private Map<Sneer1024, Object> _contentsByHash = new ConcurrentHashMap<Sneer1024, Object>();
	
	private Map<Sneer1024, FolderEntry> _folderEntriesByHash = new ConcurrentHashMap<Sneer1024, FolderEntry>();
	
	@SuppressWarnings("unused") private final WeakContract _fileContract;
	
	@SuppressWarnings("unused") private final WeakContract _folderContract;
	
	@SuppressWarnings("unused") private final WeakContract _folderEntryContract;

	{
		my(TupleSpace.class).keep(FileContents.class);
		
		_fileContract = my(TupleSpace.class).addSubscription(FileContents.class, new Consumer<FileContents>() { @Override public void consume(FileContents contents) {
			_contentsByHash.put(hash(contents), contents.bytes);
		}});
		
		my(TupleSpace.class).keep(FolderContents.class);
		
		_folderContract = my(TupleSpace.class).addSubscription(FolderContents.class, new Consumer<FolderContents>() { @Override public void consume(FolderContents contents) {
			_contentsByHash.put(hash(contents), contents);
		}});
		
		my(TupleSpace.class).keep(FolderEntry.class);
		
		_folderEntryContract = my(TupleSpace.class).addSubscription(FolderEntry.class, new Consumer<FolderEntry>() { @Override public void consume(FolderEntry entry) {
			_folderEntriesByHash.put(hash(entry), entry);
		}});
	}
	
	@Override
	public Sneer1024 publishContents(File fileOrFolder) throws IOException {
		return (fileOrFolder.isDirectory())
			? publishFolderContents(fileOrFolder)
			: publishFileContents(fileOrFolder);
	}

	private Sneer1024 publishFileContents(File file)	throws IOException {
		my(TupleSpace.class).publish(newDataBlock(file));
		return my(Crypto.class).digest(file); //Optimize byte[] is being read already.
	}
	
	private Sneer1024 publishFolderContents(File folder) throws IOException {
		List<Sneer1024> files = publishEachFile(folder);
		final FolderContents contents = new FolderContents(my(ImmutableArrays.class).newImmutableArray(files));
		my(TupleSpace.class).publish(contents);
		return hash(contents);
	}

	private List<Sneer1024> publishEachFile(File folder) throws IOException {
		List<Sneer1024> result = new ArrayList<Sneer1024>();
		for (File fileOrFolder : folder.listFiles()) {
			Sneer1024 hashOfContents = publishContents(fileOrFolder);
			FolderEntry entry = folderEntryFor(fileOrFolder, hashOfContents);
			my(TupleSpace.class).publish(entry);
						
			result.add(hash(entry));
		}
		return result;
	}

	private Sneer1024 hash(FolderEntry entry) {
		Digester digester = my(Crypto.class).newDigester();
		digester.update(bytesUtf8(entry.name));
		digester.update(entry.isFolder ? TRUE_AS_BYTES : FALSE_AS_BYTES);
		digester.update(BigInteger.valueOf(entry.lastModified).toByteArray());
		digester.update(entry.hashOfContents.bytes());
		return digester.digest();
	}
	
	private Sneer1024 hash(FolderContents folder) {
		Digester digester = my(Crypto.class).newDigester();
		for (Sneer1024 fileInfoHash : folder.contents)
			digester.update(fileInfoHash.bytes());
		return digester.digest();
	}

	private byte[] bytesUtf8(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private FolderEntry folderEntryFor(File fileOrFolder, Sneer1024 hashOfContents) {
		return new FolderEntry(
			fileOrFolder.getName(),
			fileOrFolder.isDirectory(),
			fileOrFolder.lastModified(),
			hashOfContents
		);
	}

	@Override
	public void fetchContentsInto(File destination, Sneer1024 hash) throws IOException {
		final Object data = _contentsByHash.get(hash);
		if (null == data)
			throw new NotImplementedYet();
		
		if (data instanceof ImmutableByteArray)
			fetchFileContentsInto(destination, (ImmutableByteArray)data);
		else
			fetchFolderContentsInto(destination, (FolderContents)data);
	}

	private void fetchFolderContentsInto(File folder, FolderContents contents) throws IOException {
		folder.mkdirs();
		for (Sneer1024 hash : contents.contents)
			fetchFolderEntryInto(folder, hash);
	}

	private void fetchFolderEntryInto(File folder, Sneer1024 entryHash) throws IOException {
		
		FolderEntry entry = _folderEntriesByHash.get(entryHash);
		if (null == entry)
			throw new NotImplementedYet();
		
		fetchContentsInto(new File(folder, entry.name), entry.hashOfContents);
	}

	private void fetchFileContentsInto(File destination, final ImmutableByteArray contents) throws IOException {
		my(IO.class).files().writeByteArrayToFile(destination, contents.copy());
	}
	
	private FileContents newDataBlock(File fileOrFolder) throws IOException {
		byte[] bytes = my(IO.class).files().readBytes(fileOrFolder);
		return new FileContents(my(ImmutableArrays.class).newImmutableByteArray(bytes));
	}

	private Sneer1024 hash(FileContents block) {
		return my(Crypto.class).digest(block.bytes.copy());
	};

}

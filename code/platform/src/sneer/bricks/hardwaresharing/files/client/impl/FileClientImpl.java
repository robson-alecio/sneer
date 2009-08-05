package sneer.bricks.hardwaresharing.files.client.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.hardwaresharing.files.protocol.FileContents;
import sneer.bricks.hardwaresharing.files.protocol.FileRequest;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Producer;

class FileClientImpl implements FileClient {
	
	private final CacheMap<Sneer1024, Latch> _latchesByHash = my(CacheMaps.class).newInstance();

	@SuppressWarnings("unused") private final WeakContract _fileContract;
	@SuppressWarnings("unused") private final WeakContract _folderContract;
	@SuppressWarnings("unused") private final WeakContract _cacheContract;
	
	{
		_fileContract = my(TupleSpace.class).addSubscription(FileContents.class, new Consumer<FileContents>() { @Override public void consume(FileContents contents) {
			receiveFile(contents);
		}});
		
		_folderContract = my(TupleSpace.class).addSubscription(FolderContents.class, new Consumer<FolderContents>() { @Override public void consume(FolderContents contents) {
			receiveFolder(contents);
		}});
		
		_cacheContract = my(FileCache.class).contentsAdded().addReceiver(new Consumer<Sneer1024>() { @Override public void consume(Sneer1024 hashOfContents) {
			contentsReceived(hashOfContents);
		}});
	}

	
	@Override
	public void fetchToCache(Sneer1024 hashOfContents) {
		Latch latch;

		synchronized (this) {
			if (cachedContentsOf(hashOfContents) != null)
				return;
			
			my(TupleSpace.class).publish(new FileRequest(hashOfContents));
			
			latch = _latchesByHash.get(hashOfContents, new Producer<Latch>() { @Override public Latch produce() {
				return my(Threads.class).newLatch();
			}});
		}
		
		latch.waitTillOpen();
		
		Object contents = cachedContentsOf(hashOfContents);
		if (contents instanceof FolderContents)
			fetchFolderEntries((FolderContents)contents);
	}

	private void fetchFolderEntries(FolderContents contents) {
		for (FolderEntry entry : contents.contents)
			fetchToCache(entry.hashOfContents);
	}

	private Object cachedContentsOf(Sneer1024 hashOfContents) {
		return my(FileCache.class).getContents(hashOfContents);
	}
	
	synchronized
	private void contentsReceived(Sneer1024 hashOfContents) {
		Latch latch = _latchesByHash.remove(hashOfContents);
		if (latch != null) latch.open();
	}
	

	private void receiveFile(FileContents contents) {
		my(FileCache.class).putFileContents(contents.bytes.copy());
	}

	
	private void receiveFolder(FolderContents contents) {
		my(FileCache.class).putFolderContents(contents);
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
	
	
	
	
	
	
	
	
}

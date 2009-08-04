package sneer.bricks.hardwaresharing.files.cache.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.arrays.ImmutableArray;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;

public class FileCacheTest extends BrickTest {

	private final FileCache _subject = my(FileCache.class);

	@Test
	public void cacheFileContents() {
		Sneer1024 hash = _subject.putFileContents(new byte[]{1, 2, 3});
		assertTrue(Arrays.equals(new byte[]{1, 2, 3}, (byte[])_subject.getContents(hash)));
	}

	@Test
	public void cacheFolderContents() {
		final Latch latch = my(Threads.class).newLatch();
		
		@SuppressWarnings("unused")	WeakContract contract =
			_subject.foldersAdded().addReceiver(new Consumer<FolderContents>() { @Override public void consume(FolderContents value) {
				latch.open();
			}});
		
		FolderContents folderContents = new FolderContents(immutable(Arrays.asList(new FolderEntry[]{
			new FolderEntry("readme.txt", anyReasonableDate(), hash(1)),
			new FolderEntry("src", anyReasonableDate(), hash(2)),
			new FolderEntry("docs", anyReasonableDate(), hash(3))
		})));

		Sneer1024 hash = _subject.putFolderContents(folderContents);
		
		latch.waitTillOpen();
		assertEquals(folderContents, _subject.getContents(hash));
	}

	private ImmutableArray<FolderEntry> immutable(Collection<FolderEntry> entries) {
		return my(ImmutableArrays.class).newImmutableArray(entries);
	}

	private Sneer1024 hash(int i) {
		return my(Crypto.class).digest(new byte[]{(byte)i});
	}

	private long anyReasonableDate() {
		return System.currentTimeMillis();
	}

}
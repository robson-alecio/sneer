package sneer.bricks.hardwaresharing.files.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardwaresharing.files.FileContents;
import sneer.bricks.hardwaresharing.files.FolderContents;
import sneer.bricks.hardwaresharing.files.FolderEntry;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;

class Publisher {

	static Sneer1024 publishContents(File fileOrFolder) throws IOException {
		return (fileOrFolder.isDirectory())
			? publishFolderContents(fileOrFolder)
			: publishFileContents(fileOrFolder);
	}

	private static Sneer1024 publishFileContents(File file)	throws IOException {
		my(TupleSpace.class).publish(newDataBlock(file));
		return my(Crypto.class).digest(file); //Optimize byte[] is being read already.
	}
	
	
	private static Sneer1024 publishFolderContents(File folder) throws IOException {
		List<FolderEntry> files = publishEachFolderEntry(folder);
		final FolderContents contents = new FolderContents(my(ImmutableArrays.class).newImmutableArray(files));
		my(TupleSpace.class).publish(contents);
		return Hasher.hashFolder(contents);
	}

	
	private static List<FolderEntry> publishEachFolderEntry(File folder) throws IOException {
		List<FolderEntry> result = new ArrayList<FolderEntry>();
		
		for (File fileOrFolder : listFiles(folder))
			result.add(publishFolderEntryContents(fileOrFolder));

		return result;
	}

	
	private static FolderEntry publishFolderEntryContents(File fileOrFolder) throws IOException {
		Sneer1024 hashOfContents = publishContents(fileOrFolder);
		
		return new FolderEntry(
			fileOrFolder.getName(),
			fileOrFolder.lastModified(),
			hashOfContents
		);
	}

	
	private static FileContents newDataBlock(File fileOrFolder) throws IOException {
		byte[] bytes = my(IO.class).files().readBytes(fileOrFolder);
		return new FileContents(my(ImmutableArrays.class).newImmutableByteArray(bytes));
	}

	
	private static File[] listFiles(File folder) {
		File[] result = folder.listFiles();
		return result == null
			? new File[0]
			: result;
	}

}

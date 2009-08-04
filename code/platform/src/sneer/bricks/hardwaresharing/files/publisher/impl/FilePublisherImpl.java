package sneer.bricks.hardwaresharing.files.publisher.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArray;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.hardwaresharing.files.publisher.FilePublisher;
import sneer.bricks.pulp.crypto.Sneer1024;

class FilePublisherImpl implements FilePublisher {

	
//	private final Light _errorLight = my(BlinkingLights.class).prepare(LightType.ERROR);
//	try {
//	} catch (IOException e) {
//		my(Logger.class).logShort(e, "Error reading file.");
//		my(BlinkingLights.class).turnOnIfNecessary(_errorLight, "Error reading file.", helpMessage(), e);
//	}
//	private static String helpMessage() {
//		return "There was trouble reading files from disk when trying to publish them. See log for details.";
//	}
	
	@Override
	public Sneer1024 publish(File fileOrFolder) throws IOException {
		return (fileOrFolder.isDirectory())
			? publishFolder(fileOrFolder)
			: publishFile(fileOrFolder);
	}


	private static Sneer1024 publishFile(File file) throws IOException {
		return my(FileCache.class).putFileContents(readFileContents(file));
	}

	
	private Sneer1024 publishFolder(File folder) throws IOException {
		return my(FileCache.class).putFolderContents(
			new FolderContents(immutable(
				publishEachFolderEntry(folder)
			))
		);
	}

	
	private List<FolderEntry> publishEachFolderEntry(File folder) throws IOException {
		List<FolderEntry> result = new ArrayList<FolderEntry>();
		
		for (File fileOrFolder : sortedFiles(folder))
			result.add(publishFolderEntry(fileOrFolder));
		
		return result;
	}

	
	private FolderEntry publishFolderEntry(File fileOrFolder) throws IOException {
		Sneer1024 hashOfContents = publish(fileOrFolder);
		
		return new FolderEntry(
			fileOrFolder.getName(),
			fileOrFolder.lastModified(),
			hashOfContents
		);
	}
	
	
	private static ImmutableArray<FolderEntry> immutable(List<FolderEntry> entries) {
		return my(ImmutableArrays.class).newImmutableArray(entries);
	}
	
	private static File[] sortedFiles(File folder) {
		File[] result = folder.listFiles();
		if (result == null) return new File[0];
		
		Arrays.sort(result, new Comparator<File>() { @Override public int compare(File file1, File file2) {
			return file1.getName().compareTo(file2.getName());
		}});

		return result;
	}
	
	private static byte[] readFileContents(File file) throws IOException {
		return my(IO.class).files().readBytes(file);
	}

	

	
}

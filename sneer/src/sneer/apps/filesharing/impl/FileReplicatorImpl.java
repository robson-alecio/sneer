package sneer.apps.filesharing.impl;

import java.io.IOException;

import sneer.apps.filesharing.FileReplicator;
import wheel.io.files.Directory;

public class FileReplicatorImpl implements FileReplicator {

	@Override
	public void replicate(Directory master, Directory slave) throws IOException {
		slave.deleteAllContents();
		
		String[] files = master.fileNames();
		
		for (String fileName : files) {
			String contents = master.contentsAsString(fileName);
			slave.createFile(fileName, contents);
		}
		
	
	}

}

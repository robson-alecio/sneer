package sneer.hardware.io.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import sneer.hardware.io.IO;

class IOImpl implements IO {
	
	private Files _files = new Files(){
		
		@Override public boolean isEmpty(File file) {
			if(file == null || !file.exists()) return true;
			
			if(file.isDirectory()) {
				String[] children = file.list();
				return children == null || children.length == 0;
			}
			
			return file.length() == 0;
		}
		
		@Override public String concat(String basePath, String fullFilenameToAdd) { return FilenameUtils.concat(basePath, fullFilenameToAdd); }
		@Override public Collection<File> listFiles(File directory, String[] extensions, boolean recursive) { return FileUtils.listFiles(directory, extensions, recursive); }
		@Override public void writeStringToFile(File file, String data) throws IOException { FileUtils.writeStringToFile(file, data); }
		@Override public void deleteDirectory(File directory) throws IOException { FileUtils.deleteDirectory(directory); }
		@Override public Iterator<File> iterate(File directory, String[] extensions, boolean recursive){ return FileUtils.iterateFiles(directory, extensions, recursive); }
		@Override public byte[] readFileToByteArray(File file) throws IOException { return FileUtils.readFileToByteArray(file); }
	};
	
	private Streams _streams = new Streams(){
		@Override public String toString(InputStream input) throws IOException { return IOUtils.toString(input); }
		@Override public byte[] toByteArray(InputStream input) throws IOException { return IOUtils.toByteArray(input); }
	};

	@Override
	public Files files() {
		return _files;
	}

	@Override
	public Streams streams() {
		return _streams;
	}
}
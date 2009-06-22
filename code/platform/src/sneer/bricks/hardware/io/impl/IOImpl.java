package sneer.bricks.hardware.io.impl;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import sneer.bricks.hardware.io.IO;

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
		@Override public void copyDirectory(File srcDir, File destDir) throws IOException { FileUtils.copyDirectory(srcDir, destDir); }
		@Override public Collection<File> listFiles(File directory, String[] extensions, boolean recursive) { return FileUtils.listFiles(directory, extensions, recursive); }
		@Override public void writeStringToFile(File file, String data) throws IOException { FileUtils.writeStringToFile(file, data); }
		@Override public void deleteDirectory(File directory) throws IOException { FileUtils.deleteDirectory(directory); }
		@Override public Iterator<File> iterate(File directory, String[] extensions, boolean recursive){ return FileUtils.iterateFiles(directory, extensions, recursive); }
		@Override public byte[] readFileToByteArray(File file) throws IOException { return FileUtils.readFileToByteArray(file); }
	};
	
	private Streams _streams = new Streams(){
		@Override public String toString(InputStream input) throws IOException { return IOUtils.toString(input); }
		@Override public byte[] toByteArray(InputStream input) throws IOException { return IOUtils.toByteArray(input); }
		@Override public byte[] readBytesAndClose(InputStream input) throws IOException { 
			try {
				return toByteArray(input);
			}finally{
				try { input.close(); } catch (Throwable ignore) { }
			}
		}
	};

	private FileFilters _fileFilters = new FileFilters(){
		@Override public Filter name(String name) { return adapt(FileFilterUtils.nameFileFilter(name)); }
		@Override public Filter not(Filter filter) { return adapt(FileFilterUtils.notFileFilter((IOFileFilter) filter)); }
		@Override public Filter suffix(String sulfix) { return adapt(FileFilterUtils.suffixFileFilter(sulfix)); }

		@Override public Collection<File> listFiles(File directory, Filter fileFilter, Filter dirFilter){ 
			return FileUtils.listFiles(directory, (IOFileFilter)fileFilter, (IOFileFilter)dirFilter);}
		
		private Filter adapt(IOFileFilter filter) { return new IOFileFilterAdapter(filter); }
		
		class IOFileFilterAdapter implements IOFileFilter, Filter{
			IOFileFilter _delegate;
			public IOFileFilterAdapter(IOFileFilter delegate) { _delegate = delegate; }
			@Override public boolean accept(File file) { return _delegate.accept(file);}
			@Override public boolean accept(File dir, String name) { return _delegate.accept(dir, name); }
		}
	};
	
	@Override
	public Files files() {
		return _files;
	}

	@Override
	public Streams streams() {
		return _streams;
	}

	@Override
	public void crash(Closeable closeable) {
		try {
			if(closeable!=null) closeable.close();
		} catch (IOException ignored) {}
	}

	@Override
	public FileFilters fileFilters() {
		return _fileFilters;
	}
}
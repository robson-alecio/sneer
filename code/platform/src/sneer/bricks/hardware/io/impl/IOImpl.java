package sneer.bricks.hardware.io.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.foundation.lang.Consumer;


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
		
		@Override public void copyFolder(File srcFolder, File destFolder) throws IOException { FileUtils.copyDirectory(srcFolder, destFolder); }
		@Override public Collection<File> listFiles(File folder, String[] extensions, boolean recursive) { return FileUtils.listFiles(folder, extensions, recursive); }
		@Override public void writeString(File file, String data) throws IOException { FileUtils.writeStringToFile(file, data); }
		@Override public void deleteFolder(File folder) throws IOException { FileUtils.deleteDirectory(folder); }
		@Override public Iterator<File> iterate(File folder, String[] extensions, boolean recursive){ return FileUtils.iterateFiles(folder, extensions, recursive); }
		
		@Override public String readString(File file) throws IOException {return new String(readBytes(file)); }
		@Override public void readString(final File file, final Consumer<String> content) {
			readBytes(file, new Consumer<byte[]>(){ @Override public void consume(byte[] value) { content.consume(new String(value)); }});}
		@Override public void readString(final File file, final Consumer<String> content, final Consumer<IOException> exception) {
			readBytes(file, new Consumer<byte[]>(){ @Override public void consume(byte[] value) {  content.consume(new String(value)); } }, exception);}

		@Override public byte[] readBytes(File file) throws IOException {  return FileUtils.readFileToByteArray(file);  }
		@Override public void readBytes(final File file, Consumer<byte[]> content) {
			readBytes(file, content, new Consumer<IOException>(){ @Override public void consume(IOException exception) {
				my(BlinkingLights.class).turnOn(LightType.ERROR, "Error", "Unable to read file: " + file.getAbsolutePath(),  exception, 5*60*1000);
			}});		
		}
		
		@Override public void writeByteArrayToFile(File file, byte[] data) throws IOException { FileUtils.writeByteArrayToFile(file, data); }
		
		@Override public boolean contentEquals(File file1, File file2) throws IOException {
			if (file1.isDirectory() != file2.isDirectory()) return false;
			
			return file1.isDirectory()
				? folderContentEquals(file1, file2)
				: FileUtils.contentEquals(file1, file2);
		}

		private boolean folderContentEquals(File folder1, File folder2) throws IOException {
			File[] files1 = sortedFiles(folder1);
			File[] files2 = sortedFiles(folder2);
			
			if (files1.length != files2.length) return false;
			
			for (int i = 0; i < files1.length; i++) {
				if (!nameEquals(files1[i], files2[i])) return false;
				if (!contentEquals(files1[i], files2[i])) return false;
			}
			
			return true;
		}

		private boolean nameEquals(File file, File file2) {
			return file.getName().equals(file2.getName());
		}

		private File[] sortedFiles(File folder) {
			File[] result = folder.listFiles();
			if (result == null) result = new File[0];
			Arrays.sort(result);
			return result;
		}

		@Override public void readBytes(File file, Consumer<byte[]> content, Consumer<IOException> exception) {
			try {
				content.consume(readBytes(file));
			} catch (IOException e) {
				exception.consume(e);
			}
		}
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

		@Override public Collection<File> listFiles(File folder, Filter fileFilter, Filter dirFilter){ 
			return FileUtils.listFiles(folder, (IOFileFilter)fileFilter, (IOFileFilter)dirFilter);}
		
		private Filter adapt(IOFileFilter filter) { return new IOFileFilterAdapter(filter); }
		
		class IOFileFilterAdapter implements IOFileFilter, Filter{
			IOFileFilter _delegate;
			public IOFileFilterAdapter(IOFileFilter delegate) { _delegate = delegate; }
			@Override public boolean accept(File file) { return _delegate.accept(file);}
			@Override public boolean accept(File dir, String name) { return _delegate.accept(dir, name); }
		}
	};
	
	@Override public Files files() { return _files; }
	@Override public Streams streams() { return _streams; }
	@Override public FileFilters fileFilters() { return _fileFilters; }
	@Override
	public void crash(Closeable closeable) {
		try {
			if(closeable!=null) closeable.close();
		} catch (IOException ignored) {}
	}
}
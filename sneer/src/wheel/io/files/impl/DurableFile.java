package wheel.io.files.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import wheel.io.files.File;

public class DurableFile implements File{
	RandomAccessFile _delegate;
	private final java.io.File _file;
	
	DurableFile(java.io.File file) throws FileNotFoundException{
		try{
			_file = file;
			_delegate = new RandomAccessFile(file,"rws");
		}catch(FileNotFoundException e){
			throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
		}
	}
	
	public void seek(long index) throws IOException{
		_delegate.seek(index);
	}
	
	public OutputStream outputstream(){
		return new MyOutputstream();
	}
	
	public long modificationTime() {
		return _file.lastModified();
	}
	
	private class MyOutputstream extends OutputStream{

		@Override
		public void close() throws IOException {
			_delegate.close();
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			_delegate.write(b,off,len);
		}

		@Override
		public void write(byte[] b) throws IOException {
			_delegate.write(b);
		}

		@Override
		public void write(int b) throws IOException {
			_delegate.write(b);
		}

	}
	
	//FixUrgent: this method must not exist. It exists just for backward compatibility before refactoring
	public java.io.File getFile(){
		return _file;
	}

}

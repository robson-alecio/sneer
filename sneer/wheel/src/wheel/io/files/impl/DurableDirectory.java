package wheel.io.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DurableDirectory extends AbstractDirectory {

	static private class DurableFileOutputStream extends FileOutputStream implements CloseableWithListener {

		private Listener _closeListener;

		private DurableFileOutputStream(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		public void flush() throws IOException {
			super.flush();
			getFD().sync();
		}

		@Override
		public void close() throws IOException {
			try {
				flush();
			} finally {
				_closeListener.streamClosed(this);
				super.close();
			}
		}

		public void notifyOnClose(Listener listener) {
			_closeListener = listener;
		}

	}

	static private class CloseListenableFileInputStream extends FileInputStream implements CloseableWithListener {

		private Listener _closeListener;

		private CloseListenableFileInputStream(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		public void close() throws IOException {
			_closeListener.streamClosed(this);
			super.close();
		}

		public void notifyOnClose(Listener listener) {
			_closeListener = listener;
		}

	}


	public DurableDirectory(String path) throws IOException {
		_delegate = new File(path);
		if (!_delegate.exists() && !_delegate.mkdirs()) throw new IOException("Unable to create directory " + _delegate);
	}


	private final File _delegate;


	public OutputStream createFile(String name) throws IOException {
		assertNotClosed();

		if (fileExists(name)) throwFileAlreadyExists(name);
		DurableFileOutputStream result = new DurableFileOutputStream(realFile(name));
		mindOpenStream(result, name);
		return result;
	}

	public InputStream openFile(String name) throws IOException {
		assertNotClosed();

		CloseListenableFileInputStream result = null;
		try {
			result = new CloseListenableFileInputStream(realFile(name));
		} catch (FileNotFoundException e) {
			throwFileNotFound(name);
		}

		mindOpenStream(result, name);
		return result;
	}

	@Override
	protected void physicalDelete(String name) throws IOException {
		if (realFile(name).delete()) return;
		throwUnableToDelete(name);
	}

	public boolean fileExists(String fileName) {
		assertNotClosed();

		return realFile(fileName).exists();
	}

	@Override
	protected void physicalRenameFile(String oldName, String newName) throws IOException {
		if (realFile(oldName).renameTo(realFile(newName))) return;
		throwUnableToRename(oldName, newName);
	}

	public void deleteAllContents() throws IOException {
		assertNotClosed();

		String[] fileNames = fileNames();
		for (int i = 0; i < fileNames.length; i++)
			deleteFile(fileNames[i]);
	}

	public String[] fileNames() {
		assertNotClosed();

		File[] files = _delegate.listFiles();
		String[] result = new String[files.length];
		for (int i = 0; i < files.length; i++)
			result[i] = files[i].getName();
		return result;
	}



	File realFile(String fileName) {
		return new File(_delegate, fileName);
	}

	@Override
	protected String getPath(String name) {
		return realFile(name).getAbsolutePath();
	}


}

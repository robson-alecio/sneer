package wheel.io.files.impl;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import wheel.io.files.Directory;
import wheel.io.files.impl.CloseableWithListener.Listener;

public abstract class AbstractDirectory implements Directory {

	public void createFile(String fileName, String contents) throws IOException {
		OutputStream output = null;
		try {
			output = createFile(fileName);
			new DataOutputStream(output).writeUTF(contents);
		} finally {
			if (output != null) output.close();
		}
		
	}

	public String contentsAsString(String fileName) throws IOException {
		InputStream input = null;
		try {
			input = openFile(fileName);
			return IOUtils.toString(input);
		} finally {
			if (input != null) input.close();
		}
	}

	private final Map<String, Collection<CloseableWithListener>> _openStreamsByFilename = new HashMap<String, Collection<CloseableWithListener>>();
	protected boolean _isClosed = false;
	protected abstract String getPath(String filename);

	protected void throwFileAlreadyExists(String name) throws IOException {
		throwIO("File already exists", name);
	}

	protected void throwFileNotFound(String name) throws FileNotFoundException {
		throw new FileNotFoundException("File not found: " + getPath(name));
	}

	protected void throwUnableToDelete(String name) throws IOException {
		throwIO("Unable to delete file", name);
	}

	protected void throwUnableToRename(String oldName, String newName) throws IOException {
		throwIO("Unable to rename file: " + getPath(oldName) + " to", newName);
	}

	private void throwIO(String string, String name) throws IOException {
		throw new IOException(string + ": " + getPath(name));
	}



	protected synchronized void mindOpenStream(CloseableWithListener stream, final String filename) {
		stream.notifyOnClose(new Listener() {
				public void streamClosed(CloseableWithListener closedStream) {
					forgetStream(closedStream, filename);
				}
		});

		openStreamsFor(filename).add(stream);
	}

	public synchronized void close() {
		closeStreams(allOpenStreams());

		_isClosed = true;
	}

	private ArrayList<CloseableWithListener> allOpenStreams() {
		ArrayList<CloseableWithListener> result = new ArrayList<CloseableWithListener>();

		for (String filename : _openStreamsByFilename.keySet())
			result.addAll(openStreamsFor(filename));

		return result;
	}

	private Collection<CloseableWithListener> openStreamsFor(String filename) {
		Collection<CloseableWithListener> result = _openStreamsByFilename.get(filename);
		if (result == null) {
			result = new HashSet<CloseableWithListener>();
			_openStreamsByFilename.put(filename, result);
		}
		return result;
	}

	private synchronized void forgetStream(Object closedStream, String filename) {
		Collection<?> openStreams = openStreamsFor(filename);
		openStreams.remove(closedStream);
		if (openStreams.isEmpty()) _openStreamsByFilename.remove(filename);
	}

	private void closeStreams(List<CloseableWithListener> streams) {
		Iterator<CloseableWithListener> it = streams.iterator();
		while (it.hasNext()) {
			try {
				it.next().close();
			} catch (IOException ignored) {}
		}
	}

	protected boolean hasOpenStreamsFor(String filename) {
		return !openStreamsFor(filename).isEmpty();
	}

	protected void assertNotClosed() throws IllegalStateException {
		if (_isClosed) throw new IllegalStateException("" + getClass() + " already closed.");
	}

	public synchronized void deleteFile(String name) throws IOException {
		assertNotClosed();

		if (!fileExists(name)) throwFileNotFound(name);
		if (hasOpenStreamsFor(name)) throwUnableToDelete(name);

		physicalDelete(name);
	}

	protected abstract void physicalDelete(String name) throws IOException;

	public synchronized void renameFile(String oldName, String newName) throws IOException {
		assertNotClosed();

		if (fileExists(newName)) throwFileAlreadyExists(newName);
		if (hasOpenStreamsFor(oldName)) throwUnableToDelete(oldName);

		physicalRenameFile(oldName, newName);
	}

	protected abstract void physicalRenameFile(String oldName, String newName) throws IOException;


}

package wheel.io.files.impl.tranzient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wheel.io.files.impl.AbstractDirectory;

public class TransientDirectory extends AbstractDirectory {

	private final Map<String, TransientFile> _filesByName = new HashMap<String, TransientFile>();

	public synchronized OutputStream createFile(String name) throws IOException {
		assertNotClosed();

		if (fileExists(name)) throwFileAlreadyExists(name);

		TransientFile file = new TransientFile();
		_filesByName.put(name, file);

		return createOutputStream(name, file.contents());
	}


	private OutputStream createOutputStream(final String filename, List<Byte> contents) {
		ByteListOutputStream result = new ByteListOutputStream(contents);
		mindOpenStream(result, filename);
		return result;
	}


	public synchronized InputStream openFile(String name) throws FileNotFoundException {
		assertNotClosed();

		if (!fileExists(name)) throwFileNotFound(name);

		List<Byte> contents = file(name).contents();
		ByteListInputStream result = new ByteListInputStream(contents);
		mindOpenStream(result, name);
		return result;
	}

	@Override
	protected void physicalDelete(String name) {
		_filesByName.remove(name);
	}


	public synchronized boolean fileExists(String fileName) {
		assertNotClosed();
		return _filesByName.containsKey(fileName);
	}

	@Override
	protected void physicalRenameFile(String oldName, String newName) throws IOException {
		TransientFile file = file(oldName);
		deleteFile(oldName);
		_filesByName.put(newName, file);
	}

	public synchronized void deleteAllContents() throws IOException {
		assertNotClosed();

		for (String filename : _filesByName.keySet())
			deleteFile(filename);
	}

	public synchronized String[] fileNames() {
		assertNotClosed();
		return _filesByName.keySet().toArray(new String[0]);
	}

	@Override
	public synchronized void close() {
		_filesByName.clear();
		super.close();
	}



	@Override
	protected String getPath(String filename) {
		return filename;
	}


	public synchronized TransientFile file(String fileName) throws FileNotFoundException {
		if (!fileExists(fileName)) throw new FileNotFoundException(fileName);
		
		return _filesByName.get(fileName);
	}

}

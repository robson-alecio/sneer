package sneer.lego.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;

public class FilteringDirectoryWalker extends DirectoryWalker {
	
	private File _root;
	
	public FilteringDirectoryWalker(File root, FileFilter filter) {
		super(filter, -1);
		_root = root;
	}
	
	public List<File> list() {
		List<File> result = new ArrayList<File>();
		try {
			walk(_root, result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public File root() {
		return _root;
	}

	@SuppressWarnings({"unchecked","unused"})
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		results.add(file);
	}
}
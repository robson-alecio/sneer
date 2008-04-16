package sneer.lego.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;

public class FilteringDirectoryWalker extends DirectoryWalker {
	
	protected File _root;
	
	public FilteringDirectoryWalker(File root, FileFilter filter) {
		super(filter, -1);
		_root = root;
	}

	public File root() {
		return _root;
	}
	
	@SuppressWarnings("unchecked")
	public List<File> list() {
		List<File> result = new ArrayList<File>();
		return (List<File>) walkAndCollect(result);
	}
	
	@SuppressWarnings("unchecked")
	protected Collection walkAndCollect(Collection c) {
		try {
			walk(_root, c);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return c;
	}

//	@SuppressWarnings({"unchecked","unused"})
//	@Override
//	protected void handleFile(File file, int depth, Collection results) throws IOException {
//		results.add(file);
//	}
}
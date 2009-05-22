package sneer.pulp.deployer.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import sneer.software.code.filefilters.java.impl.SimpleFilter;


class BrickImplDirectoryFactoryImpl implements BrickImplDirectoryFactory {

	private File _root;

	private List<BrickImplDirectory> _brickImplDirectories;

	BrickImplDirectoryFactoryImpl(File root) {
		_root = root;
	}

	private List<BrickImplDirectory> createImplDirectories() {
		List<BrickImplDirectory> result = new ArrayList<BrickImplDirectory>();
		SimpleFilter filter = new ImplDirectoryFinder(_root, DirectoryFileFilter.INSTANCE);
		List<File> implFolders = filter.list();
		for (File folder : implFolders) {
			BrickImplDirectory vd = new BrickImplDirectory(_root, folder);
			result.add(vd);
		}
		return result;
	}

	@Override
	public File rootDirectory() {
		return _root;
	}

	@Override
	public List<BrickImplDirectory> implDirectories() {
		if(_brickImplDirectories != null) {
			return _brickImplDirectories;
		}
		_brickImplDirectories = createImplDirectories();
		return _brickImplDirectories;
	}
}

class ImplDirectoryFinder extends SimpleFilter {

	public ImplDirectoryFinder(File root, FileFilter filter) {
		super(root, filter);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		if("impl".equals(directory.getName())) {
			results.add(directory);
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		//ignore files
	}
}
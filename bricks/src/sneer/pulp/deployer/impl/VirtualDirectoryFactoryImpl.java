package sneer.pulp.deployer.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import sneer.kernel.container.Injector;
import sneer.kernel.container.utils.io.SimpleFilter;

public class VirtualDirectoryFactoryImpl implements VirtualDirectoryFactory {

	private File _root;

	private List<VirtualDirectory> _brickDirectories;

	private Injector _injector;

	public VirtualDirectoryFactoryImpl(File root, Injector injector) {
		_root = root;
		_injector = injector;
	}

	private List<VirtualDirectory> createVirtualDirectories() {
		List<VirtualDirectory> result = new ArrayList<VirtualDirectory>();
		SimpleFilter filter = new ImplDirectoryFinder(_root, DirectoryFileFilter.INSTANCE);
		List<File> implFolders = filter.list();
		for (File folder : implFolders) {
			File parent = folder.getParentFile();
			String path = parent.getAbsolutePath().substring(_root.getAbsolutePath().length() + 1);
			VirtualDirectory vd = new VirtualDirectory(parent, path);
			_injector.inject(vd);
			result.add(vd);
		}
		return result;
	}

	@Override
	public File rootDirectory() {
		return _root;
	}

	@Override
	public List<VirtualDirectory> virtualDirectories() {
		if(_brickDirectories != null) {
			return _brickDirectories;
		}
		_brickDirectories = createVirtualDirectories();
		return _brickDirectories;
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
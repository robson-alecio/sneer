package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import sneer.bricks.deployer.impl.parser.JavaSource;
import sneer.bricks.deployer.impl.parser.JavaSourceParser;
import sneer.lego.utils.io.SimpleFilter;

public class SimpleSourceMeta implements SourceMeta {

	private File _root;

	private List<BrickDirectory> _brickDirectories = new ArrayList<BrickDirectory>();
	
	public SimpleSourceMeta(File root) {
		_root = root;
		prepare();
	}

	private void prepare() {
		SimpleFilter filter = new ImplDirectoryFinder(_root, DirectoryFileFilter.INSTANCE);
		List<File> implFolders = filter.list();
		for (File folder : implFolders) {
			File parent = folder.getParentFile();
			String path = parent.getAbsolutePath().substring(_root.getAbsolutePath().length() + 1);
			_brickDirectories.add(new BrickDirectory(parent, path));
		}
	}

	@Override
	public File rootDirectory() {
		return _root;
	}

	
	@Override
	public Map<File, List<File>> interfacesByBrick() {
		List<File> interfaces = interfaces();
		Map<File, List<File>> result = new HashMap<File, List<File>>();
		for (File file : interfaces) {
			File parent = file.getParentFile(); 
			List<File> interfacesInBrick = result.get(parent);
			if(interfacesInBrick == null) {
				interfacesInBrick = new ArrayList<File>();
				result.put(parent, interfacesInBrick);
			}
			interfacesInBrick.add(file);
		}
		return result;
	}
	

	@Override
	public List<File> interfaces() {
		List<File> result = new ArrayList<File>();
		for (BrickDirectory brickFolder : _brickDirectories) {
			result.addAll(brickFolder.api());
		}
		return result;
	}

	@Override
	public Map<File, List<File>> implByBrick() {
		SimpleFilter walker = new MyImplWalker(_root);
		List<File> files;
		files = walker.list();
		Map<File,List<File>> result = sortInDirectories(files);
		return result;
	}

	private Map<File, List<File>> sortInDirectories(List<File> files) {
		Map<File, List<File>> result = new HashMap<File, List<File>>();
		for (File file : files) {
			File parent = findSuitableParent(file);
			if(parent == null) {
				//not bellow .../impl/. How?
				continue;
			}
			
			List<File> filesInDirectory = result.get(parent);
			if(filesInDirectory == null) {
				filesInDirectory = new ArrayList<File>(); 
				result.put(parent, filesInDirectory);
			}
			filesInDirectory.add(file);
			
		}
		return result;
	}

	private File findSuitableParent(File file) {
		File parent = file.getParentFile();
		if(parent == null) 
			return null;
		if("impl".equals(parent.getName())) {
			return parent;
		}
		return findSuitableParent(parent);
	}

	@Override
	public List<BrickDirectory> brickDirectories() {
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

class MyImplWalker extends SimpleFilter {

	public MyImplWalker(File root) {
		super(root, JAVA_SOURCE_FILE_FILTER);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		JavaSource source = new JavaSourceParser(file).parse();
		if(!source.isInterface() && !source.isAccessPublic())
			results.add(file);
	}
}
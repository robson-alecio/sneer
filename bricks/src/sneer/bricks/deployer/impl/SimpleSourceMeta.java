package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import sneer.bricks.deployer.impl.parser.JavaSource;
import sneer.bricks.deployer.impl.parser.JavaSourceParser;
import sneer.lego.utils.FileUtils;
import sneer.lego.utils.io.FilteringDirectoryWalker;

public class SimpleSourceMeta implements SourceMeta {

	private File _root;

	public SimpleSourceMeta(File root) {
		_root = root;
	}

	@Override
	public File rootDirectory() {
		return _root;
	}

	
	private Map<File, List<File>> interfacesByBrick() {
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
		FilteringDirectoryWalker walker = new MyInterfaceWalker(_root);
		return walker.list();
	}

	@Override
	public Map<File, List<File>> implByBrick() {
		FilteringDirectoryWalker walker = new MyImplWalker(_root);
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
}


class MyInterfaceWalker extends FilteringDirectoryWalker {

	public MyInterfaceWalker(File root) {
		super(root, new OrFileFilter(new SuffixFileFilter(".java"), DirectoryFileFilter.INSTANCE));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		JavaSource source = new JavaSourceParser(file).parse();
		if(source.isInterface() /* && source.isAssignableTo(Brick.class) */)
			results.add(file);
//		else
//			System.out.println("Ignoring Interface: "+source);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		String name = directory.getName();
		boolean skip = name.startsWith(".") || "impl".equals(name) || FileUtils.isEmpty(directory);
		if(skip) return false;
		return true;
	}
}

class MyImplWalker extends FilteringDirectoryWalker {

	public MyImplWalker(File root) {
		super(root, new OrFileFilter(new SuffixFileFilter(".java"), DirectoryFileFilter.INSTANCE));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		JavaSource source = new JavaSourceParser(file).parse();
		if(!source.isInterface() && !source.isAccessPublic())
			results.add(file);
//		else
//			System.out.println("Ignoring Impl: "+source);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		String name = directory.getName();
		boolean skip = name.startsWith(".") || FileUtils.isEmpty(directory);
		if(skip) return false;
		return true;
	}
}
package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import sneer.bricks.deployer.impl.filters.InterfaceFinder;
import sneer.lego.Brick;
import sneer.lego.utils.JarBuilder;
import sneer.lego.utils.io.SimpleFilter;

public class BrickDirectory {

	
	private String _brickName;
	
	private File _root;
	
	private File _srcFolder;

	private File _binFolder;

	private String _path;
	
	private List<File> _apiClassFiles = new ArrayList<File>();
	
	private List<File> _apiSourceFiles = new ArrayList<File>();

	public BrickDirectory(File root, String path) {
		_path = path;
		_root = root;
	}

	public File rootDirectory() {
		return _root;
	}
	
	public String brickName() {
		return _brickName;
	}

	public void copyTo(File target) throws IOException {
		//System.out.println("Copying brick directory from: "+_root+" to: "+target);
		_srcFolder = new File(target, "src"+File.separator+_path);
		FileUtils.copyDirectory(_root, _srcFolder);
		_root = target;
	}

	public List<File> api() {
		if(_apiSourceFiles.size() > 0)
			return _apiSourceFiles;
		
		SimpleFilter walker = new InterfaceFinder(_srcFolder);
		_apiSourceFiles = walker.list(); 
		return _apiSourceFiles;
	}

	public void grabCompiledClasses(File apiDirectory) throws IOException {
		_binFolder = new File(_root, "bin");
		File target = new File(_binFolder,_path);
		ClassLoader cl = classLoaderFromRootFolder();
		List<File> sourceFiles = api();
		for (File sourceFile : sourceFiles) {
			String fileName = sourceFile.getName().replace(".java", ".class");
			String className = _path + File.separator + fileName;
			File classFile = new File(apiDirectory, className);
			if(classFile.exists()) {
				FileUtils.copyFileToDirectory(classFile, target);
				_apiClassFiles.add(new File(target, fileName));
				className = className.substring(0, className.indexOf(".class"));
				
				if(_brickName == null) 
					tryBrickName(cl, className);
				
			} else {
				FileUtils.deleteQuietly(sourceFile);
			}
		}
		
	}

	private void tryBrickName(ClassLoader cl, String className) {
		Class<?> c;
		className = FilenameUtils.separatorsToUnix(className);
		className = className.replaceAll("/", ".");
		try {
			c = cl.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
		if(Brick.class.isAssignableFrom(c))
			_brickName = className;
	}

	private ClassLoader classLoaderFromRootFolder() {
		try {
			return  new URLClassLoader(new URL[]{new URL("file://"+_root.getAbsolutePath()+"/")});
		} catch (MalformedURLException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e);
		}
	}

	public JarFile jarSrcApi() throws IOException {
		
		return jar(_apiSourceFiles, brickName()+"-src");
	}

	public JarFile jarBinaryApi() throws IOException {
		return jar(_apiClassFiles, brickName());
	}
	
	private JarFile jar(List<File> files, String jarName) throws IOException {
		File tmp = File.createTempFile(jarName+"-", ".jar");
		JarBuilder builder = JarBuilder.builder(tmp.getAbsolutePath());
		for(File file : files) {
			String entryName = _path + File.separator + file.getName();
			builder.add(entryName, file);
		}
		return new JarFile(builder.close());
	}
}

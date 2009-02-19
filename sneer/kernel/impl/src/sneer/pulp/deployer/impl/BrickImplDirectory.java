package sneer.pulp.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import sneer.kernel.container.Brick;
import sneer.kernel.container.impl.classloader.MetaClassClassLoader;
import sneer.kernel.container.jar.DeploymentJar;
import sneer.kernel.container.jar.DeploymentJarFactory;
import sneer.pulp.deployer.DeployerException;
import sneer.pulp.deployer.impl.filters.ImplFinder;
import sneer.pulp.deployer.impl.filters.ApiSourceFileFinder;
import sneer.pulp.deployer.impl.filters.LibFinder;
import wheel.io.JarBuilder;
import wheel.io.codegeneration.MetaClass;
import wheel.io.codegeneration.SimpleFilter;
import wheel.lang.Collections;
import wheel.lang.Functor;
import static wheel.lang.Environments.my;

class BrickImplDirectory {

	private final DeploymentJarFactory _sneerDeploymentFactory = my(DeploymentJarFactory.class);
	
	private String _brickName;
	
	private File _root;

	private File _path;
	
	private List<File> _jarFiles;
	
	private ImplClasses _implClasses;
	
	private List<File> _apiSourceFiles = new ArrayList<File>();
	
	private List<File> _implSourceFiles = new ArrayList<File>();

	private final List<MetaClass> _apiClassFiles = new ArrayList<MetaClass>();

	BrickImplDirectory(File root, File path) {
		_root = root;
		_path = path;
	}

	public File rootDirectory() {
		return _root;
	}
	
	public String brickName() {
		return _brickName;
	}

	public List<File> api() {
		if(_apiSourceFiles.size() > 0)
			return _apiSourceFiles;
		
		SimpleFilter walker = new ApiSourceFileFinder(_path.getParentFile());
		_apiSourceFiles = walker.list(); 
		return _apiSourceFiles;
	}

	public List<File> files() {
		if(_implSourceFiles.size() > 0)
			return _implSourceFiles;
		
		SimpleFilter walker = new ImplFinder(_path);
		_implSourceFiles = walker.list(); 
		return _implSourceFiles;
	}

	public List<File> libs() {
		if(_jarFiles != null && _jarFiles.size() > 0)
			return _jarFiles;
		
		SimpleFilter walker = new LibFinder(new File(_path, "lib"));
		_jarFiles = walker.list(); 
		return _jarFiles;
	}

	public void grabCompiledClasses(List<MetaClass> classFiles) {
		ClassLoader cl = classLoaderFor(classFiles);
		for (File apiFile : api()) {
			String className = toClassName(apiFile);
			MetaClass classFile = findMetaClass(classFiles, className);
			
			_apiClassFiles.add(classFile);

			if (!isBrick(cl, className)) continue;
			
			if(_brickName != null)
				throw new DeployerException(
						"There can only be one! "
						+ "Two brick interfaces were found in "
						+ _root + ": "
						+ _brickName + ", " + className);
			
			_brickName = className;
					
		}
		if(_brickName == null)
			throw new DeployerException("Can't find main brick interface in "+_path);
	}

	private ClassLoader classLoaderFor(List<MetaClass> classFiles) {
		return new MetaClassClassLoader(classFiles, getClass().getClassLoader());
	}

	private String toClassName(File sourceFile) {
		String className = relativePath(sourceFile) + File.separator + sourceFile.getName();
		className = FilenameUtils.separatorsToUnix(className);
		className = className.replaceAll("/", ".");
		return className.substring(0, className.indexOf(".java"));
	}

	private String relativePath(File sourceFile) {
		return relativePath(_root, sourceFile);
	}

	private String relativePath(File base, File file) {
		if (!file.getAbsolutePath().startsWith(base.getAbsolutePath()))
			throw new IllegalArgumentException("'" + file + "' must be rooted at '" + base + "'.");
		return file.getAbsolutePath().substring(base.getAbsolutePath().length() + 1);
	}

	private MetaClass findMetaClass(List<MetaClass> classFiles, String className) {
		for(MetaClass metaClass : classFiles) {
			if(className.equals(metaClass.getName()))
				return metaClass;
		}
		return null;
	}

	private boolean isBrick(ClassLoader cl, String className) {
		try {
			Class<?> c = cl.loadClass(className);
			return Brick.class.isAssignableFrom(c);
		} catch (ClassNotFoundException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

	public DeploymentJar jarSrcApi() {
		return jarFromFiles(_apiSourceFiles, "api-src");
	}

	public DeploymentJar jarSrcImpl() {
		return jarFromFiles(_implSourceFiles, "impl-src");
	}

	public DeploymentJar jarBinaryApi() {
		return jar(Collections.map(_apiClassFiles, new Functor<MetaClass, JarEntrySpec>() {

			@Override
			public JarEntrySpec evaluate(final MetaClass value) {
				return new JarEntrySpec() {
					@Override
					public File file() {
						return value.classFile();
					}
				
					@Override
					public String entryName() {
						return value.getName().replace('.', '/') + ".class";
					}
				};
			}
		}),
		"api");
	}

	public DeploymentJar jarBinaryImpl() {
		return jar(Collections.map(_implClasses._files, new Functor<File, JarEntrySpec>() { @Override public JarEntrySpec evaluate(final File value) {
					return new JarEntrySpec() {
						@Override
						public File file() {
							return value;
						}
					
						@Override
						public String entryName() {
							return relativePath(_implClasses._baseDirectory, value);
						}
					};	
				}}),
				"impl");
	}

	private DeploymentJar jarFromFiles(Iterable<File> files, String role) {
		return jar(Collections.map(files, new Functor<File, JarEntrySpec>() { @Override public JarEntrySpec evaluate(final File value) {
				return new JarEntrySpec() {
					@Override
					public File file() {
						return value;
					}
				
					@Override
					public String entryName() {
						return relativePath(value);
					}
				};
			}}), role);
	}
	
	static class ImplClasses {
		public final File _baseDirectory;
		public final List<File> _files;
		
		public ImplClasses(File baseDirectory, List<File> files) {
			_baseDirectory = baseDirectory;
			_files = files;
		}
	}

	public void setImplClasses(File baseDirectory, List<MetaClass> classFiles) {
		List<File> implClassFiles = new ArrayList<File>();
		for (MetaClass metaClass : classFiles) {
			implClassFiles.add(metaClass.classFile());
		}
		_implClasses = new ImplClasses(baseDirectory, implClassFiles);
	}
	
	interface JarEntrySpec {

		String entryName();

		File file();
		
	}

 	private DeploymentJar jar(Iterable<JarEntrySpec> files, String role) {
		try {
			File tmpJarFile = createTempJar(role, files);
			return _sneerDeploymentFactory.create(tmpJarFile);
		} catch (IOException e) {
			throw new DeployerException("Error", e);
		} 
	}

	private File createTempJar(String role, Iterable<JarEntrySpec> files)
			throws IOException {
		final String brickName = brickName();
		final String jarName = brickName + "-" + role;
		final File tmpJarFile = File.createTempFile(jarName+"-", ".jar");
		final JarBuilder builder = new JarBuilder(tmpJarFile);
		try {
			//sneer meta
			String meta = sneerMeta(brickName, "1.0-SNAPSHOT", role);
			builder.add("sneer.meta", meta);

			for(JarEntrySpec file : files) {
				builder.add(file.entryName(), file.file());
			}
		} finally {
			builder.close();
		}
		return tmpJarFile;
	}

	private String sneerMeta(String brickName, String version, String role) {
		StringBuilder builder = new StringBuilder();
		builder.append("brick-name: ").append(brickName).append("\n");
		builder.append("brick-version: ").append(version).append("\n");
		builder.append("role: ").append(role).append("\n");
		return builder.toString();
	}

	@Override
	public String toString() {
		return "VirtualDirectory for " + _brickName;
	}
}

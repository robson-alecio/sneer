package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;

import sneer.bricks.classpath.Classpath;
import sneer.bricks.classpath.ClasspathFactory;
import sneer.bricks.compiler.Compiler;
import sneer.bricks.compiler.Result;
import sneer.bricks.config.SneerConfig;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.Deployer;
import sneer.bricks.deployer.DeployerException;
import sneer.bricks.log.Logger;
import sneer.lego.Brick;
import sneer.lego.Inject;
import sneer.lego.utils.FileUtils;
import sneer.lego.utils.metaclass.MetaClass;
import wheel.lang.exceptions.NotImplementedYet;

public class DeployerImpl implements Deployer {

	@Inject
	private SneerConfig _config;
	
	@Inject
	private Compiler _compiler;
	
	@Inject
	private ClasspathFactory _cpFactory;
	
	@Inject
	private Logger log;
	
	@Override
	public List<String> list() {
		File root = brickRootDirectory();
		String[] result = root.list(DirectoryFileFilter.INSTANCE);
		return Arrays.asList(result);
	}

	private File brickRootDirectory() {
		File home = _config.sneerDirectory();
		File result = FileUtils.concat(home.getAbsolutePath(), "bricks");
		return result;
	}

	@Override
	public void deploy(BrickBundle brickBundle) {
		log.info("Deploying BrickBundle");
		List<String> brickNames = brickBundle.brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = brickBundle.brick(brickName);
			try {
				deploy(brick);
			} catch (Throwable t) {
				throw new DeployerException("Error deploying brick: "+brickName, t);
			}
		}
	}

	private void deploy(BrickFile brick) throws IOException {
		String brickName = brick.name();
		log.debug("Deploying brick: "+brickName);
		
		//1. create brick directory under sneer home
		File root = brickRootDirectory();
		File brickDirectory = new File(root, brickName);
		if(brickDirectory.exists()) {
			//FixUrgent: ask permission to overwrite?
			org.apache.commons.io.FileUtils.cleanDirectory(brickDirectory);
		} else {
			brickDirectory.mkdir();
		}
		
		//2. copy received files
		File orig = new File(brickDirectory, "orig");
		brick.copyTo(orig);
		
		//3. explode brick
		brick.explode(brickDirectory);
		
		//4. resolve brick dependencies
		resolveBrickDependencies(brick);
	}

	private void resolveBrickDependencies(BrickFile brick) {
		brick.toString();
		throw new NotImplementedYet();
	}

	@Override
	public BrickBundle pack(File path) {
		/*
		 * 
		 * 1. build class path from meta file
		 * 2. compile code
		 * 3. perform checks
		 * 		3.1 assert that there is only one brick hierarchy inside each package
		 * 		3.2 assert that all classes inside the api dir are interfaces
		 * 		3.3 assert that all classes inside impl/ are private
		 * 		3.4 fail if main api changes
		 * 4. generate hashes
		 * 5. generate brick-api.jar and brick-impl.jar
		 * 
		 */

		BrickBundle result = new BrickBundleImpl();
		VirtualDirectoryFactory factory = new VirtualDirectoryFactoryImpl(path);
		List<VirtualDirectory> brickFolders = factory.brickDirectories();

		/*
		 * API 
		 */
		File apiDirectory = createWorkDirectory("api");
		List<File> interfaceFiles = new ArrayList<File>();
		for (VirtualDirectory brickFolder : brickFolders) {
			interfaceFiles.addAll(brickFolder.api());
		}
		List<MetaClass> classFiles = compileApi(apiDirectory, interfaceFiles);
		grabCompiledClasses(brickFolders, classFiles);
		List<JarFile> jarFiles = generateApiJars(brickFolders);
		addToResult(result, jarFiles);
		
		
		/*
		 * IMPL
		 */
		Classpath api = _cpFactory.fromDirectory(apiDirectory);
		for (VirtualDirectory brickFolder : brickFolders) {
			File implDirectory = createWorkDirectory(brickFolder.brickName());
			classFiles = compileImpl(implDirectory, brickFolder, api);
		}
		
		if(true) throw new NotImplementedYet();
		List<Class<Brick>> brickInterfaces = null; //findBrickInterfaces(api);
		
		jarFiles = compileBricksAndGenerateJars(brickInterfaces, factory, api);
		addToResult(result, jarFiles);
		
		return result;
	}

	private List<MetaClass> compileImpl(File workDirectory, VirtualDirectory brickFolder, Classpath api) {
		System.out.println("Compiling brick impl: "+brickFolder.brickName());
		Classpath sneerApi = _cpFactory.sneerApi();
		Classpath cp = sneerApi.compose(api);
		Classpath libs = _cpFactory.newClasspath(); //Fix: search for brick libs
		List<File> sourceFilesInBrick = brickFolder.impl();
		Result compilationResult = _compiler.compile(sourceFilesInBrick, workDirectory, cp.compose(libs));
		if(!compilationResult.success()) {
			throw new DeployerException("Error compiling brick implementation: "+brickFolder.brickName());
		}
		return compilationResult.compiledClasses();
	}

	private List<JarFile> generateApiJars(List<VirtualDirectory> brickFolders) {
		List<JarFile> result = new ArrayList<JarFile>();
		for (VirtualDirectory brickFolder : brickFolders) {
			try {
				result.add(brickFolder.jarSrcApi());
				result.add(brickFolder.jarBinaryApi());
			} catch (IOException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
			}
		}
		return result;
	}

	private void grabCompiledClasses(List<VirtualDirectory> brickFolders, List<MetaClass> classFiles) {
		for (VirtualDirectory brickFolder : brickFolders) 
			brickFolder.grabCompiledClasses(classFiles);
	}

	private void addToResult(BrickBundle result, List<JarFile> jarFiles) {
		try {
			for (JarFile jarFile : jarFiles)
				result.add(jarFile);
		} catch (IOException e) {
			throw new DeployerException("Error adding jar entry to brick bundle",e);
		}
	}

	private List<JarFile> compileBricksAndGenerateJars(List<Class<Brick>> brickInterfaces, VirtualDirectoryFactory meta, Classpath api) {
		List<JarFile> result = new ArrayList<JarFile>();
		
		Map<File,List<File>> brickFilesByDirectory = null; //meta.implByBrick();
		Classpath sneerApi = _cpFactory.sneerApi();
		Classpath cp = sneerApi.compose(api);
		int i = 0;
		for (File brickImplDir : brickFilesByDirectory.keySet()) {
			File workDirectory = createWorkDirectory("impl-"+(i++));
			List<File> sourceFilesInBrick = brickFilesByDirectory.get(brickImplDir);
			Classpath libs = _cpFactory.newClasspath(); //Fix: search for brick libs
			Result compilationResult = _compiler.compile(sourceFilesInBrick, workDirectory, cp.compose(libs));
			if(!compilationResult.success()) {
				throw new DeployerException("Error compiling brick implementation from: "+brickImplDir);
			}
			
			/*
			 * generate impl jar
			 */
			Classpath impl = _cpFactory.fromDirectory(workDirectory);
			log.info("Searching single brick inside {}",workDirectory);
			Class<Brick> brickClass = findSingleBrickImpl(impl);
			String brickName = brickNameFromImpl(brickClass, brickInterfaces);
			File path = impl.relativeFile(brickClass);
			try {
				JarFile jarFile = null; //runJarTool(brickName,"IMPL", workDirectory, path.getParentFile());
				result.add(jarFile);
			} catch (Exception e) {
				throw new DeployerException("Can't package brick impl "+brickName);
			}
		}
		return result;
	}

	private String brickNameFromImpl(Class<Brick> brickClass, List<Class<Brick>> brickInterfaces) {
		for(Class<Brick> brickInterface : brickInterfaces) {
			if(brickInterface.isAssignableFrom(brickClass))
				return brickInterface.getName();
		}
		throw new DeployerException("Can't find suitable brick name for: "+ brickClass.getName());
	}

	private Class<Brick> findSingleBrickImpl(Classpath classpath) {
		Class<Brick> result = null;
		try {
			List<Class<Brick>> list = classpath.findAssignableTo(Brick.class);
			if(list.size() == 1) {
				result = list.get(0);
				return result;
			}
		} catch (ClassNotFoundException e1) {
			throw new DeployerException("Can't find brick implementation on "+classpath);
		}
		throw new DeployerException("Can't find brick implementation on "+classpath);
	}

	/*
	 * search for all interfaces that extend Brick 
	 */
//	private List<Class<Brick>> findBrickInterfaces(Classpath classpath) {
//		List<Class<Brick>> result;
//		try {
//			 result = classpath.findAssignableTo(Brick.class);
//		} catch (ClassNotFoundException e) {
//			throw new DeployerException("Can't find any bricks on compiled interfaces", e);
//		}
//		return result; 
//	}
	

	/*
	 * compile interfaces first. Forces clean separation
	 */
	private List<MetaClass> compileApi(File workDirectory, List<File> interfaces) {
		Classpath sneerApi = _cpFactory.sneerApi();
		Result result = _compiler.compile(interfaces, workDirectory, sneerApi);
		if(!result.success()) {
			throw new DeployerException("Error compiling interfaces");
		}
		return result.compiledClasses();
	}

	private File createWorkDirectory(String name) {
		String fullname = _config.tmpDirectory() + "/sneer/bricks/compiler/work/" + name;
		File workDirectory = new File(FilenameUtils.separatorsToSystem(fullname));
		if(!workDirectory.exists()) workDirectory.mkdirs();
		try {
			org.apache.commons.io.FileUtils.cleanDirectory(workDirectory);
		} catch (IOException e) {
			throw new DeployerException("Can't create work directory: "+name);
		}
		return workDirectory;
	}
}
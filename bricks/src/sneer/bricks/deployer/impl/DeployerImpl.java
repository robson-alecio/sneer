package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import sneer.bricks.classpath.Classpath;
import sneer.bricks.classpath.ClasspathFactory;
import sneer.bricks.compiler.Compiler;
import sneer.bricks.compiler.Result;
import sneer.bricks.config.SneerConfig;
import sneer.bricks.dependency.Dependency;
import sneer.bricks.dependency.DependencyManager;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.Deployer;
import sneer.bricks.deployer.DeployerException;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.utils.metaclass.MetaClass;

public class DeployerImpl implements Deployer {

	@Inject
	private SneerConfig _config;
	
	@Inject
	private Compiler _compiler;
	
	@Inject
	private ClasspathFactory _cpFactory;
	
	@Inject
	private Injector _injector;
	
	@Inject
	private DependencyManager _dependencies;
	
	
//	public void deploy(BrickBundle received) {
//		log.info("Deploying BrickBundle");
//		File workDirectory = createWorkDirectory("received");
//		
//		recreateOriginalDirectoryStructureWhenPublished(received, workDirectory);
//		BrickBundle repacked = pack(workDirectory);
//		
//		deployRepacked(repacked);
//	}

//	private void recreateOriginalDirectoryStructureWhenPublished(BrickBundle brickBundle, File workDirectory) {
//		List<String> brickNames = brickBundle.brickNames();
//		for (String brickName : brickNames) {
//			BrickFile brick = brickBundle.brick(brickName);
//			try {
//				brick.explodeSources(workDirectory);
//			} catch (Throwable t) {
//				throw new DeployerException("Error exploding brick sources: "+brickName, t);
//			}
//		}
//	}

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
		VirtualDirectoryFactory factory = new VirtualDirectoryFactoryImpl(path, _injector);
		List<VirtualDirectory> virtualDirectories = factory.virtualDirectories();

		/*
		 * API 
		 */
		File apiDirectory = createWorkDirectory("api");
		List<File> interfaceFiles = mergeInterfaceFiles(virtualDirectories);
		List<MetaClass> classFiles = compileApi(apiDirectory, interfaceFiles);
		for (VirtualDirectory virtual : virtualDirectories) {
			virtual.grabCompiledClasses(classFiles);
			result.add(virtual.jarSrcApi());
			result.add(virtual.jarBinaryApi());
		}
		
		/*
		 * IMPL
		 */
		Classpath api = _cpFactory.fromDirectory(apiDirectory);
		for (VirtualDirectory virtual : virtualDirectories) {
			File implDirectory = createWorkDirectory(virtual.brickName());
			classFiles = compileImpl(implDirectory, virtual, api);
			virtual.setImplClasses(classFiles);
			result.add(virtual.jarSrcImpl());
			result.add(virtual.jarBinaryImpl());
		}
		
		/*
		 * LIBS 
		 */
		for (VirtualDirectory virtual : virtualDirectories) {
			String brickName = virtual.brickName();
			List<File> dependencies = virtual.libs();
			addDepedencies(result.brick(brickName), dependencies);
		}
		
		return result;
	}

	private void addDepedencies(BrickFile brick, List<File> dependencies) {
		for (File file : dependencies) {
			Dependency dependency = _dependencies.newDependency(file);
			brick.dependencies().add(dependency);
		}
	}

	private List<File> mergeInterfaceFiles( List<VirtualDirectory> virtualDirectories) {
		List<File> interfaceFiles = new ArrayList<File>();
		for (VirtualDirectory virtual : virtualDirectories) {
			interfaceFiles.addAll(virtual.api());
		}
		return interfaceFiles;
	}

	private Classpath classpath(VirtualDirectory virtual, Classpath api) {
		Classpath sneerApi = _cpFactory.sneerApi();
		Classpath cp = sneerApi.compose(api);
		List<File> jarFiles = virtual.libs();
		Classpath libs = _cpFactory.fromJarFiles(jarFiles);
		return cp.compose(libs);
	}
	
	private List<MetaClass> compileImpl(File workDirectory, VirtualDirectory virtual, Classpath api) {
		List<File> sourceFilesInBrick = virtual.impl();
		
		if(sourceFilesInBrick == null || sourceFilesInBrick.isEmpty())
			throw new DeployerException("Can't find source files in "+virtual.rootDirectory()+". Check if your class files are public (they shouldn't be)");

		Classpath cp = classpath(virtual, api);
		//System.out.println("Compiling "+virtual.brickName());
		Result compilationResult = _compiler.compile(sourceFilesInBrick, workDirectory, cp);
		if(!compilationResult.success()) {
			throw new DeployerException("Error compiling brick implementation: "+virtual.brickName());
		}
		return compilationResult.compiledClasses();
	}

	private List<MetaClass> compileApi(File workDirectory, List<File> interfaces) {
		Classpath sneerApi = _cpFactory.sneerApi();
		Result result = _compiler.compile(interfaces, workDirectory, sneerApi);
		if(!result.success()) {
			throw new DeployerException("Error compiling brick interfaces: " + result.getErrorString());
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
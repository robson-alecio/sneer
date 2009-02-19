package sneer.pulp.deployer.impl;

import static wheel.lang.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import sneer.kernel.container.SneerConfig;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import sneer.pulp.compiler.JavaCompiler;
import sneer.pulp.compiler.Result;
import sneer.pulp.dependency.FileWithHash;
import sneer.pulp.dependency.DependencyManager;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.BrickFile;
import sneer.pulp.deployer.Deployer;
import sneer.pulp.deployer.DeployerException;
import wheel.io.codegeneration.MetaClass;

class DeployerImpl implements Deployer {

	private final SneerConfig _config = my(SneerConfig.class);
	
	private final JavaCompiler _compiler = my(JavaCompiler.class);
	
	private final ClasspathFactory _cpFactory = my(ClasspathFactory.class);
	
	private final DependencyManager _dependencies = my(DependencyManager.class);
	
	
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
		BrickImplDirectoryFactory factory = new BrickImplDirectoryFactoryImpl(path);
		List<BrickImplDirectory> implDirectories = factory.implDirectories();

		/*
		 * API 
		 */
		File apiDirectory = createWorkDirectory("api");
		List<File> interfaceFiles = mergeInterfaceFiles(implDirectories);
		List<MetaClass> classFiles = compileApi(apiDirectory, interfaceFiles);
		for (BrickImplDirectory virtual : implDirectories) {
			virtual.grabCompiledClasses(classFiles);
			result.add(virtual.jarSrcApi());
			result.add(virtual.jarBinaryApi());
		}
		
		/*
		 * IMPL
		 */
		Classpath api = _cpFactory.fromClassDir(apiDirectory);
		for (BrickImplDirectory virtual : implDirectories) {
			File implDirectory = createWorkDirectory(virtual.brickName());
			classFiles = compileImpl(implDirectory, virtual, api);
			virtual.setImplClasses(implDirectory, classFiles);
			result.add(virtual.jarSrcImpl());
			result.add(virtual.jarBinaryImpl());
		}
		
		/*
		 * LIBS 
		 */
		for (BrickImplDirectory virtual : implDirectories) {
			String brickName = virtual.brickName();
			List<File> dependencies = virtual.libs();
			addDepedencies(result.brick(brickName), dependencies);
		}
		
		return result;
	}

	private void addDepedencies(BrickFile brick, List<File> dependencies) {
		for (File file : dependencies) {
			FileWithHash dependency = _dependencies.newDependency(file);
			brick.fileDependencies().add(dependency);
		}
	}

	private List<File> mergeInterfaceFiles( List<BrickImplDirectory> implDirectories) {
		List<File> interfaceFiles = new ArrayList<File>();
		for (BrickImplDirectory impl : implDirectories) {
			interfaceFiles.addAll(impl.api());
		}
		return interfaceFiles;
	}

	private Classpath classpath(BrickImplDirectory impl, Classpath api) {
		Classpath sneerApi = _cpFactory.sneerApi();
		Classpath cp = sneerApi.compose(api);
		List<File> jarFiles = impl.libs();
		Classpath libs = _cpFactory.fromJarFiles(jarFiles);
		return cp.compose(libs);
	}
	
	private List<MetaClass> compileImpl(File workDirectory, BrickImplDirectory impl, Classpath api) {
		List<File> sourceFilesInBrick = impl.files();
		
		if(sourceFilesInBrick == null || sourceFilesInBrick.isEmpty())
			throw new DeployerException("Can't find source files in "+impl.rootDirectory()+". Check if your class files are public (they shouldn't be)");

		Classpath cp = classpath(impl, api);
		Result result = _compiler.compile(sourceFilesInBrick, workDirectory, cp);
		if(!result.success())
			throw new DeployerException("Error compiling brick implementation: " + impl.brickName() + "\n" + result.getErrorString());
		return result.compiledClasses();
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
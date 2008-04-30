package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

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
	public void deploy(BrickBundle bundle) {
		log.info("Deploying BrickBundle");
		File workDirectory = createWorkDirectory("received");
		
		recreateOriginalDirectoryStructureWhenPublished(bundle, workDirectory);
		BrickBundle repacked = pack(workDirectory);
		
		deployRepacked(repacked);
		throw new NotImplementedYet();
	}

	private void deployRepacked(BrickBundle bundle) {
		List<String> brickNames = bundle.brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = bundle.brick(brickName);
			try {
				deploy(brick);
			} catch (Throwable t) {
				throw new DeployerException("Error deploying brick: "+brickName, t);
			}
		}
	}

	private void recreateOriginalDirectoryStructureWhenPublished(BrickBundle brickBundle, File workDirectory) {
		List<String> brickNames = brickBundle.brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = brickBundle.brick(brickName);
			try {
				brick.explodeSources(workDirectory);
			} catch (Throwable t) {
				throw new DeployerException("Error exploding brick sources: "+brickName, t);
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
		brick.copyTo(brickDirectory);
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
		
		return result;
	}

	private List<File> mergeInterfaceFiles( List<VirtualDirectory> virtualDirectories) {
		List<File> interfaceFiles = new ArrayList<File>();
		for (VirtualDirectory virtual : virtualDirectories) {
			interfaceFiles.addAll(virtual.api());
		}
		return interfaceFiles;
	}

	private List<MetaClass> compileImpl(File workDirectory, VirtualDirectory virtual, Classpath api) {
		Classpath sneerApi = _cpFactory.sneerApi();
		Classpath cp = sneerApi.compose(api);
		Classpath libs = _cpFactory.newClasspath(); //Fix: search for brick libs
		List<File> sourceFilesInBrick = virtual.impl();
		Result compilationResult = _compiler.compile(sourceFilesInBrick, workDirectory, cp.compose(libs));
		if(!compilationResult.success()) {
			throw new DeployerException("Error compiling brick implementation: "+virtual.brickName());
		}
		return compilationResult.compiledClasses();
	}

	private List<MetaClass> compileApi(File workDirectory, List<File> interfaces) {
		Classpath sneerApi = _cpFactory.sneerApi();
		Result result = _compiler.compile(interfaces, workDirectory, sneerApi);
		if(!result.success()) {
			throw new DeployerException("Error compiling brick interfaces");
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
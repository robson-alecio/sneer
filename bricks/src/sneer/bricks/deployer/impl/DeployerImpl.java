package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;

import sneer.bricks.classpath.Classpath;
import sneer.bricks.classpath.ClasspathFactory;
import sneer.bricks.compiler.Compiler;
import sneer.bricks.compiler.Result;
import sneer.bricks.config.SneerConfig;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.Deployer;
import sneer.bricks.deployer.DeployerException;
import sneer.bricks.log.Logger;
import sneer.lego.Brick;
import sneer.lego.Inject;
import sneer.lego.utils.FileUtils;
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
	public void deploy(BrickBundle brick) {
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
		File workDirectory = createWorkDirectory();
		SourceMeta meta = loadSourceMetaFromFile(path);
		//Classpath bootstrap = _cpFactory.newClasspath();
		
		/*
		 * API 
		 */
		Classpath api = compileInterfaces(workDirectory, meta);
		List<Class<Brick>> brickInterfaces = findBrickInterfaces(api);
		List<JarFile> jarFiles = generateApiJars(brickInterfaces, workDirectory, api);
		//Fix: deploy jar files
		for (JarFile jarFile : jarFiles) {
			System.out.println("TODO: deploy "+jarFile.getName());
		}
		
		/*
		 * IMPL
		 */
		jarFiles = compileBricksAndGenerateJars(brickInterfaces, meta, api);
		//Fix: deploy jar files
		for (JarFile jarFile : jarFiles) {
			System.out.println("TODO: deploy "+jarFile.getName());
		}
		
		return null;

	}

	private List<JarFile> compileBricksAndGenerateJars(List<Class<Brick>> brickInterfaces, SourceMeta meta, Classpath api) {
		List<JarFile> result = new ArrayList<JarFile>();
		
		Map<File,List<File>> brickFilesByDirectory = meta.implByBrick();
		int i = 0;
		Classpath sneerApi = _cpFactory.sneerApi();
		Classpath cp = sneerApi.compose(api);
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
			String brickName = null;
			File path = impl.relativeFile(brickClass);
			try {
				JarFile jarFile = runJarTool(brickName+"-IMPL", workDirectory, path.getParentFile());
				result.add(jarFile);
			} catch (Exception e) {
				throw new DeployerException("Can't package brick impl "+brickName);
			}
		}
		return result;
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
	private List<Class<Brick>> findBrickInterfaces(Classpath classpath) {
		List<Class<Brick>> result;
		try {
			 result = classpath.findAssignableTo(Brick.class);
		} catch (ClassNotFoundException e) {
			throw new DeployerException("Can't find any bricks on compiled interfaces", e);
		}
		return result; 
	}
	
	/*
	 * generate api jars.
	 */
	private List<JarFile> generateApiJars(List<Class<Brick>> brickInterfaces, File workDirectory, Classpath classpath) {
		List<JarFile> result = new ArrayList<JarFile>(); 
		for (Class<Brick> brickClass : brickInterfaces) {
			String brickName = brickClass.getName();
			File origin = classpath.absoluteFile(brickClass);
			File path = classpath.relativeFile(brickClass);
			log.info("exporting brick api {} from: {}", brickName, origin.getParentFile());
			try {
				JarFile jarFile = runJarTool(brickName+"-API", workDirectory, path.getParentFile());
				result.add(jarFile);
			} catch (Exception e) {
				throw new DeployerException("Can't package brick api "+brickName);
			}
		}
		return result;
	}

	/*
	 * compile interfaces first. Forces clean separation
	 */
	private Classpath compileInterfaces(File workDirectory, SourceMeta meta) {
		List<File> interfaces = meta.interfaces();
		Classpath sneerApi = _cpFactory.sneerApi();
		Result compilationResult = _compiler.compile(interfaces, workDirectory, sneerApi);
		if(!compilationResult.success()) {
			throw new DeployerException("Error compiling interfaces");
		}
		Classpath result = _cpFactory.fromDirectory(workDirectory);
		return result;
	}

	private File createWorkDirectory() {
		return createWorkDirectory("api");
	}

	private File createWorkDirectory(String name) {
		String fullname = _config.tmpDirectory() + "/sneer/bricks/compiler/work/" + name;
		File workDirectory = new File(fullname);
		if(!workDirectory.exists()) workDirectory.mkdirs();
		try {
			org.apache.commons.io.FileUtils.cleanDirectory(workDirectory);
		} catch (IOException e) {
			throw new DeployerException("Can't create work directory: "+name);
		}
		return workDirectory;
	}

	private SourceMeta loadSourceMetaFromFile(File path) {
		return new SimpleSourceMeta(path);
	}

	private JarFile runJarTool(String jarName, File baseDirectory, File path) throws Exception {

		//FixUrgent: remove dependency from jdk by not calling the jar tool
		
		File jar = File.createTempFile(jarName+"-", ".jar");
		ProcessBuilder builder = new ProcessBuilder("jar", "cfv", jar.getAbsolutePath(), path.getPath());
		builder.directory(baseDirectory);

		if(log.isDebugEnabled()) {
			String command = StringUtils.join(builder.command(), " ");
			log.debug("executing command: {}"+command);
		}

		Process p = builder.start();
		p.waitFor();
		return new JarFile(jar);
	}

//	private File createMetaFile(String brickName, String version) throws IOException {
//		String jarName = brickName + "-" + version;
//		StringBuilder builder = new StringBuilder();
//		builder.append("brick-name: ").append(brickName).append("\n");
//		builder.append("brick-version: ").append(version).append("\n");
//		File manifest = File.createTempFile(jarName+"-", ".meta");
//		org.apache.commons.io.FileUtils.writeStringToFile(manifest, builder.toString());
//		return manifest;
//	}


}

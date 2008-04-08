package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;

import sneer.lego.utils.FileUtils;
import sneer.bricks.compiler.Classpath;
import sneer.bricks.compiler.Compiler;
import sneer.bricks.compiler.Result;
import sneer.bricks.config.SneerConfig;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.Deployer;
import sneer.bricks.deployer.DeployerException;
import sneer.bricks.log.Logger;
import sneer.lego.Inject;

public class DeployerImpl implements Deployer {

	@Inject
	private SneerConfig config;
	
	@Inject
	private Compiler _compiler;
	
	@Inject
	private Logger log;
	
	@Override
	public List<String> list() {
		File root = brickRootDirectory();
		String[] result = root.list(DirectoryFileFilter.INSTANCE);
		return Arrays.asList(result);
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
		
		/*
		 * compile interfaces first. Forces clean separation
		 */
		List<File> interfaces = meta.interfaces();
		Result compilationResult = _compiler.compile(interfaces, workDirectory);
		if(!compilationResult.success()) {
			throw new DeployerException("Error compiling interfaces");
		}

		/*
		 * generate api jars.
		 */
		Map<File,List<File>> interfacesByBrick = meta.interfacesByBrick();
		for (File file : interfacesByBrick.keySet()) {
			try {
				JarFile brickApi = runJarTool("brick-api", "0.1-dev", file);
			} catch (Exception e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
			}
		}
		
		//compile implementations
		Classpath api = null;
		Map<File,List<File>> brickFilesByDirectory = meta.implByBrick(); 
		for (File brickImplDir : brickFilesByDirectory.keySet()) {
			List<File> filesInBrick = brickFilesByDirectory.get(brickImplDir);
			Classpath libs;
			Classpath classpath = null; //TODO: compose api + this brick libs
			compilationResult = _compiler.compile(filesInBrick, workDirectory);
			if(!compilationResult.success()) {
				System.out.println(compilationResult.getErrorString());
				throw new DeployerException("Error compiling brick implementation from: "+brickImplDir);
			}
		}

		String brickName = null;
		String version = null;
		log.info("exporting brick {} from: {}", brickName, path);
		JarFile file;
		try {
			file = runJarTool(brickName, version, path);
			log.info("brick file {} created", file.getName());
			return new BrickBundle(file);
		} catch (Exception e) {
			throw new DeployerException("Error packing brick "+brickName+" ("+version+") from: "+path,e);
		}
	}

	private File createWorkDirectory() {
		File workDirectory = new File("/tmp/sneer/bricks/compiler/work");
		if(!workDirectory.exists()) workDirectory.mkdirs();
		try {
			org.apache.commons.io.FileUtils.cleanDirectory(workDirectory);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
		return workDirectory;
	}
	

	private SourceMeta loadSourceMetaFromFile(File path) {
		return new SimpleSourceMeta(path);
	}

	private JarFile runJarTool(String brickName, String version, File path) throws Exception {
		String jarName = brickName + "-" + version;
		File jar = File.createTempFile(jarName+"-", ".jar");
		File tmp = createMetaFile(brickName, version);
		File meta = new File(path,"sneer.meta");
		tmp.renameTo(meta);

		ProcessBuilder builder = new ProcessBuilder("jar", "cfv", 
				jar.getAbsolutePath(),
				"src", "test", "lib", "sneer.meta");
		builder.directory(path);
			
		if(log.isDebugEnabled()) {
			String command = StringUtils.join(builder.command(), " ");
			log.debug("executing command: {}"+command);
		}

		Process p = builder.start();
		p.waitFor();
		return new JarFile(jar);
	}

	private File createMetaFile(String brickName, String version) throws IOException {
		String jarName = brickName + "-" + version;
		StringBuilder builder = new StringBuilder();
		builder.append("brick-name: ").append(brickName).append("\n");
		builder.append("brick-version: ").append(version).append("\n");
		File manifest = File.createTempFile(jarName+"-", ".meta");
		org.apache.commons.io.FileUtils.writeStringToFile(manifest, builder.toString());
		return manifest;
	}

	private File brickRootDirectory() {
		File home = config.sneerDirectory();
		File result = FileUtils.concat(home.getAbsolutePath(), "bricks");
		return result;
	}
	
	private File brickDirectory(String brickName) {
		File result = FileUtils.concat(brickRootDirectory(), brickName);
		return result;
	}

	@Override
	public void deploy(BrickBundle brick) {
		String brickName = brick.getBrickName();
		File dir = brickDirectory(brickName);
		log.info("exploding brick file {} into: {}", brick.getName(), dir);
		try {
			brick.explode(dir);
			compile(dir);
		} catch (Exception e) {
			throw new DeployerException("Error deploying brick "+brickName,  e);
		}
		//TODO: find toy and call some initialization method on it
	}

	private void compile(File root) {
		File bin = FileUtils.concat(root, "bin");
		File src = FileUtils.concat(root, "src");
		File lib = FileUtils.concat(root, "lib");
		Result compilationResult = _compiler.compile(src, bin);
		if(!compilationResult.success()) {
			log.warn("Error compiling.\n"+compilationResult.getErrorString());
			throw new DeployerException("Error compiling brick on "+root+". See log for details");
		}
	}
}

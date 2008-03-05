package sneer.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;

import sneer.lego.utils.FileUtils;
import sneer.compiler.Compiler;
import sneer.compiler.Result;
import sneer.config.SneerConfig;
import sneer.deployer.BrickFile;
import sneer.deployer.Deployer;
import sneer.deployer.DeployerException;
import sneer.lego.Brick;
import sneer.log.Logger;

public class DeployerImpl implements Deployer {

	@Brick
	private SneerConfig config;
	
	@Brick
	private Compiler compiler;
	
	@Brick
	private Logger log;
	
	@Override
	public List<String> list() {
		File root = brickRootDirectory();
		String[] result = root.list(DirectoryFileFilter.INSTANCE);
		return Arrays.asList(result);
	}

	@Override
	public BrickFile pack(File path, String brickName, String version) {
		log.info("exporting brick {} from: {}", brickName, path);
		JarFile file;
		try {
			file = runJarTool(brickName, version, path);
			log.info("brick file {} created", file);
			return new BrickFile(file);
		} catch (Exception e) {
			throw new DeployerException("Error packing brick "+brickName+" ("+version+") from: "+path,e);
		}
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
		File home = config.getSneerDirectory();
		File result = FileUtils.concat(home.getAbsolutePath(), "bricks");
		return result;
	}
	
	private File brickDirectory(String brickName) {
		File result = FileUtils.concat(brickRootDirectory(), brickName);
		return result;
	}

	@Override
	public void deploy(BrickFile brick) {
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
		Result compilationResult = compiler.compile(src, bin, lib);
		if(!compilationResult.success()) {
			log.warn("Error compiling.\n"+compilationResult.getErrorString());
			throw new DeployerException("Error compiling brick on "+root+". See log for details");
		}
	}
}

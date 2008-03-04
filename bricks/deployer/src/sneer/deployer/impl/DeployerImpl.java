package sneer.deployer.impl;

import java.io.File;

import sneer.compiler.Compiler;
import sneer.compiler.Result;
import sneer.config.SneerConfig;
import sneer.deployer.Deployer;
import sneer.lego.Brick;
import sneer.lego.utils.FileUtils;
import sneer.log.Logger;

public class DeployerImpl implements Deployer {

	@Brick
	private SneerConfig config;
	
	@Brick
	private Compiler compiler;
	
	@Brick
	private Logger log;
	
	@Override
	public <T> T deploy(String brickName) {
		File dir = brickDirectory(brickName);
		log.info("loading brick from {}", dir);
		if(!tryToCompile(dir)) {
			//load
		}
		return null;
	}

	private boolean tryToCompile(File root) {
		File binDir = FileUtils.concat(root, "bin");
		if(FileUtils.isEmpty(binDir)) {
			File src = FileUtils.concat(root, "src");
			Result compilationResult = compiler.compile(src, binDir);
			if(!compilationResult.success()) {
				return false;
			}
		}
		return false;
	}

	private File brickDirectory(String brickName) {
		File home = config.getSneerDirectory();
		File result = FileUtils.concat(home.getAbsolutePath(), "bricks");
		result = FileUtils.concat(result, brickName);
		return result;
	}
}

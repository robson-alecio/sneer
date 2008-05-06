package sneer.bricks.brickmanager.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import sneer.bricks.brickmanager.BrickManager;
import sneer.bricks.brickmanager.BrickManagerException;
import sneer.bricks.config.SneerConfig;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.log.Logger;
import sneer.lego.Inject;


public class BrickManagerImpl implements BrickManager {

	@Inject
	private SneerConfig _config;

	@Inject
	private Logger _log;
	
	private Map<String, BrickFile> _bricksByName = new HashMap<String, BrickFile>();

	@Override
	public void install(BrickBundle bundle) {
		List<String> brickNames = bundle.brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = bundle.brick(brickName);
			if(okToIntall(brick)) {
				try {
					install(brick);
				} catch (Throwable t) {
					throw new BrickManagerException("Error installing brick: "+brickName, t);
				}
			} else {
				//what should we do?
				throw new BrickManagerException("brick: "+brickName+" could not be installed");
			}
		}
	}

	private boolean okToIntall(BrickFile brick) {
		String brickName = brick.name();
		BrickFile installed = _bricksByName.get(brickName);
		if(installed == null)
			return true;
		
		//compare hashes

		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	private void install(BrickFile brick) throws IOException {
		String brickName = brick.name();
		_log.debug("Installing brick: "+brickName);
		
		//1. create brick directory under sneer home
		File brickDirectory = brickDirectory(brickName);
		if(brickDirectory.exists()) {
			//FixUrgent: ask permission to overwrite?
			FileUtils.cleanDirectory(brickDirectory);
		} else {
			brickDirectory.mkdir();
		}
		
		//2. copy received files
		brick.copyTo(brickDirectory);
	
		//3. resolve dependencies
	}
	
	private File brickDirectory(String brickName) {
		File root = brickRootDirectory();
		File brickDirectory = new File(root, brickName);
		return brickDirectory;
	}
	
	private File brickRootDirectory() {
		File home = _config.sneerDirectory();
		return new File(home, "bricks");
	}


}

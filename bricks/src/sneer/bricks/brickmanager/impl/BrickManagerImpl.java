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
				install(brick);
			} else {
				//what should we do?
				throw new BrickManagerException("brick: "+brickName+" could not be installed");
			}
		}
	}

	@Override
	public BrickFile brick(String brickName) {
		return _bricksByName.get(brickName);
	}

	private boolean okToIntall(BrickFile brick) {
		String brickName = brick.name();
		BrickFile installed = _bricksByName.get(brickName);
		if(installed == null)
			return true;
		
		//compare hashes

		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void install(BrickFile brick) throws BrickManagerException {
		String brickName = brick.name();
		_log.debug("Installing brick: "+brickName);
		
		//1. create brick directory under sneer home
		File brickDirectory = brickDirectory(brickName);
		if(brickDirectory.exists()) 
			cleanDirectory(brickDirectory); //FixUrgent: ask permission to overwrite?
		else 
			brickDirectory.mkdir();
		
		//2. copy received files
		BrickFile installed;
		try {
			installed = brick.copyTo(brickDirectory);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	
		//3. resolve dependencies
		
		_bricksByName.put(brickName, installed);
	}

	private void cleanDirectory(File brickDirectory) throws BrickManagerException {
		try {
			FileUtils.cleanDirectory(brickDirectory);
		} catch (IOException e) {
			throw new BrickManagerException("",e);
		}
	}
	
	private File brickDirectory(String brickName) {
		File root = _config.brickRootDirectory();
		File brickDirectory = new File(root, brickName);
		return brickDirectory;
	}
}

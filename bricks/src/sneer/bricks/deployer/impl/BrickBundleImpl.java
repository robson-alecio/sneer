package sneer.bricks.deployer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.DeployerException;

public class BrickBundleImpl implements BrickBundle {
	
	private List<BrickFile> _bricks = new ArrayList<BrickFile>();

	public void add(JarFile jarFile) throws DeployerException {
		try {
			BrickMeta meta = new BrickMeta(jarFile);
			BrickFile brick = brick(meta.brickName());
			if(brick == null) {
				brick = new BrickFileImpl(meta);
				_bricks.add(brick);
			}
			brick.add(jarFile);
		} catch (IOException e) {
			throw new DeployerException("Error extrating meta information from jar file", e);
		}
	}

	@Override
	public BrickFile brick(String brickName) {
		for(BrickFile brick : _bricks) {
			if(brick.name().equals(brickName)) {
				return brick;
			}
		}
		return null;
	}

	@Override
	public List<String> brickNames() {
		List<String> result = new ArrayList<String>();
		for(BrickFile brick : _bricks) {
			result.add(brick.name());
		}
		return result;
	}
}

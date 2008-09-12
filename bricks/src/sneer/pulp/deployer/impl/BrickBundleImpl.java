package sneer.pulp.deployer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.kernel.container.jar.DeploymentJar;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.BrickFile;
import sneer.pulp.deployer.DeployerException;

class BrickBundleImpl implements BrickBundle {
	
	private List<BrickFile> _bricks = new ArrayList<BrickFile>();

	public void add(DeploymentJar jar) throws DeployerException {
		
		String brickName = jar.brickName();
		try {
			BrickFile brick = brick(brickName);
			if(brick == null) {
				brick = new BrickFileImpl(brickName);
				_bricks.add(brick);
			}
			brick.add(jar);
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

	@Override
	public void sort() {
		Collections.sort(_bricks);
	}
}

package sneer.bricks.deployer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.DeployerException;
import sneer.lego.utils.SneerJar;
import wheel.lang.StringUtils;

public class BrickBundleImpl implements BrickBundle {
	
	private List<BrickFile> _bricks = new ArrayList<BrickFile>();

	public void add(SneerJar jar) throws DeployerException {
		
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
	public void prettyPrint() {
		List<String> brickNames = brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = brick(brickName);
			printJarInfo(brick.api());
			printJarInfo(brick.apiSrc());
			printJarInfo(brick.impl());
			printJarInfo(brick.implSrc());
		}
		System.out.println("--");
	}

	private void printJarInfo(SneerJar jar) {
		System.out.println(jar.role().toUpperCase() + " [" + jar.brickName() + "] file: " + jar.file());
		System.out.println("sneer1024: " + StringUtils.toHexa(jar.sneer1024()) + "\n");
	}
}

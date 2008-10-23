package sneer.pulp.brickmanager.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Container;
import sneer.kernel.container.Inject;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.utils.InjectedBrick;
import sneer.pulp.brickmanager.BrickManager;
import sneer.pulp.brickmanager.BrickManagerException;
import sneer.pulp.dependency.Dependency;
import sneer.pulp.dependency.DependencyManager;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.deployer.BrickFile;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.mesh.Party;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.maps.MapRegister;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.maps.impl.MapRegisterImpl;
import static wheel.io.Logger.log;

class BrickManagerImpl implements BrickManager {

	@Inject
	private SneerConfig _config;

	@Inject
	private DependencyManager _dependencyManager;
	
	@Inject
	private KeyManager _keyManager;
	
	@Inject
	private Container _container;
	
	private final MapRegister<String, BrickFile> _bricksByName = new MapRegisterImpl<String, BrickFile>();

	@Override
	public void install(BrickBundle bundle) {
		
		/*
		 * Must install brick on the right order, otherwise the runOnceOnInstall 
		 * will fail because the brick dependencies will not be found on the filesystem.
		 * sorting will fail if dependency cycles are found
		 */
		bundle.sort(); 
		
		List<String> brickNames = bundle.brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = bundle.brick(brickName);
			if(okToInstall(brick)) {
				resolve(bundle, brick);
				install(brick);
			} else {
				//what should we do?
				throw new BrickManagerException("brick: "+brickName+" could not be installed");
			}
		}
	}

	private void resolve(BrickFile brick) {
		resolve(null, brick);
	}
	
	private void resolve(BrickBundle bundle, BrickFile brick) {
		List<InjectedBrick> injectedBricks;
		try {
			injectedBricks = brick.injectedBricks();
		} catch (IOException e) {
			throw new BrickManagerException("Error searching for injected bricks on "+brick.name(), e);
		}
		for (InjectedBrick injected : injectedBricks) {
			String wanted = injected.brickName();
			BrickFile inBundle = bundle == null ? null : bundle.brick(wanted); 
			if(inBundle == null) { 
				//not inBudle, try local registry
				inBundle = brick(wanted);
				if(inBundle == null) {
					//not found. must ask other peer via network
					BrickFile justGotten = retrieveRemoteBrick(brick.origin(), injected.brickName());
					install(justGotten);
				}
			}
		}
		brick.resolved(true);
	}

	private BrickFile retrieveRemoteBrick(PublicKey origin, String brickName) {
		Party party = _keyManager.partyGiven(origin);
		
		throw new NotImplementedYet();
		//return party.brickProxyFor(BrickManager.class).brick(brickName);
	}

	@Override
	public BrickFile brick(String brickName) {
		return _bricksByName.output().currentGet(brickName);
	}

	private boolean okToInstall(BrickFile brick) {
		String brickName = brick.name();
		BrickFile installed = brick(brickName);
		if(installed == null)
			return true;
		
		//compare hashes
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void install(BrickFile brick) throws BrickManagerException {
		String brickName = brick.name();
		log("Installing brick: "+brickName);
		
		//0. resolve injected Bricks
		if(!brick.resolved())
			resolve(brick);

		//1. create brick directory under sneer home
		File brickDirectory = setUpBrickDirectory(brickName);
		
		//2. copy received files
		BrickFile installed = copyBrickFiles(brick, brickDirectory);
		
		//3. install dependencies
		copyDependencies(brick, installed);

		//4. update origin. When origin is null we are installing the brick locally, not using meToo
		PublicKey origin = brick.origin();
		origin = origin != null ? origin : _keyManager.ownPublicKey(); 
		installed.origin(origin);
		
		//5. give the brick a chance to initialize itself (register menus, etc)
		runOnceOnInstall(installed);
		
		_bricksByName.put(brickName, installed);
	}
	
	private File setUpBrickDirectory(String brickName) {
		File brickDirectory = brickDirectory(brickName);
		//System.out.println("installing "+brickName+" on "+brickDirectory);
		
		if(brickDirectory.exists()) 
			tryToCleanDirectory(brickDirectory); //FixUrgent: ask permission to overwrite?
		else 
			brickDirectory.mkdir();
		return brickDirectory;
	}

	private void tryToCleanDirectory(File brickDirectory) {
		try {
			FileUtils.cleanDirectory(brickDirectory);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private void runOnceOnInstall(BrickFile installed) {
		Class<? extends Brick> clazz = resolveBrickInterface(installed);
		_container.produce(clazz);
	}

	private Class<? extends Brick> resolveBrickInterface(BrickFile installed) {
		try {
			return _container.resolve(installed.name());
		} catch (ClassNotFoundException e) {
			throw new BrickManagerException(e.getMessage(), e);
		}
	}

	private void copyDependencies(BrickFile brick, BrickFile installed) {
		String brickName = brick.name();
		List<Dependency> brickDependencies = brick.dependencies();
		for (Dependency dependency : brickDependencies) {
			try {
				dependency = _dependencyManager.add(brickName, dependency);
				installed.dependencies().add(dependency);
			} catch (IOException e) {
				throw new BrickManagerException("Error installing dependecy: "+dependency, e);
			}
		}
	}

	private BrickFile copyBrickFiles(BrickFile brick, File brickDirectory) {
		BrickFile installed;
		try {
			installed = brick.copyTo(brickDirectory);
		} catch (IOException e) {
			throw new BrickManagerException("Error copying brick files to: "+brickDirectory);
		}
		return installed;
	}
	
	private File brickDirectory(String brickName) {
		File root = _config.brickRootDirectory();
		File brickDirectory = new File(root, brickName);
		return brickDirectory;
	}

	@Override
	public MapSignal<String, BrickFile> bricks() {
		return _bricksByName.output();
	}
}

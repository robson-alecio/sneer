package sneer.pulp.dependency.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import sneer.lego.Inject;
import sneer.lego.Startable;
import sneer.pulp.config.SneerConfig;
import sneer.pulp.crypto.Crypto;
import sneer.pulp.crypto.Sneer1024;
import sneer.pulp.dependency.Dependency;
import sneer.pulp.dependency.DependencyManager;
import sneer.pulp.log.Logger;

public class DependencyManagerImpl implements DependencyManager, Startable {

	@Inject
	private Crypto _crypto;
	
	@Inject
	private SneerConfig _config;
	
	@Inject
	private Logger _log;

	private File _root;
	
	private Map<String, List<Dependency>> _dependenciesByBrick = new HashMap<String, List<Dependency>>();

	private Map<Sneer1024, Dependency> _dependenciesBySneer1024Hash = new HashMap<Sneer1024, Dependency>();

	
	@Override
	public void start() throws Exception {
		_root = new File(_config.brickRootDirectory(), "libs");
		if(!_root.exists())
			_root.mkdirs();
	}
	
	@Override
	public List<Dependency> dependenciesFor(String brickName) {
		return _dependenciesByBrick.get(brickName);
	}

	@Override
	public Dependency add(String brickName, Dependency dependency) throws IOException {
		List<Dependency> dependencies = _dependenciesByBrick.get(brickName);
		if(dependencies == null) {
			dependencies = new ArrayList<Dependency>();
			_dependenciesByBrick.put(brickName, dependencies);
		}
		Dependency installed = install(dependency);
		dependencies.add(installed);
		return installed;
	}

	private Dependency install(Dependency dependency) throws IOException {
		_log.info("Installing dependency: "+dependency.file() + "["+dependency.sneer1024().toHexa()+"]");

		//1. check registry first
		File file = dependency.file();
		Sneer1024 hash = _crypto.digest(file);
		Dependency installed = _dependenciesBySneer1024Hash.get(hash);
		if(installed != null) {
			//we already hold this jar. No need to reinstall it
			return installed;
		}
		
		//2. check same file name, but hash mismatch 
		String fileName = file.getName();
		File newFile = new File(_root, fileName);
		if(newFile.exists()) {
			throw new IOException("There is another dependency with the same file name ("+fileName+"), but not the same sneer1024 hash");
		}
		
		//install new dependency
		FileUtils.copyFile(file, newFile);
		Dependency result = newDependency(newFile);
		_dependenciesBySneer1024Hash.put(result.sneer1024(), dependency);
		
		return result; 
	}

	@Override
	public Dependency newDependency(File file) {
		try {
			Sneer1024 sneer1024 = _crypto.digest(file);
			return new DependencyImpl(file, sneer1024);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement
		}
	}

}

class DependencyImpl implements Dependency {

	private static final long serialVersionUID = 1L;

	private File _file;
	
	private Sneer1024 _sneer1024;
	
	public DependencyImpl(File file, Sneer1024 sneer1024) {
		_file = file;
		_sneer1024 = sneer1024;
	}

	@Override
	public File file() {
		return _file;
	}

	@Override
	public Sneer1024 sneer1024() {
		return _sneer1024;
	}

	@Override
	public String toString() {
		return _sneer1024.toString() + " " + _file;
	}
	
	
	
}
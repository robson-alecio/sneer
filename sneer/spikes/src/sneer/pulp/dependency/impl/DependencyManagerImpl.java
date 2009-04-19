package sneer.pulp.dependency.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import sneer.kernel.container.SneerConfig;
import sneer.pulp.crypto.Crypto;
import sneer.pulp.crypto.Sneer1024;
import sneer.pulp.dependency.DependencyManager;
import sneer.pulp.dependency.FileWithHash;

class DependencyManagerImpl implements DependencyManager {

	private final Crypto _crypto = my(Crypto.class);
	
	private final SneerConfig _config = my(SneerConfig.class);
	
	private File _root;
	
	private final Map<String, List<FileWithHash>> _dependenciesByBrick = new HashMap<String, List<FileWithHash>>();

	private final Map<Sneer1024, FileWithHash> _dependenciesBySneer1024Hash = new HashMap<Sneer1024, FileWithHash>();
	
	
	@Override
	public List<FileWithHash> dependenciesFor(String brickName) {
		return _dependenciesByBrick.get(brickName);
	}

	@Override
	public FileWithHash add(String brickName, FileWithHash dependency) throws IOException {
		List<FileWithHash> dependencies = _dependenciesByBrick.get(brickName);
		if(dependencies == null) {
			dependencies = new ArrayList<FileWithHash>();
			_dependenciesByBrick.put(brickName, dependencies);
		}
		FileWithHash installed = install(dependency);
		dependencies.add(installed);
		return installed;
	}

	private FileWithHash install(FileWithHash dependency) throws IOException {
		my(Logger.class).log("Installing dependency: {} [{}]", dependency.file(), dependency.sneer1024().toHexa());

		//1. check registry first
		File file = dependency.file();
		Sneer1024 hash = _crypto.digest(file);
		FileWithHash installed = _dependenciesBySneer1024Hash.get(hash);
		if(installed != null) {
			//we already hold this jar. No need to reinstall it
			return installed;
		}
		
		//2. check same file name, but hash mismatch 
		String fileName = file.getName();
		File newFile = new File(root(), fileName);
		if(newFile.exists()) {
			throw new IOException("There is another dependency with the same file name ("+fileName+"), but not the same sneer1024 hash");
		}
		
		//install new dependency
		FileUtils.copyFile(file, newFile);
		FileWithHash result = newDependency(newFile);
		_dependenciesBySneer1024Hash.put(result.sneer1024(), dependency);
		
		return result; 
	}

	private File root() {
		if (_root != null)
			return _root;
		_root = buildRoot();
		return _root;
	}

	@Override
	public FileWithHash newDependency(File file) {
		try {
			Sneer1024 sneer1024 = _crypto.digest(file);
			return new DependencyImpl(file, sneer1024);
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Implement
		}
	}
	
	private File buildRoot() {
		File root = new File(_config.brickRootDirectory(), "lib");
		if(!root.exists())
			root.mkdirs();
		return root;
	}

}

class DependencyImpl implements FileWithHash {

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
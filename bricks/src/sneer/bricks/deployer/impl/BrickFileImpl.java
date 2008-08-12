package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;

import sneer.bricks.dependency.Dependency;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.DeployerException;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.jar.SneerJar;
import sneer.lego.jar.impl.SneerJarImpl;
import sneer.lego.utils.InjectedBrick;
import sneer.lego.utils.io.NetworkFriendly;
import wheel.lang.exceptions.NotImplementedYet;

public class BrickFileImpl implements BrickFile, NetworkFriendly {

	private static final long serialVersionUID = 1L;

	private String _brickName;

	private boolean _resolved;

	private SneerJar _api;
	private SneerJar _apiSrc;
	private SneerJar _impl;
	private SneerJar _implSrc;
	
	private PublicKey _origin;
	
	private List<Dependency> _dependencies = new ArrayList<Dependency>();

	public BrickFileImpl(String brickName) {
		_brickName = brickName;
	}

	public BrickFileImpl(File target) {
		this(target.getName());
		add(new File(target,name()+"-api.jar"));
		add(new File(target,name()+"-api-src.jar"));
		add(new File(target,name()+"-impl.jar"));
		add(new File(target,name()+"-impl-src.jar"));
	}

	private void add(File file) {
		JarFile jarFile;
		try {
			jarFile = new JarFile(file);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
		add(new SneerJarImpl(file, jarFile));
	}

	@Override
	public String name() {
		return _brickName;
	}

	@Override
	public void add(SneerJar jarFile) {
		if(!jarFile.brickName().equals(name()))
			throw new DeployerException("Can't add jar file from another brick to: "+name());
		
		String role = jarFile.role();

		if("api".equals(role)) {
			_api = jarFile;
		} else if("api-src".equals(role)){
			_apiSrc = jarFile;
		} else if("impl".equals(role)){
			_impl = jarFile;
		} else if("impl-src".equals(role)){
			_implSrc = jarFile;
		} else {
			throw new DeployerException("Unknown role: "+role);
		}
	}

	@Override
	public SneerJar api() {
		return _api;
	}

	@Override
	public SneerJar apiSrc() {
		return _apiSrc;
	}

	@Override
	public SneerJar impl() {
		return _impl;
	}

	@Override
	public SneerJar implSrc() {
		return _implSrc;
	}

	@Override
	public BrickFile copyTo(File target) throws IOException {
		FileUtils.copyFile(api().file(), new File(target,name()+"-api.jar"));
		FileUtils.copyFile(apiSrc().file(), new File(target,name()+"-api-src.jar"));
		FileUtils.copyFile(impl().file(), new File(target,name()+"-impl.jar"));
		FileUtils.copyFile(implSrc().file(), new File(target,name()+"-impl-src.jar"));
		return new BrickFileImpl(target);
	}

	@Override
	public void explodeSources(File target) throws IOException {
		apiSrc().explode(target);
		implSrc().explode(target);
	}

	@Override
	public List<Dependency> dependencies() {
		return _dependencies;
	}

	@Override
	public List<InjectedBrick> injectedBricks() throws IOException {
		return impl().injectedBricks();
	}

	@Override
	public void resolved(boolean resolved) {
		_resolved = resolved;
	}

	@Override
	public boolean resolved() {
		return _resolved;
	}

	@Override
	public PublicKey origin() {
		return _origin;
	}

	@Override
	public void origin(PublicKey pk) {
		_origin = pk;
	}

	@Override
	public String toString() {
		return name() + "\n\tapi("+_api.file()+")\n\timpl("+_impl.file()+")";
	}

	@Override
	public void beforeSerialize() throws IOException {
		api().beforeSerialize();
		apiSrc().beforeSerialize();
		impl().beforeSerialize();
		implSrc().beforeSerialize();
	}

	@Override
	public void afterSerialize() throws IOException {
		api().afterSerialize();
		apiSrc().afterSerialize();
		impl().afterSerialize();
		implSrc().afterSerialize();
	}

	@Override
	public int compareTo(BrickFile other) {
		List<InjectedBrick> bricks;
		List<InjectedBrick> otherBricks;

		try {
			bricks = impl().injectedBricks();
			otherBricks = other.impl().injectedBricks();
		} catch (IOException e) {
			throw new NotImplementedYet("Can't calculate dependency graph",e);
		}
		
		if(bricks.isEmpty())
			return -1; //this brick doesn't inject other bricks. Go first

		if(otherBricks.isEmpty())
			return 1; //the other brick doesn't inject other bricks. Let him go first
		
		boolean thisInjectOther = false;
		for(InjectedBrick injected : bricks)
			if(injected.brickName().equals(other.name())) {
				thisInjectOther = true; 
				break;
			}

		boolean otherInjectThis = false;
		for(InjectedBrick injected : otherBricks)
			if(injected.brickName().equals(_brickName)) {
				otherInjectThis = true;
				break;
			}
		
		if(thisInjectOther && otherInjectThis)
			throw new NotImplementedYet("Dependency cycle detected: "+_brickName+" <-> "+other.name());
		
		if(thisInjectOther)
			return 1;
		
		if(otherInjectThis)
			return -1;
		
		return 0;
	}
}

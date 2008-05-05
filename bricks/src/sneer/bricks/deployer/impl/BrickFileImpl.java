package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.DeployerException;
import sneer.lego.utils.SneerJar;

public class BrickFileImpl implements BrickFile {

	private String _brickName;

	private SneerJar _api;
	private SneerJar _apiSrc;
	private SneerJar _impl;
	private SneerJar _implSrc;

	public BrickFileImpl(String brickName) {
		_brickName = brickName;
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
	public void copyTo(File target) throws IOException {
		FileUtils.copyFile(api().file(), new File(target,name()+"-api.jar"));
		FileUtils.copyFile(apiSrc().file(), new File(target,name()+"-api-src.jar"));
		FileUtils.copyFile(impl().file(), new File(target,name()+"-impl.jar"));
		FileUtils.copyFile(implSrc().file(), new File(target,name()+"-impl-src.jar"));
	}

	@Override
	public void explodeSources(File target) throws IOException {
		apiSrc().explode(target);
		implSrc().explode(target);
	}

	@Override
	public String toString() {
		return name() + "\n\tapi("+_api.file()+")\n\timpl("+_impl.file()+")";
	}
}

package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.DeployerException;

public class BrickFileImpl implements BrickFile {

	private BrickMeta _meta;
	
	private JarFile _api;
	private JarFile _apiSrc;
	private JarFile _impl;
	private JarFile _implSrc;

	public BrickFileImpl(BrickMeta meta) {
		_meta = meta;
	}

	@Override
	public String name() {
		return _meta.brickName();
	}

	@Override
	public void add(JarFile jarFile) throws IOException {
		BrickMeta meta = new BrickMeta(jarFile);
		if(!meta.brickName().equals(name()))
			throw new DeployerException("Can't add jar file from another brick to: "+name());
		
		String role = meta.role();

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
	public JarFile api() {
		return _api;
	}

	@Override
	public JarFile apiSrc() {
		return _apiSrc;
	}

	@Override
	public JarFile impl() {
		return _impl;
	}

	@Override
	public JarFile implSrc() {
		return _implSrc;
	}

	@Override
	public void copyTo(File target) throws IOException {
		FileUtils.copyFile(new File(api().getName()), new File(target,name()+"-api.jar"));
		FileUtils.copyFile(new File(apiSrc().getName()), new File(target,name()+"-api-src.jar"));
		FileUtils.copyFile(new File(impl().getName()), new File(target,name()+"-impl.jar"));
		FileUtils.copyFile(new File(implSrc().getName()), new File(target,name()+"-impl-src.jar"));
	}

	@Override
	public void explodeSources(File target) throws IOException {
		explode(apiSrc(), target);
		explode(implSrc(), target);
	}
	
	private void explode(JarFile jarFile, File target) throws IOException {
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if(entry.isDirectory() && !skipDirectory(name)) {
				File dir = new File(target, name);
				dir.mkdirs();
			} else if (!entry.isDirectory() && !skipFile(name)) {
				File file = new File(target, name);
				FileUtils.touch(file);
				InputStream is = jarFile.getInputStream(entry);
				IOUtils.copy(is, new FileOutputStream(file));
				IOUtils.closeQuietly(is);
			}
		}
	}

	private boolean skipFile(String name) {
		return name.endsWith("MANIFEST.MF") || name.endsWith("sneer.meta");
	}

	private boolean skipDirectory(String name) {
		return name.endsWith("META-INF/");
	}

	@Override
	public String toString() {
		return name() + "\n\tapi("+_api.getName()+")\n\timpl("+_impl.getName()+")";
	}
}

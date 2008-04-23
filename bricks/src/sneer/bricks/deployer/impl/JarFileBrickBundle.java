package sneer.bricks.deployer.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;

public class JarFileBrickBundle implements BrickBundle {

	private List<JarFile> _jarFiles = new ArrayList<JarFile>();
	
	@Override
	public void explode(File target) throws Exception {
		for(JarFile _jarFile : _jarFiles) {
			explode(_jarFile, target);
		}
	}

	private void explode(JarFile jarFile, File target) throws Exception {
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if(entry.isDirectory() && !skipDirectory(name)) {
				File dir = new File(target, name);
				dir.mkdirs();
			} else if (!entry.isDirectory() && !skipFile(name)) {
				//String fileName = name.substring(name.lastIndexOf("/")+1);
				File file = new File(target, name);
				InputStream is = jarFile.getInputStream(entry);
				IOUtils.copy(is, new FileOutputStream(file));
			}
		}
	}

	protected void add(JarFile jarFile) {
		_jarFiles.add(jarFile);
	}

	private boolean skipFile(String name) {
		return name.endsWith("MANIFEST.MF");
	}

	private boolean skipDirectory(String name) {
		return name.endsWith("META-INF/");
	}

	@Override
	public BrickFile brick(String brickName) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public List<String> brickNames() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}
}

package sneer.bricks.deployer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

public class BrickMeta {

	private Properties _props;
	
	public BrickMeta(JarFile jarFile) throws IOException {
		_props = new Properties();
		InputStream is = jarFile.getInputStream(jarFile.getEntry("sneer.meta")); 
		_props.load(is);
		IOUtils.closeQuietly(is);
	}

	public String brickName() {
		return property("brick-name");
	}
	
	public String role() {
		return property("role").toLowerCase();
	}
	
	private String property(String propertyName) {
		return _props.getProperty(propertyName);
	}

}

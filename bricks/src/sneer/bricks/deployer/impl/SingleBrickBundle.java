package sneer.bricks.deployer.impl;

import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarFile;

public class SingleBrickBundle extends JarFileBrickBundle {
	
	private JarFile _jarFile;
	
	private Properties _props;
	
	public SingleBrickBundle(JarFile file) {
		_jarFile = file;
	}

	public String getName() {
		return _jarFile.getName();
	}

	public String getBrickName() {
		return getProperty("brick-name");
	}

	public String getBrickVersion() {
		return getProperty("brick-version");
	}

	private String getProperty(String name) {
		
		if(_props == null) {
			try {
				_props = loadProperties();
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return _props.getProperty(name);
	}

	private Properties loadProperties() throws Exception {
		InputStream is = _jarFile.getInputStream(_jarFile.getEntry("sneer.meta"));
		Properties props = new Properties();
		props.load(is);
		return props;
	}
}

package sneer.deployer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

public class BrickFile {
	
	private JarFile _jarFile;
	
	private Properties _props;
	
	public BrickFile(JarFile file) {
		_jarFile = file;
	}

	public Object getName() {
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



	public void explode(File target) throws Exception {
		Enumeration<JarEntry> e = _jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry jarEntry = e.nextElement();
			String name = jarEntry.getName();
			if(jarEntry.isDirectory() && !skipDirectory(name)) {
				File dir = new File(target + File.separator + name);
				dir.mkdirs();
			} else if (!jarEntry.isDirectory() && !skipFile(name)) {
				//String fileName = name.substring(name.lastIndexOf("/")+1);
				File file = new File(target, name);
				InputStream is = _jarFile.getInputStream(jarEntry);
				IOUtils.copy(is, new FileOutputStream(file));
			}
		}
	}


	private boolean skipFile(String name) {
		return name.endsWith("MANIFEST.MF");
	}

	private boolean skipDirectory(String name) {
		return name.endsWith("META-INF/");
	}
}

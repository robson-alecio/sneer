package sneer.bricks.pulp.propertystore.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.propertystore.PropertyStore;
import sneer.foundation.brickness.StoragePath;

class PropertyStoreImpl implements PropertyStore {

	private static final String FILE_NAME = "propertystore.txt";


	private final StoragePath _config = my(StoragePath.class);

	
	private final Properties _properties = loadProperties();

	@Override
	public String get(String property) {
		return _properties.getProperty(property);
	}
	
	@Override
	public boolean containsKey(String property) {
		return _properties.containsKey(property);
	}
	
	@Override
	public void set(String property, String value) {
		_properties.setProperty(property, value);
		persist();
	}
	
	private Properties loadProperties() {
		Properties result = new Properties();
		InputStream in = null;
		try {
			in = in();
			result.load(in);
		} catch (FileNotFoundException ignore) {
//			my(Logger.class).log("File not found: {}", file().getAbsolutePath());
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} finally {
			if (in != null) my(IO.class).crash(in);
		}
		return result;
	}

	private void persist() {
		try {
			OutputStream out = out();
			try {
				_properties.store(out, "Sneer System Persistence File - Handle with Care :)");
			} finally {
				my(IO.class).crash(out);
			}
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} 
	}

	private InputStream in() throws IOException {
		return new FileInputStream(file());
	}

	private OutputStream out() throws IOException {
		return new FileOutputStream(file());
	}

	private File file() {
		return new File(_config.get(), FILE_NAME);
	}

}

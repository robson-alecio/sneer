package sneer.pulp.propertystore.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import sneer.kernel.container.Inject;
import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.pulp.propertystore.PropertyStore;
import wheel.io.Logger;
import wheel.io.Streams;
import wheel.io.files.Directory;

class PropertyStoreImpl implements PropertyStore {

	private static final String FILE_NAME = "propertystore.txt";


	@Inject
	static private PersistenceConfig _config;

	
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
		try {
			result.load(in());
		} catch (FileNotFoundException e) {
			//OK. No properties were saved yet.
			Logger.log("No properties found yet.");
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		return result;
	}

	private void persist() {
		try {
			OutputStream out = out();
			try {
				_properties.store(out, "Sneer System Persistence File - Handle with Care :)");
			} finally {
				Streams.crash(out);
			}
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} 
	}

	private InputStream in() throws IOException {
		Logger.log("Reading Sneer properties file from: {}", FILE_NAME);
		return directory().openFile(FILE_NAME);
	}

	private OutputStream out() throws IOException {
		if (directory().fileExists(FILE_NAME))
			directory().deleteFile(FILE_NAME);
		
		return directory().createFile(FILE_NAME);
	}

	private Directory directory() {
		return _config.persistenceDirectory();
	}
}

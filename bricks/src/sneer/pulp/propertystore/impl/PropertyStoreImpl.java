package sneer.pulp.propertystore.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import sneer.kernel.container.Inject;
import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.pulp.propertystore.PropertyStore;
import wheel.io.Logger;
import wheel.io.Streams;

class PropertyStoreImpl implements PropertyStore {

	@Inject
	static private PersistenceConfig _config;

	
	private final Properties _properties = loadProperties();

	@Override
	public String get(String property) {
		return _properties.getProperty(property);
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
			FileOutputStream out = out();
			try {
				_properties.store(out, "Sneer System Persistence File - Handle with Care :)");
			} finally {
				Streams.crash(out);
			}
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} 
	}

	private FileInputStream in() throws FileNotFoundException {
		Logger.log("Reading Sneer properties file from: {}", propertiesFile());
		return new FileInputStream(propertiesFile());
	}

	private FileOutputStream out() throws FileNotFoundException {
		return new FileOutputStream(propertiesFile());
	}

	private File propertiesFile() {
		return new File(_config.persistenceDirectory(), "propertystore.txt");
	}

}

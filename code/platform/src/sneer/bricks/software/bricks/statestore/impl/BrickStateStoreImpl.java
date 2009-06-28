package sneer.bricks.software.bricks.statestore.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.serialization.Serializer;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.bricks.software.directoryconfig.DirectoryConfig;

public class BrickStateStoreImpl implements BrickStateStore {

	private static final String FILE_NAME = "BrickState.xml";
	
	private final Map<Class<?>, Light> _restoreLights = new HashMap<Class<?>, Light>(); 
	private final Map<Class<?>, Light> _saveLights = new HashMap<Class<?>, Light>(); 

	@Override
	public Object readObjectFor(Class<?> brick, ClassLoader classloader) throws BrickStateStoreException {
		File file = my(DirectoryConfig.class).getStorageDirectoryFor(brick);
		if(!file.exists()) file.mkdirs();
		
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(file, FILE_NAME));
		} catch (FileNotFoundException e) {
			return null;
		}
		
		try {
			return my(Serializer.class).deserialize(stream, classloader);
		} catch (Exception e) {
			if(!_restoreLights.containsKey(brick))
				_restoreLights.put(brick, my(BlinkingLights.class).prepare(LightType.ERROR));
			
			my(BlinkingLights.class).turnOnIfNecessary(_restoreLights.get(brick), "Restore Brick State Error", "Unable to restore state for brick '" + brick.getName() + "'", e);
			throw new BrickStateStoreException(brick, e);
		} finally{
			try { stream.close(); } catch (Throwable ignore) { }
		}	
	}

	@Override
	public void writeObjectFor(Class<?> brick, Object object) {
		File file = my(DirectoryConfig.class).getStorageDirectoryFor(brick);
		if(!file.exists()) file.mkdirs();
		
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(file, FILE_NAME));
			my(Serializer.class).serialize(stream, object);
		} catch (IOException e) {
			if(!_saveLights.containsKey(brick))
				_saveLights.put(brick, my(BlinkingLights.class).prepare(LightType.ERROR));
			
			my(BlinkingLights.class).turnOnIfNecessary(_saveLights.get(brick), "Persistence Brick State Error", "Unable to persist state for brick '" + brick.getName() + "'", e);
			throw new BrickStateStoreException(brick, e);
		} finally{
			try { stream.close(); } catch (Throwable ignore) { }
		}
	}
}
package sneer.bricks.software.bricks.statestore.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sneer.bricks.pulp.serialization.Serializer;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.bricks.software.directoryconfig.DirectoryConfig;

public class BrickStateStoreImpl implements BrickStateStore {

	private static final String FILE_NAME = "BrickState.xml";

	@Override
	public Object readObjectFor(Class<?> brick, ClassLoader classloader) throws IOException, ClassNotFoundException {
		File file = my(DirectoryConfig.class).getStorageDirectoryFor(brick);
		if(!file.exists()) file.mkdirs();
		
		FileInputStream stream = new FileInputStream(new File(file, FILE_NAME));
		try {
			return my(Serializer.class).deserialize(stream, classloader);
		} finally{
			try { stream.close(); } catch (Throwable ignore) { }
		}	
	}

	@Override
	public void writeObjectFor(Class<?> brick, Object object) throws IOException {
		File file = my(DirectoryConfig.class).getStorageDirectoryFor(brick);
		if(!file.exists()) file.mkdirs();
		
		FileOutputStream stream = new FileOutputStream(new File(file, FILE_NAME));
		try {
			my(Serializer.class).serialize(stream, object);
		} finally{
			try { stream.close(); } catch (Throwable ignore) { }
		}
	}
}
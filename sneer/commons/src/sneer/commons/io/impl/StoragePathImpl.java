package sneer.commons.io.impl;

import sneer.commons.io.StoragePath;
import sneer.kernel.container.SneerConfig;
import static sneer.commons.environments.Environments.my;

class StoragePathImpl implements StoragePath {

	{
		System.out.println("This StoragePathImpl class is crap");
	}
	
	@Override
	public String get() {
		return my(SneerConfig.class).sneerDirectory().getAbsolutePath();
	}

}

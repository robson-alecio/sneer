package sneer.bricks.dependency;

import java.io.File;

import sneer.bricks.crypto.Sneer1024;

public interface Dependency {

	File file();
	
	Sneer1024 sneer1024();
}

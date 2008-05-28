package sneer.bricks.dependency;

import java.io.File;
import java.io.Serializable;

import sneer.bricks.crypto.Sneer1024;

public interface Dependency extends Serializable {

	File file();
	
	Sneer1024 sneer1024();
}

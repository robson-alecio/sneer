package sneer.pulp.dependency;

import java.io.File;
import java.io.Serializable;

import sneer.pulp.crypto.Sneer1024;

public interface FileWithHash extends Serializable {

	File file();
	
	Sneer1024 sneer1024();
}

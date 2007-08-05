package sneer.apps.filesharing;

import wheel.io.files.Directory;

public interface FileReplicator {

	void replicate(Directory master, Directory slave);

}

package sneer.apps.filesharing;

import java.io.IOException;

import wheel.io.files.Directory;

public interface FileReplicator {

	void replicate(Directory master, Directory slave) throws IOException;

}

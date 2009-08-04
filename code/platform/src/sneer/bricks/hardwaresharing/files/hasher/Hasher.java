package sneer.bricks.hardwaresharing.files.hasher;

import sneer.bricks.hardwaresharing.files.protocol.FileContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface Hasher {

	Sneer1024 hashFile(FileContents contents);
	Sneer1024 hashFile(byte[] contents);

	Sneer1024 hashFolder(FolderContents contents);


}

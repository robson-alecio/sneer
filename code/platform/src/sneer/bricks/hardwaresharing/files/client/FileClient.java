package sneer.bricks.hardwaresharing.files.client;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileClient {

	/** Fetches the contents of hashOfContents from peers into the FileCache. */
	void fetchToCache(Sneer1024 hashOfContents);

}

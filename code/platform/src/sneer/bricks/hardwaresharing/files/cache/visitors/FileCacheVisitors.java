package sneer.bricks.hardwaresharing.files.cache.visitors;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileCacheVisitors {

	void accept(Sneer1024 startingPoint, FileCacheVisitor visitor);
	
}


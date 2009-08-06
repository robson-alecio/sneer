package sneer.bricks.hardwaresharing.files.cache.visitors.impl;

import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitor;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheGuide;
import sneer.bricks.pulp.crypto.Sneer1024;

public class FileCacheGuideImpl implements FileCacheGuide {

	@Override
	public void guide(FileCacheVisitor visitor, Sneer1024 startingPoint) {
		new GuidedTour(startingPoint, visitor);
	}

}

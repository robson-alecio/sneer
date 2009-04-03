package sneer.commons.io;

import sneer.brickness.Brick;
import sneer.commons.environments.EnvironmentProperty;

@Brick
/** The place where you should persist your files. */
public interface StoragePath extends EnvironmentProperty<String> {}

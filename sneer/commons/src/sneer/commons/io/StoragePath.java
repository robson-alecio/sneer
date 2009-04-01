package sneer.commons.io;

import sneer.commons.environments.EnvironmentProperty;
import sneer.container.Brick;

@Brick
/** The place where you should persist your files. */
public interface StoragePath extends EnvironmentProperty<String> {}

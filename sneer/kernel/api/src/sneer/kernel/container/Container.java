package sneer.kernel.container;

import sneer.brickness.Environment;

public interface Container extends Environment {
	
	<T> T provide(Class<T> intrface);

	Class<? extends Brick> resolve(String brickName) throws ClassNotFoundException;

}
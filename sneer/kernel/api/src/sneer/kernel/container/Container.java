package sneer.kernel.container;

import wheel.lang.Environment;

public interface Container extends Environment.Provider {
	
	<T> T provide(Class<T> intrface);

	Class<? extends Brick> resolve(String brickName) throws ClassNotFoundException;

}
package sneer.brickness.impl;

public interface BrickInstantiator {

	Object instantiate(Class<?> brickImpl) throws Exception;

}

package sneer.brickness.impl;

public interface BrickDecorator {

	Object decorate(Class<?> brick, Object brickImpl);

}

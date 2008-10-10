package sneer.kernel.container;

/**
 * Marker interface for Bricks.
 * 
 * All implementations of all interfaces referenced by a Brick interface must
 * either declare "throws Hiccup" or return in under one microsecond.
 * 
 * All method calls to all classes in a Brick, including callbacks, will be
 * synchronized by the container.
 */
public interface Brick {

}

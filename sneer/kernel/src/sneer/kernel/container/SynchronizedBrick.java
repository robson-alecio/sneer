package sneer.kernel.container;

/** All method calls to all classes in this brick, including callbacks, will be synchronized by the container. The brick itself will be used as the synchronization monitor. */
public interface SynchronizedBrick extends Brick {

}

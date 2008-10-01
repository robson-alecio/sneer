package sneer.skin;

import sneer.kernel.container.SynchronizedBrick;

/**
 * All method calls to all classes in this brick, including callbacks, will be
 * made in the GUI (Swing) thread. The container does not allow Gui bricks
 * to call methods that throw Hiccup. */
public interface GuiBrick extends SynchronizedBrick {

}

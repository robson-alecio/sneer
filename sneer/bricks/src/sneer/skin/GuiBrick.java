package sneer.skin;

import sneer.brickness.Brick;

/** A brick that can freely manipulate Swing components without having
 * to worry about being in the Gui thread  (AWT event dispatch thread).
 * 
 * The container does not allow GuiBricks to call methods that throw Hiccup.
 * 
 * The container with call GuiBricks' constructors in the Gui thread.
 * 
 * All methods in all classes in a GuiBrick, including inner classes, will be
 * instrumented at the beginning with bytecode to run in the Gui thread if
 * not already running in it. Something equivalent to:
 * 
 *			if (!EventQueue.isDispatchThread()) {
 *				EventQueue.invokeAndWait(new Runnable(){public void run(){
 *					//recursive call to method with same args.
 *				}})
 *			}
 *			//Original method bytecode
 */
public interface GuiBrick extends Brick {

}

package sneer.bricks.pulp.natures.gui;

import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.Nature;

/** Bricks with this nature can freely manipulate Swing components without
 * having to worry about being in the GUI thread (AWT event dispatch thread).
 * 
 * The container does not allow GUI Bricks to call methods that throw Hiccup.
 * 
 * All methods of all classes in a brick annotated with @Brick(GUI.class) will be
 * instrumented at the beginning with bytecode to run in the Gui thread
 * if not already running in it. Something similar to:
 * 
 *			if (!EventQueue.isDispatchThread()) {
 *				EventQueue.invokeAndWait(new Runnable(){public void run(){
 *					//recursive call to method with same args.
 *				}})
 *			}
 *			//Original method bytecode
 */

@Brick
public interface GUI extends Nature {

}

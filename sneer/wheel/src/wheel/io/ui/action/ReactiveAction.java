/**
 * 
 */
package wheel.io.ui.action;

import wheel.reactive.Signal;

public interface ReactiveAction extends Action{
	Signal<String> signalCaption();
}
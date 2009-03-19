/**
 * 
 */
package wheel.io.ui.action;

import sneer.pulp.reactive.Signal;

public interface ReactiveAction extends Action{
	Signal<String> signalCaption();
}
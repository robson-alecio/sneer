package wheel.io.ui.action;

import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

public abstract class ActionUtility {

	public static ReactiveAction getReativeActionWrapper(final Action defaultAction) {
		ReactiveAction action = new ReactiveAction(){
			Action adaptee = defaultAction;
			
			@Override
			public Signal<String> signalCaption() {
				return new Constant<String>(adaptee.caption());
			}
	
			@Override
			public String caption() {
				return adaptee.caption();
			}
	
			@Override
			public void run() {
				adaptee.run();
			}
		};
		return action;
	}
}

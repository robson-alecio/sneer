package sneer.pulp.reactive.signalchooser;

import sneer.kernel.container.Brick;
import wheel.reactive.Signal;

public interface ElementsObserverFactory extends Brick{
	
	public interface SignalChooser<T> {
		Signal<?>[] signalsToReceiveFrom(T element);
	}
	
	public interface ElementsObserver<T> {
		void elementRemoved(T element);
		void elementAdded(T element);
	}
	
	public interface ElementListener<T> {
		void elementChanged(T element);
	}
	
	<T> ElementsObserver<T> newObserver(SignalChooser<T> chooser, ElementListener<T> listener);
}

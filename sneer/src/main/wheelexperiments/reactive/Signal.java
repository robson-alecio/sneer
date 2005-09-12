package wheelexperiments.reactive;


public interface Signal<T> {

	public interface Receiver<RT> {
		void receive(RT newValue);
	}
	
	public void addReceiver(Receiver<T> receiver);

	public T currentValue();

}

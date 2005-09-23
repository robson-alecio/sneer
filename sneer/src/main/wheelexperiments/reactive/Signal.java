package wheelexperiments.reactive;


public interface Signal<T> {
	
	public void addReceiver(Receiver<T> receiver);
	public void removeReceiver(Receiver<T> name);

	public interface Receiver<RT> {
		void receive(RT newValue);
	}

	public T currentValue();

}

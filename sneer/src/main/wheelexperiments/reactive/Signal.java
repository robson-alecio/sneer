package wheelexperiments.reactive;



/** @invariant this.toString().equals(this.currentValue().toString()) */
public interface Signal<T> extends SetSignal<T> {
	
	public void addReceiver(Receiver<T> receiver);
	public void removeReceiver(Receiver<T> name);

	public void addTransientReceiver(Receiver<T> receiver);
	public void removeTransientReceiver(Receiver<T> receiver);

	public T currentValue();
	
}

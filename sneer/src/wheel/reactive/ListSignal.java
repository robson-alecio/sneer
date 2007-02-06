package wheel.reactive;

public interface ListSignal {
	
	public interface Receiver {
		public void elementAdded(Object element);
	}

	public void addReceiver(Receiver receiver);

}

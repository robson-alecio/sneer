package wheel.reactive;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNotifier<VC> {

	private final Set<Receiver<VC>> _receivers = new HashSet<Receiver<VC>>();
	private transient Set<Receiver<VC>> _transientReceivers;
	private final Object _monitor = new Object();

	protected void notifyReceivers(VC valueChange) {
		synchronized (_monitor) {
			notify(_receivers, valueChange);
			if (_transientReceivers != null)
				notify(_transientReceivers, valueChange);
		}
	}

	@SuppressWarnings("unchecked")
	private void notify(Set<Receiver<VC>> receivers, VC valueChange) {
		Receiver<VC>[] copy;
		copy = new Receiver[receivers.size()];
		receivers.toArray(copy);
	
		for (Receiver<VC> receiver : copy) receiver.receive(valueChange);
	}

	public void addReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			boolean isNew = _receivers.add(receiver);
			assert isNew;
			initReceiver(receiver);
		}
	}

	protected abstract void initReceiver(Receiver<VC> receiver);
	
	public void removeReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			boolean wasThere = _receivers.remove(receiver);
			assert wasThere;
		}
	}

	public void addTransientReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			boolean isNew = transientReceivers().add(receiver);
			assert isNew;
			initReceiver(receiver);
		}
	}

	public void removeTransientReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			boolean wasThere = transientReceivers().remove(receiver);
			assert wasThere;
		}
	}

	private Collection<Receiver<VC>> transientReceivers() {
		if (_transientReceivers == null) _transientReceivers = new HashSet<Receiver<VC>>();  
		return _transientReceivers;
	}

}

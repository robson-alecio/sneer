package wheelexperiments.reactive;

import static wheelexperiments.assertions.Assertions.assertTrue;

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
	
		for (Receiver<VC> receiver : copy)
			receiver.receive(valueChange);
	}

	public void addReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			assertTrue(_receivers.add(receiver));
			initReceiver(receiver);
		}
	}

	protected abstract void initReceiver(Receiver<VC> receiver);
	
	public void removeReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			assertTrue(_receivers.remove(receiver));
		}
	}

	public void addTransientReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			assertTrue(transientReceivers().add(receiver));
			initReceiver(receiver);
		}
	}

	public void removeTransientReceiver(Receiver<VC> receiver) {
		synchronized (_monitor) {
			assertTrue(transientReceivers().remove(receiver));
		}
	}

	private Collection<Receiver<VC>> transientReceivers() {
		if (_transientReceivers == null) _transientReceivers = new HashSet<Receiver<VC>>();  
		return _transientReceivers;
	}

}

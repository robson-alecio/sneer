//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Alexandre Nodari.

package spikes.wheel.io.network.mocks;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.serialization.DeepCopier;
import spikes.wheel.io.network.ObjectSocket;


public class ObjectSocketMock implements ObjectSocket {

	private ObjectSocketMock _counterpart;
	private List<Object> _receivedObjects = new LinkedList<Object>();
	private Permit _permit;

	ObjectSocketMock(Permit permit) {
		initialize(permit, new ObjectSocketMock(this, permit));
	}

	private ObjectSocketMock(ObjectSocketMock counterpart, Permit permit) {
		initialize(permit, counterpart);
	}

	private void initialize(Permit permit, ObjectSocketMock counterpart) {
		_permit = permit;
		_permit.addObjectToNotify(this);
		_counterpart = counterpart;
	}

	public void writeObject(Object object) throws IOException {
		_permit.check();
		_counterpart.receive(my(DeepCopier.class).deepCopy(object));
	}

	private synchronized void receive(Object object) {
		_receivedObjects.add(object);
		notify();
	}

	public synchronized Object readObject() throws IOException {
		_permit.check();
		if (_receivedObjects.isEmpty()) my(Threads.class).waitWithoutInterruptions(this);
		_permit.check();
		return _receivedObjects.remove(0);
	}

	public void close() {
		//Implement.
	}

	public ObjectSocket counterpart() {
		return _counterpart;
	}
	
}

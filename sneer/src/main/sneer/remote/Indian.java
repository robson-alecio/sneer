package sneer.remote;



import java.io.IOException;
import java.io.Serializable;

import sneer.life.Life;
import sneer.life.LifeView;
import wheel.experiments.environment.network.ObjectSocket;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.Signal.Receiver;

public class Indian implements Query<String>, Serializable {

	static private int _nextId = 1;

	private final int _id = _nextId++;

	transient Signal<String> _observedSignal;
	transient private ObjectSocket _socket;

	transient final private Source<String> _sourceToNotify;

	Indian(Source<String> sourceToNotify) {
		_sourceToNotify = sourceToNotify;
	}
	
	public void reportAbout(Life life, ObjectSocket socket) {
		System.out.println("Sitting Bull reporting, sir.");
		_socket = socket;
		_observedSignal = life.thoughtOfTheDay();
		_observedSignal.addReceiver(new Receiver<String>() {
			public void receive(String newThoughtOfTheDay) {
				try {
					System.out.println("Sending smoke signal..." + newThoughtOfTheDay);
					_socket.writeObject(new SmokeSignal(_id, newThoughtOfTheDay));
				} catch (IOException e) {
					e.printStackTrace();
					_observedSignal.removeReceiver(this);
				}
			}
		});
		
	}

	public int id() {
		return _id;
	}

	public void receive(SmokeSignal smokeSignal) {
		_sourceToNotify.supply(smokeSignal.newValue());
	}

	private static final long serialVersionUID = 1L;

	public String executeOn(LifeView ignored) {
		return "Ignored";
	}

}

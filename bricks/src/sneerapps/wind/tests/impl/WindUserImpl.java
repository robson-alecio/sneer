package sneerapps.wind.tests.impl;

import java.util.HashSet;
import java.util.Set;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.Environment;
import sneerapps.wind.Shout;
import sneerapps.wind.Wind;
import sneerapps.wind.tests.WindUser;
import wheel.lang.Omnivore;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.SetSignal.SetValueChange;

public class WindUserImpl implements WindUser {

	@Inject
	static private Wind _wind;
	
	@Inject
	static private Environment _environment;

	@Inject
	static private KeyManager _keyManager;
	
	private final Set<Object> _referenceKeeper = new HashSet<Object>();


	@Override
	public void connectTo(WindUser peer) {
		connect(_environment, peer.environment(), peer.publicKey());
		connect(peer.environment(), _environment, publicKey());
	}

	private void connect(Environment env1, final Environment env2, final PublicKey pk2) {
		Omnivore<SetValueChange<Shout>> receiver = new Omnivore<SetValueChange<Shout>>(){
			
			@Override public void consume(SetValueChange<Shout> tupleChange) {
				for (Shout shout : tupleChange.elementsAdded()) {
					System.out.println("-------------------");
					System.out.println("shout " + shout._publisher);
					System.out.println("to " + pk2);
					if (shout._publisher.equals(pk2)) return;
					System.out.println("copied");
					env2.publish(shout);
				}
			}
		};
		
		env1.subscribe(Shout.class).addSetReceiver(receiver);
		_referenceKeeper.add(receiver);
	}

	@Override
	public void shout(String phrase) {
		_wind.shout(phrase);
	}

	@Override
	public SetSignal<String> shoutsHeard() {
		return _wind.shoutsHeard();
	}

	@Override
	public Environment environment() {
		return _environment;
	}

	@Override
	public PublicKey publicKey() {
		return _keyManager.ownPublicKey();
	}

}

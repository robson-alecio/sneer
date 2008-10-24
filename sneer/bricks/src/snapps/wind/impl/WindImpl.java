package snapps.wind.impl;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import snapps.wind.Shout;
import snapps.wind.Wind;
import snapps.wind.impl.bubble.Bubble;
import sneer.kernel.container.Inject;
import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class WindImpl implements Wind, Omnivore<Shout> {

	@Inject
	static private TupleSpace _environment;
	
	@Inject
	static private PersistenceConfig _persistence;

	private final ListRegister<Shout> _shoutsHeard;

	{
		Prevayler prevayler;
		try {
			PrevaylerFactory factory = new PrevaylerFactory();
			factory.configureJournalSerializer(new XStreamSerializer());
			factory.configurePrevalenceDirectory(_persistence.persistenceDirectory().getAbsolutePath() + "/wind");
			factory.configurePrevalentSystem(new ListRegisterImpl<Shout>());
			factory.configureTransactionFiltering(false);
			prevayler = factory.create();

		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		_shoutsHeard = Bubble.wrapStateMachine(prevayler);
		
		for (Shout shout: _shoutsHeard.output())
			_environment.acquire(shout);
		
		_environment.addSubscription(Shout.class, this);
	}

	@Override
	public ListSignal<Shout> shoutsHeard() {
		return _shoutsHeard.output();
	}

	@Override
	public void consume(Shout shout) {
		_shoutsHeard.adder().consume(shout);
	}

	@Override
	public Omnivore<String> megaphone() {
		return new Omnivore<String>(){ @Override public void consume(String phrase) {
			shout(phrase);
		}};
	}

	private void shout(String phrase) {
		_environment.publish(new Shout(phrase));
	}

}

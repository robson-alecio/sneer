package sneer.pulp.transientpropertystore.tests;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import org.junit.Test;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import sneer.brickness.impl.Brickness;
import sneer.brickness.testsupport.BrickTest;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Functor;
import sneer.pulp.transientpropertystore.TransientPropertyStore;
import wheel.io.Jars;

public class TransparentPropertyStoreTest extends BrickTest {

	 private Prevayler _prevayler = null;
	
	@Test
	public void testPropertyStore() throws IOException {
		Brickness container = new Brickness(newPersister());
		
		container.placeBrick(Jars.classpathRootFor(TransientPropertyStore.class), TransientPropertyStore.class.getName());
		Environments.runWith(container.environment(),new Runnable() { @Override public void run() {
			TransientPropertyStore subject1 = my(TransientPropertyStore.class);
			assertNull(subject1.get("Height"));
			subject1.set("Height", "1,80m");
			subject1.set("Weight", "85kg");
			assertEquals("1,80m", subject1.get("Height"));
			assertEquals("85kg", subject1.get("Weight"));
		}});

		_prevayler.close();
		
		Brickness container2 = new Brickness(newPersister());
		container2.placeBrick(Jars.classpathRootFor(TransientPropertyStore.class), TransientPropertyStore.class.getName());
		Environments.runWith(container2.environment(),new Runnable() { @Override public void run() {
			TransientPropertyStore subject2 = my(TransientPropertyStore.class);
			assertEquals("1,80m", subject2.get("Height"));
			assertEquals("85kg", subject2.get("Weight"));
		}});
		
		_prevayler.close();
	}

	private Functor<Object, Object> newPersister() {
		return new Functor<Object, Object>() {
		@Override public Object evaluate(Object brick) {
			_prevayler = prevayler(brick);
			return Bubble.wrapStateMachine(_prevayler);
		}};
	}

	private Prevayler prevayler(Object system) {
		PrevaylerFactory factory = prevaylerFactory(system);

		try {
			return factory.create();
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}


	private PrevaylerFactory prevaylerFactory(Object system) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(system);
		factory.configurePrevalenceDirectory(tmpDirectory().getAbsolutePath());
		factory.configureJournalSerializer(new XStreamSerializer());
		factory.configureTransactionFiltering(false);
		return factory;
	}

}


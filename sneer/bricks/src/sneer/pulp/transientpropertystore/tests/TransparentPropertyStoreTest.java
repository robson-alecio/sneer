package sneer.pulp.transientpropertystore.tests;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import org.junit.Test;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import sneer.brickness.impl.BrickDecorator;
import sneer.brickness.impl.Brickness;
import sneer.brickness.testsupport.BrickTest;
import sneer.commons.environments.Environments;
import sneer.pulp.transientpropertystore.TransientPropertyStore;
import sneer.pulp.transientpropertystore2.TransientPropertyStore2;
import wheel.io.Jars;

public class TransparentPropertyStoreTest extends BrickTest {

	 private Prevayler _prevayler = null;
	
	@Test
	public void testPropertyStore() throws IOException {
		runWithTransparentPersistence(new Runnable() { @Override public void run() {
			TransientPropertyStore subject1 = my(TransientPropertyStore.class);
			assertNull(subject1.get("Height"));
			subject1.set("Height", "1,80m");
			subject1.set("Weight", "85kg");
			assertEquals("1,80m", subject1.get("Height"));
			assertEquals("85kg", subject1.get("Weight"));
			
			TransientPropertyStore2 subject2 = my(TransientPropertyStore2.class);
			
			subject2.setSuffix("2");
			subject2.set("Msg", "Hi!");
			assertEquals("Hi!2", subject1.get("Msg"));
			
			subject2.setSuffix("World!");
			subject2.set("Msg", "Hi!");
			assertEquals("Hi!World!", subject1.get("Msg"));
			
		}});
		
//		runWithTransparentPersistence(new Runnable() { @Override public void run() {
//			TransientPropertyStore subject1 = my(TransientPropertyStore.class);
//			assertEquals("1,80m", subject1.get("Height"));
//			assertEquals("85kg", subject1.get("Weight"));
//			assertEquals("Hi!World!", subject1.get("Msg"));
//			
//			TransientPropertyStore2 subject2 = my(TransientPropertyStore2.class);
//			assertEquals("World!", subject2.suffix());
//			
//		}});
	}

	private void runWithTransparentPersistence(Runnable runnable)	throws IOException {
		Brickness container = new Brickness(newPersister());
		placeBrick(container, TransientPropertyStore.class);
		placeBrick(container, TransientPropertyStore2.class);
		
		Environments.runWith(container.environment(), runnable);

		_prevayler.close();
	}

	private void placeBrick(Brickness container, Class<?> brick) {
		container.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}

	private BrickDecorator newPersister() {
		_prevayler = prevayler(new MethodInvoker());
		
		return new BrickDecorator() {@Override public Object decorate(Class<?> brick, Object brickImpl) {
			return Bubble.wrapStateMachine(brick, brickImpl, _prevayler);
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


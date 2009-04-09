package sneer.pulp.transientpropertystore.tests;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import sneer.brickness.impl.BrickDecorator;
import sneer.brickness.impl.BrickInstantiator;
import sneer.brickness.impl.Brickness;
import sneer.commons.environments.Environments;
import sneer.commons.testutil.TestThatMightUseResources;
import sneer.pulp.transientpropertystore.TransientPropertyStore;
import sneer.pulp.transientpropertystore2.TransientPropertyStore2;
import wheel.io.Jars;

public class TransparentPropertyStoreTest extends TestThatMightUseResources {

	@Test
	@Ignore
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
		
		runWithTransparentPersistence(new Runnable() { @Override public void run() {
			TransientPropertyStore subject1 = my(TransientPropertyStore.class);
			assertEquals("1,80m", subject1.get("Height"));
			assertEquals("85kg", subject1.get("Weight"));
			assertEquals("Hi!World!", subject1.get("Msg"));
			
			TransientPropertyStore2 subject2 = my(TransientPropertyStore2.class);
			assertEquals("World!", subject2.suffix());
			
		}});
	}

	private void runWithTransparentPersistence(Runnable runnable)	throws IOException {
		Brickness container = new Brickness(newPrevalentInstantiator(), newPrevalentDecorator());
		placeBrick(container, TransientPropertyStore.class);
		placeBrick(container, TransientPropertyStore2.class);
		
		Environments.runWith(container.environment(), runnable);

		Bubble.close();
	}

	private BrickInstantiator newPrevalentInstantiator() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	private void placeBrick(Brickness container, Class<?> brick) {
		container.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}

	private BrickDecorator newPrevalentDecorator() {
		return new BrickDecorator() {@Override public Object decorate(Class<?> brick, Object brickImpl) {
			return Bubble.wrapStateMachine(brick, brickImpl, tmpDirectory());
		}};
	}

}


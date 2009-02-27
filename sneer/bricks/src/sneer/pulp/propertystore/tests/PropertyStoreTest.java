package sneer.pulp.propertystore.tests;

import static sneer.brickness.Environments.my;

import org.junit.Test;

import sneer.brickness.Environments;
import sneer.kernel.container.Container;
import sneer.kernel.container.Containers;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.propertystore.PropertyStore;
import wheel.testutil.TestThatMightUseResources;

public class PropertyStoreTest extends TestThatMightUseResources {

	private final PersistenceConfigMock _persistenceMock = new PersistenceConfigMock(tmpDirectory());

	@Test
	public void testPropertyStore() {
		runInNewEnvironment(new Runnable() { @Override public void run() {
			PropertyStore subject1 = my(PropertyStore.class);
			assertNull(subject1.get("Height"));
			subject1.set("Height", "1,80m");
			subject1.set("Weight", "85kg");
			assertEquals("1,80m", subject1.get("Height"));
			assertEquals("85kg", subject1.get("Weight"));
		}});
		
		runInNewEnvironment(new Runnable() { @Override public void run() {
			PropertyStore subject2 = my(PropertyStore.class);
			assertEquals("1,80m", subject2.get("Height"));
			assertEquals("85kg", subject2.get("Weight"));
		}});
	}

	private void runInNewEnvironment(Runnable runnable) {
		final Container container = Containers.newContainer(_persistenceMock);
		Environments.runWith(container, runnable);
	}
	
}


package sneer.pulp.propertystore.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.commons.environments.Environments;
import sneer.commons.io.StoragePath;
import sneer.commons.testutil.TestThatMightUseResources;
import sneer.kernel.container.Container;
import sneer.kernel.container.Containers;
import sneer.pulp.propertystore.PropertyStore;

public class PropertyStoreTest extends TestThatMightUseResources {

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
		final Container container = Containers.newContainer(new StoragePath(){ @Override public String get() {
			return tmpDirectory().getAbsolutePath();
		}});
		Environments.runWith(container, runnable);
	}
	
}


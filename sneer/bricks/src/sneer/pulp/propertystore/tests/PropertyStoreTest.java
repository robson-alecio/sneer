package sneer.pulp.propertystore.tests;

import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.propertystore.PropertyStore;
import wheel.testutil.TestThatMightUseResources;

public class PropertyStoreTest extends TestThatMightUseResources {

	private final PersistenceConfigMock _persistenceMock = new PersistenceConfigMock();

	@Test
	public void testPropertyStore() {
		PropertyStore subject1 = createSubject();

		assertNull(subject1.get("Height"));
		subject1.set("Height", "1,80m");
		subject1.set("Weight", "85kg");
		assertEquals("1,80m", subject1.get("Height"));
		assertEquals("85kg", subject1.get("Weight"));
		
		PropertyStore subject2 = createSubject();
		assertEquals("1,80m", subject2.get("Height"));
		assertEquals("85kg", subject2.get("Weight"));
		
	}

	private PropertyStore createSubject() {
		return ContainerUtils.newContainer(_persistenceMock).produce(PropertyStore.class);
	}
	
}


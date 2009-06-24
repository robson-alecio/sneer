package sneer.bricks.hardware.io.brickstatestore.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.io.brickstatestore.BrickStateStore;
import sneer.foundation.brickness.testsupport.BrickTest;

public class BrickStateStoreTest extends BrickTest {
	
	private final BrickStateStore _subject = my(BrickStateStore.class);

	@Test
	public void writeAndReadBrickState() throws Exception {
		String toStore = "Teste";

		_subject.writeObjectFor(BrickStateStore.class, toStore);
		String restored = (String) _subject.readObjectFor(BrickStateStore.class, getClass().getClassLoader());
		
		assertEquals(toStore, restored);
		assertTrue(toStore!=restored);
	}

}


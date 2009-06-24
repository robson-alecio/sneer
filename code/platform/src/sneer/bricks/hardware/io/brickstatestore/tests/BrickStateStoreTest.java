package sneer.bricks.hardware.io.brickstatestore.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.bricks.hardware.io.brickstatestore.BrickStateStore;
import sneer.foundation.brickness.testsupport.BrickTest;

public class BrickStateStoreTest extends BrickTest {
	
	private final BrickStateStore _subject = my(BrickStateStore.class);

	@Test
	public void writeAndReadBrickState() throws Exception {
		List<String> toStore = new ArrayList<String>();
		toStore.add("1");
		toStore.add("2");
		toStore.add("3");

		_subject.writeObjectFor(BrickStateStore.class, toStore);
		List<String> restored = (List<String>) _subject.readObjectFor(BrickStateStore.class, getClass().getClassLoader());
		
		assertEquals(toStore.size(), restored.size());
		assertTrue(toStore!=restored);
	}
}


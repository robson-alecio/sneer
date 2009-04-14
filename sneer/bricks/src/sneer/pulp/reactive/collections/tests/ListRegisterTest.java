package sneer.pulp.reactive.collections.tests;

import static sneer.commons.environments.Environments.my;

import java.util.ArrayList;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ReactiveCollections;
import wheel.reactive.impl.EventReceiver;

public class ListRegisterTest extends BrickTest {
	
	@Test
	public void testSize() {
		
		final ListRegister<String> _subject = my(ReactiveCollections.class).newListRegister();
		
		final ArrayList<Integer> _sizes = new ArrayList<Integer>();
		
		@SuppressWarnings("unused")
		final EventReceiver<Integer> sizeReceiver = new EventReceiver<Integer>(_subject.output().size()) {@Override public void consume(Integer value) {
			_sizes.add(value);
		}};
		
		assertEquals(0, _subject.output().currentSize());
		assertSameContents(_sizes, 0);
		
		_subject.add("spam");
		
		assertEquals(1, _subject.output().currentSize());
		assertSameContents(_sizes, 0, 1);
		
		_subject.add("eggs");
		assertEquals(2, _subject.output().currentSize());
		assertSameContents(_sizes, 0, 1, 2);
	}

}

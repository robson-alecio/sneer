package sneer.pulp.reactive.collections.tests;

import static sneer.commons.environments.Environments.my;

import java.util.ArrayList;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.CollectionSignals;
import sneer.pulp.reactive.collections.ListRegister;

public class ListRegisterTest extends BrickTest {

	@Test
	public void testSize() {
		final ListRegister<String> _subject = my(CollectionSignals.class).newListRegister();
		final ArrayList<Integer> _sizes = new ArrayList<Integer>();

		@SuppressWarnings("unused") final Object referenceToAvoidGc = my(Signals.class).receive(_subject.output().size(), new Consumer<Integer>() {@Override public void consume(Integer value) {
			_sizes.add(value);
		}});

		assertEquals(0, _subject.output().size().currentValue().intValue());
		assertSameContents(_sizes, 0);

		_subject.add("spam");
		assertEquals(1, _subject.output().size().currentValue().intValue());
		assertSameContents(_sizes, 0, 1);

		_subject.add("eggs");
		assertEquals(2, _subject.output().size().currentValue().intValue());
		assertSameContents(_sizes, 0, 1, 2);
	}
}

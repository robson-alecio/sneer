package sneer.bricks.pulp.reactive.collections.listsorter.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.Comparator;

import org.jmock.Expectations;
import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListChange;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.ListChange.Visitor;
import sneer.bricks.pulp.reactive.collections.listsorter.ListSorter;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;
import sneer.foundation.testsupport.AssertUtils;

@Ignore
public class ListSorterTest extends BrickTest {
	
	private final ListSorter _sorter = my(ListSorter.class);

	private final Visitor<Signal<Integer>> _visitor = mock(Visitor.class);
	
	private final Signal<Integer> _00 = my(Signals.class).constant(00);
	private final Signal<Integer> _01 = my(Signals.class).constant(01);
	private final Signal<Integer> _05 = my(Signals.class).constant(05);
	private final Signal<Integer> _10 = my(Signals.class).constant(10);
	private final Signal<Integer> _20 = my(Signals.class).constant(20);
	private final Signal<Integer> _30 = my(Signals.class).constant(30);
	private final Signal<Integer> _40 = my(Signals.class).constant(40);
	private final Signal<Integer> _50 = my(Signals.class).constant(50);
	private final Signal<Integer> _60 = my(Signals.class).constant(60);

	private final SignalChooser<Signal<Integer>> _chooser = new SignalChooser<Signal<Integer>>(){ @Override public Signal<?>[] signalsToReceiveFrom(Signal<Integer> element) {
		return new Signal<?>[]{element};
	}};

	@Test
	public void testVisitor() {
		checking(new Expectations(){{
			one(_visitor).elementAdded(0, _50);
			one(_visitor).elementAdded(0, _00);
			one(_visitor).elementAdded(1, _10);
			one(_visitor).elementAdded(2, _30);
			one(_visitor).elementAdded(2, _20);
			one(_visitor).elementAdded(4, _40);

			one(_visitor).elementRemoved(2, _20);
			one(_visitor).elementRemoved(2, _30);
			one(_visitor).elementRemoved(3, _50);

			one(_visitor).elementRemoved(2, _40);
			one(_visitor).elementAdded(1, _05);
		}});		

		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_60);
		src.remove(_60);

		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);
		Consumer<ListChange<Signal<Integer>>> consumer = new Consumer<ListChange<Signal<Integer>>>(){ @Override public void consume(ListChange<Signal<Integer>> value) {
			value.accept(_visitor);
		}};
		sortedList.addListReceiver(consumer);

		src.add(_50);
		src.add(_00);
		src.add(_10);
		src.add(_30);
		src.add(_20);
		src.addAt(0, _40);

		src.remove(_20);
		src.remove(_30);
		src.removeAt(1);

		src.replace(0, _05);
	}
	
	@Test
	public void removeTest() {
		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_20);
		src.add(_10);

		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(_30);
		src.add(_50);
		src.add(_40);
		src.add(_05);
		src.add(_60);

		// src = [ 20, 10, 30, 50, 40, 5, 60 ]
		AssertUtils.assertSameContents(sortedList, _05, _10, _20, _30, _40, _50, _60);

		src.remove(_20); // src = [ 10, 30, 50, 40, 5, 60 ]
		AssertUtils.assertSameContents(sortedList, _05, _10, _30, _40, _50, _60);

		src.remove(_10); // src = [ 30, 50, 40, 5, 60 ]
		AssertUtils.assertSameContents(sortedList, _05, _30, _40, _50, _60);

		src.removeAt(2); // src = [ 30, 50, 5, 60 ]
		AssertUtils.assertSameContents(sortedList, _05, _30, _50, _60);

		src.removeAt(3); // src = [ 30, 50, 5 ]
		AssertUtils.assertSameContents(sortedList, _05, _30, _50);
	}

	private Signal<Integer> signal(Register<Integer> register) {
		return register.output();
	}	
	
	@Test
	public void replaceTest() {

		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_30);
		src.add(_20);

		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(_10);
		src.add(_00);
		src.add(_40);

		// src = [ 30, 20, 10, 0, 40 ]
		AssertUtils.assertSameContents(sortedList, _00, _10, _20, _30, _40);

		src.replace(0, _60); // src = [ 60, 20, 10, 0, 40 ]
		AssertUtils.assertSameContents(sortedList, _00, _10, _20, _40, _60);

		src.replace(2, _50); // src = [ 60, 20, 50, 0, 40 ]
		AssertUtils.assertSameContents(sortedList, _00, _20, _40, _50, _60);

		src.replace(4, _01); // src = [ 60, 20, 50, 0, 1 ]
		AssertUtils.assertSameContents(sortedList, _00, _01, _20, _50, _60);

		src.replace(3, _05); // src = [ 60, 20, 50, 5, 1 ]
		AssertUtils.assertSameContents(sortedList, _01, _05, _20, _50, _60);
	}	

	@Test
	public void addTest() {

		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_20); // src = [ 20 ]
		src.add(_10); // src = [ 20, 10 ]

		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		assertEquals(2 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _10, _20);

		src.addAt(1, _05); // src = [ 20, 5, 10 ]
		assertEquals(3 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _05, _10, _20);

		src.add(_50); // src = [ 20, 5, 10, 50 ]
		assertEquals(4 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _05, _10, _20, _50);

		src.add(_30); // src = [ 20, 5, 10, 50, 30 ] 
		assertEquals(5 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _05, _10, _20, _30, _50);

		src.add(_01); // src = [ 20, 5, 10, 50, 30, 1 ]
		assertEquals(6 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _01, _05, _10, _20, _30, _50);
	}

	private int listSize(ListSignal<Signal<Integer>> sortedList) {
		return sortedList.size().currentValue().intValue();
	}

	@Test
	public void signalChooserTest() {
		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		Register<Integer> r15 = my(Signals.class).newRegister(15);
		Register<Integer> r25 = my(Signals.class).newRegister(25);
		Register<Integer> r35 = my(Signals.class).newRegister(35);
		Register<Integer> r45 = my(Signals.class).newRegister(45);

		Signal<Integer> s15 = signal(r15);
		Signal<Integer> s25 = signal(r25);
		Signal<Integer> s35 = signal(r35);
		Signal<Integer> s45 = signal(r45);

		src.add(s35);
		src.add(s45);

		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(s15);
		src.add(s25);

		src.add(_10);
		src.add(_20);
		src.add(_30);
		src.add(_40);

		// src = [ 35, 45, 15, 25, 10, 20, 30, 40 ]
		AssertUtils.assertSameContents(sortedList, _10, s15, _20, s25, _30, s35, _40, s45);

		r15.setter().consume(50); // src = [ 35, 45, 50, 25, 10, 20, 30, 40 ]
		AssertUtils.assertSameContents(sortedList, _10, _20, s25, _30, s35, _40, s45, s15);

		r25.setter().consume(5); // src = [ 35, 45, 50, 5, 10, 20, 30, 40 ]
		AssertUtils.assertSameContents(sortedList,  s25, _10, _20, _30, s35, _40, s45, s15);

		r35.setter().consume(29); // src = [ 29, 45, 50, 25, 10, 20, 30, 40 ]
		AssertUtils.assertSameContents(sortedList,  s25, _10, _20, s35, _30, _40, s45, s15);

		r45.setter().consume(60); // src = [ 29, 60, 50, 25, 10, 20, 30, 40 ]
		AssertUtils.assertSameContents(sortedList,  s25, _10, _20, s35, _30, _40, s15, s45);
	}
	
	private Comparator<Signal<Integer>> integerComparator() {
		return new Comparator<Signal<Integer>>() { @Override public int compare(Signal<Integer> integer1, Signal<Integer> integer2) {
			return integer1.currentValue().compareTo(integer2.currentValue());
		}};
	}
}
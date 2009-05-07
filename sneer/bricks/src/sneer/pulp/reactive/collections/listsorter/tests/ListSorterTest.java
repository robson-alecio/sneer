package sneer.pulp.reactive.collections.listsorter.tests;

import static sneer.commons.environments.Environments.my;

import java.util.Comparator;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.brickness.testsupport.AssertUtils;
import sneer.brickness.testsupport.BrickTest;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.CollectionSignals;
import sneer.pulp.reactive.collections.ListChange;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ListChange.Visitor;
import sneer.pulp.reactive.collections.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;

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
			
			one(_visitor).elementAdded(1, _10);
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
		src.replace(0, _10);
	}
	
	@Test
	public void removeTest() {
		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_20);
		src.add(_10);
		
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(_20);
		src.add(_30);
		src.add(_20);
		src.add(_30);
		src.add(_10);
		
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _20, _20, _30, _30);
		src.remove(_20);
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _20, _30, _30);
		src.remove(_20);
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _30, _30);
		src.removeAt(2);
		AssertUtils.assertSameContents(sortedList, _10, _10, _30, _30);
		src.removeAt(3);
		AssertUtils.assertSameContents(sortedList, _10, _30, _30);
	}	

	private Signal<Integer> signal(Register<Integer> register) {
		return register.output();
	}	
	
	@Test
	public void replaceTest() {
		
		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_30);
		src.add(_30);
		
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);
		
		src.add(_20);
		src.add(_20);
		src.add(_10);
		src.add(_20);
		src.add(_10);
		
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _20, _20, _30, _30);
		src.replace(0, _60);
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _20, _20, _30, _60);
		src.replace(2, _40);
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _20, _30, _40, _60);
		src.replace(5, _50);
		AssertUtils.assertSameContents(sortedList, _10, _10, _20, _30, _40, _50, _60);
		src.replace(6, _05);
		AssertUtils.assertSameContents(sortedList, _05, _10, _20, _30, _40, _50, _60);
	}	
	
	@Test
	public void addTest() {
		
		ListRegister<Signal<Integer>> src = my(CollectionSignals.class).newListRegister();

		src.add(_20);
		src.add(_10);
		
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);
		
		assertEquals(2 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _10, _20);
		
		src.addAt(1, _05);
		assertEquals(3 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _05, _10, _20);
		
		src.add(_10);
		assertEquals(4 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _05, _10, _10, _20);
		
		src.add(_30);
		assertEquals(5 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _05, _10, _10, _20, _30);

		src.add(_01);
		assertEquals(6 , listSize(sortedList));
		AssertUtils.assertSameContents(sortedList, _01, _05, _10, _10, _20, _30);
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
		
		AssertUtils.assertSameContents(sortedList, _10, s15, _20, s25, _30, s35, _40, s45);
		
		r15.setter().consume(50);
		AssertUtils.assertSameContents(sortedList, _10, _20, s25, _30, s35, _40, s45, s15);
		
		r25.setter().consume(5);
		AssertUtils.assertSameContents(sortedList,  s25, _10, _20, _30, s35, _40, s45, s15);
		
		r35.setter().consume(29);
		AssertUtils.assertSameContents(sortedList,  s25, _10, _20, s35, _30, _40, s45, s15);
		
		r45.setter().consume(60);
		AssertUtils.assertSameContents(sortedList,  s25, _10, _20, s35, _30, _40, s15, s45);
	}
	
	private Comparator<Signal<Integer>> integerComparator() {
		return new Comparator<Signal<Integer>>() { @Override public int compare(Signal<Integer> integer1, Signal<Integer> integer2) {
			return integer1.currentValue().compareTo(integer2.currentValue());
		}};
	}
}
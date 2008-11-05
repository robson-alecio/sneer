package sneer.pulp.reactive.listsorter.tests;

import java.util.Comparator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.listsorter.ListSorter.SignalChooser;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.testutil.TestUtils;

public class ListSorterTest extends TestThatIsInjected {
	
	@Inject
	private static ListSorter _sorter;

	private final Mockery _mockery = new JUnit4Mockery();	
	
	private final Visitor<Signal<Integer>> _visitor = _mockery.mock(Visitor.class);
	
	private final Signal<Integer> _00 = new Constant<Integer>(00);
	private final Signal<Integer> _01 = new Constant<Integer>(01);
	private final Signal<Integer> _05 = new Constant<Integer>(05);
	private final Signal<Integer> _10 = new Constant<Integer>(10);
	private final Signal<Integer> _20 = new Constant<Integer>(20);
	private final Signal<Integer> _30 = new Constant<Integer>(30);
	private final Signal<Integer> _40 = new Constant<Integer>(40);
	private final Signal<Integer> _50 = new Constant<Integer>(50);
	private final Signal<Integer> _60 = new Constant<Integer>(60);

	private final SignalChooser<Signal<Integer>> _chooser = new SignalChooser<Signal<Integer>>(){ @Override public Signal<?>[] signalsToReceiveFrom(Signal<Integer> element) {
		return new Signal<?>[]{element};
	}};
	
	@Test
	public void testVisitor() {
		_mockery.checking(new Expectations(){{
			one(_visitor).elementAdded(0, _50);
			one(_visitor).elementAdded(0, _00);
			one(_visitor).elementAdded(1, _10);
			one(_visitor).elementAdded(2, _30);
			one(_visitor).elementAdded(2, _20);
			one(_visitor).elementAdded(4, _40);
			
			one(_visitor).elementToBeRemoved(2, _20);
			one(_visitor).elementRemoved(2, _20);
			
			one(_visitor).elementToBeRemoved(2, _30);
			one(_visitor).elementRemoved(2, _30);
			
			one(_visitor).elementToBeRemoved(3, _50);
			one(_visitor).elementRemoved(3, _50);
			
			one(_visitor).elementToBeRemoved(2, _40);
			one(_visitor).elementRemoved(2, _40);
			one(_visitor).elementAdded(1, _10);
		}});		
		
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);
		
		Omnivore<ListValueChange<Signal<Integer>>> consumer = new Omnivore<ListValueChange<Signal<Integer>>>(){ @Override public void consume(ListValueChange<Signal<Integer>> value) {
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
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(_20);
		src.add(_10);
		src.add(_20);
		src.add(_30);
		src.add(_20);
		src.add(_30);
		src.add(_10);
		
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _20, _20, _30, _30);
		src.remove(_20);
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _20, _30, _30);
		src.remove(_20);
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _30, _30);
		src.removeAt(2);
		TestUtils.assertSameContents(sortedList, _10, _10, _30, _30);
		src.removeAt(3);
		TestUtils.assertSameContents(sortedList, _10, _30, _30);
	}	
	
	@Test
	public void replaceTest() {
		
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(_30);
		src.add(_30);
		src.add(_20);
		src.add(_20);
		src.add(_10);
		src.add(_20);
		src.add(_10);
		
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _20, _20, _30, _30);
		src.replace(0, _60);
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _20, _20, _30, _60);
		src.replace(2, _40);
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _20, _30, _40, _60);
		src.replace(5, _50);
		TestUtils.assertSameContents(sortedList, _10, _10, _20, _30, _40, _50, _60);
		src.replace(6, _05);
		TestUtils.assertSameContents(sortedList, _05, _10, _20, _30, _40, _50, _60);
	}	
	
	@Test
	public void addTest() {
		
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

		src.add(_20);
		TestUtils.assertSameContents(sortedList, _20);
		src.add(_10);
		TestUtils.assertSameContents(sortedList, _10, _20);
		src.addAt(1, _05);
		TestUtils.assertSameContents(sortedList, _05, _10, _20);
		src.add(_10);
		TestUtils.assertSameContents(sortedList, _05, _10, _10, _20);
		src.add(_30);
		TestUtils.assertSameContents(sortedList, _05, _10, _10, _20, _30);
		src.add(_01);
		TestUtils.assertSameContents(sortedList, _01, _05, _10, _10, _20, _30);
	}
	
	private Comparator<Signal<Integer>> integerComparator() {
		return new Comparator<Signal<Integer>>() { @Override public int compare(Signal<Integer> integer1, Signal<Integer> integer2) {
			return integer1.currentValue().compareTo(integer2.currentValue());
		}};
	}
}
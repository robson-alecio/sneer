package sneer.pulp.reactive.listsorter.tests;

import java.util.Comparator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.impl.RegisterImpl;
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
			
			one(_visitor).elementRemoved(2, _20);
			
			one(_visitor).elementRemoved(2, _30);
			
			one(_visitor).elementRemoved(3, _50);
			
			one(_visitor).elementRemoved(2, _40);
			one(_visitor).elementAdded(1, _10);
		}});		
		
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();
	
		src.add(_60);
		src.remove(_60);

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

		src.add(_20);
		src.add(_10);
		
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);

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

	private Signal<Integer> signal(Register<Integer> r10) {
		return r10.output();
	}	
	
	@Test
	public void replaceTest() {
		
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();

		src.add(_30);
		src.add(_30);
		
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);
		
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

		src.add(_20);
		src.add(_10);
		
		ListSignal<Signal<Integer>> sortedList = _sorter.sort(src.output(), integerComparator(), _chooser);
		
		assertEquals(2 , listSize(sortedList));
		TestUtils.assertSameContents(sortedList, _10, _20);
		
		src.addAt(1, _05);
		assertEquals(3 , listSize(sortedList));
		TestUtils.assertSameContents(sortedList, _05, _10, _20);
		
		src.add(_10);
		assertEquals(4 , listSize(sortedList));
		TestUtils.assertSameContents(sortedList, _05, _10, _10, _20);
		
		src.add(_30);
		assertEquals(5 , listSize(sortedList));
		TestUtils.assertSameContents(sortedList, _05, _10, _10, _20, _30);

		src.add(_01);
		assertEquals(6 , listSize(sortedList));
		TestUtils.assertSameContents(sortedList, _01, _05, _10, _10, _20, _30);
	}

	private int listSize(ListSignal<Signal<Integer>> sortedList) {
		return sortedList.size().currentValue().intValue();
	}
	
	@Test
	public void signalChooserTest() {
		ListRegister<Signal<Integer>> src = new ListRegisterImpl<Signal<Integer>>();

		Register<Integer> r15 = new RegisterImpl<Integer>(15);
		Register<Integer> r25 = new RegisterImpl<Integer>(25);
		Register<Integer> r35 = new RegisterImpl<Integer>(35);
		Register<Integer> r45 = new RegisterImpl<Integer>(45);
		
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
		
		TestUtils.assertSameContents(sortedList, _10, s15, _20, s25, _30, s35, _40, s45);
		
		r15.setter().consume(50);
		TestUtils.assertSameContents(sortedList, _10, _20, s25, _30, s35, _40, s45, s15);
		
		r25.setter().consume(5);
		TestUtils.assertSameContents(sortedList,  s25, _10, _20, _30, s35, _40, s45, s15);
		
		r35.setter().consume(29);
		TestUtils.assertSameContents(sortedList,  s25, _10, _20, s35, _30, _40, s45, s15);
		
		r45.setter().consume(60);
		TestUtils.assertSameContents(sortedList,  s25, _10, _20, s35, _30, _40, s15, s45);
	}
	
	private Comparator<Signal<Integer>> integerComparator() {
		return new Comparator<Signal<Integer>>() { @Override public int compare(Signal<Integer> integer1, Signal<Integer> integer2) {
			return integer1.currentValue().compareTo(integer2.currentValue());
		}};
	}
}
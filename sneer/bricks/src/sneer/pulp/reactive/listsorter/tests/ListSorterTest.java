package sneer.pulp.reactive.listsorter.tests;

import java.util.Comparator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.reactive.listsorter.ListSorter;
import wheel.lang.Omnivore;
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
	
	private final Visitor<Integer> _visitor = _mockery.mock(Visitor.class);
	
	@Test
	public void testVisitor() {
		_mockery.checking(new Expectations(){{
			one(_visitor).elementAdded(0, 50);
			one(_visitor).elementAdded(0, 00);
			one(_visitor).elementAdded(1, 10);
			one(_visitor).elementAdded(2, 30);
			one(_visitor).elementAdded(2, 20);
			one(_visitor).elementAdded(4, 40);
			
			one(_visitor).elementToBeRemoved(2, 20);
			one(_visitor).elementRemoved(2, 20);
			
			one(_visitor).elementToBeRemoved(2, 30);
			one(_visitor).elementRemoved(2, 30);
			
			one(_visitor).elementToBeRemoved(3, 50);
			one(_visitor).elementRemoved(3, 50);
			
			one(_visitor).elementToBeRemoved(2, 40);
			one(_visitor).elementRemoved(2, 40);
			one(_visitor).elementAdded(1, 10);
		}});		
		
		ListRegister<Integer> src = new ListRegisterImpl<Integer>();
		ListSignal<Integer> sortedList = _sorter.sort(src.output(), integerComparator());
		
		Omnivore<ListValueChange<Integer>> consumer = new Omnivore<ListValueChange<Integer>>(){ @Override public void consume(ListValueChange<Integer> value) {
			value.accept(_visitor);
		}};
		sortedList.addListReceiver(consumer);
		
		src.add(50);
		src.add(00);
		src.add(10);
		src.add(30);
		src.add(20);
		src.addAt(0, 40);
		
		src.remove(20);
		src.remove(30);
		
		src.removeAt(1);
		src.replace(0, 10);
	}
	
	@Test
	public void removeTest() {
		ListRegister<Integer> src = new ListRegisterImpl<Integer>();
		ListSignal<Integer> sortedList = _sorter.sort(src.output(), integerComparator());

		src.add(20);
		src.add(10);
		src.add(20);
		src.add(30);
		src.add(20);
		src.add(30);
		src.add(10);
		
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 20, 20, 30, 30);
		src.remove(20);
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 20, 30, 30);
		src.remove(20);
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 30, 30);
		src.removeAt(2);
		TestUtils.assertSameContents(sortedList, 10, 10, 30, 30);
		src.removeAt(3);
		TestUtils.assertSameContents(sortedList, 10, 30, 30);
	}	
	
	@Test
	public void replaceTest() {
		
		ListRegister<Integer> src = new ListRegisterImpl<Integer>();
		ListSignal<Integer> sortedList = _sorter.sort(src.output(), integerComparator());

		src.add(30);
		src.add(30);
		src.add(20);
		src.add(20);
		src.add(10);
		src.add(20);
		src.add(10);
		
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 20, 20, 30, 30);
		src.replace(0, 60);
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 20, 20, 30, 60);
		src.replace(2, 40);
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 20, 30, 40, 60);
		src.replace(5, 50);
		TestUtils.assertSameContents(sortedList, 10, 10, 20, 30, 40, 50, 60);
		src.replace(6, 5);
		TestUtils.assertSameContents(sortedList, 5, 10, 20, 30, 40, 50, 60);
	}	
	
	@Test
	public void addTest() {
		
		ListRegister<Integer> src = new ListRegisterImpl<Integer>();
		ListSignal<Integer> sortedList = _sorter.sort(src.output(), integerComparator());

		src.add(20);
		TestUtils.assertSameContents(sortedList, 20);
		src.add(10);
		TestUtils.assertSameContents(sortedList, 10, 20);
		src.addAt(1, 5);
		TestUtils.assertSameContents(sortedList, 5, 10, 20);
		src.add(10);
		TestUtils.assertSameContents(sortedList, 5, 10, 10, 20);
		src.add(30);
		TestUtils.assertSameContents(sortedList, 5, 10, 10, 20, 30);
		src.add(1);
		TestUtils.assertSameContents(sortedList, 1, 5, 10, 10, 20, 30);
	}
	
	private Comparator<Integer> integerComparator() {
		return new Comparator<Integer>() { @Override public int compare(Integer integer1, Integer integer2) {
			return integer1- integer2;
		}};
	}
}
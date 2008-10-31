package sneer.pulp.signal.listsorter.tests;

import java.util.Comparator;

import org.junit.Ignore;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.signal.listsorter.ListSorter;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.testutil.TestUtils;

public class ListSorterTest extends TestThatIsInjected {
	
	@Inject
	private static ListSorter<Integer> _sorter;

	@Test
	@Ignore
	public void test() {
		ListRegister<Integer> src = new ListRegisterImpl<Integer>();
		src.add(10);
		src.add(40);
		src.add(20);
		src.add(30);
		
		ListSignal<Integer> sortedList = _sorter.sort(src.output(), 
				new Comparator<Integer>() { @Override public int compare(Integer integer1, Integer integer2) {
					return integer1- integer2;
		}});
		
		TestUtils.assertSameContents(sortedList, 10, 20, 30, 40);
		
		src.add(5);
		TestUtils.assertSameContents(sortedList, 5, 10, 20, 30, 40);
		
		src.remove(40);
		TestUtils.assertSameContents(sortedList, 5, 10, 20, 30);
	}
}
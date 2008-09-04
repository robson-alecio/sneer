package wheel.reactive.lists.impl.tests;

import static org.junit.Assert.assertEquals;
import static wheel.testutil.TestUtils.assertListEquals;

import java.util.ArrayList;

import org.junit.Test;

import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class ListRegisterImplTest {
	
	@Test
	public void testSize() {
		
		final ListRegister<String> register = new ListRegisterImpl<String>();
		
		final ArrayList<Integer> sizes = new ArrayList<Integer>();
		new Receiver<Integer>(register.output().size()) {@Override public void consume(Integer value) {
			sizes.add(value);
		}};
		
		register.add("spam");
		
		assertEquals(1, register.output().currentSize());
		assertListEquals(sizes, 0, 1);
		
		register.add("eggs");
		assertEquals(2, register.output().currentSize());
		assertListEquals(sizes, 0, 1, 2);
	}

}

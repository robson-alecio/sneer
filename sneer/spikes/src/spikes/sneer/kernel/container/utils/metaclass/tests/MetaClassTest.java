package spikes.sneer.kernel.container.utils.metaclass.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.software.code.metaclass.MetaClass;
import sneer.software.code.metaclass.MetaClasses;
import spikes.sneer.kernel.container.utils.metaclass.tests.bean.Bean;


public class MetaClassTest extends BrickTest {

	@Test
	public void testMetaClass() throws Exception {
		MetaClass metaClass = my(MetaClasses.class).metaClass(Bean.class);
		assertTrue(metaClass.isInterface());
		assertEquals("sneer.kernel.container.utils.metaclass.tests.bean.Bean", metaClass.getName());
		assertEquals("sneer.kernel.container.utils.metaclass.tests.bean", metaClass.getPackageName());

		//assertTrue(metaClass.isAssignanbleTo(Object.class));
	}
}

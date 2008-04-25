package sneer.lego.utils.metaclass.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FilenameUtils;
import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.utils.metaclass.ClassUtils;
import sneer.lego.utils.metaclass.MetaClass;
import sneer.lego.utils.metaclass.tests.bean.Bean;

public class MetaClassTest {

	@Test
	public void testMetaClass() throws Exception {
		MetaClass metaClass = ClassUtils.metaClass(Bean.class);
		assertTrue(metaClass.isInterface());
		assertEquals("sneer.lego.utils.metaclass.tests.bean.Bean", metaClass.getName());
		assertEquals("sneer.lego.utils.metaclass.tests.bean", metaClass.getPackageName());
		//assertTrue(metaClass.isAssignanbleTo(Object.class));
	}

	@Test
	@Ignore
	public void testPathNames() {
		String filename = "c:\\a\\b\\c.txt";
		System.out.println(FilenameUtils.separatorsToUnix(filename));
		System.out.println(FilenameUtils.normalize(filename));
		System.out.println(FilenameUtils.getPath(filename));
	}
}

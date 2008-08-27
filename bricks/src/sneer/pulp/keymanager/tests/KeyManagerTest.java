package sneer.pulp.keymanager.tests;

import junit.framework.Assert;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.BrickTestSupport;
import sneer.pulp.own.name.OwnNameKeeper;

public class KeyManagerTest extends BrickTestSupport {

	@Inject
	private OwnNameKeeper ownNameKeeper;
	
	@Test
	public void testAddKey() throws Exception {
		String name = ownNameKeeper.getName();
		Assert.assertNotNull(name);
		name = "";
		int i = 0;
		
		name = "" + i++;
		ownNameKeeper.setName(name);
		Assert.assertEquals(ownNameKeeper.getName(), name);
		
		name = "" + i++;
		ownNameKeeper.nameSetter().consume(name);
		Assert.assertEquals(ownNameKeeper.name().currentValue(), name);
		
		name = "" + i++;
		ownNameKeeper.setName(name);
		Assert.assertEquals(ownNameKeeper.name().currentValue(), name);
		
		name = "" + i++;
		ownNameKeeper.nameSetter().consume(name);
		Assert.assertEquals(ownNameKeeper.getName(), name);
	}
}

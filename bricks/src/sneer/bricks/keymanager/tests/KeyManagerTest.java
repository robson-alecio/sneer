package sneer.bricks.keymanager.tests;

import junit.framework.Assert;

import org.junit.Test;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;

public class KeyManagerTest extends BrickTestSupport {

	@Inject
	private OwnNameKeeper ownNameKeeper;
	
	@Test
	public void testAddKey() throws Exception {
		String name = ownNameKeeper.getName();
		Assert.assertEquals(null, name);
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

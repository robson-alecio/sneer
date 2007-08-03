package sneer.kernel.pointofview.tests;

import junit.framework.TestCase;
import sneer.kernel.pointofview.Party;

public abstract class PointOfViewTests extends TestCase {

	protected interface Subject {

		void setName(String newName);
		Party contactNamed(String name);

	}

	protected abstract Subject me();
	protected abstract Subject other();

	public void testRemoteNameChange() {
		//if (!newTestsShouldRun()) return;
		
		me().setName("Klaus Wuestefeld");
		other().setName("Ricardo Andere de Mello");
		
		Party contact = me().contactNamed("Ricardo Andere de Mello");
		assertNotNull(contact);
		
		other().setName("Ricardo Gandhi de Mello");
		assertEquals("Ricardo Gandhi de Mello", contact.name().currentValue());
		
	}


}

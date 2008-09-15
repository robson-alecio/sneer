package wheel.io.ui.impl.tests;

import java.awt.Rectangle;

import wheel.io.files.impl.tranzient.TransientDirectory;
import wheel.io.ui.BoundsPersistence;
import wheel.io.ui.impl.DirectoryBoundsPersistence;
import junit.framework.TestCase;

public class DirectoryBoundsPersistenceTest extends TestCase {

	
	public void testPersistBounds(){
		TransientDirectory directory = new TransientDirectory();
		
		BoundsPersistence persistence = 
			new DirectoryBoundsPersistence(directory);
		
		
		String id1 = "test";
		String id2 = "test2";
		assertEquals(null, persistence.getStoredBounds(id1));
		assertEquals(null, persistence.getStoredBounds(id2));
		
		
		Rectangle boundsId1 = new Rectangle(42,42,42,42);
		Rectangle boundsId2 = new Rectangle(255,255,255,255);
		persistence.setBounds(id1, boundsId1);
		persistence.setBounds(id2, boundsId2);
		persistence.store();
		assertEquals(boundsId1, persistence.getStoredBounds(id1));
		assertEquals(boundsId2, persistence.getStoredBounds(id2));
		
		persistence = new DirectoryBoundsPersistence(directory);		
		assertEquals(boundsId1, persistence.getStoredBounds(id1));
		assertEquals(boundsId2, persistence.getStoredBounds(id2));
		
	}
}

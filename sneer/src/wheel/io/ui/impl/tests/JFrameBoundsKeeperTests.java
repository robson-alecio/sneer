package wheel.io.ui.impl.tests;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.impl.JFrameBoundsKeeperImpl;

import junit.framework.TestCase;

public class JFrameBoundsKeeperTests extends TestCase {
	
	
	private JFrameBoundsKeeper _frameBoundsKeeper;

	@Override
	protected void setUp() throws Exception {
		_frameBoundsKeeper = new JFrameBoundsKeeperImpl(new TransientBoundsPersistence());
	}
	
	public void testKeepBounds() throws InterruptedException, InvocationTargetException{
		JFrame testFrame = null;
		JFrame testFrame2 = null;
		JFrame testFrame3 = null;
		
		try {
			final Rectangle bounds = new Rectangle(0,0,100,100);
			testFrame = openTestFrame();
			this.setBounds(testFrame, bounds);
				
			testFrame2 = openTestFrame();
			assertEquals(bounds, testFrame2.getBounds());
			final Rectangle bounds2 = new Rectangle(0,0,200,100);
			this.setBounds(testFrame2, bounds2);
			
			testFrame3 = openTestFrame();
			assertEquals(bounds2, testFrame3.getBounds());

		} finally {
			for (JFrame frame : new JFrame[]{testFrame, testFrame2, testFrame3}){
				if (frame != null)
					frame.setVisible(false);
			}
		}
	}

	private void setBounds(final JFrame testFrame, final Rectangle bounds) throws InterruptedException, InvocationTargetException {
			EventQueue.invokeAndWait(new Runnable(){
				@Override
				public void run() {
					testFrame.setBounds(bounds);				
				}
			});
	}

	private JFrame openTestFrame() {
		
		final JFrame testFrame = new JFrame();
		
		String testFrameId = "testFrame";
		_frameBoundsKeeper.keepBoundsFor(testFrame, testFrameId);
		testFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		return testFrame;
	}

	
}

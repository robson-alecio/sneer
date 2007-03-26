package wheel.io.ui.impl.tests;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import junit.framework.TestCase;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.TrayIconImpl;

public class TrayIconImplTests extends TestCase {

	public void test() throws Exception {
		URL userIcon = getClass().getResource("testIcon.png");
		TrayIconImpl subject = new TrayIconImpl(userIcon);

		SayHelloAction sayHelloAction = new SayHelloAction();
		subject.addAction(sayHelloAction);
		
		final MenuItem sayHelloActionMenuItem = getSayHelloMenuItem();
		assertEquals(SayHelloAction.CAPTION, sayHelloActionMenuItem.getLabel());
		
		clickMenuItem(sayHelloActionMenuItem);
		assertEquals(SayHelloAction.HELLO_TEXT, sayHelloAction.getOutput());
		
	}

	private MenuItem getSayHelloMenuItem() {
		PopupMenu menu = SystemTray.getSystemTray().getTrayIcons()[0].getPopupMenu();
		return menu.getItem(0);
	}

	private void clickMenuItem(MenuItem menuItem) {
		ActionListener[] actionListeners = menuItem.getListeners(ActionListener.class);
		int foo = 42;
		for (ActionListener listener : actionListeners)
			listener.actionPerformed(new ActionEvent(this, foo , "bar"));
	}
	
	private static class SayHelloAction implements Action{

		public static final String HELLO_TEXT = "hello";
		public static final String CAPTION = "Say hello";
		
		private final StringBuilder out;

		public SayHelloAction(){
			out = new StringBuilder();
		}
		
		public String caption() {
			return SayHelloAction.CAPTION;
		}

		public synchronized void run() {
			out.append(HELLO_TEXT);			
		}
		
		public synchronized String getOutput(){
			return out.toString();
		}
		
	}
}

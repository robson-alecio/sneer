package sneer.hardware.gui.trayicon.tests;

import static sneer.commons.environments.Environments.my;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.hardware.gui.trayicon.SystemTrayNotSupported;
import sneer.hardware.gui.trayicon.TrayIcon;
import sneer.hardware.gui.trayicon.TrayIcons;
import wheel.io.ui.action.Action;

public class TrayIconTest extends BrickTest {

	private final TrayIcon _subject = newSubject();

	private TrayIcon newSubject() {
		if (!SystemTray.isSupported()) return null;
		
		try {
			return my(TrayIcons.class).newTrayIcon(getClass().getResource("testIcon.png"));
		} catch (SystemTrayNotSupported e) {
			throw new IllegalStateException(e);
		}
	}

	@After
	public void disposeSubject() throws Exception {
		if(SystemTray.isSupported())
			_subject.dispose();
	}
	
	@Test
	public void menuItems() throws Exception {
	
		if(!SystemTray.isSupported())
			return;
		
		SayHelloAction sayHelloAction = new SayHelloAction();
		_subject.addAction(sayHelloAction);
		
		final MenuItem sayHelloActionMenuItem = getSayHelloMenuItem();
		assertEquals(SayHelloAction.CAPTION, sayHelloActionMenuItem.getLabel());
		
		clickMenuItem(sayHelloActionMenuItem);
		assertEquals(SayHelloAction.HELLO_TEXT, sayHelloAction.getOutput());
		
	}
	
	@Test
	public void defaultAction() throws Exception {
		if(!SystemTray.isSupported())
			return;

		SayHelloAction sayHelloAction = new SayHelloAction();
		_subject.setDefaultAction(sayHelloAction);
		
		clickTrayIcon();
		assertEquals(SayHelloAction.HELLO_TEXT, sayHelloAction.getOutput());
	}

	private void clickTrayIcon() {
		for (MouseListener listener : getTrayIcon().getMouseListeners()){
			JButton aComponent = new JButton();
			listener.mouseClicked(new MouseEvent(aComponent,0,0,0,0,0,0,0,1,false,MouseEvent.BUTTON1));
		}
	}

	private MenuItem getSayHelloMenuItem() {
		PopupMenu menu = getTrayIcon().getPopupMenu();
		return menu.getItem(0);
	}

	private java.awt.TrayIcon getTrayIcon() {
		return SystemTray.getSystemTray().getTrayIcons()[0];
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

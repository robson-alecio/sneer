package sneer.bricks.skin.widgets.autoscroll.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.contract.Contract;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.autoscroll.AutoScroll;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

@Ignore
public class AutoScrollTest extends BrickTest {	

	protected JFrame _window;
	
	protected JScrollPane _subject1;
	protected JScrollPane _subject2;
	
	protected final JTextPane _field1 = new JTextPane();
	protected final JTextPane _field2 = new JTextPane();

	protected final Register<String> _register = my(Signals.class).newRegister("");
	
	@Test
	public void reactiveAutoScroll() throws Exception {
		autoScrollWithEventInsideGuiThread() ;
		autoScrollWithEventOutsideGuiThread();
		doAutoScrollTest();
	}
	
	protected void autoScrollWithEventInsideGuiThread() throws Exception {
		_subject1 = new JScrollPane();
		@SuppressWarnings("unused")
		Contract reception = my(Signals.class).receive(_register.output(), new Consumer<String>() { @Override public void consume(final String change) {
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				my(AutoScroll.class).runWithAutoscroll(_subject1, new Runnable() { @Override public void run() {
					append(_field1, change);
				}});
			}});
		}});
	}

	protected void autoScrollWithEventOutsideGuiThread() throws Exception {
		_subject2 = new JScrollPane();
		@SuppressWarnings("unused")
		Contract reception = my(Signals.class).receive(_register.output(), new Consumer<String>() { @Override public void consume(final String change) {
			my(AutoScroll.class).runWithAutoscroll(_subject2, new Runnable() { @Override public void run() {
				append(_field2, change);
			}});
		}});
	}
	
	protected void append(JTextPane field, final String change) {
		Document document = field.getDocument();
		try {
			document.insertString(document.getLength(), change, null);
		} catch (BadLocationException e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	protected void doAutoScrollTest() throws InterruptedException {
		newFrame(_subject1, _field1, 10);
		newFrame(_subject2, _field2, 150);
		
		int i = 0;
		while(i<200){
			_register.setter().consume("\n" + i++);
			Thread.sleep(100);
		}
		
		_window.setVisible(false);
		_window.dispose();
	}
	
	protected void newFrame(JScrollPane scroll, JTextPane field, int x) {
		scroll.getViewport().add(field);
		_window = new JFrame();
		_window.setLayout(new BorderLayout());
		
		_window.getContentPane().add(scroll, BorderLayout.CENTER);
		_window.setBounds(x, 10, 100, 200);
		_window.setVisible(true);
	}
}
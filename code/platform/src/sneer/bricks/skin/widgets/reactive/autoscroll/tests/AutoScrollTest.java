package sneer.bricks.skin.widgets.reactive.autoscroll.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.junit.Test;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScroll;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class AutoScrollTest extends BrickTest {	
	private final Register<String> _register = my(Signals.class).newRegister("");
	private final JTextPane _field = new JTextPane();
	
	private JFrame _window;
	private JScrollPane _subject;
	
	@Test
	public void autoScrollWithEventInsideGuiThread() throws Exception {
		
		_subject = new JScrollPane();
		@SuppressWarnings("unused")
		Reception reception = my(Signals.class).receive(_register.output(), new Consumer<String>() { @Override public void consume(final String change) {
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				appendChange(change);
			}});
		}});

		doAutoScrollTest();
		
		final ByRef<Thread> guiThread = ByRef.newInstance();
		my(GuiThread.class).invokeAndWait(new Runnable() { @Override public void run() {
			guiThread.value = Thread.currentThread();
		}});
		//guiThread.value.stop();
	}

	@Test
	public void autoScrollWithEventOutsideGuiThread() throws Exception {
		
		_subject = new JScrollPane();
		@SuppressWarnings("unused")
		Reception reception = my(Signals.class).receive(_register.output(), new Consumer<String>() { @Override public void consume(final String change) {
			appendChange(change);
		}});

		doAutoScrollTest();
	}

	private void doAutoScrollTest() throws InterruptedException {
		_subject.getViewport().add(_field);
		_window = new JFrame();
		_window.setLayout(new BorderLayout());
		
		_window.getContentPane().add(_subject, BorderLayout.CENTER);
		_window.setBounds(10, 10, 100, 200);
		_window.setVisible(true);
		
		int i = 0;
		
		while(i<20){
			_register.setter().consume("\n" + i++);
			Thread.sleep(100);
		}
		
		_window.setVisible(false);
		_window.dispose();
	}

	private void appendChange(final String change) {
		my(AutoScroll.class).runWithAutoscroll(_subject, new Runnable() { @Override public void run() {
				Document document = _field.getDocument();
				try {
					document.insertString(document.getLength(), change, null);
				} catch (BadLocationException e) {
					throw new NotImplementedYet(e); // Fix Handle this exception.
				}
		}});
	}
}
package sneer.bricks.skin.widgets.reactive.autoscroll.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScroll;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class AutoScrollTest extends BrickTest {
	
	private final Register<String> _register = my(Signals.class).newRegister("");
	private final JTextPane _field = new JTextPane();
	
	@Ignore
	@Test
	public void testScroll() throws Exception {
		
		final JScrollPane subject = new JScrollPane();
		
		@SuppressWarnings("unused")
		Reception reception = my(Signals.class).receive(_register.output(), new Consumer<String>() { @Override public void consume(final String change) {
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				my(AutoScroll.class).runWithAutoscroll(subject, new Runnable(){
					@Override public void run() {
						Document document = _field.getDocument();
						try {
							document.insertString(document.getLength(), change, null);
						} catch (BadLocationException e) {
							throw new NotImplementedYet(e); // Fix Handle this exception.
						}
					}});
			}});
		}});
		
		subject.getViewport().add(_field);
		
		JFrame frm = new JFrame();
		frm.setLayout(new BorderLayout());
		
		frm.getContentPane().add(subject, BorderLayout.CENTER);
		frm.setBounds(10, 10, 100, 200);
		frm.setVisible(true);
		
		int i = 0;
		while(true){
			_register.setter().consume("\n" + i++);
			Thread.sleep(100);
		}
	}
}
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

import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScrolls;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class AutoScrollTest extends BrickTest {
	
	private final ListRegister<String> _register = my(CollectionSignals.class).newListRegister();
	private final JTextPane _field = new JTextPane();
	
	@Test
	@Ignore
	public void testScroll() throws Exception {
		
		JScrollPane subject = my(AutoScrolls.class).create( _field, _register.output(),
				new Consumer<CollectionChange<String>>() { @Override public void consume(CollectionChange<String> change) {
						Document document = _field.getDocument();
						try {
							for (String element : change.elementsAdded())
								document.insertString(document.getLength(), element, null);
						} catch (BadLocationException e) {
							throw new NotImplementedYet(e); // Fix Handle this exception.
						}

						if (!change.elementsRemoved().isEmpty()){
							StringBuilder builder = new StringBuilder();
							for (String element : _register.output().currentElements()) 
								builder.append(element);
							_field.setText(builder.toString());
						}
					}
				});
		
		subject.getViewport().add(_field);
		
		JFrame frm = new JFrame();
		frm.setLayout(new BorderLayout());
		
		frm.getContentPane().add(subject, BorderLayout.CENTER);
		frm.setBounds(10, 10, 100, 200);
		frm.setVisible(true);
		
		int i = 0;
		while(true){
			_register.adder().consume("\n" + i++);
			Thread.sleep(100);
		}
	}
}
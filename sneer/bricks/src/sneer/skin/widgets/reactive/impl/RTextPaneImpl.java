package sneer.skin.widgets.reactive.impl;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Keymap;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import sneer.skin.widgets.reactive.NotificationPolicy;

import wheel.lang.PickyConsumer;
import wheel.reactive.Signal;

class RTextPaneImpl extends RAbstractField<JTextPane> {
	
	private static final String LINE_BREAK_STRING = "\n\r";
	private static final long serialVersionUID = 1L;

	RTextPaneImpl(Signal<String> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		super(new JTextPane(), source, setter, notificationPolicy);
		LineBorder border = new LineBorder(Color.LIGHT_GRAY);
		_textComponent.setBorder(border);
		_decorator = new ChangeInfoDecorator(border, _textComponent){ @Override void decorate(boolean notified) {
			//ignore, do nothing.
		}};
	}

	@Override
	protected void addDoneListenerCommiter() {
        Keymap kMap=_textComponent.getKeymap();
        kMap.addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) {
        	commitTextChanges();
 		}});
        insertLineBreakerListenerFor(kMap, "control ENTER");
        insertLineBreakerListenerFor(kMap, "shift ENTER");
        insertLineBreakerListenerFor(kMap, "alt ENTER");
	}

	private void insertLineBreakerListenerFor(Keymap kMap, String key) {
		kMap.addActionForKeyStroke(KeyStroke.getKeyStroke(key), new AbstractAction(){ @Override public void actionPerformed(ActionEvent e) {
        	insertLineBreak();
		}});
	}

    protected void insertLineBreak() {
         try {
			int carretPosition = _textComponent.getCaretPosition();
			StyledDocument document = (StyledDocument) _textComponent.getDocument();
			SimpleAttributeSet attributes = new SimpleAttributeSet( document.getCharacterElement(carretPosition).getAttributes());
			attributes.addAttribute(LINE_BREAK_STRING, Boolean.TRUE);
			document.insertString(carretPosition, LINE_BREAK_STRING, attributes);
			_textComponent.setCaretPosition(carretPosition+1);
		} catch (BadLocationException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
    }
    
	@Override
	public String getText() {
		return super.getText().trim();
	}
}
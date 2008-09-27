package sneer.skin.menu.impl;

import java.awt.HeadlessException;

import javax.swing.JMenuItem;

import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class ReactiveMenuItem extends JMenuItem {

	private static final long serialVersionUID = 1L;

	Signal<String> _label;

	@SuppressWarnings("unused")
	private final Receiver<String> _textReceiverCoisatoAvoidGc;
	
	public ReactiveMenuItem(Signal<String> label) throws HeadlessException {
		_label = label;
		setText(_label.currentValue());
		
		_textReceiverCoisatoAvoidGc = new Receiver<String>(_label){ @Override public void consume(String valueObject) {
			setText(valueObject);
		}};
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public String getLabel() {
		return getText();
	}

	@Override
	public String getText() {
		return _label.currentValue();
	}
}
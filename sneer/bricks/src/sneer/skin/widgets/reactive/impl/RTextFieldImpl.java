package sneer.skin.widgets.reactive.impl;

import javax.swing.JTextField;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

class RTextFieldImpl extends RAbstractField<JTextField> {
	
	private static final long serialVersionUID = 1L;

	RTextFieldImpl(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		super(new JTextField(), source, setter, notifyOnlyWhenDoneEditing);
	}
}
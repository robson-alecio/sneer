package sneer.skin.widgets.reactive.impl;

import javax.swing.JTextField;

import sneer.skin.widgets.reactive.NotificationPolicy;

import wheel.lang.PickyConsumer;
import wheel.reactive.Signal;

class RTextFieldImpl extends RAbstractField<JTextField> {
	
	private static final long serialVersionUID = 1L;

	RTextFieldImpl(Signal<String> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		super(new JTextField(), source, setter, notificationPolicy);
	}
}
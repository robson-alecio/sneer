package sneer.skin.widgets.reactive.impl;

import javax.swing.JTextField;

import sneer.pulp.reactive.Signal;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.software.lang.PickyConsumer;

class RTextFieldImpl extends RAbstractField<JTextField> {
	
	private static final long serialVersionUID = 1L;

	RTextFieldImpl(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy) {
		super(new JTextField(), source, setter, notificationPolicy);
	}
}
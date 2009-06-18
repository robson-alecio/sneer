package sneer.bricks.skin.widgets.reactive.impl;

import javax.swing.JTextField;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.skin.widgets.reactive.NotificationPolicy;
import sneer.foundation.lang.PickyConsumer;

class RTextFieldImpl extends RAbstractField<JTextField> {
	
	private static final long serialVersionUID = 1L;

	RTextFieldImpl(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy) {
		super(new JTextField(), source, setter, notificationPolicy);
	}
}
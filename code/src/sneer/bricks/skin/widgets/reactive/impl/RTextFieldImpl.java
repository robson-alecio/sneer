package sneer.bricks.skin.widgets.reactive.impl;

import javax.swing.JTextField;

import sneer.bricks.hardware.cpu.lang.PickyConsumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.skin.widgets.reactive.NotificationPolicy;

class RTextFieldImpl extends RAbstractField<JTextField> {
	
	private static final long serialVersionUID = 1L;

	RTextFieldImpl(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy) {
		super(new JTextField(), source, setter, notificationPolicy);
	}
}
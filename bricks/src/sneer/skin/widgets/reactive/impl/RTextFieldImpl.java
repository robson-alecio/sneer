package sneer.skin.widgets.reactive.impl;

import javax.swing.JComponent;
import javax.swing.JTextField;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;

public class RTextFieldImpl extends RAbstractField<JTextField> {

	RTextFieldImpl(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		super(new JTextField(), source, setter, notifyEveryChange);
	}

	@Override
	public Omnivore<String> fieldReceiver() {
		return new Omnivore<String>() {
			public void consume(final String text) {
				setText(text);
			}
		};
	}

	@Override
	public Omnivore<Pair<String, String>> textChangedReceiver() {
		return new Omnivore<Pair<String, String>>() {
			public void consume(Pair<String, String> value) {
				if (!value._a.equals(value._b))
					try {
						_setter.consume(value._b);
					} catch (Throwable ignored) {
						ignored.printStackTrace();
					}
			}
		};
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_textComponent};
	}

	private static final long serialVersionUID = 1L;
}

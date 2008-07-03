package sneer.widgets.reactive.impl;

import javax.swing.JTextField;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class RTextFieldImpl extends RAbstractField<String, JTextField> {

	RTextFieldImpl(Signal<String> source, Omnivore<String> setter) {
		super(source, setter);
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
					} catch (IllegalParameter ignored) {
					}
			}
		};
	}

	private static final long serialVersionUID = 1L;
}

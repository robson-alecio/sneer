package sneer.skin.widgets.reactive.impl;

import javax.swing.JComponent;
import javax.swing.JLabel;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;

public class RLabelImpl extends RAbstractField<JLabel> {

	RLabelImpl(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		super(new JLabel(), source, setter, notifyEveryChange);
	}

	RLabelImpl(Signal<String> source) {
		super(new JLabel(), source);
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

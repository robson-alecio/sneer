package sneer.skin.widgets.reactive.impl;

import javax.swing.JComponent;
import javax.swing.JTextField;

import wheel.io.ui.impl.UserImpl;
import wheel.lang.Consumer;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

public class RTextFieldImpl extends RAbstractField<JTextField> {
	
	private static final long serialVersionUID = 1L;

	RTextFieldImpl(Signal<String> source) {
		this(source, null, true);
	}

	RTextFieldImpl(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		super(new JTextField(), source, setter, notifyOnlyWhenDoneEditing);
	}

	@Override
	public Receiver<String> fieldReceiver() {
		return new Receiver<String>(_source) {@Override public void consume(final String text) {
			setText(text);
		}};
	}

	@Override
	public Receiver<Pair<String, String>> textChangedReceiver() {
		return new Receiver<Pair<String, String>>() {@Override public void consume(Pair<String, String> value) {
			if (!value._a.equals(value._b))
				try {
					_setter.consume(value._b);
				} catch (IllegalParameter ip) {
					new UserImpl().acknowledge(ip);
				}
		}};
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_textComponent};
	}
}
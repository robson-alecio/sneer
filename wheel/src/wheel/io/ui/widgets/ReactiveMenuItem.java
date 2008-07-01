package wheel.io.ui.widgets;

import java.awt.HeadlessException;

import javax.swing.JMenuItem;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ReactiveMenuItem extends JMenuItem implements Omnivore<String> {

	private static final long serialVersionUID = 1L;

	Signal<String> _label;
	
	public ReactiveMenuItem(Signal<String> label) throws HeadlessException {
		_label = label;
		setText(_label.currentValue());
		_label.addReceiver(this);
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

	@Override
	public void consume(String valueObject) {
		setText(valueObject);
	}
}
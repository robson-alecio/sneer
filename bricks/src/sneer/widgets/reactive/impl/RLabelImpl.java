package sneer.widgets.reactive.impl;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sneer.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RLabelImpl extends JPanel implements TextWidget{

	private JLabel _label = new JLabel();
	private final Signal<String> _text;
	private Omnivore<String> listener;
	private static final long serialVersionUID = 1L;
	
	RLabelImpl(Signal<String> text){
		_text = text;
		initComponents();
		addReceivers();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setOpaque(false);
		_label.setText(_text.currentValue());
		add(_label);
	}

	private void addReceivers() {
		_text.addReceiver(textReceiver());
	}

	private Omnivore<String> textReceiver() {
		if(listener==null)
			listener = new Omnivore<String>() {
				public void consume(final String text) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							_label.setText(text);
						}
					});
				}
			};
		return listener;
	}

	@Override
	public String getText() {
		return _label.getText();
	}

	@Override
	public JLabel getMainWidget() {
		return _label;
	}

	@Override
	public void setText(String text) {
		_label.setText(text);
	}

	@Override
	public JPanel getContainer() {
		return this;
	}
}

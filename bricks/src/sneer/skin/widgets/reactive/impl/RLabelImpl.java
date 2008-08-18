package sneer.skin.widgets.reactive.impl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RLabelImpl extends JPanel implements TextWidget{

	protected JLabel _label = new JLabel();
	private final Signal<String> _text;
	private Omnivore<String> listener;
	private static final long serialVersionUID = 1L;
	
	RLabelImpl(Signal<String> text){
		_text = text;
		initComponents();
		addReceivers();
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
					GridBagConstraints.EAST, 
					GridBagConstraints.BOTH,
					new Insets(0,0,0,0),0,0);
		setOpaque(false);
		_label.setText(_text.currentValue());
		add(_label, c);
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
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_label};
	}

}

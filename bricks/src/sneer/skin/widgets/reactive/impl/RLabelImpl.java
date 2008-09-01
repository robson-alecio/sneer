package sneer.skin.widgets.reactive.impl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RLabelImpl extends JPanel implements TextWidget<JLabel>{

	protected JLabel _textComponent = new JLabel();
	private Signal<String> _source;
	private Consumer<String> _setter = null;
	private Omnivore<String> listener;
	private static final long serialVersionUID = 1L;
	
	RLabelImpl(Signal<String> text){
		_source = text;
		initComponents();
		addReceivers();
	}

	RLabelImpl(Signal<String> source, Consumer<String> setter) {
		this(source);
		_setter = setter;
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
					GridBagConstraints.EAST, 
					GridBagConstraints.BOTH,
					new Insets(0,0,0,0),0,0);
		setOpaque(false);
		_textComponent.setText(_source.currentValue());
		add(_textComponent, c);
	}

	private void addReceivers() {
		_source.addReceiver(textReceiver());
	}

	private Omnivore<String> textReceiver() {
		if(listener==null)
			listener = new Omnivore<String>() {
				public void consume(final String text) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							_textComponent.setText(text);
						}
					});
				}
			};
		return listener;
	}

	@Override
	public JLabel getMainWidget() {
		return _textComponent;
	}

	@Override
	public JPanel getComponent() {
		return this;
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_textComponent};
	}

	@Override
	public Signal<String> output() {
		return _source;
	}

	@Override
	public Consumer<String> setter() {
		if(_setter==null)
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		
		return _setter;
	}

}

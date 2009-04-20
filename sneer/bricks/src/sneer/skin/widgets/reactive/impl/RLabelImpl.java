package sneer.skin.widgets.reactive.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.widgets.reactive.TextWidget;

class RLabelImpl extends JPanel implements TextWidget<JLabel>{

	private final JLabel _textComponent;
	private final Signal<?> _source;
	private final PickyConsumer<? super String> _setter;

	RLabelImpl(Signal<?> text){
		this(text, null);
	}

	RLabelImpl(Signal<?> source, PickyConsumer<? super String> setter) {
		_textComponent = new JLabel();
		_setter = setter;
		_source = source;

		my(Signals.class).receive(this, new Consumer<Object>() {@Override public void consume(final Object value) {
			my(GuiThread.class).invokeAndWait(new Runnable() {@Override public void run() {
				textComponent().setText(valueToString(value));
			}});
		}}, source);

		initComponents();
	}
	
	private String valueToString(final Object value) {
		return (value==null)?"":value.toString();
	}
	
	private void initComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.BOTH,
				new Insets(0,0,0,0),0,0);
		setOpaque(false);
		textComponent().setText(valueToString(_source.currentValue()));
		add(textComponent(), c);
	}

	@Override
	public JLabel getMainWidget() {
		return textComponent();
	}

	@Override
	public JPanel getComponent() {
		return this;
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{textComponent()};
	}

	@Override
	public Signal<?> output() {
		return _source;
	}

	@Override
	public PickyConsumer<? super String> setter() {
		if(_setter==null)
			throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
		
		return _setter;
	}
	
	public JLabel textComponent() {
		return _textComponent;
	}

	@Override
	public Dimension getMinimumSize() {
		return RUtil.limitSize(super.getMinimumSize());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return RUtil.limitSize(super.getPreferredSize());
	}
	
	@Override
	public Dimension getMaximumSize() {
		return RUtil.limitSize(super.getMaximumSize());
	}
}
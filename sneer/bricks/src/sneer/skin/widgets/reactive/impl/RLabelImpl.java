package sneer.skin.widgets.reactive.impl;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.reactive.Signal;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.reactive.impl.EventReceiver;
import static sneer.commons.environments.Environments.my;

class RLabelImpl extends JPanel implements TextWidget<JLabel>{

	private static final long serialVersionUID = 1L;
	
	protected final JLabel _textComponent;
	protected final Signal<?> _source;
	protected final PickyConsumer<? super String> _setter;
	
	@SuppressWarnings("unused")
	private final EventReceiver<?> _textReceiverAvoidGc;
	
	RLabelImpl(Signal<?> text){
		this(text, null);
	}

	RLabelImpl(Signal<?> source, PickyConsumer<? super String> setter) {
		_textComponent = new JLabel();
		_setter = setter;
		_source = source;
		_textReceiverAvoidGc = new EventReceiver<Object>(source) {@Override public void consume(final Object value) {
			my(GuiThread.class).invokeAndWait(new Runnable() {@Override public void run() {
				_textComponent.setText(valueToString(value));
		}});}};
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
		_textComponent.setText(valueToString(_source.currentValue()));
		add(_textComponent, c);
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
	public Signal<?> output() {
		return _source;
	}

	@Override
	public PickyConsumer<? super String> setter() {
		if(_setter==null)
			throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
		
		return _setter;
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
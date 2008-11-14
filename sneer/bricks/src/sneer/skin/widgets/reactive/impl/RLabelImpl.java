package sneer.skin.widgets.reactive.impl;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.PickyConsumer;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class RLabelImpl extends JPanel implements TextWidget<JLabel>{

	private static final long serialVersionUID = 1L;
	
	protected final JLabel _textComponent;
	protected final Signal<String> _source;
	protected final PickyConsumer<String> _setter;
	
	@SuppressWarnings("unused")
	private final Receiver<String> _textReceiverAvoidGc;
	
	RLabelImpl(Signal<String> text){
		this(text, null);
	}

	RLabelImpl(Signal<String> source, PickyConsumer<String> setter) {
		_textComponent = new JLabel();
		_setter = setter;
		_source = source;
		_textReceiverAvoidGc = new Receiver<String>(source) {@Override public void consume(final String value) {
			GuiThread.invokeAndWait(new Runnable() {@Override public void run() {
				_textComponent.setText(value);
		}});}};
		initComponents();
	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.BOTH,
				new Insets(0,0,0,0),0,0);
		setOpaque(false);
		_textComponent.setText(_source.currentValue());
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
	public Signal<String> output() {
		return _source;
	}

	@Override
	public PickyConsumer<String> setter() {
		if(_setter==null)
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		
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
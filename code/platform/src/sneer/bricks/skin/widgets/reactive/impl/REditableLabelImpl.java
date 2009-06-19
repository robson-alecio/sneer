package sneer.bricks.skin.widgets.reactive.impl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.skin.widgets.reactive.NotificationPolicy;
import sneer.bricks.skin.widgets.reactive.TextWidget;
import sneer.foundation.lang.PickyConsumer;

class REditableLabelImpl extends RPanel<JTextField> implements TextWidget<JTextField>{

	private final RLabelImpl _label;
	private final RTextFieldImpl _text;
	private final Signal<?> _source;
	private final PickyConsumer<? super String> _setter;

	REditableLabelImpl(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy) {
		_source = source;
		_setter = setter;
		_text = new RTextFieldImpl(source, setter, notificationPolicy);
		_label = new RLabelImpl(source, setter);

		initWidget();
	}

	private void initWidget() {
		
		REditableLabelImpl.this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.BOTH,
				new Insets(0,0,0,0),0,0);
		
		add(_label, c);
		add(_text, c);
		
		addEditLabelListener();		
		addCommitChangesListener();
		
		_text.setVisible(false);
		_label.setVisible(true);
	}

	protected void commitChanges() {
		_text.setVisible(false);
		_label.setVisible(true);
	}

	private void addCommitChangesListener() {
		_text.getMainWidget().addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) {
			commitChanges();
		}});
		
		_text.getMainWidget().addFocusListener(new FocusAdapter(){ @Override public void focusLost(FocusEvent e) {
			commitChanges();
		}});
	}

	private void addEditLabelListener() {
		_label.addMouseListener( new MouseAdapter(){ @Override public void mouseReleased(MouseEvent event) {
			_text.getMainWidget().setText(_label.getMainWidget().getText());
			_text.setVisible(true);
			_label.setVisible(false);
			_text.getMainWidget().requestFocus();
			_text.getMainWidget().selectAll();
		}});
	}
	
	@Override
	public JTextField getMainWidget() {
		return _text.getMainWidget();
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_text._textComponent , _label.textComponent()};
	}

	@Override
	public Signal<?> output(){
		return _source;
	}
	
	@Override
	public PickyConsumer<? super String> setter(){
		return _setter;
	}
}
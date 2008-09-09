package sneer.skin.widgets.reactive.impl;

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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public class REditableLabelImpl extends JPanel implements TextWidget<JTextField>{

	private static final long serialVersionUID = 1L;
	protected RLabelImpl _label;
	protected RTextFieldImpl _text;
	
	private final Signal<String> _source;
	private final Consumer<String> _setter;

	REditableLabelImpl(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		_source = source;
		_setter = setter;
		initWidget(source, setter, notifyOnlyWhenDoneEditing);
	}

	private void initWidget(final Signal<String> source, final Consumer<String> setter,	final boolean notifyOnlyWhenDoneEditing) {
		_text = new RTextFieldImpl(source, setter, notifyOnlyWhenDoneEditing);
		_label = new RLabelImpl(source, setter);
		
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

	private void commitChanges() {
		SwingUtilities.invokeLater(new Runnable(){@Override public void run() {
			_text.setVisible(false);
			_label.setVisible(true);
		}});
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
	public JPanel getComponent() {
		return this;
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_text._textComponent , _label._textComponent};
	}

	@Override
	public Signal<String> output(){
		return _source;
	}
	
	@Override
	public Consumer<String> setter(){
		return _setter;
	}
}
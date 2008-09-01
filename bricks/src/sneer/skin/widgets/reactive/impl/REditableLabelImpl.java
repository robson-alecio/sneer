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
	protected RLabelImpl label;
	protected RTextFieldImpl text;
	
	private final Signal<String> _source;
	private final Consumer<String> _setter;
	
	REditableLabelImpl(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		_source = source;
		_setter = setter;
		text = new RTextFieldImpl(source, setter, notifyOnlyWhenDoneEditing);
		label = new RLabelImpl(source, setter);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
					GridBagConstraints.EAST, 
					GridBagConstraints.BOTH,
					new Insets(0,0,0,0),0,0);
		
		add(label, c);
		add(text, c);
		text.setVisible(false);
		
		addEditLabelListener();		
		addCommitChangesListener();
	}

	private void commitChanges() {
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					text.setVisible(false);
					label.setVisible(true);
				}
			}
		);
	}

	private void addCommitChangesListener() {
		text.getMainWidget().addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					commitChanges();
				}
			}
		);
		
		text.getMainWidget().addFocusListener(
			new FocusAdapter(){
				@Override
				public void focusLost(FocusEvent e) {
					commitChanges();
				};
			}
		);
	}

	private void addEditLabelListener() {
		label.addMouseListener(
			new MouseAdapter(){
				@Override
				public void mouseReleased(MouseEvent event) {
					text.getMainWidget().setText(label.getMainWidget().getText());
					text.setVisible(true);
					label.setVisible(false);
					text.getMainWidget().requestFocus();
					text.getMainWidget().selectAll();
				}
			}
		);
	}
	
	@Override
	public JTextField getMainWidget() {
		return text.getMainWidget();
	}

	@Override
	public JPanel getComponent() {
		return this;
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{text._textComponent , label._textComponent};
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
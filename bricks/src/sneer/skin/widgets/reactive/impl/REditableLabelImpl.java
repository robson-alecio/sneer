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
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class REditableLabelImpl extends JPanel implements TextWidget{

	private static final long serialVersionUID = 1L;
	protected RLabelImpl label;
	protected RTextFieldImpl text;
	
	REditableLabelImpl(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		text = new RTextFieldImpl(source, setter, notifyEveryChange);
		label = new RLabelImpl(source);

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
	public String getText() {
		return text.getText();
	}
	
	@Override
	public void setText(final String _text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					text.setText(_text);
					text.revalidate();
				} catch (Exception e) {
					e.printStackTrace();
					//ignore
				}
			}
		});
	}
	
	@Override
	public JTextField getMainWidget() {
		return text.getMainWidget();
	}

	@Override
	public JPanel getContainer() {
		return this;
	}

	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{text._area , label._label};
	}
}
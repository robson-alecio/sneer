package sneer.widgets.reactive.impl;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.widgets.reactive.TextWidget;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class REditableLabelImpl extends JPanel implements TextWidget{

	private static final long serialVersionUID = 1L;
	protected RLabelImpl label;
	protected RTextFieldImpl text;
	
	public REditableLabelImpl(Signal<String> source, Omnivore<String> setter) {
		text = new RTextFieldImpl(source, setter);
		label = new RLabelImpl(source);

		this.setLayout(new FlowLayout());
		add(label);
		add(text);
		text.setVisible(false);
		
		label.addMouseListener(
			new MouseAdapter(){
				@Override
				public void mouseReleased(MouseEvent event) {
					text.setVisible(true);
					text.getWidget().selectAll();
					label.setVisible(false);
					text.getWidget().requestFocus();
				}
			}
		);		
		
		text.getWidget().addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
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
	public JTextField getWidget() {
		return text.getWidget();
	}

	@Override
	public JPanel getContainer() {
		return this;
	}
}
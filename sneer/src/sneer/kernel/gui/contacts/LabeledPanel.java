package sneer.kernel.gui.contacts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class LabeledPanel extends JPanel{
	private JLabel _label;
	
	public LabeledPanel(String title, JPanel panel, Signal<Font> font){
		setBorder(new EmptyBorder(3,3,3,3));
		setLayout(new BorderLayout());
		_label = new JLabel(title);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		add(_label,BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		if (font!=null)
			font.addReceiver(fontReceiver());
		setCurrentFont(font.currentValue());
	}
	
	private Omnivore<Font> fontReceiver() {
		return new Omnivore<Font>(){ public void consume(final Font font) {
			setCurrentFont(font);
		}};
	}
		
	public void setCurrentFont(final Font font) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				_label.setFont(font);
				_label.revalidate();
			}
		});
	}
	
	private static final long serialVersionUID = 1L;
}

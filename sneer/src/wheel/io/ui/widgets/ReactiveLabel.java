package wheel.io.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ReactiveLabel extends JPanel{

	JLabel _label = new JLabel();
	private final Signal<String> _text;
	private final Signal<Font> _font;
	
	public ReactiveLabel(Signal<String> text, final Signal<Font> font){
		_text = text;
		_font = font;
		initComponents();
		addReceivers();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setOpaque(false);
		_label.setText(_text.currentValue());
		if (_font!=null)
			_label.setFont(_font.currentValue());
		add(_label);
	}

	private void addReceivers() {
		_text.addReceiver(textReceiver());
		if (_font!=null)
			_font.addReceiver(fontReceiver());
	}

	private Omnivore<Font> fontReceiver() {
		return new Omnivore<Font>(){ public void consume(final Font font) {
			SwingUtilities.invokeLater(new Runnable(){ public void run() {
				_label.setFont(font);
			}});
		}};
	}

	private Omnivore<String> textReceiver() {
		return new Omnivore<String>(){ public void consume(final String text) {
			SwingUtilities.invokeLater(new Runnable(){ public void run() {
				_label.setText(text);
			}});
		}};
	}

	private static final long serialVersionUID = 1L;
}

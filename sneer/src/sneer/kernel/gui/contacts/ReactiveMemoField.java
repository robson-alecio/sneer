package sneer.kernel.gui.contacts;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ReactiveMemoField extends JPanel implements Omnivore<String>{
	
	private JScrollPane _scroll =  new JScrollPane();
	private JTextArea _area = new JTextArea();
	private final Signal<String> _source;
	private final boolean _editable;
	private final Omnivore<String> _setter;

	public ReactiveMemoField(Signal<String> source, Omnivore<String> setter) {
		_source = source;
		_setter = setter;
		_editable = (setter != null); //if setter == null, different textfield behaviour
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		_area.setText(_source.currentValue());
		_area.setLineWrap(true);
		setAreaBorderColor(Color.black);
		_area.selectAll();
		_area.setEditable(_editable);
		_area.setFont(FontUtil.getFont(12));
		if (_editable) 
			addChangeListeners();
		_scroll.setViewportView(_area);
		_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(_scroll);
		_source.addReceiver(this);
		setBackgroundColor();
	}

	private void setBackgroundColor() {
		if (_editable)
			_area.setBackground(Color.WHITE);
		else
			_area.setBackground(new Color(240,240,240));
	}

	private void addChangeListeners() {
		_area.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
			}
			public void focusLost(FocusEvent e) {
				commitTextChange();
			}
		});
	}
	
	private void commitTextChange() {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				if (!_source.currentValue().equals(_area.getText()))
					_setter.consume(_area.getText());
				setAreaBorderColor(Color.black);
				_area.revalidate();
			}
		});
	}
	
	private void setAreaBorderColor(Color color){
		_scroll.setBorder(new CompoundBorder(new LineBorder(color), new EmptyBorder(2,2,2,2)));
	}
	
	public void consume(final String text) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				_area.setText(text);
				_area.revalidate();
			}
		});
	}

	public String getText(){
		return _area.getText();
	}
	
	private static final long serialVersionUID = 1L;



	
}

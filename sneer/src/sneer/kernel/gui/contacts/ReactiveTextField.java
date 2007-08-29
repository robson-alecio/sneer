package sneer.kernel.gui.contacts;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ReactiveTextField extends JPanel implements Omnivore<String>{
	
	private JTextField _area = new JTextField();
	private final Signal<String> _source;
	private final boolean _editable;
	private final Omnivore<String> _setter;

	public ReactiveTextField(Signal<String> source, Omnivore<String> setter) {
		_source = source;
		_setter = setter;
		_editable = (setter != null); //if setter == null, different textfield behaviour
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		_area.setText(_source.currentValue());
		setAreaBorderColor(Color.black);
		_area.selectAll();
		_area.setEditable(_editable);
		_area.setFont(FontUtil.getFont(12));
		if (_editable) 
			addChangeListeners();
		add(_area);
		_source.addReceiver(this);
	}

	private void addChangeListeners() {
		_area.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
			}
			public void focusLost(FocusEvent e) {
				commitTextChange();
			}
		});
		_area.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
			     if (e.getKeyCode() == KeyEvent.VK_ENTER) 
			    	 commitTextChange();
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
				setAreaBorderColor(Color.red);
			}
		});
			
		_area.addActionListener(new ActionListener(){ //ENTER KEY
			public void actionPerformed(ActionEvent e) {
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
		_area.setBorder(new CompoundBorder(new LineBorder(color), new EmptyBorder(2,2,2,2)));
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

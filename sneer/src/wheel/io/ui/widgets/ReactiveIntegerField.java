package wheel.io.ui.widgets;

import java.awt.Color;
import java.awt.Font;
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

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ReactiveIntegerField extends JPanel{
	
	private JTextField _area = new JTextField();
	private final Signal<Integer> _source;
	private final boolean _editable;
	private final Consumer<Integer> _setter;

	//Implement: unify ReactiveIntegerField and ReactiveTextField with a base abstract class
	public ReactiveIntegerField(Signal<Integer> source, Consumer<Integer> setter, Font font) {
		_source = source;
		_setter = setter;
		_editable = (setter != null); //if setter == null, different textfield behaviour
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		_area.setText(Integer.toString(_source.currentValue()));
		setAreaBorderColor(Color.black);
		_area.selectAll();
		_area.setEditable(_editable);
		_area.setFont(font);
		if (_editable) 
			addChangeListeners();
		add(_area);
		_source.addReceiver(fieldReceiver());
		setBackgroundColor();
	}
	
	private Omnivore<Integer> fieldReceiver() { return new Omnivore<Integer>(){
		public void consume(final Integer text) {
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					_area.setText(Integer.toString(text));
					_area.revalidate();
				}
			});
		}};
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
				Integer value = new Integer(_area.getText());
				if (!_source.currentValue().equals(value)){
					try {
						_setter.consume(value);
					} catch (IllegalParameter e) {
					}
				}
				setAreaBorderColor(Color.black);
				_area.revalidate();
			}
		});
	}
	
	private void setAreaBorderColor(Color color){
		_area.setBorder(new CompoundBorder(new LineBorder(color), new EmptyBorder(2,2,2,2)));
	}

	public String getText(){
		return _area.getText();
	}
	
	private static final long serialVersionUID = 1L;



	
}

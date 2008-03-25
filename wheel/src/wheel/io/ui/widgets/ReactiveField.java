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
import wheel.lang.Pair;
import wheel.reactive.Signal;

public abstract class ReactiveField<U> extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public static final int ENABLED_SAVED_STATE = 0;
	public static final int ENABLED_UNSAVED_STATE = 1;
	public static final int DISABLED_STATE = 2;
	
	public int _state = ENABLED_SAVED_STATE;
	
	protected Signal<U> _source;
	protected Consumer<U> _setter;
	private final Signal<Font> _font;
	
	public ReactiveField(Signal<U> source, Consumer<U> setter, Signal<Font> font){
		_source = source;
		_setter = setter;
		_font = font;
		_state = (setter == null)?DISABLED_STATE:ENABLED_SAVED_STATE;
		initComponents();
		addReceivers();
		if (_state==ENABLED_SAVED_STATE)
			addChangeListeners();
	}

	private void addReceivers() {
		_source.addReceiver(fieldReceiver());
		if (_font!=null)
			_font.addReceiver(fontReceiver());
	}
	
	private Omnivore<Font> fontReceiver() {
		return new Omnivore<Font>(){ public void consume(final Font font) {
			setCurrentFont(font);
		}};
	}
	
	public void setCurrentFont(final Font font) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				_area.setFont(font);
				_area.revalidate();
			}
		});
	}

	public JTextField _area = new JTextField();
	
	public void initComponents(){
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		_area.selectAll();
		add(_area);
		updateView();
		firstUpdate();
	}

	private void firstUpdate() {
		fieldReceiver().consume(_source.currentValue());
		setCurrentFont(_font.currentValue());
	}
	
	public void updateView(){
		switch(_state){
			case ENABLED_SAVED_STATE:
				_area.setEditable(true);
				_area.setBackground(Color.WHITE);
				_area.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(2,2,2,2)));
				break;
			case DISABLED_STATE:
				_area.setEditable(false);
				_area.setBackground(new Color(240,240,240));
				break;
			case ENABLED_UNSAVED_STATE:
				_area.setBorder(new CompoundBorder(new LineBorder(Color.red), new EmptyBorder(2,2,2,2)));
				break;
		}
	}
	
	public void addChangeListeners() {
		_area.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
			}
			public void focusLost(FocusEvent e) {
				commitTextChanges();
			}
		});
		_area.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
			     if (e.getKeyCode() == KeyEvent.VK_ENTER) 
			    	 commitTextChanges();
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
				_state = ENABLED_UNSAVED_STATE;
				updateView();
			}
		});
			
		_area.addActionListener(new ActionListener(){ //ENTER KEY
			public void actionPerformed(ActionEvent e) {
				commitTextChanges();
			}
		});
	}
	
	public void commitTextChanges(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				textChangedReceiver().consume(new Pair<U,String>(_source.currentValue(),_area.getText()));
				_area.revalidate();
				_state = ENABLED_SAVED_STATE;
				updateView();
			}
		});
	}
	
	public String getText(){
		return _area.getText();
	}
	
	protected void setText(final String text){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				_area.setText(text);
				_area.revalidate();
			}
		});
	}

	public abstract Omnivore<Pair<U,String>> textChangedReceiver();
	
	public abstract Omnivore<U> fieldReceiver();

}

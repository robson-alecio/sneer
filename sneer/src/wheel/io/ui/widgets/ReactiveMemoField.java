package wheel.io.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

public class ReactiveMemoField extends JPanel {
	
	public static final int ENABLED_SAVED_STATE = 0;
	public static final int ENABLED_UNSAVED_STATE = 1;
	public static final int DISABLED_STATE = 2;
	
	public int _state = ENABLED_SAVED_STATE;
	
	private JScrollPane _scroll =  new JScrollPane();
	private JTextArea _area = new JTextArea();
	private final Signal<String> _source;
	private final Omnivore<String> _setter;
	private final Signal<Font> _font;
	private final int _rows;

	public ReactiveMemoField(Signal<String> source, Omnivore<String> setter, Signal<Font> font, int rows) {
		_source = source;
		_setter = setter;
		_font = font;
		_rows = rows;
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

	private void initComponents() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		_area.setLineWrap(true);
		_area.setWrapStyleWord(true);
		if (_rows>-1)
			_area.setRows(_rows);
		_area.selectAll();
		_scroll.setViewportView(_area);
		_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(_scroll);
		updateView();
		firstUpdate();
	}

	private void firstUpdate() {
		_area.setText(_source.currentValue());
		setCurrentFont(_font.currentValue());
	}
	
	public void updateView(){
		switch(_state){
			case ENABLED_SAVED_STATE:
				_area.setEditable(true);
				_area.setBackground(Color.WHITE);
				_scroll.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(2,2,2,2)));
				break;
			case DISABLED_STATE:
				_area.setEditable(false);
				_area.setBackground(new Color(240,240,240));
				break;
			case ENABLED_UNSAVED_STATE:
				_scroll.setBorder(new CompoundBorder(new LineBorder(Color.red), new EmptyBorder(2,2,2,2)));
				break;
		}
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
	
	private Omnivore<String> fieldReceiver() { return new Omnivore<String>(){
		public void consume(final String text) {
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					_area.setText(text);
					_area.revalidate();
				}
			});
		}};
	}

	private void addChangeListeners() {
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
			
		/*_area.addActionListener(new ActionListener(){ //ENTER KEY
			public void actionPerformed(ActionEvent e) {
				commitTextChanges();
			}
		});*/
	}
	
	public void commitTextChanges(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				if (!_source.currentValue().equals(_area.getText()))
					_setter.consume(_area.getText());
				_area.revalidate();
				_state = ENABLED_SAVED_STATE;
				updateView();
			}
		});
	}
	
	public String getText(){
		return _area.getText();
	}
	
	private static final long serialVersionUID = 1L;



	
}

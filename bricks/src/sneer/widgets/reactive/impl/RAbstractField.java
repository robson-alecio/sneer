package sneer.widgets.reactive.impl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sneer.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;

public abstract class RAbstractField<U, WIDGET> extends JPanel implements TextWidget {
	private static final long serialVersionUID = 1L;

	public static final int ENABLED_SAVED_STATE = 0;
	public static final int ENABLED_UNSAVED_STATE = 1;
	public static final int DISABLED_STATE = 2;

	public int _state = ENABLED_SAVED_STATE;

	protected Signal<U> _source;
	protected Consumer<U> _setter;
	public JTextField _area = new JTextField();
	
	protected Omnivore<U> _fieldReciver;
	protected Omnivore<Pair<U, String>> _textChangedReceiver;
	
	public abstract Omnivore<U> fieldReceiver();
	public abstract Omnivore<Pair<U, String>> textChangedReceiver();
	
	RAbstractField(Signal<U> source, Consumer<U> setter, boolean notifyEveryChange) {
		_source = source;
		_setter = setter;
		_state = (setter == null) ? DISABLED_STATE : ENABLED_SAVED_STATE;
		initComponents();
		addReceivers();
		if (_state == ENABLED_SAVED_STATE)
			addChangeListeners(notifyEveryChange);
	}

	private void addReceivers() {
		_fieldReciver=fieldReceiver();
		_source.addReceiver(_fieldReciver);
	}

	public void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_area.selectAll();
		add(_area);
		updateView();
		firstUpdate();
	}

	private void firstUpdate() {
		fieldReceiver().consume(_source.currentValue());
	}

	public void updateView() {
		switch (_state) {
		case ENABLED_SAVED_STATE:
			_area.setEditable(true);
			_area.setBorder(new CompoundBorder(new LineBorder(Color.black),
					new EmptyBorder(2, 2, 2, 2)));
			break;
		case DISABLED_STATE:
			_area.setEditable(false);
			break;
		case ENABLED_UNSAVED_STATE:
			_area.setBorder(new CompoundBorder(new LineBorder(Color.red),
					new EmptyBorder(2, 2, 2, 2)));
			break;
		}
	}

	public void addChangeListeners(boolean notifyEveryChange) {
		_area.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				_state = ENABLED_UNSAVED_STATE;
				updateView();
			}
		});

		if(notifyEveryChange){
			notifyEveryChange();
			return;
		}
		
		notifyActionPerformed();
	}
	private void notifyEveryChange() {
		_area.getDocument().addDocumentListener(
			new DocumentListener(){

				@Override
				public void changedUpdate(DocumentEvent e) {
					commitTextChanges();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					commitTextChanges();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					commitTextChanges();
				}
			}
		);
	}
	
	private void notifyActionPerformed() {
		_area.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				commitTextChanges();
			}
		});
		_area.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					commitTextChanges();
			}
		});

		_area.addActionListener(new ActionListener(){ //ENTER KEY
			public void actionPerformed(ActionEvent e) {
				commitTextChanges();
			}
		});
	}

	public void commitTextChanges() {
		_textChangedReceiver = textChangedReceiver();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Pair<U, String> pair = new Pair<U, String>(_source.currentValue(), getText());
				_textChangedReceiver.consume(pair);
				_area.revalidate();
				_state = ENABLED_SAVED_STATE;
				updateView();
			}
		});
	}

	public String getText() {
		return _area.getText();
	}
	
	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					_area.setText(text);
					_area.revalidate();
				} catch (Exception e) {
					e.printStackTrace();
					//ignore
				}
			}
		});
	}
	
	@Override
	public JTextField getMainWidget() {
		return _area;
	}
	
	@Override
	public JPanel getContainer() {
		return this;
	}	
}
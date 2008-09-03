package sneer.skin.widgets.reactive.impl;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.lang.Pair;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

public abstract class RAbstractField<WIDGET extends JComponent> extends JPanel implements TextWidget<WIDGET> {
	private static final long serialVersionUID = 1L;

	public static final int ENABLED_SAVED_STATE = 0;
	public static final int ENABLED_UNSAVED_STATE = 1;
	public static final int DISABLED_STATE = 2;

	public int _state = ENABLED_SAVED_STATE;

	protected Signal<String> _source;
	protected Consumer<String> _setter;
	public WIDGET _textComponent;
	
	protected Receiver<String> _fieldReciver;
	protected Receiver<Pair<String, String>> _textChangedReceiver;

	private Border _defaultBorder;
	
	public abstract Receiver<String> fieldReceiver();
	public abstract Receiver<Pair<String, String>> textChangedReceiver();
	
	RAbstractField(WIDGET textComponent, Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		_source = source;
		_setter = setter;
		_textComponent = textComponent;
		_state = (setter == null) ? DISABLED_STATE : ENABLED_SAVED_STATE;
		initAbstractWidget(textComponent, notifyOnlyWhenDoneEditing);
		addReceivers();
	}
	
	private void initAbstractWidget(final WIDGET textComponent, final boolean notifyOnlyWhenDoneEditing) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						setLayout(new GridBagLayout());
						GridBagConstraints c;
						c = new GridBagConstraints(0,0,1,1,1.0,1.0,
									GridBagConstraints.EAST, 
									GridBagConstraints.BOTH,
									new Insets(0,0,0,0),0,0);
						if (_textComponent instanceof JTextComponent) {
							JTextComponent txt = (JTextComponent) _textComponent;
							txt.selectAll();
						}
						add(_textComponent, c);
						updateView();
						firstUpdate();
						
						if (_state == ENABLED_SAVED_STATE)
							addChangeListeners(notifyOnlyWhenDoneEditing);
						
						_defaultBorder = textComponent.getBorder();
						setOpaque(false);
					}
				}
		);	
	}

	RAbstractField(WIDGET textComponent, Signal<String> source) {
		this(textComponent, source, null, false);
	}
	
	private void addReceivers() {
		_fieldReciver=fieldReceiver();
//		_source.addReceiver(_fieldReciver);
	}

	private void firstUpdate() {
		fieldReceiver().consume(_source.currentValue());
	}

	public void updateView() {
		switch (_state) {
		case ENABLED_SAVED_STATE:
			if (_textComponent instanceof JTextComponent) {
				JTextComponent txt = (JTextComponent) _textComponent;
				txt.setEditable(true);
			}
			_textComponent.setBorder(_defaultBorder);
			break;
		case DISABLED_STATE:
			if (_textComponent instanceof JTextComponent) {
				JTextComponent txt = (JTextComponent) _textComponent;
				txt.setEditable(false);
			}
			_textComponent.setBorder(_defaultBorder);
			break;
		case ENABLED_UNSAVED_STATE:
			_textComponent.setBorder(new CompoundBorder(new LineBorder(Color.red),
					new EmptyBorder(2, 2, 2, 2)));
			break;
		}
	}

	public void addChangeListeners(boolean notifyOnlyWhenDoneEditing) {
		_textComponent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				_state = ENABLED_UNSAVED_STATE;
				updateView();
			}
		});

		if(!notifyOnlyWhenDoneEditing){
			notifyEveryChange();
			return;
		}
		
		notifyActionPerformed();
	}
	private void notifyEveryChange() {
		if (_textComponent instanceof JTextComponent) {
			JTextComponent txt = (JTextComponent) _textComponent;
			txt.getDocument().addDocumentListener(
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
	}
	
	private void notifyActionPerformed() {
		_textComponent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				commitTextChanges();
			}
		});
		_textComponent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					commitTextChanges();
			}
		});
//		_area.addActionListener(new ActionListener(){ //ENTER KEY
//			public void actionPerformed(ActionEvent e) {
//				commitTextChanges();
//			}
//		});
	}

	public void commitTextChanges() {
		_textChangedReceiver = textChangedReceiver();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Pair<String, String> pair = new Pair<String, String>(_source.currentValue(), getText());
				_textChangedReceiver.consume(pair);
				_textComponent.revalidate();
				_state = ENABLED_SAVED_STATE;
				updateView();
			}
		});
	}

	public String getText() {
		try {
			return (String) _textComponent.getClass().getMethod("getText", new Class[0]).invoke(_textComponent, new Object[0]);
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement
		}
	}
	
	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					_textComponent.getClass().getMethod("setText", new Class[]{String.class}).invoke(_textComponent, new Object[]{text});
				} catch (Exception e) {
					throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement
				}
			}
		});
	}
	
	@Override
	public WIDGET getMainWidget() {
		return _textComponent;
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}	

	@Override
	public Signal<String> output(){
		return _source;
	}
	
	@Override
	public Consumer<String> setter(){
		return _setter;
	}	
}
package sneer.skin.widgets.reactive.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.exceptions.NotImplementedYet;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.reactive.Signal;
import sneer.skin.colors.Colors;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.PickyConsumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.impl.EventReceiver;


abstract class RAbstractField<WIDGET extends JTextComponent> extends JPanel implements TextWidget<WIDGET> {
	
	private static final long serialVersionUID = 1L;
	
	protected final Signal<?> _source;
	protected final PickyConsumer<? super String> _setter;
	protected final WIDGET _textComponent;
	protected final NotificationPolicy _notificationPolicy;

	protected final EventReceiver<?> _fieldReciver;

	protected ChangeInfoDecorator _decorator;
	protected String _lastNotified = "";
	
	public boolean _notified = true;

	private final Environment _environment;


	RAbstractField(WIDGET textComponent, Signal<?> source) {
		this(textComponent, source, null, NotificationPolicy.OnTyping);
	}
	
	RAbstractField(WIDGET textComponent, Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy) {
		_environment = my(Environment.class);
		
		_source = source;
		_setter = setter;
		_textComponent = textComponent;
		_notificationPolicy = notificationPolicy;
		_fieldReciver=fieldReceiver();	
		_decorator = new ChangeInfoDecorator(_textComponent);
		
		initGui();
		initChangeListeners();
	}
	
	private void initGui() {
		if(_setter == null){
			_textComponent.setEditable(false);
		}
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0,0,1,1,1.0,1.0, 
													 GridBagConstraints.EAST, 
													 GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0);
		add(_textComponent, c);
		setOpaque(false);
	}

	private void initChangeListeners() {
		addKeyListenerToCommitOnKeyTyped();
		if ( _notificationPolicy == NotificationPolicy.OnEnterPressedOrLostFocus )
			addFocusListenerToCommitWhenLost();
		addDoneListenerCommiter();
	}


	private void addKeyListenerToCommitOnKeyTyped() {
		_textComponent.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) {
			setNotified(_notificationPolicy == NotificationPolicy.OnTyping,  getText());
			Environments.runWith(_environment, new Runnable() { @Override public void run() {
				commitIfNecessary();
			}});
		}

		private void commitIfNecessary() {
			my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
				_textComponent.invalidate();
				_textComponent.getParent().validate();
				if ( _notificationPolicy == NotificationPolicy.OnTyping ) commitTextChanges();
			}});
		}});
	}
	
	private void addFocusListenerToCommitWhenLost() {
		_textComponent.addFocusListener(new FocusAdapter() {  @Override  public void focusLost(FocusEvent e) {
			commitTextChanges();
		}});
	}
	
	protected void addDoneListenerCommiter() {
		try {
			Method m = _textComponent.getClass().getMethod("addActionListener", new Class[]{ActionListener.class});
			m.invoke(_textComponent, new Object[]{new ActionListener(){@Override public void actionPerformed(ActionEvent e) {
				commitTextChanges();
			}}});
		} catch (Exception ex) {
			_textComponent.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
						commitTextChanges();		
					}});
			}});
		} 	
	}

	public void commitTextChanges() {
		String text = getText();
		if (text.equals( currentValue())) return;
		my(GuiThread.class).assertInGuiThread();
		consume(text);
		refreshTextComponent();
		setNotified(true, text);
	}

	private void refreshTextComponent() {
		_textComponent.setText(currentValue());
		_textComponent.revalidate();
	}

	private String currentValue() {
		return valueToString(_source.currentValue());
	}

	private String valueToString(Object value) {
		return (value==null)?"":value.toString();
	}
	
	public String getText() {
		String tmp = tryReadText();
		return tmp==null ? "": tmp;
	}
	
	public void setText(final String text) {
		my(GuiThread.class).assertInGuiThread();
		String currentValue = tryReadText();
		
		if(currentValue==null || text==null){
			trySetText(text);
			return;
		}
		
		if(!currentValue.equals(text))
			trySetText(text);
	}
	
	private void trySetText(final String text){
		try {
			_textComponent.getClass().getMethod("setText", new Class[]{String.class}).invoke(_textComponent, new Object[]{text});
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet("Invalid Widget", e);
		}		
	}

	private String tryReadText(){
		try {
			return (String) _textComponent.getClass().getMethod("getText", new Class[0]).invoke(_textComponent, new Object[0]);
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet("Invalid Widget", e);
		}
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
	public Signal<?> output(){
		return _source;
	}
	
	@Override
	public PickyConsumer<? super String> setter(){
		return _setter;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return RUtil.limitSize(super.getMinimumSize());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return RUtil.limitSize(super.getPreferredSize());
	}
	
	@Override
	public Dimension getMaximumSize() {
		return RUtil.limitSize(super.getMaximumSize());
	}
	
	private void setNotified(boolean isNotified, String newText) {
		if(_lastNotified.equals(newText))
			isNotified = true;
			
		_notified = isNotified;
		_decorator.decorate(isNotified);
		
		if(isNotified) _lastNotified = newText;
	}
	
	@Override
	public JComponent[] getWidgets() {
		return new JComponent[]{_textComponent};
	}
	
	public EventReceiver<?> fieldReceiver() {
		return new EventReceiver<Object>(_source) {@Override public void consume(final Object text) {
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				if (!_notified) return;
				setText(valueToString(text));
			}});
		}};
	}
	
	protected void consume(String text) {
		try {
			_setter.consume(text);
		} catch (IllegalParameter ip) {
			throw new NotImplementedYet(ip);
		}
	}
	
	protected class ChangeInfoDecorator{
		
		private final JComponent _target;
		private boolean _isOpaque;
		private Color _bgColor;
		
		ChangeInfoDecorator(JComponent target){
			_target = target;
			_isOpaque = target.isOpaque();
			_bgColor = target.getBackground();
		}
		
		void decorate(final boolean notified) {
			my(GuiThread.class).assertInGuiThread();
			if(notified) {
				_target.setOpaque(_isOpaque);
				_target.setBackground(_bgColor);
				return;
			}
			_target.setOpaque(true);
			_target.setBackground(my(Colors.class).invalid());
		}	
	}
}

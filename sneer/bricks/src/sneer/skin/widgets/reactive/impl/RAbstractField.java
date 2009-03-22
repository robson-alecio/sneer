package sneer.skin.widgets.reactive.impl;

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
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import sneer.pulp.reactive.Signal;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.GuiThread;
import wheel.io.ui.impl.UserImpl;
import wheel.lang.PickyConsumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.impl.EventReceiver;

abstract class RAbstractField<WIDGET extends JTextComponent> extends JPanel implements TextWidget<WIDGET> {
	
	private static final long serialVersionUID = 1L;
	
	protected final Signal<String> _source;
	protected final PickyConsumer<String> _setter;
	protected final WIDGET _textComponent;
	protected final NotificationPolicy _notificationPolicy;

	protected final EventReceiver<String> _fieldReciver;

	protected ChangeInfoDecorator _decorator;
	protected String _lastNotified = "";
	
	public boolean _notified = true;


	RAbstractField(WIDGET textComponent, Signal<String> source) {
		this(textComponent, source, null, NotificationPolicy.OnTyping);
	}
	
	RAbstractField(WIDGET textComponent, Signal<String> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		_source = source;
		_setter = setter;
		_textComponent = textComponent;
		_notificationPolicy = notificationPolicy;
		_fieldReciver=fieldReceiver();	
		_decorator = new ChangeInfoDecorator(_textComponent.getBorder(), _textComponent);
		
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
			GuiThread.invokeLater(new Runnable(){ @Override public void run() {
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
					GuiThread.invokeLater(new Runnable(){ @Override public void run() {
						commitTextChanges();		
					}});
			}});
		} 	
	}

	public void commitTextChanges() {
		String text = getText();
		if (text.equals( currentValue())) return;
		GuiThread.assertInGuiThread();
		consume(text);
		refreshTextComponent();
		setNotified(true, text);
	}

	private void refreshTextComponent() {
		_textComponent.setText(currentValue());
		_textComponent.revalidate();
	}

	private String currentValue() {
		return _source.currentValue();
	}
	
	public String getText() {
		String tmp = tryReadText();
		return tmp==null ? "": tmp;
	}
	
	public void setText(final String text) {
		GuiThread.assertInGuiThread();
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
	public Signal<String> output(){
		return _source;
	}
	
	@Override
	public PickyConsumer<String> setter(){
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
	
	public EventReceiver<String> fieldReceiver() {
		return new EventReceiver<String>(_source) {@Override public void consume(final String text) {
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				if (!_notified) return;
				setText(text);
			}});
		}};
	}
	
	protected void consume(String text) {
		try {
			_setter.consume(text);
		} catch (IllegalParameter ip) {
			new UserImpl().acknowledge(ip);
		}
	}
}

class ChangeInfoDecorator{
	
	private final Border _defaultBorder;
	private final JComponent _target;
	
	ChangeInfoDecorator(Border defaultBorder, JComponent target){
		_defaultBorder = defaultBorder;
		_target = target;
	}
	
	void decorate(final boolean _notified) {
		GuiThread.assertInGuiThread();
		if(_notified) {
			_target.setBorder(_defaultBorder);
			return;
		}
		_target.setBorder(new CompoundBorder(new LineBorder(Color.red), new EmptyBorder(2, 2, 2, 2)));
	}	
}
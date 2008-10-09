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

import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

abstract class RAbstractField<WIDGET extends JTextComponent> extends JPanel implements TextWidget<WIDGET> {
	
	private static final long serialVersionUID = 1L;
	
	protected final boolean _notifyOnlyWhenDoneEditing;
	protected final Signal<String> _source;
	protected final Consumer<String> _setter;
	protected final WIDGET _textComponent;

	protected final Receiver<String> _fieldReciver;

	protected final ChangeInfoDecorator _decorator;
	
	public boolean _notified = true;

	protected abstract Receiver<String> fieldReceiver();
	protected abstract void consume(String text);
	
	RAbstractField(WIDGET textComponent, Signal<String> source) {
		this(textComponent, source, null, false);
	}
	
	RAbstractField(WIDGET textComponent, Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		_source = source;
		_setter = setter;
		_textComponent = textComponent;
		_notifyOnlyWhenDoneEditing = notifyOnlyWhenDoneEditing;
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
		addFocusListenerToCommitWhenLost();		
		addDoneListenerCommiter();
	}
	
	private void addKeyListenerToCommitOnKeyTyped() {
		_textComponent.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) {
			setNotified(!_notifyOnlyWhenDoneEditing);
			_textComponent.invalidate();
			_textComponent.getParent().validate();
		
			if (!_notifyOnlyWhenDoneEditing)
				commitTextChanges();		
		}});
	}
	
	private void addFocusListenerToCommitWhenLost() {
		_textComponent.addFocusListener(new FocusAdapter() { @Override public void focusLost(FocusEvent e) {
			commitTextChanges();
		}});
	}
	
	private void addDoneListenerCommiter() {
		try {
			Method m = _textComponent.getClass().getMethod("addActionListener", new Class[]{ActionListener.class});
			m.invoke(_textComponent, new Object[]{new ActionListener(){@Override public void actionPerformed(ActionEvent e) {
				commitTextChanges();
			}}});
		} catch (Exception ex) {
			_textComponent.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					commitTextChanges();		
			}});
		} 	
	}

	public void commitTextChanges() {
		if (getText().equals( currentValue())) return;
		GuiThread.assertInGuiThread();
		consume(getText());
		refreshTextComponent();
		setNotified(true);
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
		GuiThread.invokeLater(new Runnable(){ @Override public void run() {
			String currentValue = tryReadText();
			
			if(currentValue==null || text==null){
				trySetText(text);
				return;
			}
			
			if(!currentValue.equals(text))
				trySetText(text);
		}});
	}
	
	private void trySetText(final String text){
		try {
			_textComponent.getClass().getMethod("setText", new Class[]{String.class}).invoke(_textComponent, new Object[]{text});
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet("Invalid Widget", e);
		}		
	}

	private String tryReadText(){
		try {
			return (String) _textComponent.getClass().getMethod("getText", new Class[0]).invoke(_textComponent, new Object[0]);
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet("Invalid Widget", e);
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
	public Consumer<String> setter(){
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
	
	private void setNotified(boolean isNotified) {
		_notified = isNotified;
		_decorator.decorate(_notified);
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
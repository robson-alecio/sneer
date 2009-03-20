package sneer.skin.widgets.reactive.impl;

import javax.swing.JFrame;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;
import sneer.skin.widgets.reactive.WindowWidget;
import wheel.lang.Consumer;
import wheel.reactive.impl.Receiver;

class RFrameImpl extends JFrame implements WindowWidget<JFrame>{

	protected final Register<String> _titleRegister;
	protected final Consumer<String> _titleSetter;
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private final Receiver<String> _titleReceiverAvoidGc;
	
	RFrameImpl(Signal<String> source) {
		this(source, null);
	}
	
	RFrameImpl(Signal<String> source, Consumer<String> setter){
		_titleRegister = new RegisterImpl<String>(null);
		_titleSetter = setter;
		_titleReceiverAvoidGc = titleReceiverFor(source);
	}

	private Receiver<String> titleReceiverFor(Signal<String> signal) {
		return new Receiver<String>(signal) {@Override public void consume(final String title) {
			_titleRegister.setter().consume(title);
			setTitle(title);
		}};
	}

	@Override
	public Signal<String> output() {
		return _titleRegister.output();
	}

	@Override
	public Consumer<String> setter() {
		return _titleSetter;
	}

	@Override
	public JFrame getMainWidget() {
		return this;
	}
}
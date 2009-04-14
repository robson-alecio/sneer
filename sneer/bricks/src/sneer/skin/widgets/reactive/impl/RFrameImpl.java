package sneer.skin.widgets.reactive.impl;

import static sneer.commons.environments.Environments.my;

import javax.swing.JFrame;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.widgets.reactive.WindowWidget;
import sneer.software.lang.PickyConsumer;
import wheel.reactive.impl.EventReceiver;

class RFrameImpl extends JFrame implements WindowWidget<JFrame>{

	protected final Register<String> _titleRegister;
	protected final PickyConsumer<String> _titleSetter;
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private final EventReceiver<?> _titleReceiverAvoidGc;
	
	RFrameImpl(Signal<?> source) {
		this(source, null);
	}
	
	RFrameImpl(Signal<?> source, PickyConsumer<String> setter){
		_titleRegister = my(Signals.class).newRegister(null);
		_titleSetter = setter;
		_titleReceiverAvoidGc = titleReceiverFor(source);
	}

	private EventReceiver<?> titleReceiverFor(Signal<?> signal) {
		return new EventReceiver<Object>(signal) {@Override public void consume(final Object title) {
			_titleRegister.setter().consume(valueToString(title));
			setTitle(valueToString(title));
		}

		private String valueToString(final Object title) {
			return (title==null)?"":title.toString();
		}};
	}

	@Override
	public Signal<String> output() {
		return _titleRegister.output();
	}

	@Override
	public PickyConsumer<String> setter() {
		return _titleSetter;
	}

	@Override
	public JFrame getMainWidget() {
		return this;
	}
}
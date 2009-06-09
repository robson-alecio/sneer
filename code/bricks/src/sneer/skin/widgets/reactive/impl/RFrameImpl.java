package sneer.skin.widgets.reactive.impl;

import static sneer.commons.environments.Environments.my;

import javax.swing.JFrame;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.widgets.reactive.Widget;

class RFrameImpl extends JFrame implements Widget<JFrame> {

	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	RFrameImpl(Signal<?> titleSignal) {
		_referenceToAvoidGc = my(Signals.class).receive(titleSignal, new Consumer<Object>() { @Override public void consume(final Object title) {
			setTitle(valueToString(title));
		}});
	}

	private String valueToString(Object title) {
		return (title == null) ? "" : title.toString();
	}

	@Override
	public JFrame getMainWidget() {
		return this;
	}
}
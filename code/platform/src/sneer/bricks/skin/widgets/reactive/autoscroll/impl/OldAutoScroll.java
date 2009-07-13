package sneer.bricks.skin.widgets.reactive.autoscroll.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.FocusAdapter;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;

class OldAutoScroll<T> {
	
	private final JScrollPane _scroll = my(SynthScrolls.class).create();
	private boolean _shouldAutoscroll = true;
	private Reception _reception;

	JScrollPane scrollPane() {
		return _scroll;
	}
	
	OldAutoScroll(EventSource<T> eventSource, Consumer<T> receiver) {
		initReceivers(eventSource, receiver);
		holdReceivers();
	}

	private void holdReceivers() {
		_scroll.addFocusListener(new FocusAdapter(){
			@SuppressWarnings({ "unchecked", "unused" })
			OldAutoScroll _refToAvoidGc = OldAutoScroll.this;
		});
	}

	private boolean isAtEnd() {
		final ByRef<Boolean> result = ByRef.newInstance();
		my(GuiThread.class).invokeAndWaitForWussies(new Runnable(){ @Override public void run() {
			result.value =  scrollModel().getValue() + scrollModel().getExtent() == scrollModel().getMaximum();
		}});
		return result.value;
	}		
	
	private void placeAtEnd() {
		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			scrollModel().setValue(scrollModel().getMaximum()-scrollModel().getExtent());
		}});
	}
	
	private BoundedRangeModel scrollModel() {
		return _scroll.getVerticalScrollBar().getModel();
	}	
	
	private void initReceivers(EventSource<T> eventSource, Consumer<T> consumer) {
		_reception = my(Signals.class).receive(eventSource, wrapper(consumer));
	}
	
	private Consumer<T> wrapper(final	Consumer<T> consumer) {
		return new Consumer<T>(){ @Override public void consume(T value){
			_shouldAutoscroll = isAtEnd();
			consumer.consume(value);
			doAutoScroll();
		}};
	}
	
	private void doAutoScroll() {
		if(_shouldAutoscroll) 
			placeAtEnd();
	}
	
	@Override
	protected void finalize() throws Throwable {
		_reception.dispose();
	}
}
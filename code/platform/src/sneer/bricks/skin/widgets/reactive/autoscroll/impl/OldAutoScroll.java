package sneer.bricks.skin.widgets.reactive.autoscroll.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.ListSignal;
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

	OldAutoScroll(JComponent keyTypeSource, ListSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver) {
		initReceivers(inputSignal, receiver);
		keyTypeSource.addKeyListener(new KeyAdapter(){@Override public void keyReleased(KeyEvent e) {
			if(_shouldAutoscroll) 
				placeAtEnd();
		}});
		
		_scroll.addFocusListener(new FocusAdapter(){
			@SuppressWarnings({ "unchecked", "unused" })
			OldAutoScroll _refToAvoidGc = OldAutoScroll.this;
		});
	}
	
	public OldAutoScroll(EventSource<T> eventSource) {
		initReceivers(eventSource);
	}
	
	private boolean isAtEnd() {
		final ByRef<Boolean> result = ByRef.newInstance();
		my(GuiThread.class).invokeAndWaitForWussies(new Runnable(){ @Override public void run() {
			result.value =  scrollModel().getValue() + scrollModel().getExtent() == scrollModel().getMaximum();
		}});
		return result.value;
	}		
	
	private void placeAtEnd() {
		my(GuiThread.class).invokeLaterForWussies(new Runnable(){ @Override public void run() {
			scrollModel().setValue(scrollModel().getMaximum()-scrollModel().getExtent());
		}});
	}
	
	private BoundedRangeModel scrollModel() {
		return _scroll.getVerticalScrollBar().getModel();
	}	
	
	private void initReceivers(ListSignal<T> inputSignal, Consumer<CollectionChange<T>> consumer) {
		_reception = my(Signals.class).receive(inputSignal, wrapper(consumer));
	}
	
	private Consumer<CollectionChange<T>> wrapper(final	Consumer<CollectionChange<T>> consumer) {
		return new Consumer<CollectionChange<T>>(){ @Override public void consume(CollectionChange<T> value){
			doAutoScroll(consumer, value);
		}};
	}
	
	private void doAutoScroll( final Consumer<CollectionChange<T>> consumer, CollectionChange<T> value) {
		_shouldAutoscroll = isAtEnd();
		consumer.consume(value);
		doAutoScroll();
	}
	
	private void doAutoScroll() {
		if(_shouldAutoscroll) 
			placeAtEnd();
	}
	
	private void initReceivers(EventSource<T> eventSource) {
		_reception = my(Signals.class).receive(eventSource, new Consumer<T>(){ @Override public void consume(T value) {
			doAutoScroll();
		}});
	}
	
	@Override
	protected void finalize() throws Throwable {
		_reception.dispose();
	}
}
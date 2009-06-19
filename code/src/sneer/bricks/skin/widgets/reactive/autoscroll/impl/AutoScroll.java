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
import sneer.bricks.pulp.reactive.collections.impl.SimpleListReceiver;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;

class AutoScroll<T> {
	
	private final JScrollPane _scroll = my(SynthScrolls.class).create();
	private boolean _shouldAutoscroll = true;
	private Reception _reception;

	JScrollPane scrollPane() {
		return _scroll;
	}

	AutoScroll(JComponent keyTypeSource, ListSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver) {
		keyTypeSource.addKeyListener(new KeyAdapter(){@Override public void keyReleased(KeyEvent e) {
			if(_shouldAutoscroll) 
				placeAtEnd();
		}});
		initReceivers(inputSignal, receiver);
		
		_scroll.addFocusListener(new FocusAdapter(){
			@SuppressWarnings({ "unchecked", "unused" })
			AutoScroll _refToAvoidGc = AutoScroll.this;
		});
	}
	
	public AutoScroll(EventSource<T> eventSource) {
		initReceivers(eventSource);
	}
	
	private boolean isAtEnd() {
		final ByRef<Boolean> result = ByRef.newInstance();
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
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
	
	private void initReceivers(ListSignal<T> inputSignal, Consumer<CollectionChange<T>> consumer) {
		initPreChangeReceiver(inputSignal);		
		_reception = my(Signals.class).receive(consumer, inputSignal);
		initPosChangeReceiver(inputSignal);
	}
	
	private void initReceivers(EventSource<T> eventSource) {
		_reception = my(Signals.class).receive(new Consumer<T>(){ @Override public void consume(T value) {
			if(_shouldAutoscroll) 
				placeAtEnd();
		}}, eventSource);
	}

	@SuppressWarnings("unused")
	private SimpleListReceiver<T> _preChangeReceiverAvoidGc;
	private void initPreChangeReceiver(ListSignal<T> inputSignal) {
		_preChangeReceiverAvoidGc = new MySimpleListReceiver(inputSignal){ @Override protected void fire() {
			_shouldAutoscroll = isAtEnd();
		}};
	}
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<T> _posChangeReceiverAvoidGc;
	private void initPosChangeReceiver(ListSignal<T> inputSignal) {
		_posChangeReceiverAvoidGc = new MySimpleListReceiver(inputSignal){ @Override protected void fire() {
			if(_shouldAutoscroll) placeAtEnd();
		}};
	}
	
	private abstract class MySimpleListReceiver extends SimpleListReceiver<T>{
		public MySimpleListReceiver(ListSignal<T> inputSignal) { super(inputSignal); }
		@Override protected void elementAdded(T newElement) { fire();}
		@Override protected void elementPresent(T element) 		{ fire();}
		@Override protected void elementRemoved(T element) 	{ fire();}

		protected abstract void fire();
	}
	
	@Override
	protected void finalize() throws Throwable {
		_reception.dispose();
	}
}
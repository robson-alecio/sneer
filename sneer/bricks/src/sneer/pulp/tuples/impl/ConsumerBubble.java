package sneer.pulp.tuples.impl;

import java.util.List;

import org.prevayler.Prevayler;

import sneer.hardware.cpu.exceptions.IllegalParameter;
import sneer.hardware.cpu.lang.Consumer;


@SuppressWarnings("unchecked")
class ConsumerBubble extends PickyConsumerBubble implements Consumer {

	ConsumerBubble(Prevayler prevayler, List<String> getterPathToOmnivore) {
		super(prevayler, getterPathToOmnivore);
	}

	@Override
	public void consume(Object vo) {
		try {
			super.consume(vo);
		} catch (IllegalParameter e) {
			throw new IllegalStateException(e);
		}
	}

}

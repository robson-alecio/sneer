package sneer.bricks.pulp.tuples.impl;

import java.util.List;

import org.prevayler.Prevayler;

import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;



@SuppressWarnings("unchecked")
class PickyConsumerBubble implements PickyConsumer {

	private final List<String> _getterPathToConsumer;
	private final Prevayler _prevayler;

	PickyConsumerBubble(Prevayler prevayler, List<String> getterPathToConsumer) {
		_getterPathToConsumer = getterPathToConsumer;
		_prevayler = prevayler;
	}

	public void consume(Object vo) throws Refusal {
		try {
			_prevayler.execute(new Consumption(_getterPathToConsumer, vo));
		} catch (Refusal e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}

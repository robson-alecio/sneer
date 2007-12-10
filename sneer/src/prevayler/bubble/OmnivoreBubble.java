package prevayler.bubble;

import java.util.List;

import org.prevayler.Prevayler;

import wheel.lang.Omnivore;
import wheel.lang.exceptions.IllegalParameter;

@SuppressWarnings("unchecked")
class OmnivoreBubble extends ConsumerBubble implements Omnivore {

	OmnivoreBubble(Prevayler prevayler, List<String> getterPathToOmnivore) {
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

package sneer.pulp.reactive;

import sneer.pulp.events.EventSource;

/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends EventSource<VO> {
	
	VO currentValue();
	
}


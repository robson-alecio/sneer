package sneer.bricks.pulp.reactive;

import sneer.bricks.pulp.events.EventSource;

/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends EventSource<VO> {
	
	VO currentValue();
	
}


package wheel.reactive;

import wheel.reactive.sets.SetSignal;


/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends EventSource<VO>, SetSignal<VO> {  //Fix: Make Signal extend ListSignal.
	
	public VO currentValue();
	
}


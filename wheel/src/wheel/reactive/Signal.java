package wheel.reactive;

import wheel.reactive.lists.ListSignal;
import wheel.reactive.sets.SetSignal;


/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends SetSignal<VO>, ListSignal<VO>, EventSource<VO> {  //Fix: Make Signal extend ListSignal.
	
	public VO currentValue();
	
}


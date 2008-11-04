package wheel.reactive;

/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends EventSource<VO> {
	
	public VO currentValue();
	
}


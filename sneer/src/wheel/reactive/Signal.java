package wheel.reactive;



/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends SetSignal<VO> {
	
	public void addReceiver(Receiver<VO> receiver);
	public void removeReceiver(Receiver<VO> name);

	public void addTransientReceiver(Receiver<VO> receiver);
	public void removeTransientReceiver(Receiver<VO> receiver);

	public VO currentValue();
	
}

class Todo2 {
	private int todo_make_signal_extend_list_signal;
}
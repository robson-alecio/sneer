package wheel.reactive.lists;

import java.util.Iterator;

import wheel.lang.Omnivore;

public interface ListSource<VO> extends Iterable<VO>{

	ListSignal<VO> output();

	//Fix: ListSource should use the omnivore instead of this
	void add(VO element);
	boolean remove(VO element);

	Omnivore<VO> adder();
	
	public Iterator<VO> iterator();

}
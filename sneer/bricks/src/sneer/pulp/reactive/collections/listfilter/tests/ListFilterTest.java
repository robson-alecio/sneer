package sneer.pulp.reactive.collections.listfilter.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.commons.lang.Functor;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ReactivePredicate;
import sneer.pulp.reactive.collections.SetRegister;
import sneer.pulp.reactive.collections.SetSignal;
import sneer.pulp.reactive.collections.impl.SetRegisterImpl;
import sneer.pulp.reactive.collections.listfilter.ListFilter;

public class ListFilterTest extends BrickTest {
	
	private final ListFilter _subject = my(ListFilter.class);

	private final Signal<Integer> _10 = my(Signals.class).constant(10);
	private final Signal<Integer> _20 = my(Signals.class).constant(20);
	private final Signal<Integer> _30 = my(Signals.class).constant(30);
	private final Signal<Integer> _40 = my(Signals.class).constant(40);
	private final Signal<Integer> _50 = my(Signals.class).constant(50);

	@Test
	public void addRemoveTest() {
		SetRegister<Signal<Integer>> src = new SetRegisterImpl<Signal<Integer>>();
		SetSignal<Signal<Integer>> filtered = _subject.filter(src.output(), reactivePredicate());

		src.add(_10);
		src.add(_30);
		src.add(_20);
		src.add(_40);
		
		assertEquals(1 , listSize(filtered));
		assertEquals(4 , listSize(src.output()));
		
		src.add(_50);
		assertEquals(2 , listSize(filtered));
		assertEquals(5 , listSize(src.output()));
	
		src.add(_50);
		assertEquals(2 , listSize(filtered));		
		assertEquals(5 , listSize(src.output()));
		
		src.remove(_50);
		assertEquals(1 , listSize(filtered));		
		assertEquals(4 , listSize(src.output()));
}
	
	@Test
	public void changeValueTest() {
		SetRegister<Signal<Integer>> src = new SetRegisterImpl<Signal<Integer>>();
		SetSignal<Signal<Integer>> filtered = _subject.filter(src.output(), reactivePredicate());
		
		Register<Integer> r15 = my(Signals.class).newRegister(15);
		Register<Integer> r25 = my(Signals.class).newRegister(25);
		Register<Integer> r35 = my(Signals.class).newRegister(35);
		Register<Integer> r45 = my(Signals.class).newRegister(45);
		
		Signal<Integer> s15 = signal(r15);
		Signal<Integer> s25 = signal(r25);
		Signal<Integer> s35 = signal(r35);
		Signal<Integer> s45 = signal(r45);
		
		src.add(s15);
		src.add(s25);
		src.add(s25);
		src.add(s35);
		
		assertEquals(1 , listSize(filtered));
		assertEquals(3 , listSize(src.output()));
		
		src.add(s45);
		assertEquals(2 , listSize(filtered));
		assertEquals(4 , listSize(src.output()));
		
		r15.setter().consume(150);
		assertEquals(3 , listSize(filtered));
		assertEquals(4 , listSize(src.output()));
		
		r25.setter().consume(250);
		assertEquals(4 , listSize(filtered));
		assertEquals(4 , listSize(src.output()));

		r25.setter().consume(25);
		assertEquals(3 , listSize(filtered));
		assertEquals(4 , listSize(src.output()));
	}
	
	private ReactivePredicate<Signal<Integer>> reactivePredicate() {
		return new ReactivePredicate<Signal<Integer>>() {
			@Override	 public Signal<Boolean> evaluate(Signal<Integer> signalValue) {
				return my(Signals.class).adapt(signalValue, new Functor<Integer, Boolean>(){
					@Override public Boolean evaluate(Integer integerValue) {
						return integerValue > 30;
		}});}};
	}	
	
	private int listSize(SetSignal<Signal<Integer>> sortedList) {
		return sortedList.size().currentValue().intValue();
	}
	
	private Signal<Integer> signal(Register<Integer> register) {
		return register.output();
	}	
}
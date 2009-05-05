package sneer.pulp.reactive.collections.listfilter.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.hardware.cpu.lang.Predicate;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.pulp.reactive.collections.listfilter.ListFilter;
import sneer.pulp.reactive.signalchooser.SignalChooser;

public class ListFilterTest extends BrickTest {
	
	private final ListFilter _subject = my(ListFilter.class);

	private final Signal<Integer> _10 = my(Signals.class).constant(10);
	private final Signal<Integer> _20 = my(Signals.class).constant(20);
	private final Signal<Integer> _30 = my(Signals.class).constant(30);
	private final Signal<Integer> _40 = my(Signals.class).constant(40);
	private final Signal<Integer> _50 = my(Signals.class).constant(50);

	private final SignalChooser<Signal<Integer>> _chooser = new SignalChooser<Signal<Integer>>(){ @Override public Signal<?>[] signalsToReceiveFrom(Signal<Integer> element) {
		return new Signal<?>[]{element};
	}};
	
	@Test
	public void addRemoveTest() {
		ListRegister<Signal<Integer>> src = my(ReactiveCollections.class).newListRegister();
		ListSignal<Signal<Integer>> filtered = _subject.filter(src.output(), integerFilter(), _chooser);

		src.add(_10);
		src.addAt(0, _20);
		src.add(_30);
		src.add(_40);
		
		assertEquals(1 , listSize(filtered));
		assertEquals(4 , listSize(src.output()));
		
		src.addAt(0, _50);
		assertEquals(2 , listSize(filtered));
		assertEquals(5 , listSize(src.output()));
	
		src.add(_50);
		assertEquals(3 , listSize(filtered));		
		assertEquals(6 , listSize(src.output()));
		
		src.remove(_50);
		assertEquals(2 , listSize(filtered));		
		assertEquals(5 , listSize(src.output()));
}
	
	@Test
	public void changeValueTest() {
		ListRegister<Signal<Integer>> src = my(ReactiveCollections.class).newListRegister();
		ListSignal<Signal<Integer>> filtered = _subject.filter(src.output(), integerFilter(), _chooser);
		
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
		src.addAt(0, s25);
		src.add(s35);
		assertEquals(1 , listSize(filtered));
		assertEquals(5 , listSize(src.output()));
		
		src.add(s45);
		assertEquals(2 , listSize(filtered));
		assertEquals(6 , listSize(src.output()));
		
		r15.setter().consume(150);
		assertEquals(3 , listSize(filtered));
		assertEquals(6 , listSize(src.output()));
		
		r25.setter().consume(250);
		assertEquals(6 , listSize(filtered));
		assertEquals(6 , listSize(src.output()));

		r25.setter().consume(25);
		assertEquals(3 , listSize(filtered));
		assertEquals(6 , listSize(src.output()));
	}
	
	private Predicate<Signal<Integer>> integerFilter() {
		return new Predicate<Signal<Integer>>() {  @Override public boolean evaluate(Signal<Integer> element) {
			return element.currentValue()>30;
		}};
	}	
	
	private int listSize(ListSignal<Signal<Integer>> sortedList) {
		return sortedList.size().currentValue().intValue();
	}
	
	private Signal<Integer> signal(Register<Integer> register) {
		return register.output();
	}	
}
package snapps.wind.impl;

import java.util.Comparator;

import snapps.wind.Shout;
import snapps.wind.Wind;
import sneer.kernel.container.Inject;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.listsorter.ListSorter.SignalChooser;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class WindImpl implements Wind, Omnivore<Shout> {

	@Inject
	static private TupleSpace _environment;
	
	@Inject
	static private ListSorter _sorter;
	
	private final SignalChooser<Shout> _chooser;
	private final ListSignal<Shout> _sortedShouts;
	private final Comparator<Shout> _comparator;
	private final ListRegister<Shout> _shoutsHeard = new ListRegisterImpl<Shout>();

	WindImpl(){
		_environment.addSubscription(Shout.class, this);
		_environment.keep(Shout.class);
		
		_chooser = new SignalChooser<Shout>(){ @Override public Signal<?>[] signalsToReceiveFrom(Shout element) {
			return new Signal<?>[]{new Constant<Long>(element.publicationTime())};
		}};
		
		_comparator = new Comparator<Shout>(){ @Override public int compare(Shout o1, Shout o2) {
			return (int) (o2.publicationTime() - o1.publicationTime());
		}};
		
		_sortedShouts = _sorter.sort(_shoutsHeard.output(), _comparator, _chooser);
	}

	@Override
	public ListSignal<Shout> shoutsHeard() {
		return _sortedShouts;
	}

	@Override
	public void consume(Shout shout) {
		_shoutsHeard.adder().consume(shout);
	}

	@Override
	public Omnivore<String> megaphone() {
		return new Omnivore<String>(){ @Override public void consume(String phrase) {
			shout(phrase);
		}};
	}

	private void shout(String phrase) {
		_environment.publish(new Shout(phrase));
	}
}
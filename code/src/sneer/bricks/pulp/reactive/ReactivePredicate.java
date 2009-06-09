package sneer.bricks.pulp.reactive;

import static sneer.foundation.commons.environments.Environments.my;

public interface ReactivePredicate<T> {

	Signal<Boolean> evaluate(T value);

	ReactivePredicate<Object> TRUE = new ReactivePredicate<Object>() { @Override public Signal<Boolean> evaluate(Object ignored) { return my(Signals.class).constant(true); }};
	ReactivePredicate<Object> FALSE = new ReactivePredicate<Object>() { @Override public Signal<Boolean> evaluate(Object ignored) {  return my(Signals.class).constant(false); }};

}

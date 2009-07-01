package sneer.bricks.pulp.distribution.filtering.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.pulp.distribution.filtering.TupleFilterManager;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.Predicate;

class TupleFilterManagerImpl implements TupleFilterManager {

	private final Map<Class<? extends Tuple>, Predicate<?>> _censorsByTupleType = new HashMap<Class<? extends Tuple>, Predicate<?>>();

	@Override
	public void block(Class<? extends Tuple> tupleType) {
		setCensor(tupleType, Predicate.FALSE);
	}

	@Override
	public <T extends Tuple> void setCensor(Class<T> tupleType,	Predicate<? super T> censor) {
		if (censorFor(tupleType) != null) throw new IllegalStateException("Censor for " + tupleType + " already set. Maybe for some superclass of " + tupleType);

		_censorsByTupleType.put(tupleType, censor);
	}
	
	@Override
	public boolean canBePublished(Tuple tuple) {
		Predicate<Tuple> censor = (Predicate<Tuple>)censorFor(tuple.getClass());
		return censor == null
			? true
			: censor.evaluate(tuple);
	}

	private Predicate<?> censorFor(Class<? extends Tuple> tupleType) {
		Predicate<?> result = _censorsByTupleType.get(tupleType);
		if (result != null) return result;
		
		if (tupleType == Tuple.class) return null;
		
		return censorFor((Class<Tuple>)tupleType.getSuperclass());
	}

}
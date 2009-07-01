package sneer.bricks.pulp.distribution.filtering;

import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.Predicate;

@Brick
public interface TupleFilterManager {

	void block(Class<? extends Tuple> tupleType);
	
	/** @param censor evaluates to true if the given tuple can be published to contacts, false otherwise. */
	<T extends Tuple> void setCensor(Class<T> tupleType, Predicate<? super T> censor);

	boolean canBePublished(Tuple tuple);

}

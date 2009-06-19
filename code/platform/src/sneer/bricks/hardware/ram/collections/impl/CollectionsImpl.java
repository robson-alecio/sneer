package sneer.bricks.hardware.ram.collections.impl;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import sneer.bricks.hardware.ram.collections.Collections;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.Predicate;

public class CollectionsImpl implements Collections{

	@Override
	public <I, O> Collection<O> map(Collection<I> inputCollection, Functor<? super I, ? extends O> functor) {
		return CollectionUtils.collect(inputCollection, adapt(functor));
	}

	@Override
	public <T> Collection<T> filter(Collection<T> inputCollection, Predicate<T> predicate) {
		return CollectionUtils.select(inputCollection, adapt(predicate));
	}

	private <I, O> Transformer adapt( final Functor<? super I, ? extends O> delegate) {
		return new Transformer(){ @Override public Object transform(Object input) {
			return delegate.evaluate((I) input);
		}};
	}

	private <T> org.apache.commons.collections.Predicate adapt( final Predicate<T> delegate) {
		return new org.apache.commons.collections.Predicate(){ @Override public boolean evaluate(Object value) {
			return delegate.evaluate((T)value);
		}};
	}

}

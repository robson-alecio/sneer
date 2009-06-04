package spikes.wheel.reactive.impl;

/** Avoid garbage collection of another object while the wrapped signal has references. Useful for implementing gates. */
public class AbstractOwnerReference<T> {

	protected final Object _referenceToAvoidGC;

	public AbstractOwnerReference(Object objectToSaveFromGC) {
		_referenceToAvoidGC = objectToSaveFromGC;
	}
}
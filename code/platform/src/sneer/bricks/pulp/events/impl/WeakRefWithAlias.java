package sneer.bricks.pulp.events.impl;


class WeakRefWithAlias<T> extends java.lang.ref.WeakReference<T> {
	
	final String _alias;
	
	WeakRefWithAlias(T object) {
		super(object);
		_alias = aliasFor(object);
	}
	
	@Override
	public String toString() {
		return _alias;
	}
	
	@Override
	public boolean equals(Object other) {
		return ((WeakRefWithAlias<T>) other).get() == get();
	}
	
	@Override
	public int hashCode() {
		T payload = get();
		return payload.hashCode();
	}

	static String aliasFor(Object object) {
		return "" + object.getClass() + "@" + System.identityHashCode(object);
		//USE object.toString() HERE ONLY FORDEBUGGING. DO NOT COMMIT.
	}

}
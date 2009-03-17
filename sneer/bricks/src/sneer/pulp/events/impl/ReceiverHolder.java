package sneer.pulp.events.impl;

import wheel.lang.Types;

class ReceiverHolder<T> extends java.lang.ref.WeakReference<T> {
	
	final String _alias;
	
	ReceiverHolder(T receiver) {
		this(receiver, getAlias(receiver));
	}
	
	ReceiverHolder(T receiver, String alias) {
		super(receiver);
		_alias = alias;
	}
	
	@Override
	public String toString() {
		return _alias;
	}
	
	@Override
	public boolean equals(Object obj) {
		final ReceiverHolder<T> holder = Types.cast(obj);
		return holder.get() == get();
	}
	
	static String getAlias(Object object) {
		return "" + object.getClass() + "@" + System.identityHashCode(object);
		//USE object.toString() HERE ONLY FORDEBUGGING. DO NOT COMMIT.
	}

}
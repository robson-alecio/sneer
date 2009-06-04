package sneer.hardware.cpu.lang.impl;

import org.apache.commons.lang.ArrayUtils;

import sneer.hardware.cpu.lang.Lang;

class LangImpl implements Lang {

	private final Arrays _arrays = new ArraysImpl();
	
	@Override	public Arrays arrays() { return _arrays; }

}

class ArraysImpl implements Lang.Arrays {
	@Override	public void reverse(Object[] array) { ArrayUtils.reverse(array); }
}
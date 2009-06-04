package sneer.hardware.cpu.apachecommonslang.impl;

import org.apache.commons.lang.ArrayUtils;

import sneer.hardware.cpu.apachecommonslang.Arrays;

class ArraysImpl implements Arrays {

	@Override
	public void reverse(Object[] array) {
		ArrayUtils.reverse(array);
	}

}

package sneer.hardware.cpu.apachecommonslang.impl;

import sneer.hardware.cpu.apachecommonslang.Arrays;
import sneer.hardware.cpu.apachecommonslang.Lang;

class LangImpl implements Lang {

	private final Arrays _arrays = new ArraysImpl();
	
	@Override
	public Arrays arrays() {
		return _arrays;
	}

}

package sneer.lego.tests.impl;

import sneer.lego.tests.MakeMeSerializable;
import sneer.lego.tests.SomeClass;

public class MakeMeSerializableImpl implements MakeMeSerializable {

	@Override
	public SomeClass someClass() {
		return new SomeClass(){};
	}

}

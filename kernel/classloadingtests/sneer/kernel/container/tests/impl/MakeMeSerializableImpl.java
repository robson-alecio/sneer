package sneer.kernel.container.tests.impl;

import sneer.kernel.container.tests.MakeMeSerializable;
import sneer.kernel.container.tests.SomeClass;

public class MakeMeSerializableImpl implements MakeMeSerializable {

	@Override
	public SomeClass someClass() {
		return new SomeClass(){};
	}

}

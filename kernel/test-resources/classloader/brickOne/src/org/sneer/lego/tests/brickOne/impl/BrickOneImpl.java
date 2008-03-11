package org.sneer.lego.tests.brickOne.impl;

import org.sneer.lego.tests.brickOne.BrickOne;
import org.sneer.lego.tests.brickOne.SomeValue;

public class BrickOneImpl implements BrickOne
{
	public SomeValue doAnything() {
		return new SomeValueImpl();
	}
}

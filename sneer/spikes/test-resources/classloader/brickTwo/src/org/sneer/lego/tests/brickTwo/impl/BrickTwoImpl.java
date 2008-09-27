package org.sneer.lego.tests.brickTwo.impl;

import org.sneer.lego.tests.brickOne.BrickOne;
import org.sneer.lego.tests.brickTwo.BrickTwo;

import sneer.lego.Brick;


public class BrickTwoImpl implements BrickTwo 
{
	@Brick
	private BrickOne _one;
	
	@Override
	public String doSomething() {
		return _one.doAnything().exec() + " brick2";
	}
	
}

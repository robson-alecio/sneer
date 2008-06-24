package sneer.bricks.mesh.impl;

import java.lang.reflect.Method;

import sneer.lego.Brick;
import wheel.lang.exceptions.NotImplementedYet;


class BrickInvocation {

	private final Class<? extends Brick> _brickInterface;
	private final Method _method;
	private final Object[] _args;

	public BrickInvocation(Class<? extends Brick> brickInterface, Method method, Object[] args) {
		_brickInterface = brickInterface;
		_method = method;
		_args = args;
		
		_brickInterface.toString();
		_method.toString();
		_args.toString();
		
		throw new NotImplementedYet();
	}

}

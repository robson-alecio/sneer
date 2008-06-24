package sneer.bricks.mesh.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import sneer.lego.Brick;
import wheel.lang.Casts;


class BrickProxy implements InvocationHandler {

	static <B extends Brick> B createFor(Class<B> brickInterface, BrickInvocator intermediary) {
		return Casts.uncheckedGenericCast(
			java.lang.reflect.Proxy.newProxyInstance(
				BrickProxy.class.getClassLoader(),
				new Class<?>[]{ brickInterface },
				new BrickProxy(brickInterface, intermediary)));
	}
	
	private <B extends Brick> BrickProxy(Class<B> brickInterface, BrickInvocator intermediary) {
		_brickInterface = brickInterface;
		_intermediary = intermediary;
	}

	private final Class<? extends Brick> _brickInterface;
	private final BrickInvocator _intermediary;


	@Override
	public Object invoke(Object impliedProxy, Method method, Object[] args)	throws Throwable {
		return _intermediary.invoke(
			new BrickInvocation(_brickInterface, method, args));
	}
	
}

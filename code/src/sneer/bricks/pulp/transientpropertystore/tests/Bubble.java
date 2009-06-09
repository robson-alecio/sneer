package sneer.bricks.pulp.transientpropertystore.tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

public class Bubble {

	static private File _persistenceDirectory;
	static private Prevayler _prevayler;
	static private boolean _insidePrevayler = false;


	public static <STATE_MACHINE> STATE_MACHINE wrapStateMachine(Class<?> brick, Object brickImpl, File persistenceDirectory) {
		return (STATE_MACHINE)wrap(brick, brickImpl, persistenceDirectory);
	}

	private static <T> T wrap(Class<?> brick, Object brickImpl, File persistenceDirectory) {
		_persistenceDirectory = persistenceDirectory;
		InvocationHandler handler = new Bubble(brick, brickImpl).handler();
		return (T) Proxy.newProxyInstance(brick.getClassLoader(), interfaces(brick), handler);
	}

	private static Class<?>[] interfaces(Class<?> brick) {
		return new Class[]{brick, BrickProxy.class};
	}

	private Bubble(Class<?> brick, Object brickImpl) {
		_brick = brick;
		_brickImpl = brickImpl;
	}
	
	private Class<?> _brick;
	private final Object _brickImpl;
	
	
	private InvocationHandler handler() {
		return new InvocationHandler() { @Override public Object invoke(Object proxyImplied, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("brickImpl"))
				return _brickImpl;
			
			if (_insidePrevayler)
				return Invocation.invoke(_brickImpl,  method, args);
			
			_insidePrevayler = true;
			try {
				return prevayler().execute(new Invocation(_brick, method, args));
			} finally {
				_insidePrevayler = false;
			}
		}};
	}
	private Prevayler prevayler() {
		if (_prevayler == null) _prevayler = newPrevayler();
		return _prevayler;
	}


	private Prevayler newPrevayler() {
		PrevaylerFactory factory = prevaylerFactory();

		try {
			return factory.create();
		} catch (IOException e) {
			throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}

	}

	private PrevaylerFactory prevaylerFactory() {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem("Ignored");
		factory.configurePrevalenceDirectory(_persistenceDirectory.getAbsolutePath());
		factory.configureJournalSerializer(new XStreamSerializer());
		factory.configureTransactionFiltering(false);
		return factory;
	}

	public static void close() throws IOException {
		try {
			_prevayler.close();
		} finally {
			_prevayler = null;
		}
	}

}

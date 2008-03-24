package spikes.klaus.remotesignals;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Register;
import wheel.reactive.impl.SourceImpl;


public class RemoteSignallingFacade {

	private final Omnivore<Object> _remoteReceiver;
	private final Signal<Object> _lastSentValue;
	private Object _proxy;

	public RemoteSignallingFacade(Omnivore<Object> remoteReceiver, Signal<Object> lastSentValue) {
		_remoteReceiver = remoteReceiver;
		_lastSentValue = lastSentValue;
		
		_lastSentValue.addReceiver(new Omnivore<Object>(){
			public void consume(Object valueObject) {
				_lastSentValue.removeReceiver(this);

				Class<?> clazz;
				try {
					clazz = Class.forName((String)_lastSentValue.currentValue());
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
				Class<?>[] interfaces = clazz.getInterfaces();
				
				_proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, createHandler(_lastSentValue));
			}});
	}

	public void sendSignals(Object object) {
		Class<?> clazz = object.getClass();
		_remoteReceiver.consume(object.getClass().getName());

		Method method = clazz.getMethods()[0];
		Signal<Object> signal = null;
		try {
			signal = Casts.uncheckedGenericCast(method.invoke(object, new Object[]{}));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		signal.addReceiver(retramsmitter(_remoteReceiver));
		
	}

	private Omnivore<Object> retramsmitter(final Omnivore<Object> remoteReceiver) {
		return new Omnivore<Object>(){public void consume(Object valueObject) {
			remoteReceiver.consume(valueObject);
		}};
	}

	
	
	
	
	
	public Object getProxyForRemoteObject() {
		return _proxy;
	}

	private InvocationHandler createHandler(final Signal<Object> lastReceivedValue) {
		return new InvocationHandler() {
			Register<Object> _source = new SourceImpl<Object>(null);
			
			{
				lastReceivedValue.addReceiver(receiver());
			}
			
			private Omnivore<Object> receiver() {
				return new Omnivore<Object>(){ 

				public void consume(Object valueObject) {
					_source.setter().consume(valueObject);
				}};
			}

			public Object invoke(Object proxyIgnored, Method methodIgnored, Object[] args) throws Throwable {
		
				return _source.output();
			}
		};
	}



	


}

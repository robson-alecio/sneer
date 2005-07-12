package sneer.remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.prevayler.foundation.Cool;

import sneer.life.LifeView;

class LifeViewProxy implements InvocationHandler {

	private final QueryExecuter _queryExecuter;
	private Date _lastSighting;
	private Map<String, Object> _methodInvocationCache = new HashMap<String, Object>(); //FIXME: Argument to the method is not being taken into account. MessagesSentTo(nickname) for example.
	private Map<String, LifeView> _contactCache = new HashMap<String, LifeView>();

	public static LifeView createBackedBy(QueryExecuter queryExecuter) {
		LifeView lifeView = (LifeView)Proxy.newProxyInstance(LifeView.class.getClassLoader(), new Class[] { LifeView.class }, new LifeViewProxy(queryExecuter));
		Cool.startDaemon(pinger(lifeView));
		return lifeView;
	}

	private LifeViewProxy(QueryExecuter queryExecuter) {
		_queryExecuter = queryExecuter;
	}

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
		if (methodName.equals("hashCode"))
            return hashCode();
        if (methodName.equals("toString"))
            return "LifeView proxy " + hashCode();
        
        if (methodName.equals("contact")) //FIXME: Check the LifeView interface through reflection to guarantee this method has not been renamed.
        	return routingProxy((String)args[0]);
        
        if (methodName.equals("lastSighting")) //FIXME: Check the LifeView interface through reflection to guarantee this method has not been renamed.
            return _lastSighting;
        
        try {
			Object result = _queryExecuter.execute(new MethodInvocation(method, args, LifeView.CALLING_NICKNAME.get()));
			updateCache(methodName, result);
			return result;
		} catch (RuntimeException e) {
			return _methodInvocationCache.get(methodName);
		}
        
    }

	private void updateCache(String methodName, Object result) {
		_methodInvocationCache.put(methodName, result);
		_lastSighting = new Date(); //FIXME: In fact this should update all values in the LifeView at once, to avoid inconsistency among different values.
	}

	private LifeView routingProxy(String nickname) {
    	LifeView cached = _contactCache.get(nickname);
    	if (cached != null) return cached;

    	LifeView proxy = createBackedBy(new QueryRouter(nickname, _queryExecuter));
		_contactCache.put(nickname, proxy);
    	return proxy;
	}
	
	static private Runnable pinger(final LifeView lifeView) {
		return new Runnable() {
			public void run() {
				while (true) {
					// force a remote query to be executed (see execute method above)
					lifeView.name();
//					Cool.sleep(1000 * 60); //TODO Optimize - Sleep again instead of pinging, if this connection was used recently.
					Cool.sleep(1000 * 4);
				}
			}
		};
	}

}

package sneer.remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sneer.life.LifeView;

class LifeViewProxy implements InvocationHandler {

	private final QueryExecuter _queryExecuter;
	private Date _lastSighting;
	private Map<String,Object> _methodInvocationCache = new HashMap<String, Object>();

	public static LifeView createBackedBy(QueryExecuter queryExecuter) {
		return (LifeView)Proxy.newProxyInstance(LifeView.class.getClassLoader(), new Class[] { LifeView.class }, new LifeViewProxy(queryExecuter));
	}

	public LifeViewProxy(QueryExecuter queryExecuter) {
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
		LifeView candidate = createBackedBy(new QueryRouter(nickname, _queryExecuter));
		if (isNavigationBroken(candidate)) return null;
		return candidate;
	}

	private boolean isNavigationBroken(LifeView candidate) {
		return candidate.name() == null;
	}

}

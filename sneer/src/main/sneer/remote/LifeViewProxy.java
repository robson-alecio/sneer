package sneer.remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sneer.life.LifeView;

class LifeViewProxy implements InvocationHandler {

	private final QueryExecuter _queryExecuter;

	public static LifeView createBackedBy(QueryExecuter queryExecuter) {
		return (LifeView)Proxy.newProxyInstance(LifeView.class.getClassLoader(), new Class[] { LifeView.class }, new LifeViewProxy(queryExecuter));
	}

	public LifeViewProxy(QueryExecuter queryExecuter) {
		_queryExecuter = queryExecuter;
	}

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("hashCode"))
            return hashCode();
        if (method.getName().equals("toString"))
            return "LifeView proxy " + hashCode();
        if (method.getName().equals("contact"))
            return routingProxy((String)args[0]);
        return _queryExecuter.execute(new MethodInvocation(method, args, LifeView.CALLING_NICKNAME.get()));
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

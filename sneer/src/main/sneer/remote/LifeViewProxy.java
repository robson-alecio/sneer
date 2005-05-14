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
        return _queryExecuter.execute(new MethodInvocation(method, args));
    }

	private LifeView routingProxy(String nickname) {
		return createBackedBy(new QueryRouter(nickname, _queryExecuter));
	}

}

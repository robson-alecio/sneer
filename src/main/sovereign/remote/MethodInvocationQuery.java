//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sovereign.remote;

import java.lang.reflect.Method;

import org.prevayler.foundation.Cool;

import sovereign.LifeView;

public class MethodInvocationQuery implements Query {

    private final String _methodName;
    private final Class[] _argTypes;
    private final Object[] _args;

    public MethodInvocationQuery(Method method, Object[] args) {
        _methodName = method.getName();
        _argTypes = method.getParameterTypes();
        _args = args;
    }

    public Object executeOn(LifeView life) {
        try {
            Method method = LifeView.class.getMethod(_methodName, _argTypes);
            return method.invoke(life, _args);
        } catch (Exception e) {
            Cool.unexpected(e);
            return null;
        }
    }

}

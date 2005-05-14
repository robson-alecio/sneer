//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.prevayler.foundation.Cool;

import sneer.life.LifeView;
import sneer.life.NoneOfYourBusiness;


public class MethodInvocation implements Query {

	private static final long serialVersionUID = 1L;
	
	private final String _methodName;
    private final Class[] _argTypes;
    private final Object[] _args;

    public MethodInvocation(Method method, Object[] args) {
        _methodName = method.getName();
        _argTypes = method.getParameterTypes();
        _args = args;
    }

    public Object executeOn(LifeView life) {
        try {
        	try {
		        Method method = LifeView.class.getMethod(_methodName, _argTypes);
				return method.invoke(life, _args);
	        } catch (InvocationTargetException e) {
				if (e.getCause() instanceof NoneOfYourBusiness) return e.getCause();
				throw e;
	        }
        } catch (Exception e) {
            Cool.unexpected(e);
            return null;
        }
    }

}

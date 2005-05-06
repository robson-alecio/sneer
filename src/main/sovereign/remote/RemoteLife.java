//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira, Fabio Roger Manera.

package sovereign.remote;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.prevayler.foundation.network.ObjectSocket;

import sovereign.LifeView;
import sovereign.NoneOfYourBusiness;

public class RemoteLife implements InvocationHandler {

    private final ObjectSocket _socket;
    private final Map<String, LifeView> _remoteContactsByNickname = new HashMap<String, LifeView>();
	
	
    static public LifeView createWith(ObjectSocket socket, String ticket) {
        return (LifeView)Proxy.newProxyInstance(LifeView.class.getClassLoader(), new Class[] { LifeView.class }, new RemoteLife(socket, ticket));
    }
    
	private RemoteLife(ObjectSocket socket, String ticket) {
		_socket = socket;
		if (ticket != null) {
			try {
//				 FIXME: I dont think it is a good design to mix LifeServer with RemoteLife stuff..
				_socket.writeObject(LifeServer.REQUEST_FOR_SERVER); 
				_socket.writeObject(ticket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private LifeView remoteContact(LifeView contact, String nickname) {
	    LifeView result = _remoteContactsByNickname.get(nickname); 
	    if (result != null) return result;
	    
	    if (!contact.nicknames().contains(nickname)) return null;
        
	    result = createWith(new QueryRouter(_socket, nickname), null);
        _remoteContactsByNickname.put(nickname, result);
        return result;
	}

    private Object executeRemote(Query query) {
		Object result;
		try {
            _socket.writeObject(query);
			result = _socket.readObject();
		} catch (Exception x) {
			x.printStackTrace();
			return null;
			//TODO Exception handling.
		}
		if (result instanceof NoneOfYourBusiness) throw new NoneOfYourBusiness((NoneOfYourBusiness)result);
		return result;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("hashCode"))
            return hashCode();
        if (method.getName().equals("toString"))
            return toString();
        if (method.getName().equals("contact"))
            return remoteContact((LifeView)proxy, (String)args[0]);
        return executeRemote(new MethodInvocationQuery(method, args));
    }
}

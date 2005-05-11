//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.remote;

import java.io.IOException;

import org.prevayler.foundation.network.ObjectSocket;

public class QueryRouter implements ObjectSocket {

    private final ObjectSocket _delegate;
    private final String _nickname;

    public QueryRouter(ObjectSocket socket, String nickname) {
        _delegate = socket;
        _nickname = nickname;
    }

	public void writeObject(Object object) throws IOException {

		_delegate.writeObject(new RoutedQuery(_nickname,(Query)object));
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return _delegate.readObject();
    }

    public void close() throws IOException {
        _delegate.close();
    }

 
}

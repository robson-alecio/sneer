//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.remote;

import sneer.life.LifeView;

public class RoutedQuery<T> implements Query<T> {
	
	private static final long serialVersionUID = 1L;

	private String _nickname;
	private Query<T> _delegate;

	public RoutedQuery(String nickname, Query<T> query) {
		_nickname = nickname;
		_delegate = query;
	}

	public T executeOn(LifeView life) {
		LifeView destination = life.contact(_nickname);
		if (destination == null)
		    return null;
        return _delegate.executeOn(destination);
	}

}

//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.old.remote;

import java.io.IOException;


public class QueryRouter<T> implements QueryExecuter {

    private final String _nickname;
    private final QueryExecuter _delegate;

	public QueryRouter(String nickname, QueryExecuter queryExecuter) {
        _delegate = queryExecuter;
        _nickname = nickname;
	}

	@SuppressWarnings("unchecked")
	public <U> U execute(Query<U> query) throws IOException {
		return _delegate.execute(
			(query instanceof Indian)
			? (Query<U>)new ForwardingIndian(_nickname, (Indian)query)
			: new RoutedQuery<U>(_nickname, query));
    } 
}

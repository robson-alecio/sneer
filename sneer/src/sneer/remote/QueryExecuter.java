package sneer.remote;

import java.io.IOException;

interface QueryExecuter {

	<T> T execute(Query<T> query) throws IOException;

}

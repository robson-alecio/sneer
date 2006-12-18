package sneer.old.remote;

import java.io.IOException;

interface QueryExecuter {

	<T> T execute(Query<T> query) throws IOException;

}

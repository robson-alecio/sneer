package sneer.remote;

interface QueryExecuter {

	<T> T execute(Query<T> query);

}

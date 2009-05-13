package sneer.pulp.log;

public interface Filter {

	boolean acceptLog(String message);

}

package sneer.pulp.prevalence.journal;

public interface Journal {

	void append(Object entry);
	Iterable<Object> allEntries();
	
}

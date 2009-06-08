// Author: Klaus Wuestefeld
// I hereby place the contents of this file in the public domain.

package spikes.klaus.prevalence;

public interface Transactor {

	Object businessSystem();

	void execute(Transaction transaction);

}
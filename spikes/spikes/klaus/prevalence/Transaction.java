// Author: Klaus Wuestefeld
// I hereby place the contents of this file in the public domain.

package spikes.klaus.prevalence;

public interface Transaction {

	void executeOn(Object businessSystem);

}
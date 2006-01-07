package transactions;

import prevalence.Transaction;
import sneer.business.SneerBusiness;

public class Birth implements Transaction {

	private final String _name;

	public Birth(String name) {
		_name = name;
	}
	
	public void executeOn(Object businessSystem) {
		((SneerBusiness)businessSystem).foster(_name);
	}

	private static final long serialVersionUID = 1L;
}

package sneer.core.transactions;

import prevalence.Transaction;
import sneer.core.SneerBusiness;

public class NameChange implements Transaction {

	private final String _oldName;
	private final String _newName;

	public NameChange(String oldName, String newName) {
		_oldName = oldName;
		_newName = newName;
	}
	
	public void executeOn(Object businessSystem) {
		((SneerBusiness)businessSystem).changeName(_oldName, _newName);
	}

}

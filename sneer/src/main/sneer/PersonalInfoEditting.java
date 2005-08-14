package sneer;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.Sneer.User;
import sneer.life.LifeView;

public class PersonalInfoEditting implements Transaction {

	private final String _name;
	private final String _thoughtOfTheDay;

	public PersonalInfoEditting(User _user, LifeView life) {
		_name = _user.confirmName(life.name());
		_thoughtOfTheDay = _user.thoughtOfTheDay(life.thoughtOfTheDay());
	}

	public void executeOn(Object home, Date ignored) {
		((Home)home).life().name(_name);
		((Home)home).life().thoughtOfTheDay(_thoughtOfTheDay);
	}

	private static final long serialVersionUID = 1L;

}

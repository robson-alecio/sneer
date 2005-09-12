package sneer;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.Sneer.User;
import sneer.life.JpgImage;
import sneer.life.Life;
import sneer.life.LifeView;

public class PersonalInfoEditting implements Transaction {

	private final String _name;
	private final String _thoughtOfTheDay;
	private final boolean _hasPictureChanged;
	private final JpgImage _newPicture;
	

	public PersonalInfoEditting(User _user, LifeView life) {
		_name = _user.confirmName(life.name());
		_thoughtOfTheDay = _user.thoughtOfTheDay(life.thoughtOfTheDay().currentValue());
		_newPicture = confirmPicture(_user, life);
		_hasPictureChanged = _newPicture != null;
	}

	private JpgImage confirmPicture(User _user, LifeView life) {
		JpgImage currentPicture = life.picture();
		JpgImage result = _user.confirmPicture(currentPicture);
		return result == currentPicture	? null : result;
	}

	public void executeOn(Object home, Date ignored) {
		Life life = ((Home)home).life();
		
		life.name(_name);
		life.thoughtOfTheDay(_thoughtOfTheDay);
		
		if (_hasPictureChanged) life.picture(_newPicture);
	}

	private static final long serialVersionUID = 1L;

}

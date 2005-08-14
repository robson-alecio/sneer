package sneer.remote;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import sneer.life.JpgImage;
import sneer.life.LifeView;

class LifeSighting implements LifeView, Serializable {

	private final String _name;
	private final String _thoughtOfTheDay;
	private JpgImage _picture;

	private final String _profile;
	private final String _contactInfo;
	
	private final Set<String> _nicknames;

	private final List<String> _publicMessages;

	private final Date _creation;

	public LifeSighting(LifeView lifeView) {
		if (lifeView.lastSightingDate() == null) {
			_name = null;
			_thoughtOfTheDay = null;
			_picture = null;
			_profile = null;
			_contactInfo = null;
			_nicknames = null;
			_publicMessages = null;
			_creation = null;
		} else {
			_name = lifeView.name();
			_thoughtOfTheDay = lifeView.thoughtOfTheDay();
			_profile = lifeView.profile();
			_picture = lifeView.picture();
			_contactInfo = lifeView.contactInfo();
			_nicknames = lifeView.nicknames();
			_publicMessages = lifeView.publicMessages();
			_creation = new Date();
		}
	}

	public String name() {
		return _name;
	}

	public String thoughtOfTheDay() {
		return _thoughtOfTheDay;
	}

	public Set<String> nicknames() {
		return _nicknames;
	}

	public LifeView contact(String nickname) {
		throw new UnsupportedOperationException();
	}

	public String profile() {
		return _profile;
	}

	public String contactInfo() {
		return _contactInfo;
	}

	public List<String> messagesSentTo(String contact) {
		contact.toString();
		throw new RuntimeException("To be implemented");
	}

	public List<String> publicMessages() {
		return _publicMessages;
	}

	public Date lastSightingDate() {
		return _creation;
	}

	public JpgImage picture() {
		return _picture;
	}

	private static final long serialVersionUID = 1L;

}

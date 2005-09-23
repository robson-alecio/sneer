package sneer.remote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sneer.life.JpgImage;
import sneer.life.LifeView;

class LifeCache implements Serializable {

	private final String _name;
	private final String _thoughtOfTheDay;
	private JpgImage _picture;

	private final String _profile;
	private final String _contactInfo;
	
	private final Set<String> _nicknames;

	private Map<String, Object> _things;

	private final Date _creation;

	
	public LifeCache(LifeView lifeView) {
		if (lifeView == null || lifeView.lastSightingDate() == null) {
			_name = null;
			_thoughtOfTheDay = null;
			_picture = null;
			_profile = null;
			_contactInfo = null;
			_nicknames = null;
			_things = null;
			_creation = null;
		} else {
			_name = lifeView.name();
			_thoughtOfTheDay = lifeView.thoughtOfTheDay().currentValue();
			_profile = lifeView.profile();
			_picture = lifeView.picture();
			_contactInfo = lifeView.contactInfo();
			_nicknames = lifeView.nicknames();
			_things = lifeView.things();
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

	public String profile() {
		return _profile;
	}

	public String contactInfo() {
		return _contactInfo;
	}

	public List<String> messagesSentToMe() {
		return new ArrayList<String>();
		//FIXME To be implemented.
	}

	public Date lastSightingDate() {
		return _creation;
	}

	public JpgImage picture() {
		return _picture;
	}

	public Object thing(String name) {
		return _things.get(name);
	}
	
	public Map<String, Object> things() {
		return _things;
	}

	private LifeCache() {  //Required by XStream to run on JVMs other than Sun's.
		this(null);
	}

	private static final long serialVersionUID = 1L;

}

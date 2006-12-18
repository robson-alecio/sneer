package sneer.old.remote;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import sneer.old.life.LifeView;

class LifeCache implements Serializable {

	private final String _contactInfo;
	
	private Map<String, Object> _things;

	private final Date _creation;

	
	public LifeCache(LifeView lifeView) {
		if (lifeView == null || lifeView.lastSightingDate() == null) {
			_contactInfo = null;
			_things = null;
			_creation = null;
		} else {
			_contactInfo = lifeView.contactInfo();
			_things = lifeView.things();
			_creation = new Date();
		}
	}

	public String contactInfo() {
		return _contactInfo;
	}

	public Date lastSightingDate() {
		return _creation;
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

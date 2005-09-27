//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz.

package sneer.life;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.SourceImpl;
import wheelexperiments.reactive.signals.SetSignal;
import wheelexperiments.reactive.signals.SetSource;


public class LifeImpl implements Life, Serializable {

	private String _name;
	private final Source<String> _thoughtOfTheDay = new SourceImpl<String>();
	private JpgImage _pictureOld;
	private final Source<JpgImage> _picture = new SourceImpl<JpgImage>();

	private String _profile;
    private String _contactInfo;

	private SetSource<String> _nicknames = new SetSource<String>();
	private final Map<String, LifeView> _contactsByNickname = new HashMap<String, LifeView>();
	private final Map<String, Object> _thingsByName = new HashMap<String, Object>();


	public LifeImpl(String name) {
		name(name);
	}
	
	public void name(String newName) {
		_name = newName;
	}

	public void thoughtOfTheDay(String thought) {
		_thoughtOfTheDay.supply(thought);
	}
	
	public SetSignal<String> nicknames() {
		return _nicknames;
	}

	public void giveSomebodyANickname(LifeView somebody, String nickname) throws IllegalArgumentException {
		if (somebody == null || nickname == null || nickname.equals("")) throw new IllegalArgumentException();
		if (_contactsByNickname.containsKey(nickname)) throw new IllegalArgumentException();
		
		_contactsByNickname.put(nickname, somebody);
		_nicknames.add(nickname);
	}

	public void changeNickname(String oldNickname, String newNickname) throws IllegalArgumentException {
		LifeView lifeView = contact(oldNickname);
		giveSomebodyANickname(lifeView, newNickname);
		forgetNickname(oldNickname);
	}

	public void forgetNickname(String oldNickname) {
		_contactsByNickname.remove(oldNickname);
		_nicknames.remove(oldNickname);
	}

	public String name() {
		return _name;
	}

	public Signal<String> thoughtOfTheDay() {
		return _thoughtOfTheDay;
	}

	public LifeView contact(String nickname) {
		return _contactsByNickname.get(nickname);
	}


    public void profile(String profile) {
        _profile = profile;
    }

    public String profile() {
        return _profile;
    }

    public void contactInfo(String contactInfo) {
        _contactInfo = contactInfo;
    }

    public String contactInfo() {
        return _contactInfo;
    }

	public List<String> messagesSentToMe() {
		// FIXME Auto-generated method stub
		return null;
	}
    
	public Date lastSightingDate() {
		return new Date(); //A local LifeView is always up-to-date.
	}

	public JpgImage pictureOld() {
		return _pictureOld;
	}

	public Signal<JpgImage> picture() {
		return _picture;
	}

	public void picture(JpgImage picture) {
		_pictureOld = picture;
		_picture.supply(picture);
	}

	public Object thing(String name) {
		return _thingsByName.get(name);
	}

	public void thing(String name, Object thing) {
		_thingsByName.put(name, thing);
	}
	
	public Map<String, Object> things() {
		return _thingsByName;
	}

	private static final long serialVersionUID = 1L;
}

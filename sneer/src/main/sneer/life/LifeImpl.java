//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz.

package sneer.life;

import java.util.*;


public class LifeImpl implements Life {

	private String _name;
	private String _thoughtOfTheDay;

	private final Map<String, LifeView> _contactsByNickname = new HashMap<String, LifeView>();

    private String _profile;
    private String _contactInfo;

    private final List<String> _publicMessages = new ArrayList<String>();

	public LifeImpl(String name) {
		name(name);
	}
	
	public void name(String newName) {
		_name = newName;
	}

	public void thoughtOfTheDay(String thought) {
		_thoughtOfTheDay = thought;
	}

	public Set<String> nicknames() {
		return new HashSet<String>(_contactsByNickname.keySet());
	}

	public void giveSomebodyANickname(LifeView somebody, String nickname) throws IllegalArgumentException {
		if (somebody == null || nickname == null) throw new IllegalArgumentException();
		if (_contactsByNickname.containsKey(nickname)) throw new IllegalArgumentException();
		
		_contactsByNickname.put(nickname, somebody);
	}

	public void changeNickname(String oldNickname, String newNickname) throws IllegalArgumentException {
		LifeView lifeView = contact(oldNickname);
		giveSomebodyANickname(lifeView, newNickname);
		forgetNickname(oldNickname);
	}

	public void forgetNickname(String oldNickname) {
		_contactsByNickname.remove(oldNickname);
	}

	public String name() {
		return _name;
	}

	public String thoughtOfTheDay() {
		return _thoughtOfTheDay;
	}

	public LifeView contact(String nickname) {
		return (LifeView)_contactsByNickname.get(nickname);
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

    public void send(String message) {
        _publicMessages.add(message);
    }

	public List<String> publicMessages() {
		return _publicMessages;
    }

	public Date lastSightingDate() {
		return new Date(); //A local LifeView is always up-to-date.
	}

}

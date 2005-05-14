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

    private final Map<LifeView, List<String>> _messagesSentByContact = new HashMap<LifeView, List<String>>();

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

    public void send(String message, String toNickname) {
        if (!nicknames().contains(toNickname)) throw new IllegalArgumentException("Unknown contact: " + toNickname);
        innerMessagesSentTo(contact(toNickname)).add(message);
    }

    public List<String> messagesSentTo(String nickname) {
		return messagesSentTo(contact(nickname));
    }

	private List<String> messagesSentTo(LifeView contact) {
		if (!isAccessAllowed(contact)) throw new NoneOfYourBusiness();
        return innerMessagesSentTo(contact);
    }

	private boolean isAccessAllowed(LifeView life) {
		if (1 == 1) return true; //FIXME Recover calling contact logic.
		if (CALLING_CONTACT.get() == this) return true;
		if (life == CALLING_CONTACT.get()) return true;
		return false;
	}

	private List<String> innerMessagesSentTo(LifeView contact) {
		List<String> result = _messagesSentByContact.get(contact);
		if (result == null) {
            result = new ArrayList<String>();
            _messagesSentByContact.put(contact, result);
        }
		return result;
	}

	public List<String> messagesSentToMe() {
		return messagesSentTo(CALLING_CONTACT.get());
    }

}

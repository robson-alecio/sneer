//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz.

package sovereign;

import java.util.*;


public class LifeImpl implements Life {

	private String _name;

	private final Map _contactsByNickname = new HashMap();

    private String _profile;
    private String _contactInfo;

    private final Map _messagesSentByContact = new HashMap();

	public LifeImpl(String name) {
		changeName(name);
	}

	public void changeName(String newName) {
		_name = newName;
	}

	public Set nicknames() {
		return new HashSet(_contactsByNickname.keySet());
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
		if (CALLING_CONTACT.life() == this) return true;
		if (life == CALLING_CONTACT.life()) return true; //FIXME: This is the root of intermittent errors.
		return false;
	}

	private List innerMessagesSentTo(LifeView contact) {
		List result = (List)_messagesSentByContact.get(contact);
		if (result == null) {
            result = new ArrayList();
            _messagesSentByContact.put(contact, result);
        }
		return result;
	}

	public List<String> messagesSentToMe() {
		return messagesSentTo(CALLING_CONTACT.life());
    }

}

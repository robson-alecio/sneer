//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package sovereign;

import java.util.*;


public class LifeImpl implements Life {

	private String _name;

	private final Map<String, LifeView> _contactsByNickname = new HashMap<String, LifeView>();

    private String _profile;
    private String _contactInfo;

    private final Map<LifeView, List<String>> _messagesSentByContact = new HashMap<LifeView, List<String>>();

	public LifeImpl(String name) {
		changeName(name);
	}

	public void changeName(String newName) {
		_name = newName;
	}

	public Set<String> nicknames() {
		return new HashSet<String>(_contactsByNickname.keySet());
	}

	public void giveSomebodyANickname(LifeView somebody, String nickname) throws IllegalArgumentException {
		//System.out.println ("give: " + nickname +" " + somebody+ " ("+this+")");
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

    public void send(String message, String toNickname) {
        if (!nicknames().contains(toNickname)) throw new IllegalArgumentException("Unknown contact: " + toNickname);
        innerMessagesSentTo(contact(toNickname)).add(message);
    }

    public List messagesSentTo(String nickname) {
		return messagesSentTo(contact(nickname));
    }

	private List messagesSentTo(LifeView contact) {
		if (!isAccessAllowed(contact)) throw new NoneOfYourBusiness();
        return innerMessagesSentTo(contact);
    }
	
	private LifeView callingContact() {
		// FIXME: CALLING_CONTACT.life() should return this instead of null. Fabio.
		//I don't see how that can be done? Klaus.
		return CALLING_CONTACT.life() == null ? this : CALLING_CONTACT.life(); //FIXME: null is too weak. It cannot be open by default. Klaus.
	}

	private boolean isAccessAllowed(LifeView life) {
		if (callingContact() == this) return true; 
		if (callingContact() == life) return true;
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

	public List messagesSentToMe() {
		return messagesSentTo(CALLING_CONTACT.life());
    }
	
	public boolean somebodyAskingToBeYourFriend (LifeView somebody) {
		giveSomebodyANickname(somebody, somebody.name());
		return true;
	}

}

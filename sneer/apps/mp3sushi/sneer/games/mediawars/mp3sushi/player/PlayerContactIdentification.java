package sneer.games.mediawars.mp3sushi.player;

import sneer.kernel.business.contacts.ContactId;
import wheel.reactive.Signal;

public class PlayerContactIdentification extends PlayerIdentification {
	private static long lastId = 1;
	
	private Long _id;
	private Signal<String> _nick;
	private ContactId _contactId;
	
	public PlayerContactIdentification(Signal<String> nick, ContactId contactId) {
		super();
		this._nick = nick;
		this._contactId = contactId;
		this._id = lastId ++;
	} 

	@Override
	public Long getId() {
		return _id;
	}

	@Override
	public Signal<String> getNick() {
		return _nick;
	}

	public ContactId getContactId() {
		return _contactId;
	} 

	public PlayerExportInfo exportInfo() {
		return new PlayerExportInfo(_id, _nick.currentValue());
	}
	
}

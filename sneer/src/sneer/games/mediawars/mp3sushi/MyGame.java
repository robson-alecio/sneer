package sneer.games.mediawars.mp3sushi;

import java.util.HashMap;

import sneer.games.mediawars.mp3sushi.player.PlayerContactIdentification;
import sneer.games.mediawars.mp3sushi.player.PlayerExportInfo;
import sneer.games.mediawars.mp3sushi.player.PlayerExternalIdentification;
import sneer.games.mediawars.mp3sushi.player.PlayerHostIdentification;
import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class MyGame extends Game {

	private ListSource<PlayerIdentification> _players = new ListSourceImpl<PlayerIdentification>();
	private HashMap<ContactId, PlayerContactIdentification> _contactPlayer = new HashMap<ContactId, PlayerContactIdentification>(); 
	private HashMap<Long, PlayerIdentification> _longIdPlayer = new HashMap<Long, PlayerIdentification>();
	private PlayerHostIdentification _hostPlayer;
	private ContactId _externalHostId;
	private Signal<String> _ownName;
	
	
	public MyGame(Signal<String> ownName) {
		setNotPlaying();
		_ownName = ownName;
	}
	
	public ListSignal<PlayerIdentification> getPlayers() {
		return _players.output();
	}

	public void waitingPlayerForConfiguration(GameConfiguration configuration) {
		_gameConfiguration.setter().consume(configuration);
		_status.setter().consume(WAITING_PLAYERS);
		_hostPlayer = new PlayerHostIdentification(_ownName);
		addPlayerIdentification(_hostPlayer);
		
	}

	public boolean canReceiveJoins() {
		// Implement Auto-generated method stub
		return WAITING_PLAYERS.equals(this.getStatus().output().currentValue());
	}

	public void joinAccepted(TheirsGame hostGame, ContactId contactiD) {
		this._externalHostId = contactiD;
		this._gameConfiguration.setter().consume(hostGame._gameConfiguration.output().currentValue());
		this._status.setter().consume(JOINED);
		
	}

	public void setNotPlaying() {
		_status.setter().consume(NOT_PLAYING);
		for (int i = 0; i < _players.output().currentSize();) _players.remove(0);
		_contactPlayer.clear() ;
		_longIdPlayer.clear();
		_hostPlayer = null;
		_externalHostId = null;
	}
	
	public void setPlaying() {
		_status.setter().consume(PLAYING);
	}
	
	public PlayerContactIdentification addPlayerFromContact(ContactAttributes contactAttributes) {
		PlayerContactIdentification pi = new PlayerContactIdentification(contactAttributes.nick(), contactAttributes.id());
		_contactPlayer.put(contactAttributes.id(), pi);
		addPlayerIdentification(pi);
		return pi;
	}

	private void addPlayerIdentification(PlayerIdentification pi) {
		_longIdPlayer.put(pi.getId(), pi);
		_players.add(pi);
	}

	public HashMap<ContactId, PlayerContactIdentification> getContactPlayer() {
		return _contactPlayer;
	}

	public void addExternalPlayer(PlayerExportInfo peinfo) {
		PlayerExternalIdentification pei = new PlayerExternalIdentification(peinfo);
		addPlayerIdentification(pei);
	}

	public PlayerHostIdentification getHostPlayer() {
		return _hostPlayer;
	}

	public ContactId getExternalHostId() {
		return _externalHostId;
	}

	public HashMap<Long, PlayerIdentification> getLongIdPlayer() {
		return _longIdPlayer;
	}

	public void quit() {
		this.setNotPlaying();
		_gameConfiguration.setter().consume(null);
		
	}

	public void removePlayerFromContactId(ContactId contactiD) {
		PlayerContactIdentification pi = _contactPlayer.get(contactiD);
		_contactPlayer.remove(contactiD);
		_longIdPlayer.remove(pi.getId());
		_players.remove(pi);
	}
	
}

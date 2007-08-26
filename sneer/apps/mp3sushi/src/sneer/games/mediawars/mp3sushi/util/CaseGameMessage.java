package sneer.games.mediawars.mp3sushi.util;

import java.util.HashMap;

import sneer.games.mediawars.mp3sushi.GameMessage;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Packet;

public class CaseGameMessage {

	private HashMap<String, CaseGameMessageConsume> caseMap = new HashMap<String ,CaseGameMessageConsume>();
	private CaseGameMessageConsume otherElse;
	
	public void addCase(String key, CaseGameMessageConsume casePacketConsume) {
		caseMap.put(key, casePacketConsume);
	}
	
	public void setOtherElse(CaseGameMessageConsume casePacketConsume) {
		otherElse = casePacketConsume;
	}
	
	public void switchCasePacket(Packet packet) {
		ContactId contactiD = packet._contactId;
		GameMessage message = (GameMessage) packet._contents;
		CaseGameMessageConsume casePacketConsume = caseMap.get(message.getType());
		if (casePacketConsume == null) {
			if (otherElse == null) return;
			otherElse.consume(message.getType(), contactiD, message.getContent());
		} else {
			casePacketConsume.consume(message.getType(), contactiD, message.getContent());
		}
	}
}

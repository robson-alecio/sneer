package sneer.apps.transferqueue;

import sneer.kernel.business.contacts.ContactId;

public class TransferKey{
	public final String _transferId;
	public final ContactId _contactId;

	public TransferKey(String transferId, ContactId contactId){
		_transferId = transferId;
		_contactId = contactId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) return false;
		if (!(obj instanceof TransferKey)) return false;
		TransferKey temp = (TransferKey)obj;
		return _transferId.equals(temp._transferId)&&_contactId.equals(temp._contactId);
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
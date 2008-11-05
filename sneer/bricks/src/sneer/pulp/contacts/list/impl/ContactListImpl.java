package sneer.pulp.contacts.list.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.contacts.list.ContactInfo;
import sneer.pulp.contacts.list.ContactList;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.VisitorAdapter;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.ListRegisterImpl;

class ContactListImpl extends ListRegisterImpl<ContactInfo> implements ContactList{

	@Inject
	static private ContactManager _contacts;
	
	@SuppressWarnings("unused")
	private Visitor<Contact> _visitor;
	
	private final Map<Contact, ContactInfo> _contactInfos = new HashMap<Contact, ContactInfo>();
	private Omnivore<ListValueChange<Contact>> _contactListReceiverToAvoidGc;

	ContactListImpl(){
		initRegisterContent();
		initContactsListReceiver();
	}

	private void initRegisterContent() {
		for (Contact contact : _contacts.contacts()) {
			addContact(contact);
		}
	}

	private void initContactsListReceiver() {
		_contactListReceiverToAvoidGc = new Omnivore<ListValueChange<Contact>>(){@Override public void consume(ListValueChange<Contact> change) {
			contactListChanged(change);
		}};
		_contacts.contacts().addListReceiver(_contactListReceiverToAvoidGc);
	}
	
	private void replaceContact(final Contact oldContact, final Contact newContact) {
		removeContact(oldContact);
		addContact(newContact);
	}		
	
	private void removeContact(final Contact contact) {
		ContactInfo contactInfo = _contactInfos.get(contact);
		_contactInfos.remove(contact);
		remove(contactInfo);
	}		
	
	private void addContact(final Contact contact) {
		ContactInfo contactInfo = new ContactInfo(contact);
		_contactInfos.put(contact, contactInfo);
		add(contactInfo);
	}	
	
	private void contactListChanged(ListValueChange<Contact> change) {
		_visitor = new VisitorAdapter<Contact>(){
			@Override public void elementAdded(int index, Contact contact) { addContact(contact);	}
			@Override public void elementInserted(int index, Contact contact) { addContact(contact);}
			@Override public void elementRemoved(int index, Contact contact) { removeContact(contact); }
			@Override public void elementReplaced(int index, Contact oldContact, Contact newContact) { replaceContact( oldContact, newContact); }
		};
		change.accept(_visitor);
	}
	
	@Override
	public ContactInfo contactInfo(Contact contact){
		return _contactInfos.get(contact);
	}
}
package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.kernel.business.Contact;
import wheel.reactive.Receiver;
import wheel.reactive.list.ListSignal;
import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public class ContactListPrinter implements ListSignal<String> {

	private final ListSignal<Contact> _input;

	public ContactListPrinter(ListSignal<Contact> input) {
		_input = input;
	}

	@Override
	public void addListReceiver(Receiver<ListValueChange> receiver) {
		_input.addListReceiver(receiver);
	}

	@Override
	public List<String> currentValue() {
		return printList(_input.currentValue());
	}

	private List<String> printList(List<Contact> input) {
		List<Contact> inputList = input;
		ArrayList<String> result = new ArrayList<String>(inputList.size());
		
		for (Contact contact : inputList) result.add(print(contact));
		
		return result;
	}

	private String print(Contact contact) { //Fix: this must be reactive.
		return onlineTag(contact) + " ** " + contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue();
	}

	
	private String onlineTag(Contact contact) {
		return contact.isOnline().currentValue().booleanValue()
			? "On  :) "
			: "Off :( ";
	}

}

package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.kernel.business.Contact;
import wheel.reactive.AbstractNotifier;
import wheel.reactive.Receiver;
import wheel.reactive.list.ListElementAdded;
import wheel.reactive.list.ListElementReplaced;
import wheel.reactive.list.ListReplaced;
import wheel.reactive.list.ListSignal;
import wheel.reactive.list.ListValueChangeVisitorAdapter;
import wheel.reactive.list.ListSignal.ListValueChange;

public class ContactListPrinter extends AbstractNotifier<ListValueChange> implements ListSignal<String> {

	private class MyContactReceiver implements Receiver<String> {

		private int _contactIndex;

		public MyContactReceiver(int contactIndex) {
			_contactIndex = contactIndex;
		}

		@Override
		public void receive(String valueChange) {
			notifyReceivers(new ListElementReplaced(_contactIndex));
		}

	}


	private class MyListReceiver implements Receiver<ListValueChange>, ListValueChangeVisitor {

		@Override
		public void receive(ListValueChange listChange) {
			listChange.accept(this);
		}

		@Override
		public void elementAdded(int index) {
			_input.currentValue().get(index).nick().addReceiver(new MyContactReceiver(index));
			notifyReceivers(new ListElementAdded(index));
		}

		@Override
		public void elementRemoved(int index) {
			
			
		}

		@Override
		public void elementReplaced(int index) {
			// Implement Auto-generated method stub
			
		}

		@Override
		public void listReplaced() {
			// Implement Auto-generated method stub
			
		}

	}


	private final ListSignal<Contact> _input;

	public ContactListPrinter(ListSignal<Contact> input) {
		_input = input;
		_input.addListReceiver(new MyListReceiver());
	}

	@Override
	public void addListReceiver(Receiver<ListValueChange> receiver) {
		addReceiver(receiver);
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
		return onlineTag(contact) + " - " + contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue();
	}

	
	private String onlineTag(Contact contact) {
		return contact.isOnline().currentValue().booleanValue()
			? "On  :)"
			: "Off :(";
	}

	@Override
	protected void initReceiver(Receiver<ListValueChange> receiver) {
		receiver.receive(ListReplaced.SINGLETON);
	}

}

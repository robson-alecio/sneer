package sneer.kernel.gui.contacts;

import sneer.kernel.business.Contact;
import wheel.reactive.AbstractNotifier;
import wheel.reactive.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.ListElementAdded;
import wheel.reactive.lists.impl.ListElementReplaced;
import wheel.reactive.lists.impl.ListReplaced;

public class ContactListPrinter {

	private class MyOutput extends AbstractNotifier<ListValueChange> implements ListSignal<String> {

		@Override
		public void addListReceiver(Receiver<ListValueChange> receiver) {
			addReceiver(receiver);
		}

		@Override
		public String currentGet(int index) {
			return print(_input.currentGet(index));
		}

		@Override
		public int currentSize() {
			return _input.currentSize();
		}

		@Override
		protected void initReceiver(Receiver<ListValueChange> receiver) {
			receiver.receive(ListReplaced.SINGLETON);
		}

		@Override
		protected void notifyReceivers(ListValueChange valueChange) {
			super.notifyReceivers(valueChange);
		}
	}


	private class MyContactReceiver implements Receiver<String> {

		private int _contactIndex;

		public MyContactReceiver(int contactIndex) {
			_contactIndex = contactIndex;
		}

		@Override
		public void receive(String valueChange) {
			_output.notifyReceivers(new ListElementReplaced(_contactIndex));
		}

	}


	private class MyListReceiver implements Receiver<ListValueChange>, Visitor {

		@Override
		public void receive(ListValueChange listChange) {
			listChange.accept(this);
		}

		@Override
		public void elementAdded(int index) {
			_input.currentGet(index).nick().addReceiver(new MyContactReceiver(index));
			_output.notifyReceivers(new ListElementAdded(index));
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
	private MyOutput _output = new MyOutput();

	public ContactListPrinter(ListSignal<Contact> input) {
		_input = input;
		_input.addListReceiver(new MyListReceiver());
	}

	private String print(Contact contact) { //Fix: this must be reactive.
		return onlineTag(contact) + " - " + contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue();
	}

	
	private String onlineTag(Contact contact) {
		return contact.isOnline().currentValue().booleanValue()
			? "On  :)"
			: "Off :(";
	}

	public ListSignal<String> output() {
		return _output;
	}

}

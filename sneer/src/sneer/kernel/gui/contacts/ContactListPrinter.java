package sneer.kernel.gui.contacts;

import sneer.kernel.business.contacts.Contact;
import wheel.reactive.AbstractNotifier;
import wheel.reactive.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.AbstractListReceiver;
import wheel.reactive.lists.impl.ListElementAdded;
import wheel.reactive.lists.impl.ListElementRemoved;
import wheel.reactive.lists.impl.ListElementReplaced;
import wheel.reactive.lists.impl.ListReplaced;
import wheel.reactive.lists.impl.SimpleListReceiver;

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
			receiver.receive(new ListReplaced(0, currentSize()));
		}

		@Override
		protected void notifyReceivers(ListValueChange valueChange) {
			super.notifyReceivers(valueChange);
		}
	}


	private class MyContactReceiver<VO> implements Receiver<VO> {

		private int _contactIndex;

		public MyContactReceiver(int contactIndex) { //Fix: call contact.removeReceiver(this) when no longer needed
			_contactIndex = contactIndex;
		}

		@Override
		public void receive(VO valueChange) {
			_output.notifyReceivers(new ListElementReplaced(_contactIndex));
		}

	}


	private class MyListReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			Contact contact = _input.currentGet(index);
			contact.nick().addReceiver(new MyContactReceiver<String>(index));
			contact.host().addReceiver(new MyContactReceiver<String>(index));
			contact.port().addReceiver(new MyContactReceiver<Integer>(index));
			contact.isOnline().addReceiver(new MyContactReceiver<Boolean>(index));
			
			_output.notifyReceivers(new ListElementAdded(index));
		}

		@Override
		public void elementRemoved(int index) {
			_output.notifyReceivers(new ListElementRemoved(index));
		}

	}


	private final ListSignal<Contact> _input;
	private MyOutput _output = new MyOutput();

	public ContactListPrinter(ListSignal<Contact> input) {
		_input = input;
		_input.addListReceiver(new MyListReceiver());
	}

	private String print(Contact contact) { //Fix: Use ContactPrinter instead of this.
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

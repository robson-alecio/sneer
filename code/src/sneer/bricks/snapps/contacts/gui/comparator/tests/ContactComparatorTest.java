package sneer.bricks.snapps.contacts.gui.comparator.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.connection.ByteConnection;
import sneer.bricks.pulp.connection.ConnectionManager;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.listsorter.ListSorter;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.bricks.snapps.contacts.gui.comparator.ContactComparator;
import sneer.foundation.brickness.testsupport.AssertUtils;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Contribute;

public class ContactComparatorTest extends BrickTest {
	
	@Contribute final ConnectionManager _connectionsMock = new ConnectionManagerMock();
	
	private ListSorter _sorter;
	private final ListRegister<Contact> _contacts = my(CollectionSignals.class).newListRegister();
	private final SignalChooser<Contact> _chooser = new SignalChooser<Contact>(){ @Override public Signal<?>[] signalsToReceiveFrom(Contact element) {
		return new Signal<?>[]{((ContactMock)element)._isOnline, element.nickname() };
	}};
	
	@Test
	@Ignore
	public void testComparator() {
		
		final ContactMock truea = new ContactMock("a", true);
		final ContactMock trueA = new ContactMock("A", true);
		final ContactMock trueB = new ContactMock("B", true);
		final ContactMock falseA = new ContactMock("A", false);
		final ContactMock falseB = new ContactMock("B", false);

		ContactComparator comparator = my(ContactComparator.class);
		_sorter = my(ListSorter.class);

		_contacts.add(falseA);
		_contacts.add(trueB);
		
		ListSignal<Contact> sortedList = _sorter.sort(_contacts.output(), comparator, _chooser);
		
		_contacts.add(trueA);
		_contacts.add(falseB);
		_contacts.add(truea);
		
		AssertUtils.assertSameContents(sortedList, truea, trueA, trueB, falseA, falseB);
	}
}

class ConnectionManagerMock implements ConnectionManager{
	@Override 
	public ByteConnection connectionFor(final Contact contact) {
		return new ByteConnection(){
			@Override 
			public Signal<Boolean> isOnline() {
				return ((ContactMock)contact)._isOnline;
			}
			@Override public void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver) { throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet();}
		};
	}
	@Override public void manageIncomingSocket(Contact contact, ByteArraySocket socket) { throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); }
	@Override public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) { throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); }
	@Override public void closeConnectionFor(Contact contact) {	throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); }
}

class ContactMock implements Contact{

	final Signal<Boolean> _isOnline;
	final Register<String> _nick = my(Signals.class).newRegister("");

	ContactMock(String nick, boolean isOnline) {
		_nick.setter().consume(nick);
		_isOnline = my(Signals.class).constant(isOnline);
	}

	@Override
	public Signal<String> nickname() {
		return _nick.output();
	}
	
	@Override
	public String toString() {
		return _isOnline.currentValue() + " - " + _nick.output().currentValue();
	}
}
package snapps.contacts.gui.comparator.tests;

import static sneer.brickness.Environments.my;

import org.junit.Test;

import snapps.contacts.gui.comparator.ContactComparator;
import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.TestInBricknessEnvironment;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.testutil.TestUtils;

public class ContactComparatorTest extends TestInBricknessEnvironment {
	
	@Contribute final ConnectionManager _connectionsMock = new ConnectionManagerMock();
	
	private ListSorter _sorter;
	private final ListRegister<Contact> _contacts = new ListRegisterImpl<Contact>();
	private final SignalChooser<Contact> _chooser = new SignalChooser<Contact>(){ @Override public Signal<?>[] signalsToReceiveFrom(Contact element) {
		return new Signal<?>[]{((ContactMock)element)._isOnline, element.nickname() };
	}};
	
	@Test
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
		
		TestUtils.assertSameContents(sortedList, truea, trueA, trueB, falseA, falseB);
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
			@Override public void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver) { throw new wheel.lang.exceptions.NotImplementedYet();}
		};
	}
	@Override public void manageIncomingSocket(Contact contact, ByteArraySocket socket) { throw new wheel.lang.exceptions.NotImplementedYet(); }
	@Override public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) { throw new wheel.lang.exceptions.NotImplementedYet(); }
}

class ContactMock implements Contact{

	final Signal<Boolean> _isOnline;
	final Signal<String> _nick;

	ContactMock(String nick, boolean isOnline) {
		_nick = new Constant<String>(nick);
		_isOnline = new Constant<Boolean>(isOnline);
	}

	@Override
	public Signal<String> nickname() {
		return _nick;
	}
	
	@Override
	public String toString() {
		return _isOnline.currentValue() + " - " + _nick.currentValue();
	}
}
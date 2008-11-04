package snapps.contacts.gui.comparator.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import snapps.contacts.gui.comparator.ContactsComparator;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.listsorter.ListSorter;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.testutil.TestUtils;

public class ContactsComparatorTest {
	
	private final Mockery _mockery = new JUnit4Mockery();	
	private ListSorter _sorter;
	private final ListRegister<Contact> _contacts = new ListRegisterImpl<Contact>();
	ConnectionManager _connectionManagerMock = _mockery.mock(ConnectionManager.class);
	
	@Test
	public void testComparator() {
		
		final MyContact trueA = new MyContact("A", true);
		final MyContact trueB = new MyContact("B", true);
		final MyContact falseA = new MyContact("A", false);
		final MyContact falseB = new MyContact("B", false);

		_mockery.checking(new Expectations(){{
			allowing(_connectionManagerMock).connectionFor(trueA); 
				will(returnValue(new MyByteConnection(trueA)));
				
			allowing(_connectionManagerMock).connectionFor(trueB);  
				will(returnValue(new MyByteConnection(trueB)));
				
			allowing(_connectionManagerMock).connectionFor(falseA); 
				will(returnValue(new MyByteConnection(falseA)));
				
			allowing(_connectionManagerMock).connectionFor(falseB); 
				will(returnValue(new MyByteConnection(falseB)));
		}});		

		Container container =  ContainerUtils.newContainer(_connectionManagerMock);
		ContactsComparator comparator = container.produce(ContactsComparator.class);
		_sorter = container.produce(ListSorter.class);

		_contacts.add(falseA);
		_contacts.add(trueB);
		_contacts.add(trueA);
		_contacts.add(falseB);
		
		ListSignal<Contact> sortedList = _sorter.sort(_contacts.output(), comparator);
		TestUtils.assertSameContents(sortedList, trueA, trueB, falseA, falseB);
	}
}
class MyByteConnection implements ByteConnection{
	
	private final MyContact _contact;

	MyByteConnection(MyContact contact){
		_contact = contact;
	}
	
	@Override
	public void initCommunications(PacketScheduler sender,	Omnivore<byte[]> receiver) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement}
	}

	@Override
	public Signal<Boolean> isOnline() {
		return _contact._isOnline;
	}
	
}

class MyContact implements Contact{
	Constant<String> _nick;
	Constant<Boolean> _isOnline;
	
	MyContact(String nick, boolean isOnline) {
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
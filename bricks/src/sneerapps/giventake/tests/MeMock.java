package sneerapps.giventake.tests;

import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Brick;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.lists.ListSignal;

public class MeMock implements Me {
	
	@Override
	public <B extends Brick> B brickProxyFor(Class<B> brickInterface) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public ListSignal<Contact> contacts() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Party navigateTo(Contact contact) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	public void connectTo(MeMock meMock) {
		meMock.toString();
		//Nao consegui criar um ListSource - Kalecser
		throw new NotImplementedYet();
	}

}

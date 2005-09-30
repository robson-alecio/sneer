//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;


import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.life.LifeView;
import junit.framework.TestCase;

public class Freedom1 extends TestCase {

	protected Life _me;

	@Override
	public void setUp() throws Exception {
		getALife();
	}

	public void testNaming() throws Exception {
    	checkMyName("Klaus Wuestefeld");
    	changeMyNameAFewTimes();
    }	
	
	private void getALife() {
		_me = getALifeFor("Klaus Wuestefeld");
	}

	protected Life getALifeFor(String name) {
		return new LifeImpl(name);
	}

    private void checkMyName(String expected) {
    	checkName(expected, _me);
    }

    protected void checkName(String expected, LifeView lifeView) {
		assertEquals(expected, lifeView.name().currentValue());
	}

    private void changeMyNameAFewTimes() {
    	changeMyNameTo("klauswuestefeld");
    	changeMyNameTo("KlausWuestefeld");
    	changeMyNameTo("Klaus Wuestefeld");
    }

    private void changeMyNameTo(String newName) {
    	_me.name(newName);
    	checkMyName(newName);
    }

}

//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer;

public class Freedoms2_5 extends Freedom2  {

	public void testProfileSharing() throws Exception {
		String zibasProfile =
		    "passions: simplicity\n" +
		    "sports: soccer\n" +
		    "activities: gym, voley\n" +
		    "books: Dune, Lord of the Flies, Shibumi\n" +
		    "movies: Matrix, Instinct\n" +
		    "cuisines: Italian"; 
		_ziba.profile(zibasProfile);

		assertEquals(zibasProfile, myContact("Ziba").profile());
	}

	public void testContactInfoSharing() throws Exception {
		String zibasContactInfo =
		    "E-mail:ziba@soares.net\n" +
		    "Phone: +55 (11) 555-1234\n" +
		    "Address: R Juquiá 166\n" +
		    "Home Page: http://www.soares.net/ziba";
		_ziba.contactInfo(zibasContactInfo);

		assertEquals(zibasContactInfo, myContact("Ziba").contactInfo());
/*		LifeView contact = myContact("Ziba")
				.contact("Zezo")
				.contact("Ziba");
		assertEquals(zibasContactInfo, contact.contactInfo()); */ //FIXME Uncomment to get freeze.
	}
	
}

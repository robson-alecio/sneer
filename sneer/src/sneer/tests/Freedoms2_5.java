//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.tests;


public class Freedoms2_5 extends Freedom2  {

	public void testContactInfoSharing() throws Exception {
		String zibasContactInfo =
		    "E-mail:ziba@soares.net\n" +
		    "Phone: +55 (11) 555-1234\n" +
		    "Address: R Juquiá 166\n" +
		    "Home Page: http://www.soares.net/ziba";
		_ziba.contactInfo(zibasContactInfo);

		assertEquals(zibasContactInfo, myContact("Ziba").contactInfo());
		
		waitForUpdates(myContact("Ziba").contact("Zezo"));
		waitForUpdates(myContact("Ziba").contact("Zezo").contact("Ziba"));
		assertEquals(zibasContactInfo, myContact("Ziba").contact("Zezo").contact("Ziba").contactInfo());
	}
	
}

//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package sneer.life;

public class NoneOfYourBusiness extends RuntimeException {

	public NoneOfYourBusiness() {}
	
	public NoneOfYourBusiness(NoneOfYourBusiness cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}

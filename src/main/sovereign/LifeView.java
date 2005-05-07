//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package sovereign;

import java.util.List;
import java.util.Set;


public interface LifeView {

	public String name();
	
	public Set<String> nicknames();
	public LifeView contact(String nickname);

    public String profile();
    public String contactInfo();

    public List<String> messagesSentTo(String contact);
	public List<String> messagesSentToMe();
	
	//This should be somewhere else, owned by LiveResponder. Fabio.
	//Why? The contracts of the methods in this interface already has an implicit dependency on CallingContact. Klaus. 
	public static final	CallingContact CALLING_CONTACT = new CallingContact(); 
}

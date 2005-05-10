//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

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
	
	public static final	ThreadLocal<LifeView> CALLING_CONTACT = new ThreadLocal<LifeView>();
}

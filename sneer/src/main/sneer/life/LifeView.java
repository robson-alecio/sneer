//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.life;

import java.util.Date;
import java.util.List;
import java.util.Set;

import wheelexperiments.reactive.Signal;


public interface LifeView {

	public String name();
	public Signal<String> thoughtOfTheDay();
	public JpgImage picture();
	
	public Set<String> nicknames();
	public LifeView contact(String nickname);

    public String profile();
    public String contactInfo();

	public List<String> messagesSentToMe();

	public Date lastSightingDate();
	
	public static final	ThreadLocal<String> CALLING_NICKNAME = new ThreadLocal<String>();

}

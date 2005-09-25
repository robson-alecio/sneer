//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.life;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import wheelexperiments.reactive.Signal;


public interface LifeView {

	public String name();
	public Signal<String> thoughtOfTheDay();
	public Signal<JpgImage> picture();
	
	public Set<String> nicknames();
	public LifeView contact(String nickname);

    public String contactInfo();
    
	public Map<String, Object> things(); //FIXME Move to Life because it is modifiable.
	public Object thing(String name);
	
	public Date lastSightingDate();
	
	public static final	ThreadLocal<String> CALLING_NICKNAME = new ThreadLocal<String>();

}

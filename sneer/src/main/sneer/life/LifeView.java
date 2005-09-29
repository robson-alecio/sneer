//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.life;

import java.util.Date;
import java.util.Map;

import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.signals.SetSignal;


public interface LifeView {

	public Signal<String> name();
	public Signal<String> thoughtOfTheDay();
	public Signal<JpgImage> picture();
	
	public SetSignal<String> nicknames();
	public LifeView contact(String nickname);

    public String contactInfo();
    
	public Map<String, Object> things(); //FIXME Move to Life because it is modifiable.
	public Object thing(String name);
	
	public Date lastSightingDate();
	
	public static final	ThreadLocal<String> CALLING_NICKNAME = new ThreadLocal<String>();

}

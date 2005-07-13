package sneer.life;

import java.util.Date;

public interface View {

	/**
	 * Returns the Date this View was seen for the last time. If this returns null, all other values in the View are undefined (thay can be null).
	 */
	Date lastSightingDate();
	
	//TODO: Date lastChangeDate(); 
	
}

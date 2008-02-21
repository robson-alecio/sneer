package spikes.legobricks.security.impl;

import java.util.HashMap;
import java.util.Map;

import spikes.lego.Brick;
import spikes.lego.Container;
import spikes.lego.LegoException;
import spikes.lego.Toy;
import spikes.legobricks.security.Sentinel;
import spikes.legobricks.security.Sorry;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;

public class SentinelImpl implements Sentinel {

	@Brick
	private User user;
	
	@Brick
	private Container container;
	
	private Map<String, Boolean> cache = new HashMap<String, Boolean>();

	
	
	@Override
	public void check(String resourceName) {

		Toy toy = findToy();
		System.out.println(toy);
		
		//check cache
		Boolean b = cache.get(resourceName);
		if(b != null && b) return;
		
		Object response = null;
		try {
			response = user.choose("Allow access to "+resourceName+"?", "Always", "Yes", "No", "Never");
		} catch (CancelledByUser e) {
			response = "No";
		} 
		
		if("No".equals(response) || "Never".equals(response)) {
			if("Never".equals(response)) 
				cache.put(resourceName, Boolean.FALSE);
			
			throw new Sorry("Access not allowed to "+resourceName);
		}
		if("Always".equals(response))
			cache.put(resourceName, Boolean.TRUE);
		
	}

	/*
	 * Fix: this is broken. el.getClassnName() returns the implementation, of course, not the interface 
	 * that is useful for toy finding.
	 */
	private Toy findToy() {
		Toy result = null;
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (StackTraceElement el : elements) {
			try {
				Object brick = container.produce(el.getClassName());
				if(brick instanceof Toy)
					result = (Toy) brick;
			} catch (LegoException lex) {
				//not a brick
			}
		}
		return result;
	}
}

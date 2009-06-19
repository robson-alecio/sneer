package spikes.legobricks.security.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import spikes.legobricks.security.Sentinel;
import spikes.legobricks.security.Sorry;
import spikes.wheel.io.ui.CancelledByUser;
import spikes.wheel.io.ui.User;

public class SentinelImpl implements Sentinel {

	private final User _user = my(User.class);
		
	private Map<String, Boolean> _preAuthorizations = new HashMap<String, Boolean>();

	
	
	@Override
	public void check(String resourceName) {
	
		//check cache
		Boolean preAuthorized = _preAuthorizations.get(resourceName);
		if (preAuthorized != null) {
			if (preAuthorized) return;
			throw createException(resourceName);
		}
		
		Object response = null;
		try {
			response = _user.choose("Allow access to "+resourceName+"?", "Always", "Yes", "No", "Never");
		} catch (CancelledByUser e) {
			response = "No";
		}

		if("Yes".equals(response)) return;

		if("Always".equals(response)) {
			_preAuthorizations.put(resourceName, Boolean.TRUE);
			return;
		}

		if("Never".equals(response)) 
			_preAuthorizations.put(resourceName, Boolean.FALSE);
			
		throw createException(resourceName);
	}

	private Sorry createException(String resourceName) {
		return new Sorry("Access not allowed to "+resourceName);
	}

}

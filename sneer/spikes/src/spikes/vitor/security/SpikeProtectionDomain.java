package spikes.vitor.security;

import java.awt.AWTPermission;
import java.security.Permission;
import java.security.ProtectionDomain;

public class SpikeProtectionDomain extends ProtectionDomain {

	public SpikeProtectionDomain() {
		super(null, null, null, null);
	}

	@Override
	public boolean implies(Permission permission) {
		System.err.println("> >" + permission);
		if (permission instanceof AWTPermission)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "spikeProtectionDomain";
	}
	
	

}

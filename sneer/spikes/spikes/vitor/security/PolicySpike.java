package spikes.vitor.security;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.Provider;

public class PolicySpike extends Policy {
	@Override
	public Parameters getParameters() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Provider getProvider() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String getType() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void refresh() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public boolean implies(ProtectionDomain domain,
			Permission permission) {
		//System.out.println(">>>>>>>> Domain: " + domain.getClass() +	"  Permission: " + permission);
//		if (permission instanceof AllPermission) {
//			System.out.println("ALL PERMISSIONS");
//			return false;
//		}
		System.err.println("  Domain: " + domain.getClass());
		return true;
	}
}
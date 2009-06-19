package spikes.vitor.security;

import java.awt.AWTPermission;
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
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		// Implement Auto-generated method stub
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		// Implement Auto-generated method stub
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Provider getProvider() {
		// Implement Auto-generated method stub
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String getType() {
		// Implement Auto-generated method stub
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void refresh() {
		// Implement Auto-generated method stub
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		System.out.println(">>>>>>>> Domain: " + domain.getClass() +	"  Permission: " + permission);
//		if (permission instanceof AllPermission) {
//			System.out.println("ALL PERMISSIONS");
//			return false;
//		}
		if(permission instanceof AWTPermission) return false;
		
//		ClassLoader cl = domain.getClassLoader();
//		if(cl instanceof OldBrickClassLoader) {
//			OldBrickClassLoader bcl = (OldBrickClassLoader) cl;
//			System.out.println("Checking "+permission+" access for: "+bcl.mainClass());
//		}
		return true;
	}

}
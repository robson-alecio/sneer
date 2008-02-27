package spikes.vitor.security;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.Provider;

import sneer.lego.BrickClassLoader;

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
	public boolean implies(ProtectionDomain domain, Permission permission) {
		//System.out.println(">>>>>>>> Domain: " + domain.getClass() +	"  Permission: " + permission);
//		if (permission instanceof AllPermission) {
//			System.out.println("ALL PERMISSIONS");
//			return false;
//		}
		//if(permission instanceof SocketPermission) return false;
		ClassLoader cl = domain.getClassLoader();
		if(cl instanceof BrickClassLoader) {
			BrickClassLoader bcl = (BrickClassLoader) cl;
			System.out.println("Checking "+permission+" access for: "+bcl.getMainClass());
		}
		return true;
	}

}
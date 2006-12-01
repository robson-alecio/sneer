package spikes.vitor.security;

import java.security.AccessControlException;

public class SneerSecurityManager extends SecurityManager {
	
	private ThreadGroup pluginGroup;
	
	public SneerSecurityManager(ThreadGroup pluginGroup) {
		this.pluginGroup = pluginGroup;
	}
	
	@Override
	public void checkRead(String property) {
    	if (Thread.currentThread().getThreadGroup() == pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}

	@Override
	public void checkWrite(String property) {
    	if (Thread.currentThread().getThreadGroup() == pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}	
	
	public void checkPropertyAccess(String property) {
    	if (Thread.currentThread().getThreadGroup() == pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}

	public void checkCreateClassLoader() {
    	if (Thread.currentThread().getThreadGroup() == pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}
	
    @Override
	public void checkPackageAccess(String pkg) throws SecurityException {
    	if (Thread.currentThread().getThreadGroup() != pluginGroup) {
    		return;
    	}
    	
    	if (!pkg.startsWith("javax.swing"))
    		return;
    	
   		throw new SecurityException("Can't access swing classes");
    }

}

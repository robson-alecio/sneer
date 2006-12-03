package spikes.vitor.security;

public class SneerSecurityManager extends SecurityManager {
	
	private ThreadGroup _pluginGroup;
	
	public SneerSecurityManager(ThreadGroup pluginGroup) {
		_pluginGroup = pluginGroup;
	}
	
	@Override
	public void checkRead(String property) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}

	@Override
	public void checkWrite(String property) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}	
	
	@Override
	public void checkPropertyAccess(String property) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}

	@Override
	public void checkCreateClassLoader() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("Can't access System properties");
    	}
	}
	
    @Override
	public void checkPackageAccess(String pkg) throws SecurityException {
    	if (Thread.currentThread().getThreadGroup() != _pluginGroup) {
    		return;
    	}
    	
    	if (!pkg.startsWith("javax.swing"))
    		return;
    	
   		throw new SecurityException("Can't access swing classes");
    }

}

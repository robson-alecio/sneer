package spikes.vitor.security;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

public class SneerSecurityManager extends SecurityManager {
	
	private SecurityManager defaultManager;
	private ThreadGroup _pluginGroup;
	
	public SneerSecurityManager(SecurityManager manager, ThreadGroup pluginGroup) {
		_pluginGroup = pluginGroup;
		defaultManager = manager;
	}	

	public boolean hasDefaultSM() {
		return defaultManager != null;
	}
	
	@Override
	public void checkAccept(String host, int port) {
	   	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
	   		throw new SecurityException("You can't accept connections");
	   	}		
	   	if (hasDefaultSM()) {
		    defaultManager.checkAccept(host, port);
	   	}
	}

	@Override
	public void checkAccess(Thread t) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access Threads");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkAccess(t);
	   	}
	}

	@Override
	public void checkAccess(ThreadGroup g) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access Groups of Threads");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkAccess(g);
	   	}
	}

	@Override
	public void checkAwtEventQueueAccess() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access AWT");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkAwtEventQueueAccess();
	   	}
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't accept the Connections");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkConnect(host, port, context);
	   	}
	}

	@Override
	public void checkConnect(String host, int port) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't accept Connections");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkConnect(host, port);
	   	}
	}

	@Override
	public void checkDelete(String file) {
		if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
			throw new SecurityException("You can't delete Files");
		}		
		if (hasDefaultSM()) {
		    defaultManager.checkDelete(file);
	   	}
	}

	@Override
	public void checkExec(String cmd) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't execute anything");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkExec(cmd);
	   	}
	}

	@Override
	public void checkExit(int status) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't exit");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkExit(status);
	   	}
	}

	@Override
	public void checkLink(String lib) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't check link");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkLink(lib);
	   	}
	}

	@Override
	public void checkListen(int port) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't Listen ports");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkListen(port);
	   	}
	}

	@Override
	public void checkMemberAccess(Class<?> clazz, int which) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't do multicast");
    	}	
    	if (hasDefaultSM()) {
    	    defaultManager.checkMemberAccess(clazz, which);
	   	}
	}

	@Override
	public void checkMulticast(InetAddress maddr, byte ttl) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't do multicast");
    	}	
    	if (hasDefaultSM()) {
    	    defaultManager.checkMulticast(maddr, ttl);
	   	}
	}

	@Override
	public void checkMulticast(InetAddress maddr) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't do multicast");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkMulticast(maddr);
	   	}
	}

	@Override
	public void checkPackageDefinition(String pkg) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		if (pkg.startsWith("sneer."))
    			throw new SecurityException("You can't use the packages of Sneer");
    	}	
    	if (hasDefaultSM()) {
    	    defaultManager.checkPackageDefinition(pkg);
	   	}
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		System.out.println(perm.getName());
		
		if (hasDefaultSM()) {
	
		    defaultManager.checkPermission(perm, context);
	   	}
	}

	@Override
	public void checkPermission(Permission perm) {
		System.out.println(perm.getName());
		if (hasDefaultSM()) {
		    defaultManager.checkPermission(perm);
	   	}
	}

	@Override
	public void checkPrintJobAccess() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access the Printer");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkPrintJobAccess();
	   	}
	}

	@Override
	public void checkPropertiesAccess() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access the properties");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkPropertiesAccess();
	   	}
	}

	@Override
	public void checkRead(FileDescriptor fd) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't read");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkRead(fd);
	   	}
	}

	@Override
	public void checkRead(String file, Object context) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't read");
    	}		
    	if (hasDefaultSM()) {
    	    defaultManager.checkRead(file);
	   	}
	}

	@Override
	public void checkSecurityAccess(String target) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't check the Security");
    	}	
    	if (hasDefaultSM()) {
    	    defaultManager.checkSecurityAccess(target);
	   	}
	}

	@Override
	public void checkSetFactory() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access the Clipboard");
    	}		
		
		if (hasDefaultSM()) {
		    defaultManager.checkSetFactory();
	   	}
	}

	@Override
	public void checkSystemClipboardAccess() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access the Clipboard");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkSystemClipboardAccess();
	   	}
	}

	@Override
	public boolean checkTopLevelWindow(Object window) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access Windows");
    	}
    	if (hasDefaultSM()) {
    		return defaultManager.checkTopLevelWindow(window);
	   	}
    	return true;
	}

	@Override
	public void checkWrite(FileDescriptor fd) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't write files");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkWrite(fd);
	   	}
	}



	@Override
	public void checkRead(String property) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't read file");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkRead(property);
	   	}
	}

	@Override
	public void checkWrite(String property) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't write File");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkWrite(property);
   		}
	}	
	
	@Override
	public void checkPropertyAccess(String property) {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access System properties");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkPropertyAccess(property);
	   	}
	}

	@Override
	public void checkCreateClassLoader() {
    	if (Thread.currentThread().getThreadGroup() == _pluginGroup) {
    		throw new SecurityException("You can't access Class Loader");
    	}
    	if (hasDefaultSM()) {
    	    defaultManager.checkCreateClassLoader();
	   	}
	}
	
    @Override
	public void checkPackageAccess(String pkg) throws SecurityException {
    	if (Thread.currentThread().getThreadGroup() != _pluginGroup) {
    		return;
    	}
    	
    	//if (!pkg.startsWith("javax.swing"))
    		return;
    	
    	//if (hasDefaultSM()) {
    	 //   defaultManager.checkPackageAccess(pkg);
    	//}
    	
   		//throw new SecurityException("Can't access swing classes");
    }

}

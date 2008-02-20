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

	private boolean hasDefaultSM() {
		return defaultManager != null;
	}
	
	private void checkSneerPermission() {
		if (Thread.currentThread().getThreadGroup() != _pluginGroup) return;
		
   		try {
			throw new SecurityException("BUM!");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void checkAccept(String host, int port) {
		checkSneerPermission();
	   	if (hasDefaultSM()) {
		    defaultManager.checkAccept(host, port);
	   	}
	}

	@Override
	public void checkAccess(Thread t) {
		checkSneerPermission();
	}

	@Override
	public void checkAccess(ThreadGroup g) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkAccess(g);
	   	}
	}

	@Override
	public void checkAwtEventQueueAccess() {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkAwtEventQueueAccess();
	   	}
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkConnect(host, port, context);
	   	}
	}

	@Override
	public void checkConnect(String host, int port) {
		checkSneerPermission();	
    	if (hasDefaultSM()) {
    	    defaultManager.checkConnect(host, port);
	   	}
	}

	@Override
	public void checkDelete(String file) {
		checkSneerPermission();
		if (hasDefaultSM()) {
		    defaultManager.checkDelete(file);
	   	}
	}

	@Override
	public void checkExec(String cmd) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkExec(cmd);
	   	}
	}

	@Override
	public void checkExit(int status) {
		checkSneerPermission();		
    	if (hasDefaultSM()) {
    	    defaultManager.checkExit(status);
	   	}
	}

	@Override
	public void checkLink(String lib) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkLink(lib);
	   	}
	}

	@Override
	public void checkListen(int port) {
		checkSneerPermission();	
    	if (hasDefaultSM()) {
    	    defaultManager.checkListen(port);
	   	}
	}

	@Override
	public void checkMemberAccess(Class<?> clazz, int which) {
		checkSneerPermission();	
    	if (hasDefaultSM()) {
    	    defaultManager.checkMemberAccess(clazz, which);
	   	}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void checkMulticast(InetAddress maddr, byte ttl) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkMulticast(maddr, ttl);
	   	}
	}

	@Override
	public void checkMulticast(InetAddress maddr) {
		checkSneerPermission();
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
    	checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkPackageDefinition(pkg);
	   	}
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		System.out.println(perm.getName());
		checkSneerPermission();
		if (hasDefaultSM()) {
		    defaultManager.checkPermission(perm, context);
	   	}
	}

	@Override
	public void checkPermission(Permission perm) {
		System.out.println(perm.getName());
		System.out.println(perm.getClass().getName());
		checkSneerPermission();
		if (hasDefaultSM()) {
		    defaultManager.checkPermission(perm);
	   	}
	}

	@Override
	public void checkPrintJobAccess() {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkPrintJobAccess();
	   	}
	}

	@Override
	public void checkPropertiesAccess() {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkPropertiesAccess();
	   	}
	}

	@Override
	public void checkRead(FileDescriptor fd) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkRead(fd);
	   	}
	}

	@Override
	public void checkRead(String file, Object context) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkRead(file);
	   	}
	}

	@Override
	public void checkSecurityAccess(String target) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkSecurityAccess(target);
	   	}
	}

	@Override
	public void checkSetFactory() {
		checkSneerPermission();
		
		if (hasDefaultSM()) {
		    defaultManager.checkSetFactory();
	   	}
	}

	@Override
	public void checkSystemClipboardAccess() {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkSystemClipboardAccess();
	   	}
	}

	@Override
	public boolean checkTopLevelWindow(Object window) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    		return defaultManager.checkTopLevelWindow(window);
	   	}
    	return true;
	}

	@Override
	public void checkWrite(FileDescriptor fd) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkWrite(fd);
	   	}
	}



	@Override
	public void checkRead(String property) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkRead(property);
	   	}
	}

	@Override
	public void checkWrite(String property) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkWrite(property);
   		}
	}	
	
	@Override
	public void checkPropertyAccess(String property) {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkPropertyAccess(property);
	   	}
	}

	@Override
	public void checkCreateClassLoader() {
		checkSneerPermission();
    	if (hasDefaultSM()) {
    	    defaultManager.checkCreateClassLoader();
	   	}
	}
	
    @Override
	public void checkPackageAccess(String pkg) throws SecurityException {
    	if (!pkg.equals("spikes.vitor.security.plugins"))
    		checkSneerPermission();
    	
    	
    	System.out.println("CheckAccess: " + pkg);
    	
    }

}

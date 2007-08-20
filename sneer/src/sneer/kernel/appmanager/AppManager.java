package sneer.kernel.appmanager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.SneerDirectories;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Contact;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class AppManager {
	
	private static final String JAR_NAME = "app.zip";
	private ListSource<SovereignApplicationUID> _publishedApps = new ListSourceImpl<SovereignApplicationUID>();
	private User _user;
	private Communicator _communicator;
	private ListSignal<Contact> _contacts;
	
	public AppManager(User user, Communicator communicator, ListSignal<Contact> contacts){
		_user = user;
		_communicator = communicator;
		_contacts = contacts;
	}

	private void createDirectories(){ //should be moved to install???
		SneerDirectories.appsDirectory().mkdirs();
		SneerDirectories.compiledAppsDirectory().mkdirs();
		SneerDirectories.appSourceCodesDirectory().mkdirs();
	}
	
	public void rebuild(){
		AppTools.removeRecursive(SneerDirectories.compiledAppsDirectory());
		AppTools.removeRecursive(SneerDirectories.appSourceCodesDirectory());
		for(SovereignApplicationUID app:_publishedApps.output())
			_publishedApps.remove(app);
		
	}
	
	public void removeAppDirectories(String installName){
		AppTools.removeRecursive(new File(SneerDirectories.appsDirectory(),installName));
		AppTools.removeRecursive(new File(SneerDirectories.appSourceCodesDirectory(),installName));
		AppTools.removeRecursive(new File(SneerDirectories.compiledAppsDirectory(),installName));
		rebuild();
	}
	
	public void publish(File originalSourceDirectory) { //Fix: what if the app is already installed? test appuid
	
		File packagedTempDirectory = AppTools.createTempDirectory("packaged");
		File sourceTempDirectory = AppTools.createTempDirectory("source");
		File compiledTempDirectory = AppTools.createTempDirectory("compiled");
		
		try{
			packageApp(originalSourceDirectory, packagedTempDirectory);
		
			processApp(packagedTempDirectory, sourceTempDirectory, compiledTempDirectory);
			SovereignApplication app = loadApp(compiledTempDirectory);
			
			String installName = AppTools.uniqueName(app.defaultName());
			
			copyToFinalPlace(packagedTempDirectory, sourceTempDirectory, compiledTempDirectory, installName);
			
		}catch(Exception e){
			e.printStackTrace();
			Log.log(e);
			AppTools.removeRecursive(packagedTempDirectory);
			AppTools.removeRecursive(sourceTempDirectory);
			AppTools.removeRecursive(compiledTempDirectory);
		}
		
	}
	
	private void copyToFinalPlace(File packagedTempDirectory, File sourceTempDirectory, File compiledTempDirectory, String installName) throws IOException{
		File packagedDirectory = new File(SneerDirectories.appsDirectory(),installName);
		File sourceDirectory = new File(SneerDirectories.appSourceCodesDirectory(),installName);
		File compiledDirectory = new File(SneerDirectories.compiledAppsDirectory(),installName);
		try{
			packagedDirectory.mkdirs();
			sourceDirectory.mkdirs();
			compiledDirectory.mkdirs();
			AppTools.copyRecursive(packagedTempDirectory, packagedDirectory);
			AppTools.copyRecursive(sourceTempDirectory, sourceDirectory);
			AppTools.copyRecursive(compiledTempDirectory, compiledDirectory);
		}catch(Exception e){
			removeAppDirectories(installName);
			throw new IOException("Could not copy to final directories");
		}
	}

	@SuppressWarnings("deprecation")
	private SovereignApplication loadApp(File compiledAppDirectory) throws Exception {
		File classesDirectory = new File(compiledAppDirectory,"classes");
		File applicationFile = AppTools.findApplicationClass(compiledAppDirectory);
		String packageName = AppTools.pathToPackage(classesDirectory, applicationFile.getParentFile());
		URL[] urls = new URL[]{classesDirectory.toURL()}; //in the future libs directory will be added here
		URLClassLoader ucl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());  
		Class<?> clazz = ucl.loadClass(packageName+".Application"); 
		AppConfig config = new AppConfig(_user, new AppChannelFactory(_communicator), _contacts, _publishedApps.output());
		Class<?>[] types = {AppConfig.class};
		Object[] instances = {config};
		Constructor<?> constructor = clazz.getConstructor(types);
		return (SovereignApplication) constructor.newInstance(instances);
	}
	
	private void packageApp(File sourceDirectory, File targetDirectory){
		try{
			File zipFile = new File(targetDirectory,JAR_NAME);
			AppTools.zip(sourceDirectory, zipFile);
			AppTools.generateAppUID(zipFile);
		}catch(Exception e){
			Log.log(e);
			e.printStackTrace();
		}
	}
	
	private void processApp(File packagedDirectory, File sourceDirectory, File compiledDirectory) throws Exception{	
			File zipFile = new File(packagedDirectory, JAR_NAME);
			AppTools.unzip(zipFile, sourceDirectory);
			File ApplicationSourceFile = AppTools.findApplicationSource(sourceDirectory);
			File[] sources = new File[]{ApplicationSourceFile};
			compile(sources, sourceDirectory, compiledDirectory);
	}
	
	public SovereignApplication appByUID(String appUID){
		for(SovereignApplicationUID app:_publishedApps.output())
			if (app._appUID.equals(appUID))
				return app._sovereignApplication;
		return null;
	}
	
	private void compile(File[] sources, File sourceDirectory, File targetDirectory) throws Exception{
		File targetClassesDirectory = new File(targetDirectory,"classes");
		targetClassesDirectory.mkdirs();
		String sneerJarLocation = null;
		try{
			sneerJarLocation = AppTools.tryToFindSneerLocation().getAbsolutePath();
		}catch(Exception e){
			Log.log(e);
			e.printStackTrace();
			_user.acknowledgeNotification("Sneer.jar not found. If you are not running from the jar (from eclipse for example) you need SneerXXXX.jar as the ONLY jar in the .bin directory.");
			throw new IOException("Could not find Sneer.jar!!!!");
		}
		
		System.out.println(sneerJarLocation);
		for(File source:sources){	
			System.out.println("Compiling "+source.getName());
			String[] parameters = {"-classpath",sneerJarLocation+File.pathSeparator+targetClassesDirectory.getAbsolutePath(),"-sourcepath",sourceDirectory.getAbsolutePath()+"/src","-d",targetClassesDirectory.getAbsolutePath(),source.getAbsolutePath()};
			com.sun.tools.javac.Main.compile(parameters);
		}
	}
	
	public ListSource<SovereignApplicationUID> publishedApps(){
		createDirectories();

		loadApps();
		
		return _publishedApps;
	}

	private void loadApps() {
		File[] compiledAppDirectories = SneerDirectories.compiledAppsDirectory().listFiles();
		for(File compiledAppDirectory:compiledAppDirectories){
			String candidateApp = compiledAppDirectory.getName();
			if (isAppLoaded(candidateApp))
				continue;
			try{
				File appUIDFile = AppTools.findAppUID(new File(SneerDirectories.appsDirectory(),compiledAppDirectory.getName()));
				String appUID = new String(AppTools.getBytesFromFile(appUIDFile));
				_publishedApps.add(new SovereignApplicationUID(compiledAppDirectory.getName(),appUID,loadApp(compiledAppDirectory)));
			}catch(Exception e){
				Log.log(e);
				e.printStackTrace();
			}
		}
	}

	private boolean isAppLoaded(String appName) {
		for(SovereignApplicationUID app:_publishedApps.output())
			if (app._appName.equals(appName))
				return true;
		return false;
	}

}

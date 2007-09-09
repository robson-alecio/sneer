package sneer.kernel.appmanager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import sneer.SneerDirectories;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Party;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class AppManager {

	public static final String JAR_NAME = "app.zip";

	private ListSource<SovereignApplicationUID> _publishedApps = new ListSourceImpl<SovereignApplicationUID>();

	private User _user;
	private Communicator _communicator;
	private ListSignal<ContactAttributes> _contactAttributes;
	private final Omnivore<Notification> _briefUserNotifier;

	private final Party _me;

	public AppManager(User user, Communicator communicator, Party me, ListSignal<ContactAttributes> contactAttributes, Omnivore<Notification> briefUserNotifier) {
		_user = user;
		_me = me;
		_communicator = communicator;
		_contactAttributes = contactAttributes;
		_briefUserNotifier = briefUserNotifier;
	}

	private void createDirectories() { //should be moved to install???
		SneerDirectories.appsDirectory().mkdirs();
		SneerDirectories.compiledAppsDirectory().mkdirs();
		SneerDirectories.appSourceCodesDirectory().mkdirs();
	}

	public void removeApp(String installName) {
		AppTools.removeRecursive(new File(SneerDirectories.appsDirectory(), installName));
		AppTools.removeRecursive(new File(SneerDirectories.appSourceCodesDirectory(), installName));
		AppTools.removeRecursive(new File(SneerDirectories.compiledAppsDirectory(), installName));
		SovereignApplicationUID app = findByName(installName);
		if (app==null)
			return;
		_publishedApps.remove(app);
	}
	
	public SovereignApplicationUID findByName(String installName){
		for(SovereignApplicationUID candidate:_publishedApps.output())
			if (candidate._installName.equals(installName))
				return candidate;
		return null;
	}

	public String publish(File originalSourceDirectory) { //Fix: what if the app is already installed? test appuid
		String installName = null;
		
		File packagedTempDirectory = AppTools.createTempDirectory("packaged");
		File sourceTempDirectory = AppTools.createTempDirectory("source");
		File compiledTempDirectory = AppTools.createTempDirectory("compiled");

		try {
			String appUID = packageApp(originalSourceDirectory, packagedTempDirectory);

			processApp(packagedTempDirectory, sourceTempDirectory, compiledTempDirectory);
			SovereignApplicationInfo info = discoverApplicationInfo(compiledTempDirectory);
			
			installName = info.defaultName()+"-"+appUID;

			copyToFinalPlace(packagedTempDirectory, sourceTempDirectory, compiledTempDirectory, installName);

			SovereignApplication app = startApp(new File(SneerDirectories.compiledAppsDirectory(), installName), info);
			registerApp(installName,app,info);

		} catch (Exception e) {
			e.printStackTrace();
			Log.log(e);
		}
		AppTools.removeRecursive(packagedTempDirectory);
		AppTools.removeRecursive(sourceTempDirectory);
		AppTools.removeRecursive(compiledTempDirectory);
		return installName;
	}
	
	public String publishFromZipFile(File zipFile){ //for metoo transfers
		String installName = null;
		File tempDirectory = AppTools.createTempDirectory("packaged");
		try {
			AppTools.unzip(zipFile, tempDirectory);
			installName = publish(tempDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			Log.log(e);
		}
		AppTools.removeRecursive(tempDirectory);
		return installName;
	}

	private void copyToFinalPlace(File packagedTempDirectory, File sourceTempDirectory, File compiledTempDirectory, String installName) throws IOException {
		File packagedDirectory = new File(SneerDirectories.appsDirectory(), installName);
		File sourceDirectory = new File(SneerDirectories.appSourceCodesDirectory(), installName);
		File compiledDirectory = new File(SneerDirectories.compiledAppsDirectory(), installName);
		try {
			packagedDirectory.mkdirs();
			sourceDirectory.mkdirs();
			compiledDirectory.mkdirs();
			AppTools.copyRecursive(packagedTempDirectory, packagedDirectory);
			AppTools.copyRecursive(sourceTempDirectory, sourceDirectory);
			AppTools.copyRecursive(compiledTempDirectory, compiledDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			Log.log(e);
			removeApp(installName);
			throw new IOException("Could not copy to final directories");
		}
	}

	private SovereignApplication startApp(File compiledAppDirectory, SovereignApplicationInfo info) throws Exception {
		File classesDirectory = new File(compiledAppDirectory, "classes");
		List<URL> pathList = classpath(classesDirectory);
		URL[] urls =  pathList.toArray(new URL[0]); 
		File applicationFile = AppTools.findApplicationClass(compiledAppDirectory);
		String packageName = AppTools.pathToPackage(classesDirectory, applicationFile.getParentFile());
		
		SovereignApplication app = (SovereignApplication)test1(packageName+ ".Application", urls).newInstance();
		
		startApp(app, info);
		
		return app;
	}
	
	private SovereignApplicationInfo discoverApplicationInfo(File compiledAppDirectory) throws MalformedURLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException{
		File classesDirectory = new File(compiledAppDirectory, "classes");
		List<URL> pathList = classpath(classesDirectory);
		URL[] urls =  pathList.toArray(new URL[0]); 
		
		File applicationInfoFile = AppTools.findApplicationInfo(compiledAppDirectory);
		String packageName = AppTools.pathToPackage(classesDirectory, applicationInfoFile.getParentFile());
		
		return (SovereignApplicationInfo)test1(packageName + ".ApplicationInfo",urls).newInstance();
	}

	@SuppressWarnings("unused")
	private Class<?> test1(String completeName, URL[] urls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		URLClassLoader ucl = new URLClassLoader(urls, this.getClass().getClassLoader());
		Class<?> clazz = ucl.loadClass(completeName);
		System.out.println("test 1 - loaded by: "+ clazz.getClassLoader().getClass().getName());
		return clazz;
	}
	
	@SuppressWarnings("unused")
	private Class<?> test2(String completeName, URL[] urls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		URLClassLoader systemClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class<?> sysLoaderClass = URLClassLoader.class;
		Method method = sysLoaderClass.getDeclaredMethod("addURL", new Class[] {URL.class});
		method.setAccessible(true);
		for(URL url:urls)
			method.invoke(systemClassLoader, new Object[] {url});
		Class<?> clazz = systemClassLoader.loadClass(completeName);
		System.out.println("test 2 - loaded by: "+ clazz.getClassLoader().getClass().getName());
		return clazz;
	}
	
	@SuppressWarnings("unused")
	private Class<?> test3(String completeName, URL[] urls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		URLClassLoader ucl = new URLClassLoader(urls, SovereignApplication.class.getClassLoader());
		URLClassLoader systemClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class<?> sysLoaderClass = URLClassLoader.class;
		Method method = sysLoaderClass.getDeclaredMethod("addURL", new Class[] {URL.class});
		method.setAccessible(true);
		for(URL url:urls)
			method.invoke(systemClassLoader, new Object[] {url});
		Class<?> clazz = ucl.loadClass(completeName);
		System.out.println("test 3 - loaded by: "+ clazz.getClassLoader().getClass().getName());
		return clazz;
	}

	private List<URL> classpath(File classesDirectory) throws MalformedURLException {
		String classpath = System.getProperty("java.class.path");
		String[] paths = classpath.split(File.pathSeparator);
		List<URL> pathList = new ArrayList<URL>();
		pathList.add(classesDirectory.toURI().toURL());
		for(String temp:paths)
			pathList.add((new File(temp)).toURI().toURL());
		return pathList;
	}
	
	public void startApp(SovereignApplication app, SovereignApplicationInfo info){
		app.start(currentAppConfig(info));
	}

	private AppConfig currentAppConfig(SovereignApplicationInfo info) {
		return new AppConfig(_user, _communicator.getChannel(info.defaultName(), info.trafficPriority()), _me.contacts(), _contactAttributes, _me.name(), _briefUserNotifier, null);  //FixUrgent Create the blower passing the [packagedDirectory]/prevalence directory.
	}

	private String packageApp(File sourceDirectory, File targetDirectory) {
		try {
			File zipFile = new File(targetDirectory, JAR_NAME);
			AppTools.zip(sourceDirectory, zipFile);
			return AppTools.generateAppUID(zipFile);
		} catch (Exception e) {
			Log.log(e);
			e.printStackTrace();
		}
		return null;
	}

	private void processApp(File packagedDirectory, File sourceDirectory, File compiledDirectory) throws Exception {
		File zipFile = new File(packagedDirectory, JAR_NAME);
		AppTools.unzip(zipFile, sourceDirectory);
		File ApplicationSourceFile = AppTools.findApplicationSource(sourceDirectory);
		File ApplicationInfoSourceFile = AppTools.findApplicationInfoSource(sourceDirectory);
		File[] sources = new File[] { ApplicationSourceFile, ApplicationInfoSourceFile};
		compile(sources, sourceDirectory, compiledDirectory);
	}

	public SovereignApplication appByUID(String appUID) {
		for (SovereignApplicationUID app : _publishedApps.output())
			if (app._appUID.equals(appUID))
				return app._sovereignApplication;
		return null;
	}

	private void compile(File[] sources, File sourceDirectory, File targetDirectory) throws Exception {
		File targetClassesDirectory = new File(targetDirectory, "classes");
		targetClassesDirectory.mkdirs();
		String sneerJarLocation = null;
		try {
			sneerJarLocation = AppTools.tryToFindSneerLocation().getAbsolutePath();
		} catch (Exception e) {
			Log.log(e);
			e.printStackTrace();
			_user.acknowledgeNotification("Sneer.jar not found. If you are not running from the jar (from eclipse for example) you need SneerXXXX.jar as the ONLY jar in the .bin directory.");
			throw new IOException("Could not find Sneer.jar!!!!");
		}

		System.out.println(sneerJarLocation);
		for (File source : sources) {
			System.out.println("Compiling " + source.getName());
			String[] parameters = { "-classpath", sneerJarLocation + File.pathSeparator + targetClassesDirectory.getAbsolutePath(), "-sourcepath", sourceDirectory.getAbsolutePath() + "/src", "-d", targetClassesDirectory.getAbsolutePath(), source.getAbsolutePath() };
			com.sun.tools.javac.Main.compile(parameters);
		}
	}

	public ListSource<SovereignApplicationUID> publishedApps() {
		createDirectories();
		try {
			loadApps();
		} catch (Exception e) {
			Log.log(e);
			e.printStackTrace();
		}
		return _publishedApps;
	}

	private void loadApps() throws Exception {
		File[] compiledAppDirectories = SneerDirectories.compiledAppsDirectory().listFiles();
		for (File compiledAppDirectory : compiledAppDirectories) {
			String candidateApp = compiledAppDirectory.getName();
			if (isAppLoaded(candidateApp))
				continue;
			SovereignApplicationInfo info = discoverApplicationInfo(compiledAppDirectory);
			SovereignApplication app = startApp(compiledAppDirectory, info);
			registerApp(compiledAppDirectory.getName(), app, info);
		}
	}

	public void registerApp(String installName, SovereignApplication app, SovereignApplicationInfo info) throws IOException{
		System.out.println("Registering new Application: " + installName);
		File appUIDFile = AppTools.findAppUID(new File(SneerDirectories.appsDirectory(), installName));
		String appUID = new String(AppTools.getBytesFromFile(appUIDFile));
		_publishedApps.add(new SovereignApplicationUID(installName, appUID, app, info));
	}

	private boolean isAppLoaded(String installName) {
		for (SovereignApplicationUID app : _publishedApps.output())
			if (app._installName.equals(installName))
				return true;
		return false;
	}

}

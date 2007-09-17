package sneer.kernel.appmanager;

import java.io.File;
import java.io.IOException;

import sneer.SneerDirectories;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Party;
import wheel.io.Log;
import wheel.io.ModifiedURLClassLoader;
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

	private final ModifiedURLClassLoader _classloader;

	public AppManager(User user, Communicator communicator, Party me, ListSignal<ContactAttributes> contactAttributes, Omnivore<Notification> briefUserNotifier, ModifiedURLClassLoader classloader) {
		_user = user;
		_me = me;
		_communicator = communicator;
		_contactAttributes = contactAttributes;
		_briefUserNotifier = briefUserNotifier;
		_classloader = classloader;
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
		_communicator.removeChannel(app._info.defaultName());
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
	
	//Refactor: in the future convert to wheel.io.File
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
		File applicationFile = AppTools.findApplicationClass(compiledAppDirectory);
		String packageName = AppTools.pathToPackage(classesDirectory, applicationFile.getParentFile());
		SovereignApplication app = (SovereignApplication)loadClass(packageName+ ".Application", classesDirectory.getAbsolutePath()).newInstance();
		startApp(app, info);
		return app;
	}
	
	private SovereignApplicationInfo discoverApplicationInfo(File compiledAppDirectory) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException{
		File classesDirectory = new File(compiledAppDirectory, "classes");
		File applicationInfoFile = AppTools.findApplicationInfo(compiledAppDirectory);
		String packageName = AppTools.pathToPackage(classesDirectory, applicationInfoFile.getParentFile());
		return (SovereignApplicationInfo)loadClass(packageName + ".ApplicationInfo",classesDirectory.getAbsolutePath()).newInstance();
	}

	//individual classloaders
	/*private Class<?> loadClass(String completeName, String path) throws ClassNotFoundException, IllegalArgumentException {
		ModifiedURLClassLoader classloader = new ModifiedURLClassLoader(_classloader);
		classloader.addPath(path);
		Class<?> clazz = classloader.loadClass(completeName);
		return clazz;
	}*/
	
	//same classloader
	private Class<?> loadClass(String completeName, String path) throws ClassNotFoundException, IllegalArgumentException {
		_classloader.addPath(path);
		Class<?> clazz = _classloader.loadClass(completeName);
		return clazz;
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

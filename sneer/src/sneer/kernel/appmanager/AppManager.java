package sneer.kernel.appmanager;

import static wheel.i18n.Language.translate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipException;

import sneer.SneerDirectories;
import sneer.apps.asker.Asker;
import sneer.apps.transferqueue.TransferQueue;
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

	private final Asker _asker;

	private final TransferQueue _transfer;

	public AppManager(User user, Communicator communicator, Party me, ListSignal<ContactAttributes> contactAttributes, Omnivore<Notification> briefUserNotifier, Asker asker, TransferQueue transfer) {
		_user = user;
		_me = me;
		_communicator = communicator;
		_contactAttributes = contactAttributes;
		_briefUserNotifier = briefUserNotifier;
		_asker = asker;
		_transfer = transfer;
		createDirectories();
	}

	private void createDirectories() { //should be moved to install???
		SneerDirectories.appsDirectory().mkdirs();
		SneerDirectories.compiledAppsDirectory().mkdirs();
		SneerDirectories.appSourceCodesDirectory().mkdirs();
		AppTools.cleanupTempDirectory();
	}

	public void removeApp(String installName) {
		AppTools.removeRecursive(new File(SneerDirectories.appsDirectory(), installName));
		AppTools.removeRecursive(new File(SneerDirectories.appSourceCodesDirectory(), installName));
		AppTools.removeRecursive(new File(SneerDirectories.compiledAppsDirectory(), installName));
		SovereignApplicationUID app = findByName(installName);
		if (app==null)
			return;
		_communicator.crashChannel(app._sovereignApplication.defaultName());
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
			SovereignApplication tempApp = loadApp(compiledTempDirectory);
			
			installName = tempApp.defaultName()+"-"+appUID;

			copyToFinalPlace(packagedTempDirectory, sourceTempDirectory, compiledTempDirectory, installName);

			SovereignApplication app = loadApp(new File(SneerDirectories.compiledAppsDirectory(), installName));
			startApp(app);
			registerApp(installName,app);

		} catch (CompilationFailure e) {
		} catch (Exception e) {
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

	private SovereignApplication loadApp(File compiledAppDirectory) throws Exception {
		File classesDirectory = new File(compiledAppDirectory, "classes");
		File applicationFile = AppTools.findApplicationClass(compiledAppDirectory);
		String packageName = AppTools.pathToPackage(classesDirectory, applicationFile.getParentFile());
		SovereignApplication app = (SovereignApplication)loadClass(packageName+ ".Application", classesDirectory.getAbsolutePath()).newInstance();
		return app;
	}

	private Class<?> loadClass(String completeName, String path) throws ClassNotFoundException, IllegalArgumentException {
		URL[] urls;
		try {
			urls = new URL[] {new File(path).toURI().toURL()};
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException();
		}
		URLClassLoader classloader = new URLClassLoader(urls, this.getClass().getClassLoader());
		return classloader.loadClass(completeName);
	}

	public void startApp(SovereignApplication app){
		AppConfig config = new AppConfig(_user, _communicator.openChannel(app.defaultName(), app.trafficPriority(), app.getClass().getClassLoader()), _me.contacts(), _contactAttributes, _me.name(), _briefUserNotifier, null,_asker, _transfer);  //FixUrgent Create the blower passing the [packagedDirectory]/prevalence directory.
		app.start(config);
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

	private void processApp(File packagedDirectory, File sourceDirectory, File compiledDirectory) throws ZipException, IOException, CompilationFailure {
		File zipFile = new File(packagedDirectory, JAR_NAME);
		AppTools.unzip(zipFile, sourceDirectory);
		File ApplicationSourceFile = AppTools.findApplicationSource(sourceDirectory);
		File[] sources = new File[] { ApplicationSourceFile };
		compile(sources, sourceDirectory, compiledDirectory);
	}

	public SovereignApplication appByUID(String appUID) {
		for (SovereignApplicationUID app : _publishedApps.output())
			if (app._appUID.equals(appUID))
				return app._sovereignApplication;
		return null;
	}

	private void compile(File[] sources, File sourceDirectory, File targetDirectory) throws IOException, CompilationFailure {
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
			String[] parameters = { "-classpath", sneerJarLocation + File.pathSeparator + targetClassesDirectory.getAbsolutePath(), "-sourcepath", sourceDirectory.getAbsolutePath() , "-d", targetClassesDirectory.getAbsolutePath(), source.getAbsolutePath() };
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (com.sun.tools.javac.Main.compile(parameters, new PrintWriter(out))!=0){
				Log.log(out.toString());
				_user.acknowledgeNotification(translate("Compile Error. See the Sneer log file for details."));
				throw new CompilationFailure();
			}
		}
	}
	
	public class CompilationFailure extends Exception{

		private static final long serialVersionUID = 1L;
		
	}

	public ListSource<SovereignApplicationUID> publishedApps() {
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
			
			SovereignApplication app = loadApp(compiledAppDirectory);
			startApp(app);
			registerApp(compiledAppDirectory.getName(), app);
		}
	}

	public void registerApp(String installName, SovereignApplication app) throws IOException{
		System.out.println("Registering new Application: " + installName);
		File appUIDFile = AppTools.findAppUID(new File(SneerDirectories.appsDirectory(), installName));
		String appUID = new String(AppTools.getBytesFromFile(appUIDFile));
		_publishedApps.add(new SovereignApplicationUID(installName, appUID, app));
	}

	private boolean isAppLoaded(String installName) {
		for (SovereignApplicationUID app : _publishedApps.output())
			if (app._installName.equals(installName))
				return true;
		return false;
	}

}

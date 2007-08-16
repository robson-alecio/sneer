package sneer.kernel.appmanager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import sneer.SneerDirectories;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Contact;
import wheel.io.Jars;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class AppManager {
	
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
		removeRecursive(SneerDirectories.compiledAppsDirectory());
		removeRecursive(SneerDirectories.appSourceCodesDirectory());
		//_installedApps.clear();
		for(SovereignApplicationUID app:_publishedApps.output())
			_publishedApps.remove(app);
		
	}
	
	private void install(String appName, File jarFile) throws IOException{
		File installDirectory = new File(SneerDirectories.appsDirectory(),appName);
		installDirectory.mkdir();
		AppTools.copy(jarFile,new File(installDirectory,jarFile.getName()));
		rebuild();
	}
	
	public void remove(String appName){
		removeRecursive(new File(SneerDirectories.appsDirectory(),appName));
		removeRecursive(new File(SneerDirectories.appSourceCodesDirectory(),appName));
		removeRecursive(new File(SneerDirectories.compiledAppsDirectory(),appName));
		rebuild();
	}
	
	public void publish(File srcFolder, String appUID) throws IOException{
		File tempFile = File.createTempFile("appSneer", ".zip");
		AppTools.zip(srcFolder, tempFile);
		install(appUID, tempFile);
		tempFile.delete();
	}
	
	public SovereignApplication appByUID(String appUID){
		for(SovereignApplicationUID app:_publishedApps.output())
			if (app._uid.equals(appUID))
				return app._sovereignApplication;
		return null;
	}
	
	public boolean isAppPublished(String appUID){
		return (appByUID(appUID)!=null);
	}
	
	private void unpackageApps(){
		for(File appDirectory:notUnpackagedApps()){
			File jar = appDirectory.listFiles()[0]; //first file should be the jar
			File target = new File(SneerDirectories.appSourceCodesDirectory(),appDirectory.getName());
			target.mkdir();
			System.out.println("Unpackaging "+jar.getName());
			try{
				AppTools.unzip(jar,target);
			}catch(Exception e){
				target.delete();
				Log.log(e);
				e.printStackTrace();
			}
		}
	}
	
	private List<File> notUnpackagedApps(){ 
		List<File> notUnpackagedApps = new ArrayList<File>();
		for(File appDirectory:SneerDirectories.appsDirectory().listFiles()){
			if (alreadyUnpackaged(appDirectory))
				continue;
			notUnpackagedApps.add(appDirectory);	
		}
		return notUnpackagedApps;
	}
	
	private void compileApps(){
		for(File sourceDirectory:notCompiledApps()){

			String targetDirectory=SneerDirectories.compiledAppsDirectory()+"/"+sourceDirectory.getName()+"/"+"classes";
			String sourceApplication = AppTools.findFile(sourceDirectory, new FilenameFilter(){
				public boolean accept(File dir, String name) {
					return (name.equals("Application.java"));
				}
			}).getAbsolutePath();
			(new File(targetDirectory)).mkdirs();

			System.out.println("Compiling "+sourceApplication);
			System.out.println(tryToFindSneerLocation().getAbsolutePath());
			try{
				String[] parameters = {"-classpath",tryToFindSneerLocation().getAbsolutePath()+File.pathSeparator+targetDirectory,"-sourcepath",sourceDirectory.getAbsolutePath()+"/src","-d",targetDirectory,sourceApplication};
				com.sun.tools.javac.Main.compile(parameters);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Could not compile "+sourceApplication); //Fix: use log
				removeRecursive(new File(targetDirectory));
			}
		}
	}
	
	private void removeRecursive(File file){
		if (file.isDirectory())
			for(File children:file.listFiles())
				removeRecursive(children);
		file.delete();
	}
	
	private File tryToFindSneerLocation() {
		try{
			URL url = Jars.jarGiven(AppManager.class);
			return urlToFile(url);
		}catch(Exception e){
			File eclipseProjectRoot = new File("."); //fallback. if it is not running inside jar, try to find jar from bin directory.
			File result = firstJarInDirectory(new File(eclipseProjectRoot,"bin"));
			if (result == null) _user.acknowledgeNotification("Sneer.jar not found. If you are not running from the jar (from eclipse for example) you need SneerXXXX.jar as the ONLY jar in the .bin directory.");
			return result;
		}
	}

	private File firstJarInDirectory(File eclipseProjectBin) {
		for(File file:eclipseProjectBin.listFiles()){
			if (file.getName().endsWith(".jar"))
				return file;
		}
		return null;
	}
	
	static File urlToFile(URL url) {
        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            try {
                uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            } catch (URISyntaxException e1) {
                throw new IllegalArgumentException("broken URL: " + url);
            }

        }
        return new File(uri);
    }
	
	public ListSource<SovereignApplicationUID> publishedApps(){
		createDirectories();
		unpackageApps();
		compileApps();
		loadApps();
		return _publishedApps;
	}

	private void loadApps() {
		File[] compiledAppDirectories = SneerDirectories.compiledAppsDirectory().listFiles();
		for(File compiledAppDirectory:compiledAppDirectories){
			if (isAppPublished(compiledAppDirectory.getName()))
				continue;
			_publishedApps.add(new SovereignApplicationUID(appLoad(compiledAppDirectory),compiledAppDirectory.getName()));
		}
	}

	@SuppressWarnings("deprecation")
	private SovereignApplication appLoad(File compiledAppDirectory) {
			File classesDirectory = new File(compiledAppDirectory,"classes");
			File applicationFile = AppTools.findFile(compiledAppDirectory, new FilenameFilter(){
				public boolean accept(File dir, String name) {
					return name.equals("Application.class");
				}
			});
			String packageName = AppTools.pathToPackage(classesDirectory, applicationFile.getParentFile());
			try {
				URL[] urls = new URL[]{classesDirectory.toURL()}; //in the future libs directory will be added here
				URLClassLoader ucl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());  
				Class<?> clazz = ucl.loadClass(packageName+".Application"); 
				AppConfig config = new AppConfig(_user,new AppChannelFactory(_communicator),_contacts);
				Class<?>[] types = {AppConfig.class};
				Object[] instances = {config};
				Constructor<?> constructor = clazz.getConstructor(types);
				return (SovereignApplication) constructor.newInstance(instances);
			} catch (Exception e) {
				Log.log(e);
				e.printStackTrace();
			}  
		return null;

	}
	
	private List<File> notCompiledApps(){ 
		List<File> notCompiledApps = new ArrayList<File>();
		for(File sourceDirectory:SneerDirectories.appSourceCodesDirectory().listFiles()){
			if (alreadyCompiled(sourceDirectory))
				continue;
			notCompiledApps.add(sourceDirectory);	
		}
		return notCompiledApps;
	}
	
	private boolean alreadyUnpackaged(File appDirectory) {
		for(File sourceCodeDirectory:SneerDirectories.appSourceCodesDirectory().listFiles()){
			if (sourceCodeDirectory.getName().equals(appDirectory.getName()))
				return true;
		}
		return false;
	}

	private boolean alreadyCompiled(File sourceDirectory) {
		for(File compiledDirectory:SneerDirectories.compiledAppsDirectory().listFiles()){
			if (sourceDirectory.getName().equals(compiledDirectory.getName()))
				return true;
		}
		return false;
	}
	
}

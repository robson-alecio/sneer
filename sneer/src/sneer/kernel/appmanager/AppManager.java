package sneer.kernel.appmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import sneer.SneerDirectories;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Contact;
import wheel.io.Jars;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;

public class AppManager {
	
	private Map<String,App> _installedApps = new Hashtable<String,App>();
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
		_installedApps.clear();
	}
	
	public void install(String appName, File jarFile) throws IOException{
		File installDirectory = new File(SneerDirectories.appsDirectory(),appName);
		installDirectory.mkdir();
		copy(jarFile,new File(installDirectory,jarFile.getName()));
		rebuild();
	}
	
	private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        out.close();
    }
	
	public void remove(String appName){
		removeRecursive(new File(SneerDirectories.appsDirectory(),appName));
		removeRecursive(new File(SneerDirectories.appSourceCodesDirectory(),appName));
		removeRecursive(new File(SneerDirectories.compiledAppsDirectory(),appName));
		rebuild();
	}
	
	
	private void unpackageApps(){
		for(File appDirectory:notUnpackagedApps()){
			File jar = appDirectory.listFiles()[0]; //first file should be the jar
			File target = new File(SneerDirectories.appSourceCodesDirectory(),appDirectory.getName());
			target.mkdir();
			System.out.println("Unpackaging "+jar.getName());
			try{
				unzip(jar,target);
			}catch(Exception e){
				System.out.println("Could not Unpackage "+jar.getName()); //Fix: use log
				target.delete();
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

			String targetDirectory=SneerDirectories.compiledAppsDirectory()+File.separator+sourceDirectory.getName();
			String sourceApplication=sourceDirectory+File.separator+"sneer"+File.separator+"apps"+File.separator+sourceDirectory.getName()+File.separator+"Main.java";
			(new File(targetDirectory)).mkdir();
			System.out.println("Compiling "+sourceApplication);
			System.out.println(tryToFindSneerLocation().getAbsolutePath()); //Attention... make sure you have an updated Sneer.jar by regenerating it using build.xml
			try{
				String[] parameters = {"-classpath",tryToFindSneerLocation().getAbsolutePath()+File.pathSeparator+sourceDirectory.getPath(),"-d",targetDirectory,sourceApplication};
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
			File eclipseProjectRoot = new File("."); //fallback. if it is not running inside jar, try to find jar from jarbuilder directory.
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
	
	public Map<String,App> installedApps(){
		createDirectories();
		unpackageApps();
		compileApps();
		loadApps();
		return _installedApps;
	}

	private void loadApps() {
		File[] compiledAppDirectories = SneerDirectories.compiledAppsDirectory().listFiles();
		for(File compiledAppDirectory:compiledAppDirectories){
			if (isAppLoaded(compiledAppDirectory))
				continue;
			_installedApps.put(compiledAppDirectory.getName(),appLoad(compiledAppDirectory));
		}
	}

	@SuppressWarnings("deprecation")
	private App appLoad(File compiledAppDirectory) {
			try {
				URL[] urls = new URL[]{compiledAppDirectory.toURL()};
				URLClassLoader ucl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());  
				Class<?> clazz = ucl.loadClass(compiledAppDirectory.getName()+".Application"); 
				AppConfig config = new AppConfig(_user,new AppChannelFactory(_communicator),_contacts);
				Class<?>[] types = {AppConfig.class};
				Object[] instances = {config};
				Constructor<?> constructor = clazz.getConstructor(types);
				return (App) constructor.newInstance(instances);
			} catch (Exception e) {
				Log.log(e);
				e.printStackTrace();
			}  
		return null;
	}
	
	private boolean isAppLoaded(File compiledAppDirectory){
		return (_installedApps.get(compiledAppDirectory.getName())!=null);
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
	
	static final int _BUFFER = 2048;
	
	private void unzip(File source, File target) throws Exception{
	         BufferedOutputStream dest = null;
	         BufferedInputStream is = null;
	         ZipEntry entry;
	         ZipFile zipfile = new ZipFile(source);
	         Enumeration<?> e = zipfile.entries();
	         while(e.hasMoreElements()) {
	            entry = (ZipEntry) e.nextElement();
	            System.out.println("Extracting: " +entry);
	            File targetFile = new File(target.getPath()+File.separator+entry.getName());
	            if (entry.isDirectory()){
	            	targetFile.mkdirs();
	            	continue;
	            }
	            is = new BufferedInputStream(zipfile.getInputStream(entry));
	            int count;
	            byte data[] = new byte[_BUFFER];
	            
	            FileOutputStream fos = new FileOutputStream(targetFile);
	            dest = new BufferedOutputStream(fos, _BUFFER);
	            while ((count = is.read(data, 0, _BUFFER)) 
	              != -1) {
	               dest.write(data, 0, count);
	            }
	            dest.flush();
	            dest.close();
	            is.close();
	         }
	}
	
}

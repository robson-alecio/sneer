package sneer.kernel.appmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
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
import wheel.io.Jars;

public class AppManager {
	
	private static Map<String,App> _installedApps = new Hashtable<String,App>();
	
	private AppManager(){
	}
	
	private static void createDirectories(){ //should be moved to install???
		SneerDirectories.appsDirectory().mkdirs();
		SneerDirectories.compiledAppsDirectory().mkdirs();
		SneerDirectories.appSourceCodesDirectory().mkdirs();
	}
	
	public static void rebuild(){
		removeRecursive(SneerDirectories.compiledAppsDirectory());
		removeRecursive(SneerDirectories.appSourceCodesDirectory());
		_installedApps.clear();
	}
	
	
	private static void unpackageApps(){
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
	
	private static List<File> notUnpackagedApps(){ 
		List<File> notUnpackagedApps = new ArrayList<File>();
		for(File appDirectory:SneerDirectories.appsDirectory().listFiles()){
			if (alreadyUnpackaged(appDirectory))
				continue;
			notUnpackagedApps.add(appDirectory);	
		}
		return notUnpackagedApps;
	}
	
	private static void compileApps(){
		for(File sourceDirectory:notCompiledApps()){

			String targetDirectory=SneerDirectories.compiledAppsDirectory()+File.separator+sourceDirectory.getName();
			String sourceApplication=sourceDirectory+File.separator+"sneer"+File.separator+"apps"+File.separator+sourceDirectory.getName()+File.separator+"Main.java";
			(new File(targetDirectory)).mkdir();
			System.out.println("Compiling "+sourceApplication);
			System.out.println(tryToFindSneerLocation().getAbsolutePath());
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
	
	private static void removeRecursive(File file){
		if (file.isDirectory())
			for(File children:file.listFiles())
				removeRecursive(children);
		file.delete();
	}
	
	private static File tryToFindSneerLocation(){ //FixUrgent: this does not works correctly in eclipse, a temporary fix was made
		try{
			URL url = Jars.jarGiven(AppManager.class);
			return urlToFile(url);
		}catch(Exception e){
			File eclipseProjectRoot = new File("."); //fallback. if it is not running inside jar, try to find jar from jarbuilder directory.
			return firstJarInDirectory(new File(eclipseProjectRoot,"bin"));	
		}
	}

	private static File firstJarInDirectory(File eclipseProjectBin) {
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
	
	public static Map<String,App> installedApps(){
		createDirectories();
		unpackageApps();
		compileApps();
		loadApps();
		return _installedApps;
	}

	private static void loadApps() {
		File[] compiledAppDirectories = SneerDirectories.compiledAppsDirectory().listFiles();
		for(File compiledAppDirectory:compiledAppDirectories){
			if (isAppLoaded(compiledAppDirectory))
				continue;
			_installedApps.put(compiledAppDirectory.getName(),appLoad(compiledAppDirectory));
		}
	}

	@SuppressWarnings("deprecation")
	private static App appLoad(File compiledAppDirectory) {
		try {
			
			URL[] urls = new URL[]{compiledAppDirectory.toURL()};  
			URLClassLoader ucl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());  
			Class clazz = ucl.loadClass("sneer.apps."+compiledAppDirectory.getName()+".Main");  
			return (App) clazz.newInstance();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static boolean isAppLoaded(File compiledAppDirectory){
		return (_installedApps.get(compiledAppDirectory.getName())!=null);
	}
	
	private static List<File> notCompiledApps(){ 
		List<File> notCompiledApps = new ArrayList<File>();
		for(File sourceDirectory:SneerDirectories.appSourceCodesDirectory().listFiles()){
			if (alreadyCompiled(sourceDirectory))
				continue;
			notCompiledApps.add(sourceDirectory);	
		}
		return notCompiledApps;
	}
	
	private static boolean alreadyUnpackaged(File appDirectory) {
		for(File sourceCodeDirectory:SneerDirectories.appSourceCodesDirectory().listFiles()){
			if (sourceCodeDirectory.getName().equals(appDirectory.getName()))
				return true;
		}
		return false;
	}

	private static boolean alreadyCompiled(File sourceDirectory) {
		for(File compiledDirectory:SneerDirectories.compiledAppsDirectory().listFiles()){
			if (sourceDirectory.getName().equals(compiledDirectory.getName()))
				return true;
		}
		return false;
	}
	
	static final int _BUFFER = 2048;
	
	private static void unzip(File source, File target) throws Exception{
	         BufferedOutputStream dest = null;
	         BufferedInputStream is = null;
	         ZipEntry entry;
	         ZipFile zipfile = new ZipFile(source);
	         Enumeration e = zipfile.entries();
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

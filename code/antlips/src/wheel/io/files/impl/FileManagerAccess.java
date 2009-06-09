package wheel.io.files.impl;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

public class FileManagerAccess {

	public static void openDirectory(File directory) {
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) { //Fix: UNTESTED
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { new URL("file://" + directory.getAbsolutePath()) });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec("explorer.exe " + directory.getAbsolutePath());
			else {
				//assume Unix or Linux
				String[] browsers = { "nautilus", "konqueror", "firefox", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
						browser = browsers[count];
				if (browser == null)
					throw new Exception("Could not find web browser");
				Runtime.getRuntime().exec(new String[] { browser, directory.getAbsolutePath() });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
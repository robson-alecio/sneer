package sneer;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
	private static final Language instance = new Language();
	public ResourceBundle res = ResourceBundle.getBundle("sneer.AppResource");
	
	private Language(){
	}
	
	public static void changeLocale(Locale locale){
		Locale.setDefault(locale);
		instance.res = ResourceBundle.getBundle("sneer.AppResource");
	}
	
	public static String string(String key){
		return instance.res.getString(key);
	}
	
	public static String string(String key, Object...args){
		return String.format(string(key), args);
	}

}

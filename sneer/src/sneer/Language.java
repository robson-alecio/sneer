package sneer;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
	private static final Language instance = new Language();
	public ResourceBundle res = ResourceBundle.getBundle("sneer.AppResource");
	
	private Language(){
	}
	
	public static Language getInstance(){
		return instance;
	}
	
	public static void change(Locale locale){
		Locale.setDefault(locale);
		getInstance().res = ResourceBundle.getBundle("sneer.AppResource");
	}
	
	public static String string(String key){
		return getInstance().res.getString(key);
	}

}

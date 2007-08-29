package sneer.kernel.gui.contacts;

import java.awt.Font;
import java.io.InputStream;


public class FontUtil {

	public static Font _defaultFont;
	
	public static Font getFont(){
		if (_defaultFont == null){
			InputStream fis = FontUtil.class.getResourceAsStream("/sneer/kernel/gui/contacts/bip.ttf");
			try{
				_defaultFont =  Font.createFont(Font.TRUETYPE_FONT, fis);
			}catch(Exception e){
				_defaultFont = new Font("Arial",Font.PLAIN,14);
			}
		}
		return _defaultFont;
	}
	
	public static Font getFont(float size){
		return getFont().deriveFont(size);
	}
	
}

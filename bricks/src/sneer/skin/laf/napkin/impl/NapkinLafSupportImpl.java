package sneer.skin.laf.napkin.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;

import org.jdesktop.swingx.painter.Painter;

import sneer.skin.laf.impl.AbstractLafSupportImpl;
import sneer.skin.laf.napkin.NapkinLafSupport;

public class NapkinLafSupportImpl extends AbstractLafSupportImpl implements NapkinLafSupport{

	public NapkinLafSupportImpl(){
		super(new SneerNapkinLookAndFeel());
	}

	@Override
	protected void runAction(LookAndFeel laf) {
		changeLookAndFeel(laf);
		super.runAction(laf);	
	}

	@SuppressWarnings("unchecked")
	private void changeLookAndFeel(LookAndFeel laf) {
		UIDefaults defaults = laf.getDefaults();
		FontUIResource font = new  FontUIResource(new Font("Arial", Font.PLAIN,13));
		defaults.put("TextArea.font", font);
		defaults.put("TextField.font", font);
		
	    Painter painter = new Painter(){
			@Override
			public void paint(Graphics2D g, Object object, int width, int height) {
				//ignore
			}
		};
		defaults.put("TaskPaneContainer.backgroundPainter", painter);	
		defaults.put("TaskPane.borderColor", Color.LIGHT_GRAY);	    
	}
}

class SneerNapkinLookAndFeel extends NapkinLookAndFeel {

	private static final long serialVersionUID = 1L;
	protected UIDefaults _uiDefaults = null;

	@Override
	public UIDefaults getDefaults() {
		if(_uiDefaults==null){
			_uiDefaults= super.getDefaults();
		}
		return _uiDefaults;
	}
}
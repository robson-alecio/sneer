package sneer.skin.laf.napkin.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.painter.Painter;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
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
		//change fonts
		FontUIResource font = new  FontUIResource(new Font("Arial", Font.PLAIN,13));
		laf.getDefaults().put("TextArea.font", font);
		laf.getDefaults().put("TextField.font", font);
		laf.getDefaults().put("TaskPane.borderColor", Color.LIGHT_GRAY);
		
		//Change TaskPaneContainer.backgroundPainter
		UIDefaults defaults = laf.getDefaults();
	    defaults.put("TaskPaneContainer.backgroundPainter", 
	    	new Painter(){
				@Override
				public void paint(Graphics2D g, Object object, int width, int height) {
					//ignore
				}
	    	}
	    );	
	    
	    laf.getDefaults().put("TaskPaneContainer.opaque", false);
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
package sneer.skin.laf.napkin.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;

import org.jdesktop.swingx.painter.Painter;

import sneer.lego.Inject;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.napkin.NapkinLafSupport;
import wheel.io.ui.Action;

public class NapkinLafSupportImpl implements NapkinLafSupport{
	@Inject
	static private LafManager register;
	
	private Action action;

	public NapkinLafSupportImpl(){
		final SneerNapkinLookAndFeel laf = new SneerNapkinLookAndFeel();
		action = new Action(){
			@Override
			public String caption() {
				return laf.getName();
			}

			@Override
			public void run() {
				try {
					changeLookAndFeel(laf);
					UIManager.setLookAndFeel(laf);
					register.getRootContainer().refreshLaf();
				} catch (UnsupportedLookAndFeelException e) {
					// ignore: same L&F
				}
			}
		};
		
		register.registerLookAndFeel(action);
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

	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public void setLastUsedAction(Action last) {
		//ignore
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
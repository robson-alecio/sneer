package sneer.skin.laf.napkin.impl;

import java.awt.Font;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.skin.laf.impl.AbstractLafSupportImpl;
import sneer.skin.laf.napkin.NapkinLafSupport;

public class NapkinLafSupportImpl extends AbstractLafSupportImpl implements NapkinLafSupport{

	public NapkinLafSupportImpl(){
		super(new NapkinLookAndFeel());
	}

	@Override
	protected void runAction(LookAndFeel laf) {
        changeDefaultFonts();
		super.runAction(laf);
	}

	private void changeDefaultFonts() {
		Font font = new Font("Serif", Font.PLAIN, 14);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
	}
}
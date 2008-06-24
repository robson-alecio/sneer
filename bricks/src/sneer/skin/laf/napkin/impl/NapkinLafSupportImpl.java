package sneer.skin.laf.napkin.impl;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.skin.laf.impl.AbstractLafSupportImpl;
import sneer.skin.laf.napkin.NapkinLafSupport;

public class NapkinLafSupportImpl extends AbstractLafSupportImpl implements NapkinLafSupport{

	public NapkinLafSupportImpl(){
		super(new NapkinLookAndFeel());
	}
}
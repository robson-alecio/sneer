package sneer.skin.laf.so.impl;

import javax.swing.UIManager;

import sneer.skin.laf.impl.AbstractLafSupportImpl;
import sneer.skin.laf.so.SOLafSupport;

public class SOLafSupportImpl extends AbstractLafSupportImpl implements SOLafSupport {

	public SOLafSupportImpl(){
		super(UIManager.getSystemLookAndFeelClassName());
	}
}
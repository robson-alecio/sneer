package sneer.skin.laf.metal.impl;

import javax.swing.plaf.metal.MetalLookAndFeel;

import sneer.skin.laf.impl.AbstractLafSupportImpl;
import sneer.skin.laf.metal.MetalLafSupport;

public class MetalLafSupportImpl extends AbstractLafSupportImpl implements MetalLafSupport {

	public MetalLafSupportImpl(){
		super(new MetalLookAndFeel());
	}
}
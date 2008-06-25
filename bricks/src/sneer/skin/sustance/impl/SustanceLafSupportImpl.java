package sneer.skin.sustance.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.list.TreeList;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SkinInfo;

import sneer.skin.laf.impl.AbstractLafSupportImpl;
import sneer.skin.sustance.SustanceLafSupport;

public class SustanceLafSupportImpl extends AbstractLafSupportImpl implements SustanceLafSupport{

	public SustanceLafSupportImpl(){
		super(getLaFs(), "Sustance");
	}
	
	@SuppressWarnings("unchecked")
	protected static List<SubstanceLookAndFeel> getLaFs(){
		Collection<SkinInfo> skinsInfo = SubstanceLookAndFeel.getAllSkins().values();
		List<SubstanceLookAndFeel> lafs = new TreeList();
		for (SkinInfo skinInfo : skinsInfo) {
			try {
				lafs.add((SubstanceLookAndFeel) Class.forName(findLafName(skinInfo)).newInstance());
			} catch (Exception e) {
				System.out.println(findLafName(skinInfo) + " not found!");
			}
		}
		return lafs;
	}

	private static String findLafName(SkinInfo skinInfo) {
		String name = skinInfo.getClassName();
		name = name.replace("Skin", "");
		String parts[] = name.split("\\.");
		String skinName = parts[parts.length-1];
		String lafName = "Substance" + skinName + "LookAndFeel";
		name=name.replaceAll(skinName, lafName);
		return name;
		
	}
}
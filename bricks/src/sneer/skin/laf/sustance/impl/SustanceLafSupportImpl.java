package sneer.skin.laf.sustance.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.LookAndFeel;

import org.apache.commons.collections.list.TreeList;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SkinInfo;

import sneer.lego.Inject;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.sustance.SustanceLafSupport;
import wheel.io.ui.action.Action;

public class SustanceLafSupportImpl implements SustanceLafSupport{

	@Inject
	static private LafManager register;
	
	private Map<LookAndFeel, Action> map;
	private Action lastUsedAction;

	public SustanceLafSupportImpl(){
		map = register.registry(getLaFs(), "Sustance", this);
		lastUsedAction = map.values().iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	protected static List<LookAndFeel> getLaFs(){
		Collection<SkinInfo> skinsInfo = SubstanceLookAndFeel.getAllSkins().values();
		List<LookAndFeel> lafs = new TreeList();
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

	@Override
	public Action getAction() {
		return map.get(lastUsedAction);
	}
	
	@Override
	public void setLastUsedAction(Action last) {
		lastUsedAction = last;
	}
}
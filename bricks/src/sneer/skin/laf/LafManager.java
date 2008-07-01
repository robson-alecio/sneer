package sneer.skin.laf;

import java.util.List;
import java.util.Map;

import javax.swing.LookAndFeel;

import wheel.io.ui.Action;

public interface LafManager {

	public void registerLookAndFeel(Action action);

	public void registerLookAndFeel(String lafClassName, Action action);

	public Map<LookAndFeel, Action> registry(List<LookAndFeel> laf, String group, LafSupport lafSupport);

	public Action registerLookAndFeel(LookAndFeel laf);

	public Action registerLookAndFeel(String name);
	
	public LafContainer getRootContainer();

	public void setActiveLafSupport(LafSupport tmp);

}
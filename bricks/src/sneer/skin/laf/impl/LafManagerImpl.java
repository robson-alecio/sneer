package sneer.skin.laf.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafContainer;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.LafSupport;
import sneer.skin.mainMenu.MainMenu;
import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;
import wheel.io.ui.action.Action;

public class LafManagerImpl implements LafManager{

	@Inject
	protected static MainMenu mainMenu;
	
	@Inject
	protected static MenuFactory<JComponent> menuFactory;
	
	@Inject
	protected static Dashboard dashboard;

	public Action registerLookAndFeel(String name) {
		try {
			LookAndFeel laf = (LookAndFeel) Class.forName(name).newInstance();
			return registerLookAndFeel(laf);
		} catch (Exception e) {
			return null;
		}
	}

	public void registerLookAndFeel(String name, Action action) {
		try {
			Class.forName(name).newInstance(); //try initialize L&F
			registerLookAndFeel(action);
		} catch (Exception e) {
			//do not add a action
		}
	}

	public Action registerLookAndFeel(LookAndFeel laf) {
		Action action = initAction(laf);
		registerLookAndFeel(action);
		return action;
	}
	
	public void registerLookAndFeel(Action action) {
		mainMenu.getLookAndFeelMenu().addAction(action);
	}
	
	public Map<LookAndFeel, Action> registry(List<LookAndFeel> laf, String group, LafSupport lafSupport) {
		Map<LookAndFeel, Action> map = new Hashtable<LookAndFeel, Action>();
		Menu<JComponent> menu = menuFactory.createMenuGroup(group);
		mainMenu.getLookAndFeelMenu().addGroup(menu);
		for (LookAndFeel lookAndFeel : laf) {
			Action action = initAction(lookAndFeel, lafSupport);
			menu.addAction(action);
			map.put(lookAndFeel, action);
		}
		return map;
	}
	
	public LafContainer getRootContainer(){
		return dashboard;
	}

	private Action initAction(final LookAndFeel laf) {
		return initAction(laf,null);
	}
	
	private Action initAction(final LookAndFeel laf, final LafSupport lafSupport) {
		final Action action = new Action(){
			@Override
			public String caption() {
				return laf.getName();
			}

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(laf);
					getRootContainer().refreshLaf();
					if(lafSupport!=null)
						lafSupport.setLastUsedAction(this);
				} catch (UnsupportedLookAndFeelException e) {
					// ignore: same L&F
				}
			}
		};
		return action;
	}

	@Override
	public void setActiveLafSupport(LafSupport tmp) {
		tmp.getAction().run();
	}
}
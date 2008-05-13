package sneerappsui.topten.impl;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JComboBox;

import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.lego.Startable;
import sneer.skin.viewmanager.View;
import sneer.skin.viewmanager.ViewManager;
import sneerapps.topten.Topten;
import sneerappsui.topten.ToptenGui;
import wheel.io.ui.impl.ComboBoxSignalModel;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.impl.ListSourceImpl;

class ToptenGuiImpl implements ToptenGui, Startable {

	@Inject
	private ViewManager _appMenu;

//	@Inject
//	private Topten _topten;
	
	@Override
	public void start() throws Exception {
		_appMenu.register(new View(){

			@Override
			public void init(final Container container, Signal<Party> activeParty) {
				container.setLayout(new FlowLayout());
				
				JComboBox categories = new JComboBox();
				container.add(categories);
				
				final ListSourceImpl<String> categoriesSource = new ListSourceImpl<String>();
				final ComboBoxSignalModel<String> categoriesModel = new ComboBoxSignalModel<String>(categoriesSource.output());
				categories.setModel(categoriesModel);
				
				Signal<String> nameSignal = activeParty.currentValue().signal("Name");
				nameSignal.addReceiver(new Omnivore<String>(){@Override public void consume(String name) {
					categoriesSource.add(name);
				}});
			}
			
		});
	}

}

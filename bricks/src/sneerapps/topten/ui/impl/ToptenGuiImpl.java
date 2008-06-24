package sneerapps.topten.ui.impl;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JComboBox;

import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.lego.Startable;
import sneer.skin.viewmanager.PartyView;
import sneer.skin.viewmanager.ViewManager;
import sneerapps.topten.ui.ToptenGui;
import spikes.legobricks.name.OwnNameKeeper;
import wheel.io.ui.impl.ComboBoxSignalModel;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class ToptenGuiImpl implements ToptenGui, Startable {

	@Inject
	private ViewManager _viewManager;

//	@Inject
//	private Topten _topten;
	
	@Override
	public void start() throws Exception {
		_viewManager.register(new PartyView(){

			@Override
			public void init(final Container container, Signal<Party> activeParty) {
				container.setLayout(new FlowLayout());
				
				JComboBox categories = new JComboBox();
				container.add(categories);
				
				final ListRegisterImpl<String> categoriesSource = new ListRegisterImpl<String>();
				final ComboBoxSignalModel<String> categoriesModel = new ComboBoxSignalModel<String>(categoriesSource.output());
				categories.setModel(categoriesModel);
				
				Signal<String> nameSignal = activeParty.currentValue().brickProxyFor(OwnNameKeeper.class).name();
				nameSignal.addReceiver(new Omnivore<String>(){@Override public void consume(String name) {
					categoriesSource.add(name);
				}});
			}
			
		});
	}

}

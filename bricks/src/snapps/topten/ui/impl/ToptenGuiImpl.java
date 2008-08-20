package snapps.topten.ui.impl;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JComboBox;

import snapps.topten.ui.ToptenGui;
import sneer.lego.Inject;
import sneer.lego.Startable;
import sneer.skin.viewmanager.Snapp;
import sneer.skin.viewmanager.ViewManager;
import wheel.io.ui.impl.ComboBoxSignalModel;
import wheel.reactive.lists.impl.ListRegisterImpl;

class ToptenGuiImpl implements ToptenGui, Startable {

	@Inject
	private ViewManager _viewManager;

//	@Inject
//	private Topten _topten;
	
	@Override
	public void start() throws Exception {
		_viewManager.register(new Snapp(){

			@Override
			public void init(final Container container) {
				container.setLayout(new FlowLayout());
				
				JComboBox categories = new JComboBox();
				container.add(categories);
				
				final ListRegisterImpl<String> categoriesSource = new ListRegisterImpl<String>();
				final ComboBoxSignalModel<String> categoriesModel = new ComboBoxSignalModel<String>(categoriesSource.output());
				categories.setModel(categoriesModel);
				
			}

			@Override
			public String getName() {
				return "Topten";
			}
			
		});
	}

}

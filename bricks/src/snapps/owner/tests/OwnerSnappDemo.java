package snapps.owner.tests;

import java.awt.BorderLayout;

import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.viewmanager.Snapp;
import sneer.skin.widgets.reactive.RFactory;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class OwnerSnappDemo  {
	
	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.nameSetter().consume("Sandro Bihaiko");
		
		OwnTaglineKeeper ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		ownTaglineKeeper.taglineSetter().consume("Minha frase do dia!!");
		
		RFactory rfactory = container.produce(RFactory.class);
		
		container.produce(OwnerSnapp.class);
		
		SnappManager manager = container.produce(SnappManager.class);
		new OwnerSnappDemoSnapp(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter(), manager);
		new OwnerSnappDemoSnapp(rfactory, ownTaglineKeeper.tagline(), ownTaglineKeeper.taglineSetter(), manager);
		
		container.produce(Dashboard.class);
	}
}

class OwnerSnappDemoSnapp implements Snapp{

	private final RFactory _rfactory;
	private final Signal<String> _output;
	private final Omnivore<String> _setter;
	
	public OwnerSnappDemoSnapp(RFactory rfactory, Signal<String> output,	Omnivore<String> setter, SnappManager manager) {
		_rfactory = rfactory;
		_output = output;
		_setter = setter;
		manager.registerSnapp(this);
	}

	@Override
	public void init(java.awt.Container container) {
		container.setLayout(new BorderLayout());
		container.add(
			_rfactory.newEditableLabel(_output, _setter).getComponent(), BorderLayout.CENTER
		);
	}
	
}
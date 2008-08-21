package snapps.owner.tests;

import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.ownName.OwnNameKeeper;
import sneer.pulp.ownTagline.OwnTaglineKeeper;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.viewmanager.Snapp;
import sneer.skin.widgets.reactive.RFactory;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class OwnerSnappDemo  {
	
	static int y = 10;

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.setName("Sandro Bihaiko");
		
		OwnTaglineKeeper ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		ownTaglineKeeper.taglineSetter().consume("Minha frase do dia!!");
		
		RFactory rfactory = container.produce(RFactory.class);
		
		Dashboard dashboard = container.produce(Dashboard.class);
		OwnerSnapp snapp = container.produce(OwnerSnapp.class);
		dashboard.installSnapp(snapp);
		
		dashboard.installSnapp(new SnappTest(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter()));
		dashboard.installSnapp(new SnappTest(rfactory, ownTaglineKeeper.tagline(), ownTaglineKeeper.taglineSetter()));
		
	}
}

class SnappTest implements Snapp{

	private final RFactory _rfactory;
	private final Signal<String> _output;
	private final Omnivore<String> _setter;
	
	public SnappTest(RFactory rfactory, Signal<String> output,	Omnivore<String> setter) {
				_rfactory = rfactory;
				_output = output;
				_setter = setter;
	}

	@Override
	public void init(java.awt.Container container) {
		container.add(
			_rfactory.newEditableLabel(_output, _setter, true).getContainer()
		);
	}
	@Override
	public String getName() {
		return "Output Test";
	}
}
package snapps.owner.tests;

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
	
	static int y = 10;

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.setName("Sandro Bihaiko");
		
		OwnTaglineKeeper ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		ownTaglineKeeper.taglineSetter().consume("Minha frase do dia!!");
		
		RFactory rfactory = container.produce(RFactory.class);
		
		container.produce(OwnerSnapp.class);
		
		SnappManager manager = container.produce(SnappManager.class);
		new SnappTest(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter(), manager);
		new SnappTest(rfactory, ownTaglineKeeper.tagline(), ownTaglineKeeper.taglineSetter(), manager);
		
		container.produce(Dashboard.class);
	}
}

class SnappTest implements Snapp{

	private final RFactory _rfactory;
	private final Signal<String> _output;
	private final Omnivore<String> _setter;
	
	public SnappTest(RFactory rfactory, Signal<String> output,	Omnivore<String> setter, SnappManager manager) {
				_rfactory = rfactory;
				_output = output;
				_setter = setter;
				manager.registerSnapp(this);
	}

	@Override
	public void init(java.awt.Container container) {
		container.add(
			_rfactory.newEditableLabel(_output, _setter, true).getComponent()
		);
	}
	@Override
	public String getName() {
		return "Output Test";
	}
}
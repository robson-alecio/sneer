package snapps.owner.tests;

import java.awt.BorderLayout;

import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class OwnerSnappDemo  {
	
	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.nameSetter().consume("Sandro Bihaiko");
		
		OwnTaglineKeeper ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		ownTaglineKeeper.taglineSetter().consume("Minha frase do dia!!");
		
		ReactiveWidgetFactory rfactory = container.produce(ReactiveWidgetFactory.class);
		
		container.produce(OwnerSnapp.class);
		
		InstrumentManager manager = container.produce(InstrumentManager.class);
		new OwnerSnappDemoSnapp(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter(), manager);
		new OwnerSnappDemoSnapp(rfactory, ownTaglineKeeper.tagline(), ownTaglineKeeper.taglineSetter(), manager);
		
		container.produce(Dashboard.class);
	}
}

class OwnerSnappDemoSnapp implements Instrument{

	private final ReactiveWidgetFactory _rfactory;
	private final Signal<String> _output;
	private final Omnivore<String> _setter;
	
	public OwnerSnappDemoSnapp(ReactiveWidgetFactory rfactory, Signal<String> output,	Omnivore<String> setter, InstrumentManager manager) {
		_rfactory = rfactory;
		_output = output;
		_setter = setter;
		manager.registerInstrument(this);
	}

	@Override
	public void init(java.awt.Container container) {
		container.setLayout(new BorderLayout());
		container.add(
			_rfactory.newEditableLabel(_output, _setter).getComponent(), BorderLayout.CENTER
		);
	}
	
}
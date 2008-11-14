package snapps.owner.tests;

import java.awt.BorderLayout;

import snapps.owner.gui.OwnerGui;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public class OwnerDemo  {
	
	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.nameSetter().consume("Sandro Bihaiko");
		
		OwnTaglineKeeper ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		ownTaglineKeeper.taglineSetter().consume("Minha frase do dia!!");
		
		ReactiveWidgetFactory rfactory = container.produce(ReactiveWidgetFactory.class);
		
		container.produce(OwnerGui.class);
		
		InstrumentManager manager = container.produce(InstrumentManager.class);
		new OwnerInstrument(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter(), manager);
		new OwnerInstrument(rfactory, ownTaglineKeeper.tagline(), ownTaglineKeeper.taglineSetter(), manager);
		
		container.produce(Dashboard.class);
	}
}

class OwnerInstrument implements Instrument{

	private final ReactiveWidgetFactory _rfactory;
	private final Signal<String> _output;
	private final Consumer<String> _setter;
	
	public OwnerInstrument(ReactiveWidgetFactory rfactory, Signal<String> output,	Consumer<String> setter, InstrumentManager manager) {
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

	@Override
	public int defaultHeight() {
		return ANY_HEIGHT;
	}
}
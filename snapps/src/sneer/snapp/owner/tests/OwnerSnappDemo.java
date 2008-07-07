package sneer.snapp.owner.tests;

import javax.swing.SwingUtilities;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.viewmanager.Snapp;
import sneer.widgets.reactive.RFactory;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class OwnerSnappDemo  {
	
	static int y = 10;
	private static Omnivore<String> log;
	static{
		log = new Omnivore<String>(){
			@Override
			public void consume(String valueObject) {
				System.out.println(valueObject);
			}
		};
	}

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();
		initLafs(container);

		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.setName("Sandro Bihaiko");
		RFactory rfactory = container.produce(RFactory.class);
		
		ownNameKeeper.name().addReceiver(log);
		
		newSnaap(rfactory, container, ownNameKeeper, false);
		newSnaap(rfactory, container, ownNameKeeper, true);
	}

	private static void newSnaap(RFactory rfactory, Container container, OwnNameKeeper ownNameKeeper, boolean notifyEveryChange) {
		Dashboard dashboard = container.produce(Dashboard.class);
		dashboard.installSnapp(new SnappTest(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter(), notifyEveryChange));
	}
	
	private static void initLafs(final Container container) {
		try {
			SwingUtilities.invokeAndWait(
				new Runnable(){
					@Override
					public void run() {
						NapkinLafSupport tmp = container.produce(NapkinLafSupport.class);
						LafManager reg = container.produce(LafManager.class);
						reg.setActiveLafSupport(tmp);
					}
				}
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class SnappTest implements Snapp{

	private final RFactory _rfactory;
	private final Signal<String> _output;
	private final Omnivore<String> _setter;
	private final boolean _notifyEveryChange;
	
	public SnappTest(RFactory rfactory, Signal<String> output,	Omnivore<String> setter, boolean notifyEveryChange) {
				_rfactory = rfactory;
				_output = output;
				_setter = setter;
				_notifyEveryChange = notifyEveryChange;
	}

	@Override
	public void init(java.awt.Container container) {
		container.add(
			_rfactory.newEditableLabel(_output, _setter, _notifyEveryChange).getContainer()
		);
	}
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}
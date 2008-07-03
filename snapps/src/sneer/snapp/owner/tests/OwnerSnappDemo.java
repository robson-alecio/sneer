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
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class OwnerSnappDemo  {

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();
		initLafs(container);

//		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
//		ownNameKeeper.setName("Sandro Bihaiko");
		
		Register<String> register = new RegisterImpl<String>("Jose das Coves");

		RFactory rfactory = container.produce(RFactory.class);
		Dashboard dashboard = container.produce(Dashboard.class);
		dashboard.installSnapp(new SnappTest(rfactory, register.output(), register.setter() ));
		dashboard.installSnapp(new SnappTest(rfactory, register.output(), register.setter() ));
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
	
	public SnappTest(RFactory rfactory, OwnNameKeeper ownNameKeeper){
		this(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter());
	}
	
	public SnappTest(RFactory rfactory, Signal<String> output,
			Omnivore<String> setter) {
				_rfactory = rfactory;
				_output = output;
				_setter = setter;
	}

	@Override
	public void init(java.awt.Container container) {
		container.add(
			_rfactory.newEditableLabel(_output, _setter).getContainer()
		);
	}
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}
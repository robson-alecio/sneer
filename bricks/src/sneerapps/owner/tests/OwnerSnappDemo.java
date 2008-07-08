package sneerapps.owner.tests;

import javax.swing.SwingUtilities;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.bricks.ownTagline.OwnTaglineKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.metal.MetalLafSupport;
import sneer.skin.laf.motif.MotifLafSupport;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.laf.so.SOLafSupport;
import sneer.skin.laf.sustance.SustanceLafSupport;
import sneer.skin.viewmanager.Snapp;
import sneer.widgets.reactive.RFactory;
import sneerapps.owner.OwnerSnapp;
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
		
		OwnTaglineKeeper ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		ownTaglineKeeper.setTagline("Minha frase do dia!!");
		
		RFactory rfactory = container.produce(RFactory.class);
		ownNameKeeper.name().addReceiver(log);
		
		Dashboard dashboard = container.produce(Dashboard.class);
		OwnerSnapp snapp = container.produce(OwnerSnapp.class);
		dashboard.installSnapp(snapp);
		
		dashboard.installSnapp(new SnappTest(rfactory, ownNameKeeper.name(), ownNameKeeper.nameSetter()));
		dashboard.installSnapp(new SnappTest(rfactory, ownTaglineKeeper.tagline(), ownTaglineKeeper.taglineSetter()));
		
	}

	private static void initLafs(final Container container) {
		container.produce(SOLafSupport.class);
		container.produce(MetalLafSupport.class);
		container.produce(MotifLafSupport.class);
		container.produce(SustanceLafSupport.class);
		
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
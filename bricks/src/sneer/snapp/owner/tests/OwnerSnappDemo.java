package sneer.snapp.owner.tests;

import javax.swing.JFrame;

import sneer.bricks.name.OwnNameKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.laf.so.SOLafSupport;
import sneer.snapp.owner.OwnerSnapp;
import sneer.widgets.reactive.RFactory;

public class OwnerSnappDemo  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		OwnerSnapp ownerSnapp = container.produce(OwnerSnapp.class);
		
		initLafs(container);
		installSnapp(container, ownerSnapp);
		createTestFrame(container, ownerSnapp);
	}

	private static void installSnapp(Container container, OwnerSnapp ownerSnapp) {
		Dashboard dashboard = container.produce(Dashboard.class);
		dashboard.installSnapp(ownerSnapp);
	}

	private static void createTestFrame(Container container, OwnerSnapp ownerSnapp) {
		OwnNameKeeper ownNameKeeper = ownerSnapp.getOwnNameKeeper();
		RFactory rfactory = container.produce(RFactory.class);
		JFrame frm = new JFrame();
		frm.getContentPane().add(
				rfactory.newTextField(
						ownNameKeeper.name(), 
						ownNameKeeper.nameSetter()
				).getContainer()
		);
		frm.setBounds(10, 10, 200, 200);
		frm.setVisible(true);
//		ownerSnapp.getEditableLable().setT
	}
	
	private static void initLafs(Container container) {
		container.produce(SOLafSupport.class);
		NapkinLafSupport tmp = container.produce(NapkinLafSupport.class);
		LafManager reg = container.produce(LafManager.class);
		reg.setActiveLafSupport(tmp);
	}
}
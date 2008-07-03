package sneer.snapp.owner.tests;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.bricks.ownName.OwnNameKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.snapp.owner.OwnerSnapp;
import sneer.widgets.reactive.RFactory;

public class OwnerSnappDemo  {

	public static void main(String[] args) throws Exception {
		initLafs();

		Container container = ContainerUtils.getContainer();
		OwnerSnapp ownerSnapp = container.produce(OwnerSnapp.class);
		
		installSnapp(container, ownerSnapp);
		createTestFrame(container, ownerSnapp);
	}

	private static void installSnapp(Container container, OwnerSnapp ownerSnapp) {
		Dashboard dashboard = container.produce(Dashboard.class);
		dashboard.installSnapp(ownerSnapp);
	}

	private static void createTestFrame(Container container, OwnerSnapp ownerSnapp) {
		OwnNameKeeper ownNameKeeper = ownerSnapp.getOwnNameKeeper();
        System.out.println(System.identityHashCode(ownNameKeeper.name()));
        System.out.println(ownNameKeeper.nameSetter());

        ownNameKeeper.setName("Sandro Bihaiko");
		
		RFactory rfactory = container.produce(RFactory.class);
		JFrame frm = new JFrame();
		frm.getContentPane().add(
			rfactory.newTextField(
					ownNameKeeper.name(), 
					ownNameKeeper.nameSetter()
			).getContainer()
		);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setBounds(10, 10, 200, 200);
		frm.setVisible(true);

	}
	
	private static void initLafs() {
		NapkinLookAndFeel laf = new NapkinLookAndFeel();
		try {
			UIManager.setLookAndFeel(laf);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
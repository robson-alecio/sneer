package sneer.bricks.ownName.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;

public class OwnNameKeeperDemo {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		RFactory rfactory = container.produce(RFactory.class);
		
		OwnNameKeeper ownNameKeeper = container.produce(OwnNameKeeper.class);
		ownNameKeeper.setName("Sandro Luiz Bihaiko");
		
		TextWidget newTextField = rfactory.newTextField(ownNameKeeper.name(), ownNameKeeper.nameSetter());
		
		createTestFrame(newTextField).setBounds(10, 10, 300, 100);
		createTestFrame(rfactory.newEditableLabel(ownNameKeeper.name(), ownNameKeeper.nameSetter())).setBounds(10, 120, 300, 100);
		createTestFrame(rfactory.newLabel(ownNameKeeper.name())).setBounds(10, 240, 300, 100);
	}

	private static JFrame createTestFrame(final TextWidget textWidget) {
		final JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frm.getContentPane().setLayout(new FlowLayout());
					frm.getContentPane().add(textWidget.getContainer());
					frm.setVisible(true);
				}
			}
		);
		return frm;
	}
}
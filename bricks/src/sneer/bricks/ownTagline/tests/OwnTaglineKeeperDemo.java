package sneer.bricks.ownTagline.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.bricks.ownTagline.OwnTaglineKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;

public class OwnTaglineKeeperDemo extends BrickTestSupport {

	private static final Omnivore<String> log;
	static{
		log = new Omnivore<String>() {
			@Override
			public void consume(String valueObject) {
				System.out.println(valueObject);
			}
		};
	}

	@Inject
	private RFactory _rfactory;
	
	@Inject
	private OwnTaglineKeeper _ownTaglineKeeper;

	public static void main(String[] args) throws Exception {
		OwnTaglineKeeperDemo demo = initializeDemo();
		createWidgets(demo);
		demo._ownTaglineKeeper.tagline().addReceiver(log);
	}
	
	private static OwnTaglineKeeperDemo initializeDemo() {
		OwnTaglineKeeperDemo demo = new OwnTaglineKeeperDemo();
		Container container = ContainerUtils.getContainer();
		demo._rfactory = container.produce(RFactory.class);	
		demo._ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		demo._ownTaglineKeeper.taglineSetter().consume("O novo parágrafo começa depois do ponto final.");
		return demo;
	}

	private static void createWidgets(OwnTaglineKeeperDemo demo) {
		
		TextWidget newTextField1 = demo._rfactory.newTextField(demo._ownTaglineKeeper.tagline(), demo._ownTaglineKeeper.taglineSetter());
		final JFrame frm1 =createTestFrame(newTextField1);
		
		TextWidget newTextField2 = demo._rfactory.newEditableLabel(demo._ownTaglineKeeper.tagline(), demo._ownTaglineKeeper.taglineSetter());
		final JFrame frm2 =createTestFrame(newTextField2);
		
		frm1.setBounds(10, 10, 300, 100);
		frm2.setBounds(10, 120, 300, 100);
		
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					frm1.setVisible(true);
					frm2.setVisible(true);
				}
			}
		);
	}

	private static JFrame createTestFrame(final TextWidget textWidget) {
		JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(new FlowLayout());
		frm.getContentPane().add(textWidget.getContainer());
		return frm;
	}
}
package sneer.pulp.own.tagline.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.BrickTestSupport;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.reactive.impl.Receiver;

public class OwnTaglineKeeperDemo extends BrickTestSupport {

	@SuppressWarnings("unused")
	private static Receiver<String> _log;

	@Inject
	private RFactory _rfactory;
	
	@Inject
	private OwnTaglineKeeper _ownTaglineKeeper;

	public static void main(String[] args) throws Exception {
		OwnTaglineKeeperDemo demo = initializeDemo();
		createWidgets(demo);
		_log = new Receiver<String>(demo._ownTaglineKeeper.tagline()){@Override public void consume(String valueObject) {
			System.out.println(valueObject);
		}};
	}
	
	private static OwnTaglineKeeperDemo initializeDemo() {
		OwnTaglineKeeperDemo demo = new OwnTaglineKeeperDemo();
		Container container = ContainerUtils.getContainer();
		demo._rfactory = container.produce(RFactory.class);	
		demo._ownTaglineKeeper = container.produce(OwnTaglineKeeper.class);
		demo._ownTaglineKeeper.taglineSetter().consume("O novo parágrafo começa depois do ponto final.");
		return demo;
	}

	private static void createWidgets(final OwnTaglineKeeperDemo demo) {
		SwingUtilities.invokeLater(	new Runnable(){ @Override
			public void run() {
				TextWidget<JTextField> newTextField1 = demo._rfactory.newTextField(demo._ownTaglineKeeper.tagline(), demo._ownTaglineKeeper.taglineSetter());
				final JFrame frm1 =createTestFrame(newTextField1);
				
				TextWidget<JTextField> newTextField2 = demo._rfactory.newEditableLabel(demo._ownTaglineKeeper.tagline(), demo._ownTaglineKeeper.taglineSetter());
				final JFrame frm2 =createTestFrame(newTextField2);
				
				frm1.setBounds(10, 10, 300, 100);
				frm2.setBounds(10, 120, 300, 100);
				frm1.setVisible(true);
				frm2.setVisible(true);
			}});
	}

	private static JFrame createTestFrame(final TextWidget<?> textWidget) {
		JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(new FlowLayout());
		frm.getContentPane().add(textWidget.getComponent());
		return frm;
	}
}
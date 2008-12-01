package sneer.skin.snappmanager.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import tests.TestThatIsInjected;
import wheel.io.ui.GuiThread;
import wheel.reactive.impl.Receiver;

public class OwnNameKeeperDemo extends TestThatIsInjected {

	@SuppressWarnings("unused")
	private static Receiver<String> _receiver;

	@Inject
	private ReactiveWidgetFactory rfactory;
	
	@Inject
	private OwnNameKeeper ownNameKeeper;

	public static void main(String[] args) throws Exception {
		OwnNameKeeperDemo demo = initializeDemo();
		createWidgets(demo);
		
		_receiver = new Receiver<String>(demo.ownNameKeeper.name()){@Override public void consume(String valueObject) {
			System.out.println(valueObject);
		}};
	}
	
	private static OwnNameKeeperDemo initializeDemo() {
		OwnNameKeeperDemo demo = new OwnNameKeeperDemo();
		Container container = ContainerUtils.getContainer();
		demo.rfactory = container.provide(ReactiveWidgetFactory.class);	
		demo.ownNameKeeper = container.provide(OwnNameKeeper.class);
		demo.ownNameKeeper.nameSetter().consume("Sandro Luiz Bihaiko");
		return demo;
	}

	private static void createWidgets(final OwnNameKeeperDemo demo) {
		GuiThread.strictInvokeLater( new Runnable(){ @Override public void run() {
			TextWidget<JTextField> newTextField1 = demo.rfactory.newTextField(demo.ownNameKeeper.name(), demo.ownNameKeeper.nameSetter());
			final JFrame frm1 =createTestFrame(newTextField1);
			TextWidget<JTextField> newTextField2 = demo.rfactory.newEditableLabel(demo.ownNameKeeper.name(), demo.ownNameKeeper.nameSetter());
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
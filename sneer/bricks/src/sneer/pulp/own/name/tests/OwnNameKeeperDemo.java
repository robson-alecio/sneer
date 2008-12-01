package sneer.pulp.own.name.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.GuiThread;
import wheel.reactive.impl.Receiver;

public class OwnNameKeeperDemo {

	@SuppressWarnings("unused")
	private static Receiver<String> _receiver;

	@Inject
	private ReactiveWidgetFactory rfactory;
	
	@Inject
	private OwnNameKeeper ownNameKeeper;

	public static void main(String[] args) throws Exception {
		OwnNameKeeperDemo demo = initializeDemo();
		createWidgets(demo);
		
		_receiver = 
			new Receiver<String>(demo.ownNameKeeper.name()){@Override public void consume(String valueObject) {
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

	private static void createWidgets(OwnNameKeeperDemo demo) {
		
		TextWidget<JTextField> newTextField1 = demo.rfactory.newTextField(demo.ownNameKeeper.name(), demo.ownNameKeeper.nameSetter());
		createTestFrame(newTextField1, 10, 10, 300, 100);
		
		TextWidget<JTextField> newTextField2 = demo.rfactory.newEditableLabel(demo.ownNameKeeper.name(), demo.ownNameKeeper.nameSetter());
		createTestFrame(newTextField2, 10, 120, 300, 100);
	}

	private static void createTestFrame(final TextWidget<?> textWidget, final int x, final int y, final int w, final int h) {
		GuiThread.strictInvokeLater(	new Runnable(){@Override	public void run() {
			JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.getContentPane().setLayout(new FlowLayout());
			frm.getContentPane().add(textWidget.getComponent());
			frm.setBounds(x, y, w, h);
			frm.setVisible(true);
		}});
	}
}
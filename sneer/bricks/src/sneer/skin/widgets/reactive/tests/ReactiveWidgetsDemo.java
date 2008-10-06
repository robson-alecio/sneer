package sneer.skin.widgets.reactive.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.reactive.Register;
import wheel.reactive.impl.Receiver;
import wheel.reactive.impl.RegisterImpl;

public class ReactiveWidgetsDemo {

	@SuppressWarnings("unused")
	private static Receiver<String> _receiver;

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		ReactiveWidgetFactory rfactory = container.produce(ReactiveWidgetFactory.class);
		Register<String> register = new RegisterImpl<String>("Jose das Coves");
				
		TextWidget<?> newTextField;
		
		newTextField = rfactory.newTextField(register.output(), register.setter());
		createTestFrame(newTextField, 10, 10, 300, 100, "notifyOnlyWhenDoneEditing=false");

		newTextField = rfactory.newEditableLabel(register.output(), register.setter());
		createTestFrame(newTextField, 10, 120, 300, 100, "notifyOnlyWhenDoneEditing=false");

		newTextField = rfactory.newLabel(register.output());
		createTestFrame(newTextField, 10, 240, 300, 100, "notifyOnlyWhenDoneEditing=false");


		newTextField = rfactory.newTextField(register.output(), register.setter(), true);
		createTestFrame(newTextField, 350, 10, 300, 100, "notifyOnlyWhenDoneEditing=true");

		newTextField = rfactory.newEditableLabel(register.output(), register.setter(), true);
		createTestFrame(newTextField, 350, 120, 300, 100, "notifyOnlyWhenDoneEditing=true");

		_receiver = new Receiver<String>(register.output()){
			@Override
			public void consume(String valueObject) {
				System.out.println(valueObject);
			}
		};
	}

	private static void createTestFrame(final TextWidget<?> textWidget, final int x, final int y, final int width, final int height, final String title) {
		SwingUtilities.invokeLater(new Runnable(){@Override public void run() {
			final JFrame frm = new JFrame();
			frm.setTitle(textWidget.getClass().getSimpleName() + " - " + title);
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.getContentPane().setLayout(new FlowLayout());
			frm.getContentPane().add(textWidget.getComponent());
			frm.setVisible(true);
			frm.setBounds(x, y, width, height);
		}});
	}
}
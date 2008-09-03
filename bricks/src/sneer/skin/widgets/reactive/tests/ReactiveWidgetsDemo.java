package sneer.skin.widgets.reactive.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.reactive.Register;
import wheel.reactive.impl.Receiver;
import wheel.reactive.impl.RegisterImpl;

public class ReactiveWidgetsDemo {

	@SuppressWarnings("unused")
	private static Receiver<String> _receiver;

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		RFactory rfactory = container.produce(RFactory.class);
		Register<String> register = new RegisterImpl<String>("Jose das Coves");
				
		TextWidget<?> newTextField;
		
		newTextField = rfactory.newTextField(register.output(), register.setter());
		createTestFrame(newTextField, 10, 10, 300, 100);

		newTextField = rfactory.newEditableLabel(register.output(), register.setter());
		createTestFrame(newTextField, 10, 120, 300, 100);

		newTextField = rfactory.newLabel(register.output());
		createTestFrame(newTextField, 10, 240, 300, 100);

		_receiver = new Receiver<String>(register.output()){
			@Override
			public void consume(String valueObject) {
				System.out.println(valueObject);
			}
		};
	}

	private static void createTestFrame(final TextWidget<?> textWidget, final int x, final int y, final int width, final int height) {
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					final JFrame frm = new JFrame();
					frm.setTitle(textWidget.getClass().getSimpleName());
					frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frm.getContentPane().setLayout(new FlowLayout());
					frm.getContentPane().add(textWidget.getComponent());
					frm.setVisible(true);
					frm.setBounds(x, y, width, height);
				}
			}
		);
	}
}
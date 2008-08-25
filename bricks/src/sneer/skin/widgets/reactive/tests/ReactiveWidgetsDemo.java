package sneer.skin.widgets.reactive.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class ReactiveWidgetsDemo {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		RFactory rfactory = container.produce(RFactory.class);
		Register<String> register = new RegisterImpl<String>("Jose das Coves");
				
		TextWidget newTextField;
		
		newTextField = rfactory.newTextField(register.output(), register.setter());
		createTestFrame(newTextField).setBounds(10, 10, 300, 100);

		newTextField = rfactory.newEditableLabel(register.output(), register.setter());
		createTestFrame(newTextField).setBounds(10, 120, 300, 100);

		newTextField = rfactory.newLabel(register.output());
		createTestFrame(newTextField).setBounds(10, 240, 300, 100);

		register.output().addReceiver(
			new Omnivore<String>(){
				@Override
				public void consume(String valueObject) {
					System.out.println(valueObject);
				}
			}
		);
	}

	private static JFrame createTestFrame(final TextWidget textWidget) {
		final JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(new FlowLayout());
		frm.getContentPane().add(textWidget.getContainer());

		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					frm.setVisible(true);
				}
			}
		);
		return frm;
	}
}
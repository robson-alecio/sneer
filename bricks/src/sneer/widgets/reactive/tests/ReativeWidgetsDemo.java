package sneer.widgets.reactive.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class ReativeWidgetsDemo {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		
//		iniLaf(container);		
		
		RFactory rfactory = container.produce(RFactory.class);
		Register<String> register = new RegisterImpl<String>("Jose das Coves");
				
		TextWidget newTextField = rfactory.newTextField(register.output(), register.setter());
		
		createTestFrame(newTextField).setBounds(10, 10, 300, 100);
		createTestFrame(rfactory.newEditableLabel(register.output(), register.setter())).setBounds(10, 120, 300, 100);
		createTestFrame(rfactory.newLabel(register.output())).setBounds(10, 240, 300, 100);
		
		register.output().addReceiver(
			new Omnivore<String>(){
				@Override
				public void consume(String valueObject) {
					System.out.println(valueObject);
				}
			}
		);
	}

//	private static void iniLaf(Container container) {
//		LafManager reg = container.produce(LafManager.class);
//		reg.setActiveLafSupport(container.produce(NapkinLafSupport.class));
//	}

	private static JFrame createTestFrame(final TextWidget textWidget) {
		final JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					frm.getContentPane().setLayout(new FlowLayout());
					frm.getContentPane().add(textWidget.getContainer());
					frm.setVisible(true);
					frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
		);
		return frm;
	}
}
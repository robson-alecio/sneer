package sneer.skin.widgets.reactive.tests;

import static wheel.lang.Types.cast;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class ReactiveListDemo {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		RFactory rfactory = container.produce(RFactory.class);
		final Register<String[]> register = new RegisterImpl<String[]>(new String[]{"Klaus", "Sandro","Bamboo", "Nell"});
		
		newFrame(register, rfactory, 0);
		newFrame(register, rfactory, 200);
	}

	private static void newFrame(final Register<String[]> register, RFactory rfactory, int width) {
		JFrame f = new JFrame("Smooth List Drop");
		ListWidget<String> listw = cast(rfactory.newList(register.output(), register.setter()));
		final JList list = (JList) listw.getMainWidget(); 
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JScrollPane(list));
		f.pack();
		f.setLocation(new Point(width,10));
		f.setVisible(true);
	}
}
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
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class CustomListProviderDemo {

	final static String ONLINE = "Sandro";
	final static String OFFLINE = "Bamboo";
	final static String AWAY = "Klaus";

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		RFactory rfactory = container.produce(RFactory.class);
		ListRegister<String> register = new ListRegisterImpl<String>();
		register.add(ONLINE);
		register.add(OFFLINE);
		register.add(AWAY);
		
		createJFrame(register, rfactory, 0);
	}

	private static void createJFrame(ListRegister<String> register, RFactory rfactory, int width) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addReactiveListWidget(register, rfactory, f);
		
		f.pack();
		f.setLocation(new Point(width,10));
		f.setVisible(true);
	}

	private static void addReactiveListWidget(final ListRegister<String> register, RFactory rfactory, JFrame f) {
		ListWidget<String> listw = cast(rfactory.newList(register));
		final JList list = (JList) listw.getMainWidget(); 
		f.getContentPane().add(new JScrollPane(list));
	}
}
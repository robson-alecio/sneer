package spikes.klaus.jlist;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import wheel.io.ui.GuiThread;
import wheel.io.ui.impl.ListSignalModel;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class JListModelDemo {

	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 300, 500);
		
		frame.setLayout(new BorderLayout());
		JList jList = new JList();
		frame.getContentPane().add(jList, BorderLayout.CENTER);
		
		final ListRegister<String> list = new ListRegisterImpl<String>();
		jList.setModel(new ListSignalModel<String>(list.output()));

		frame.setVisible(true);
		
		GuiThread.strictInvokeAndWait(new Runnable(){ @Override public void run() {
			list.adder().consume("11111");
		}});
		
	}

}

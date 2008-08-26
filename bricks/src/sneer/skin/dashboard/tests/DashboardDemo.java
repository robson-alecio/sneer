package sneer.skin.dashboard.tests;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sneer.kernel.container.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.viewmanager.Snapp;
import wheel.lang.Threads;

public class DashboardDemo  {

	public static void main(String[] args) throws Exception {
		sneer.kernel.container.Container container = ContainerUtils.getContainer();

		SnappManager snappManager = container.produce(SnappManager.class);
		installSampleSnapps(snappManager);
		container.produce(Dashboard.class);

		Threads.sleepWithoutInterruptions(30000);
	}
	
	private static void installSampleSnapps(SnappManager snappManager) {
		snappManager.registerSnapp(new Snapp1());
		snappManager.registerSnapp(new Snapp2());
		snappManager.registerSnapp(new Snapp3());
    }
	
}

class Snapp1 implements Snapp{

	@Override
	public void init(Container container) {
		container.setLayout(new BoxLayout(container, 
				BoxLayout.PAGE_AXIS));
		
		DefaultComboBoxModel model = new DefaultComboBoxModel(
			new Object[]{"Blue","Read", "Black", "White"}
		);
		JComboBox combo = new JComboBox();
		combo.setModel(model);
		
		JList list = new JList(model);
		
		container.add(combo);
		container.add(list);
		
		
	}
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}

class Snapp2 implements Snapp{

	@Override
	public void init(Container container) {
		container.setLayout(new BoxLayout(container, 
								BoxLayout.PAGE_AXIS));
		container.add(new JCheckBox("Option 1"));
		container.add(new JCheckBox("Option 2"));
		container.add(new JCheckBox("Option 3"));
		container.add(new JCheckBox("Option 4"));
	}
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}

class Snapp3 implements Snapp{
	@Override
	public void init(Container container) {
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		JTextArea textArea = new JTextArea(15, 20);
        container.add(new JScrollPane(textArea));
	}
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}
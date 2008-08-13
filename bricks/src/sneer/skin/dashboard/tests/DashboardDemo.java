package sneer.skin.dashboard.tests;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.dashboard.SnappFrame;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.metal.MetalLafSupport;
import sneer.skin.laf.motif.MotifLafSupport;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.laf.so.SOLafSupport;
import sneer.skin.laf.sustance.SustanceLafSupport;
import sneer.skin.viewmanager.Snapp;
import wheel.lang.Threads;

public class DashboardDemo  {

	public static void main(String[] args) throws Exception {
		sneer.lego.Container container = ContainerUtils.getContainer();

		NapkinLafSupport tmp = container.produce(NapkinLafSupport.class);
		LafManager reg = container.produce(LafManager.class);
		reg.setActiveLafSupport(tmp);

		container.produce(SOLafSupport.class);
		container.produce(MetalLafSupport.class);
		container.produce(MotifLafSupport.class);
		container.produce(SustanceLafSupport.class);

		Dashboard dashboard = container.produce(Dashboard.class);
		installSampleSnapps(dashboard);

		Threads.sleepWithoutInterruptions(30000);
	}
	
	private static void installSampleSnapps(Dashboard dashboard) {
		
		dashboard.installSnapp(new Snapp1());
		dashboard.installSnapp(new Snapp2());
		dashboard.installSnapp(new Snapp3());
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
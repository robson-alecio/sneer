package spikes.demos;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sneer.commons.environments.Environments;
import sneer.kernel.container.Containers;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import wheel.io.Logger;

public class DashboardDemo  {
	public DashboardDemo(){	
		Environments.my(Dashboard.class);
		installSampleInstrument();
	}
	
	private static void installSampleInstrument() {
		InstrumentManager manager = Environments.my(InstrumentManager.class);
		manager.registerInstrument(new Snapp1());
		manager.registerInstrument(new Snapp2());
		manager.registerInstrument(new Snapp3());
    }
	
	public static void main(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		Environments.runWith(Containers.newContainer(), new Runnable(){ @Override public void run() {
			new DashboardDemo();
		}});
	}
}

class Snapp1 implements Instrument{

	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		
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
	public int defaultHeight() {
		return 200;
	}

	@Override
	public String title() {
		return "foo";
	}
}

class Snapp2 implements Instrument{

	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		container.setLayout(new BoxLayout(container,  BoxLayout.PAGE_AXIS));
		container.add(new JCheckBox("Option 1"));
		container.add(new JCheckBox("Option 2"));
		container.add(new JCheckBox("Option 3"));
		container.add(new JCheckBox("Option 4"));
	}

	@Override
	public int defaultHeight() {
		return 50;
	}

	@Override
	public String title() {
		return null;
	}
}

class Snapp3 implements Instrument{
	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		JTextArea textArea = new JTextArea(15, 20);
        container.add(new JScrollPane(textArea));
	}
	
	@Override
	public int defaultHeight() {
		return 50;
	}
	
	@Override
	public String title() {
		return null;
	}
}
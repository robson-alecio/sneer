package sneer.skin.main.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.Environments;
import sneer.pulp.logging.out.LogToSystemOut;
import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.Instrument;
import static sneer.commons.environments.Environments.my;



public class RunMe{
	
	{
		my(LogToSystemOut.class);
		createAndShowGUI();
	}

	private void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container ref[] = new Container[1];
        DashboardPanel dashboardPane = new DashboardPanel();
        dashboardPane.install(new Instrument(){
        	@Override public String title() { return "Test 1";	}
			@Override public int defaultHeight() { return 80; }
			@Override public void init(InstrumentPanel container) {
				Container contentPane = container.contentPane();
				contentPane.setLayout(new BorderLayout());
				contentPane.add(new JButton("Click Me!"), BorderLayout.CENTER);
				ref[0] = contentPane;
			}
		});
        
		frame.setContentPane(dashboardPane);
        frame.setBounds(10, 10, 300, 600);
        frame.setVisible(true);
        
        logTree(ref[0]);
    }
    
	public static void main(String[] args) throws Exception {
		Environments.runWith(new SystemBrickEnvironment(), new Runnable(){ @Override public void run() {
			new RunMe();
		}});
	}
	
	public static void logTree(Container root) {
		Container parent = root;
		String tree = parent.getClass().getSimpleName();
		while(parent.getParent()!=null){
			parent = parent.getParent();
			tree = parent.getClass().getSimpleName() +  "." + tree;
		}
		
		System.out.println(tree);
	}
}
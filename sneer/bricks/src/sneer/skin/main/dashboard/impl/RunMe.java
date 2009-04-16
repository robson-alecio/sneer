package sneer.skin.main.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

import sneer.commons.environments.Environments;
import sneer.kernel.container.ContainersOld;
import sneer.skin.main.dashboard.InstrumentWindow;
import sneer.skin.main.instrumentregistry.Instrument;
import wheel.io.Logger;

public class RunMe{
	
	{ createAndShowGUI(); }

	private void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container ref[] = new Container[1];
        DashboardPane dashboardPane = new DashboardPane();
        dashboardPane.install(new Instrument(){
        	@Override public String title() { return "Test 1";	}
			@Override public int defaultHeight() { return 80; }
			@Override public void init(InstrumentWindow container) {
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
		Logger.redirectTo(System.out);
		Environments.runWith(ContainersOld.newContainer(), new Runnable(){ @Override public void run() {
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
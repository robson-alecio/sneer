package spikes.sandro.swing.layers;

import javax.swing.JFrame;

public class RunMe{
	
    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(new DashboardPane());
        frame.setBounds(10, 10, 300, 600);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
        	createAndShowGUI();
        }});
    }
}
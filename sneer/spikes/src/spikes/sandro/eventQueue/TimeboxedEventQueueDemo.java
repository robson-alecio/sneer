package spikes.sandro.eventQueue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TimeboxedEventQueueDemo {

	public static void main(String[] args) {
		TimeboxedEventQueue.startQueueing();
		JButton btn = initGui();
		btn.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) {
			forEverYoung();
		}});
	}
	
	private static void forEverYoung() {
		long t0 = System.currentTimeMillis();
		while(true){
			System.out.println(System.currentTimeMillis() - t0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static JButton initGui() {
		JFrame frm = new JFrame();
		frm.setLayout(new BorderLayout());
		JButton btn = new JButton("Send" );
		frm.add(btn, BorderLayout.CENTER);
		frm.setBounds(10,10,100,50);
		frm.setVisible(true);
		return btn;
	}
}

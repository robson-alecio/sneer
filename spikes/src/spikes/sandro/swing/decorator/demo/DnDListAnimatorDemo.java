package spikes.sandro.swing.decorator.demo;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import spikes.sandro.swing.decorator.ListAnimatorDecorator;
import spikes.sandro.swing.decorator.ListAnimatorMouseListener;

public class DnDListAnimatorDemo {

	public static void main(String[] args) throws Exception {
		JFrame f = new JFrame("Smooth List Drop");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DefaultListModel model = new DefaultListModel();
		model.addElement("Klaus");
		model.addElement("Bamboo");
		model.addElement("Sandro");
		model.addElement("Nell");
		
		final JList list = new JList(model);
		
		addDnDListeners(list);
		
		f.getContentPane().add(new JScrollPane(list));
		f.pack();
		f.setVisible(true);
	}

	private static void addDnDListeners(final JList lst) {
		ListAnimatorDecorator smoother = new ListAnimatorDecorator(lst) {
			@Override
			protected void move(int fromIndex, int toIndex) {
				DefaultListModel model = (DefaultListModel) lst.getModel();
				Object tmp = model.getElementAt(fromIndex);
				model.removeElementAt(fromIndex);
				model. add(toIndex,tmp );
				lst.revalidate();
				lst.repaint();
			}
		};

		ListAnimatorMouseListener listener = new ListAnimatorMouseListener(smoother);
		lst.addMouseListener(listener);
		lst.addMouseMotionListener(listener);
	}
}
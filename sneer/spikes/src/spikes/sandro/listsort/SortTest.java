package spikes.sandro.listsort;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.listsorter.ListSorter.SignalChooser;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class SortTest {

	public static void main(String[] args) throws Exception {
		
		ListRegister<Integer> source = new ListRegisterImpl<Integer>();
		source.add(0);
		source.add(4);
		
		Container container = ContainerUtils.newContainer();
		ListSorter sorter = container.produce(ListSorter.class);
		
		Comparator<Integer> comparator = new Comparator<Integer>(){ @Override public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}};
		
		final SignalChooser<Integer> chooser = new SignalChooser<Integer>(){ @Override public Signal<?>[] signalsToReceiveFrom(Integer element) {
			return new Signal<?>[]{new Constant<Integer>(element)};
		}};
		
		ListSignal<Integer> sorted = sorter.sort(source.output(), comparator, chooser);
		initGui(container, sorted);
		addData(source);
	}

	private static void initGui(final Container container, final ListSignal<Integer> sorted) throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){ @Override public void run() {
			ReactiveWidgetFactory factory = container.produce(ReactiveWidgetFactory.class);
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			ListWidget<Integer> widget;
			try {
				widget = factory.newList(sorted, newLabelProvider());
				frame.getContentPane().add(widget.getMainWidget(), BorderLayout.CENTER);
				frame.setBounds(10,10,200,300);
				frame.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}});
	}

	private static LabelProvider<Integer> newLabelProvider()
			throws IOException {
		return new LabelProvider<Integer>(){
			
			Signal<Image> _image = new Constant<Image>(ImageIO.read(SortTest.class.getResource("online.png")));
			
			@Override
			public Signal<Image> imageFor(Integer element) {
				return _image;
			}

			@Override
			public Signal<String> labelFor(Integer element) {
				return new Constant<String>(""+element);
			}};
	}

	private static void addData(ListRegister<Integer> source) {
		source.add(3);
		source.add(1);
		source.add(2);
	}
}
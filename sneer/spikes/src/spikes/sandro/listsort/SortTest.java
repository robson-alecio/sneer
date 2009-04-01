package spikes.sandro.listsort;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.kernel.container.ContainersOld;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.ui.TimeboxedEventQueue;
import wheel.reactive.impl.mocks.RandomBoolean;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class SortTest {
	
	static final Map<String, ByRef<String>> _byRefs = new HashMap<String, ByRef<String>>();
	static final Map<ByRef<String>, Register<String>> _registers = new HashMap<ByRef<String>, Register<String>>();
	static final Map<String, RandomBoolean> _onlineMap = new HashMap<String, RandomBoolean>();

	public static void main(String[] args) throws Exception {
		
		Environments.runWith(ContainersOld.newContainer(), new Runnable(){ @Override public void run() {
			try {
				start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
		
	}

	private static void start() throws Exception {
		
		ListRegister<SortTestElement> source = new ListRegisterImpl<SortTestElement>();
		
		ListSorter sorter = my(ListSorter.class);
		
		Comparator<SortTestElement> comparator = new Comparator<SortTestElement>(){ @Override public int compare(SortTestElement o1, SortTestElement o2) {
			boolean online1 = o1.isOnline().currentValue();
			boolean online2 = o2.isOnline().currentValue();

			if (online1 != online2)
				return online1 ? -1 : 1;

			return o1.nick().compareTo(o2.nick());
		}};
		
		final SignalChooser<SortTestElement> chooser = new SignalChooser<SortTestElement>() {	@Override public Signal<?>[] signalsToReceiveFrom(SortTestElement element) {
			return new Signal<?>[] { element.isOnline() };
		}};
		
		ListSignal<SortTestElement> sorted = sorter.sort(source.output(), comparator, chooser);
		initGui(sorted);
		addData(source);

	}

	private static void initGui(final ListSignal<SortTestElement> sorted) throws Exception {
		TimeboxedEventQueue.startQueueing(3000);
		
		SwingUtilities.invokeAndWait(new Runnable(){ @Override public void run() {
			ReactiveWidgetFactory factory = my(ReactiveWidgetFactory.class);
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			ListWidget<SortTestElement> widget;
			
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

	private static LabelProvider<SortTestElement> newLabelProvider()	throws IOException {
		return new LabelProvider<SortTestElement>(){
			
			Signal<? extends Image> _on = my(Signals.class).constant(ImageIO.read(SortTest.class.getResource("online.png")));
			Signal<? extends Image> _off = my(Signals.class).constant(ImageIO.read(SortTest.class.getResource("offline.png")));
			
			@Override
			public Signal<? extends Image> imageFor(SortTestElement element) {
				return element.isOnline().currentValue() ? _on : _off;
			}

			@Override
			public Signal<String> labelFor(SortTestElement element) {
				return my(Signals.class).constant(element.nick());
			}};
	}

	private static void addData(ListRegister<SortTestElement> source) {
		source.add(new SortTestElement());
		source.add(new SortTestElement());
		source.add(new SortTestElement());
		source.add(new SortTestElement());
		source.add(new SortTestElement());
		source.add(new SortTestElement());
		source.add(new SortTestElement());
	}
}
package spikes.sandro.listsort;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.ui.TimeboxedEventQueue;
import wheel.lang.ByRef;
import wheel.lang.Environment;
import static wheel.lang.Environment.my;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.impl.mocks.RandomBoolean;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class SortTest {
	
	static final Map<String, ByRef<String>> _byRefs = new HashMap<String, ByRef<String>>();
	static final Map<ByRef<String>, Register<String>> _registers = new HashMap<ByRef<String>, Register<String>>();
	static final Map<String, RandomBoolean> _onlineMap = new HashMap<String, RandomBoolean>();

	public static void main(String[] args) throws Exception {
		
		Environment.runWith(ContainerUtils.newContainer(), new Runnable(){ @Override public void run() {
			try {
				start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
		
	}

	private static void start() throws Exception {
		
		ListRegister<ByRef<String>> source = new ListRegisterImpl<ByRef<String>>();
		
		ListSorter sorter = my(ListSorter.class);
		
		Comparator<ByRef<String>> comparator = new Comparator<ByRef<String>>(){ @Override public int compare(ByRef<String> o1, ByRef<String> o2) {
			boolean online1 = _onlineMap.get(o1.value).output().currentValue();
			boolean online2 = _onlineMap.get(o2.value).output().currentValue();
			if(online1!=online2)
				return online1?-1:1;
			return o1.value.compareTo(o2.value);
		}};
		
		final SignalChooser<ByRef<String>> chooser = new SignalChooser<ByRef<String>>() {	@Override public Signal<?>[] signalsToReceiveFrom(ByRef<String> element) {
			return new Signal<?>[] { _registers.get(element).output(), 
										 _onlineMap.get(element.value).output() };
		}};
		
		ListSignal<ByRef<String>> sorted = sorter.sort(source.output(), comparator, chooser);
		initGui(sorted);
		addData(source);

	}

	private static void add(ListRegister<ByRef<String>> source, String value) {
		ByRef<String> byRefValue = ByRef.newInstance(value);
		_registers.put(byRefValue, new RegisterImpl<String>(value));
		_byRefs.put(value, byRefValue);
		_onlineMap.put(value, new RandomBoolean());
		source.add(byRefValue);
	}
	
	private static void initGui(final ListSignal<ByRef<String>> sorted) throws Exception {
		TimeboxedEventQueue.startQueueing(3000);
		
		SwingUtilities.invokeAndWait(new Runnable(){ @Override public void run() {
			ReactiveWidgetFactory factory = Environment.my(ReactiveWidgetFactory.class);
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			ListWidget<ByRef<String>> widget;
			
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

	private static LabelProvider<ByRef<String>> newLabelProvider()	throws IOException {
		return new LabelProvider<ByRef<String>>(){
			
			Signal<Image> _on = new Constant<Image>(ImageIO.read(SortTest.class.getResource("online.png")));
			Signal<Image> _off = new Constant<Image>(ImageIO.read(SortTest.class.getResource("offline.png")));
			
			@Override
			public Signal<Image> imageFor(ByRef<String> element) {
				return isOnline(element) ? _on : _off;
			}

			private Boolean isOnline(ByRef<String> element) {
				return _onlineMap.get(element.value).output().currentValue();
			}

			@Override
			public Signal<String> labelFor(ByRef<String> element) {
				return new Constant<String>(" " + isOnline(element) + " - " +element.value);
			}};
	}

	private static void addData(ListRegister<ByRef<String>> source) {
		add(source,"1");
		add(source,"2");
		add(source,"3");
		add(source,"4");
		add(source,"5");
		add(source,"6");
		add(source,"7");
		add(source,"8");
		add(source,"9");
	}
}
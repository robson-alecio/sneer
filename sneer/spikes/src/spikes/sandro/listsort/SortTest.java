package spikes.sandro.listsort;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.listsorter.ListSorter.SignalChooser;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.lang.ByRef;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class SortTest {
	
	static final Map<Integer, ByRef<Integer>> _byRefs = new HashMap<Integer, ByRef<Integer>>();
	static final Map<ByRef<Integer>, Register<Integer>> _registers = new HashMap<ByRef<Integer>, Register<Integer>>();

	public static void main(String[] args) throws Exception {
		
		ListRegister<ByRef<Integer>> source = new ListRegisterImpl<ByRef<Integer>>();
		
		Container container = ContainerUtils.newContainer();
		ListSorter sorter = container.produce(ListSorter.class);
		
		Comparator<ByRef<Integer>> comparator = new Comparator<ByRef<Integer>>(){ @Override public int compare(ByRef<Integer> o1, ByRef<Integer> o2) {
			return o2.value.compareTo(o1.value);
		}};
		
		final SignalChooser<ByRef<Integer>> chooser = new SignalChooser<ByRef<Integer>>() {
			Map<ByRef<Integer>, Signal<?>[]> signals = new HashMap<ByRef<Integer>, Signal<?>[]>();
			
			@Override
			public Signal<?>[] signalsToReceiveFrom(ByRef<Integer> element) {
				if(!signals.containsKey(element))
					signals.put(element, new Signal<?>[] { _registers.get(element).output() });
				return signals.get(element);
			}
		};
		
		add(source, -4);
		add(source, -2);
		
		ListSignal<ByRef<Integer>> sorted = sorter.sort(source.output(), comparator, chooser);
		initGui(container, sorted);
		addData(source);
		
	}

	private static void add(ListRegister<ByRef<Integer>> source, int value) {
		ByRef<Integer> byRefValue = ByRef.newInstance(value);
		if(!_registers.containsKey(byRefValue)){
			Register<Integer> register = new RegisterImpl<Integer>(value);
			_registers.put(byRefValue, register);
		}		
		_byRefs.put(key(value), byRefValue);
		source.add(byRefValue);
		
	}

	private static int key(int value) {
		return value<0?-value:value;
	}
	
	private static void changeValue(int value) {
		ByRef<Integer> byRef = _byRefs.get(key(value));
		byRef.value = -byRef.value;
		Register<Integer> register = _registers.get(byRef);
		register.setter().consume(byRef.value);
	}
	
	private static void initGui(final Container container, final ListSignal<ByRef<Integer>> sorted) throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){ @Override public void run() {
			ReactiveWidgetFactory factory = container.produce(ReactiveWidgetFactory.class);
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			ListWidget<ByRef<Integer>> widget;
			
			JToggleButton button = new JToggleButton("Test");
			button.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
				changeValue(2);
				changeValue(4);
				changeValue(6);
			}});
			
			frame.getContentPane().add(button, BorderLayout.SOUTH);
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

	private static LabelProvider<ByRef<Integer>> newLabelProvider()	throws IOException {
		return new LabelProvider<ByRef<Integer>>(){
			
			Signal<Image> _on = new Constant<Image>(ImageIO.read(SortTest.class.getResource("online.png")));
			Signal<Image> _off = new Constant<Image>(ImageIO.read(SortTest.class.getResource("offline.png")));
			
			@Override
			public Signal<Image> imageFor(ByRef<Integer> element) {
				return element.value>=0 ? _on : _off;
			}

			@Override
			public Signal<String> labelFor(ByRef<Integer> element) {
				return new Constant<String>(""+element.value);
			}};
	}

	private static void addData(ListRegister<ByRef<Integer>> source) {
		add(source, -3);
		add(source, -1);
		add(source, 0);
		add(source, -5);
		add(source, -6);
	}
}
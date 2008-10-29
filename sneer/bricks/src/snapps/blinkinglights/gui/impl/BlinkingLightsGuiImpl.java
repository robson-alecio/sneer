package snapps.blinkinglights.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.blinkinglights.gui.BlinkingLightsGui;
import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.ui.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

class BlinkingLightsGuiImpl implements BlinkingLightsGui {
	
	@Inject
	static private InstrumentManager _instrumentManager;

	@Inject
	static private BlinkingLights _blinkingLights;

	@Inject
	static private ReactiveWidgetFactory _rfactory;
	
	private ListWidget<Light> _lightsList;

	private Container _container;

	private JDialog _alertWindow;
	private JTextPane _alertTextPane;

	private final static Map<LightType, Constant<Image>> _images = new HashMap<LightType, Constant<Image>>();
	static {
		_images.put(LightType.GOOD_NEWS, new Constant<Image>(loadImage("good_news.png")));
		_images.put(LightType.INFO, new Constant<Image>(loadImage("info.png")));
		_images.put(LightType.WARN, new Constant<Image>(loadImage("warn.png")));
		_images.put(LightType.ERROR, new Constant<Image>(loadImage("error.png")));
	}
	
	public BlinkingLightsGuiImpl(){
		_instrumentManager.registerInstrument(this);
	} 
	
	private static Image loadImage(String fileName) {
		return Images.getImage(BlinkingLightsGuiImpl.class.getResource(fileName));
	}

	private Constant<Image> image(Light light) {
		return _images.get(light.type());
	}	

	private void showMessage(Light light){
		_alertWindow.setTitle(light.type().name());
		setAlertWindowBounds();
		Container panel = _alertWindow.getContentPane();
		panel.setLayout(new BorderLayout());
		
		_alertTextPane = new JTextPane();
		_alertTextPane.setText(createMessage(light));
		_alertTextPane.setOpaque(false);
		_alertTextPane.setEditable(false);
		
		JScrollPane scroll = new JScrollPane();
		scroll.getViewport().add(_alertTextPane);
		scroll.setOpaque(false);
		panel.add(scroll, BorderLayout.CENTER);
		scroll.setBorder(new EmptyBorder(5,5,5,5));
		
		_alertWindow.setVisible(true);
	}

	private void setAlertWindowBounds() {
		int windowWidth = 300;
		int space = 20;
		Point location = _container.getLocationOnScreen();
		_alertWindow.setBounds(location.x-windowWidth-space, location.y, windowWidth, _container.getHeight());
	}

	private String createMessage(Light light) {
		String stack = "";
		String msg = "";
		
		if(light.error()!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(out);
			light.error().printStackTrace(ps);
			stack = "\n " + new String(out.toByteArray());
			ps.close();
			msg = light.error().getMessage();
		}
		
		msg = msg==null?"":msg;
		
		return light.caption() + "\n " + msg + stack;
	}	
	
	@Override
	public void init(Container container) {
		_container = container;
		_lightsList = _rfactory.newList(_blinkingLights.lights(), new BlinkingLightsLabelProvider());
		
		iniGui();
		initMouseListener();
	}

	private void iniGui() {
		//Optimize set the scroll panel size to same size of window to prevent a BL label crop.
		//			 label now:        "bla, bla, bla, bla, bla, bla, b"  (crop: "la")  
		//			 label after fix: "bla, bla, bla, bla, bla, bla..."
		_alertWindow = new JDialog((JFrame)SwingUtilities.windowForComponent(_container), false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		_container.setLayout(new BorderLayout());
		_container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_lightsList.getComponent());
		scrollPane.setMinimumSize(size(_container));
		scrollPane.setPreferredSize(size(_container));
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
		_lightsList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_lightsList.getComponent().getBackground());
	}
	
	private void initMouseListener() {
		_lightsList.getComponent().addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(final MouseEvent event) {
			Light light = getClickedLight(event);
			if(light!=null)
				showMessage(light);
		}
		
		private Light getClickedLight(final MouseEvent event) {
			JList list = (JList)event.getSource();
			list.setSelectedIndex(list.locationToIndex(event.getPoint()));
			Light light = (Light)list.getSelectedValue();
			return light;
		}});
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 100 );
	}
	
	private String getName() {
		return "Blinking Lights";
	}

	final class BlinkingLightsLabelProvider implements LabelProvider<Light> {
				
		@Override
		public Signal<String> labelFor(Light light) {
			return new Constant<String>(light.caption());
		}

		@Override
		public Signal<Image> imageFor(Light light) {
			return image(light);
		}
	}
}
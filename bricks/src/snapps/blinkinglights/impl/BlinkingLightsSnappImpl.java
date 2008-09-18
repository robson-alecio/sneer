package snapps.blinkinglights.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.blinkinglights.BlinkingLightsSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

class BlinkingLightsSnappImpl implements BlinkingLightsSnapp {
	
	@Inject
	static private SnappManager _snapps;

	@Inject
	static private BlinkingLights _blinkingLights;

	@Inject
	static private ReactiveWidgetFactory _rfactory;
	
	private ListWidget<Light> _lightsList;

	private Container _container;

	private final static Map<LightType, Constant<Image>> _images = new HashMap<LightType, Constant<Image>>();
	static {
		_images.put(LightType.GOOD_NEWS, new Constant<Image>(loadImage("good_news.png")));
		_images.put(LightType.INFO, new Constant<Image>(loadImage("info.png")));
		_images.put(LightType.WARN, new Constant<Image>(loadImage("warn.png")));
		_images.put(LightType.ERROR, new Constant<Image>(loadImage("error.png")));
	}
	
	public BlinkingLightsSnappImpl(){
		_snapps.registerSnapp(this);
	} 
	
	private static Image loadImage(String fileName) {
		return Images.getImage(BlinkingLightsSnappImpl.class.getResource(fileName));
	}

	private Constant<Image> image(Light light) {
		return _images.get(light.type());
	}	

	private void showMessage(Light light){
		LightType type = light.type();
		String title = type.name();
		int optType = type==LightType.ERROR?JOptionPane.ERROR_MESSAGE: 
			  		  type==LightType.WARN? JOptionPane.WARNING_MESSAGE:
				      type==LightType.GOOD_NEWS? JOptionPane.PLAIN_MESSAGE:
						  				 JOptionPane.INFORMATION_MESSAGE;
		
		JOptionPane.showMessageDialog(_container, createMessage(light), title, optType);
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
		
		return light.message() + "\n " + msg + stack;
	}	
	@Override
	public void init(Container container) {
		_container = container;
		_lightsList = _rfactory.newList(_blinkingLights.lights(), new BlinkingLightsLabelProvider());
		
		iniGui();
		initMouseListener();
	}

	private void iniGui() {
		JScrollPane scrollPane = new JScrollPane();
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
		return new Dimension(container.getSize().width, 90 );
	}
	
	private String getName() {
		return "Blinking Lights";
	}

	public final class BlinkingLightsLabelProvider implements LabelProvider<Light> {
				
		@Override
		public Signal<String> labelFor(Light light) {
			return new Constant<String>(light.message());
		}

		@Override
		public Signal<Image> imageFor(Light light) {
			return image(light);
		}
	}
}

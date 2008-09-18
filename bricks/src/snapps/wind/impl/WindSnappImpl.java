package snapps.wind.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.wind.WindSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

class WindSnappImpl implements WindSnapp {
	
	@Inject
	static private SnappManager _snapps;

	@Inject
	static private BlinkingLights _blinkingLights;

	@Inject
	static private ReactiveWidgetFactory _rfactory;

	private final static Signal<Image> _meImage;
	//private final static Signal<Image> _otherImage;
	
	private ListWidget<Light> _lightsList;

	private Container _container;

	static {
		_meImage = new Constant<Image>(loadImage("me.png"));
		//_otherImage = new Constant<Image>(loadImage("other.png"));
	}
	
	public WindSnappImpl(){
		_snapps.registerSnapp(this);
	} 
	
	private static Image loadImage(String fileName) {
		return Images.getImage(WindSnappImpl.class.getResource(fileName));
	}

	private Signal<Image> image(@SuppressWarnings("unused") Light light) {
		//return _images.get(light.type());
		return _meImage;
	}	

	private void showMessage(Light light){
		LightType type = light.type();
		String title = type.name();
		int optType = type==LightType.ERROR?JOptionPane.ERROR_MESSAGE: 
					  type==LightType.WARN? JOptionPane.WARNING_MESSAGE:
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
		return "Wind";
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

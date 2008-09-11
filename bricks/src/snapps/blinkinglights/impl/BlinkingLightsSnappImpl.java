package snapps.blinkinglights.impl;

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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.blinkinglights.BlinkingLightsSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import wheel.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

public class BlinkingLightsSnappImpl implements BlinkingLightsSnapp {

	static final Image INFO = getImage("info.png");
	static final Image ERROR = getImage("error.png");
	static final Image WARN = getImage("warn.png");
	
	@Inject
	static private SnappManager _snapps;

	@Inject
	static private BlinkingLights _blinkingLights;

	@Inject
	static private RFactory _rfactory;
	
	private ListWidget<Light> _lightsList;
	private Container _container;

	
	public BlinkingLightsSnappImpl(){
		_snapps.registerSnapp(this);
	} 

	private static Image getImage(String fileName) {
		return Images.getImage(BlinkingLightsSnappImpl.class.getResource(fileName));
	}
	
	@Override
	public void init(Container container) {
		_container = container;
		_lightsList = _rfactory.newList(_blinkingLights.lights(), new BlinkingLightsLabelProvider());
		
		iniGui(container);
		initWarnings();
	}

	private void iniGui(Container container) {
		JScrollPane scrollPane = new JScrollPane();
		container.setLayout(new BorderLayout());
		container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_lightsList.getComponent());
		scrollPane.setMinimumSize(size(container));
		scrollPane.setPreferredSize(size(container));
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
		_lightsList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_lightsList.getComponent().getBackground());
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 90 );
	}

	private void initWarnings() {
		_lightsList.getComponent().addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(final MouseEvent event) {
			
			if(isInvalidMouseClick(event)) 
				return;
			
			Light light = getClickedLight(event);
            
            if(light.isWarn()){
                JOptionPane.showMessageDialog(_container, createMessage(light), "Warn:", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(light.isError()){
            	JOptionPane.showMessageDialog(_container, createMessage(light), "Error:", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(_container, createMessage(light), "Info:", JOptionPane.INFORMATION_MESSAGE);
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

		private Light getClickedLight(final MouseEvent event) {
			JList list = (JList)_lightsList.getComponent();
			list.setSelectedIndex(list.locationToIndex(event.getPoint()));
            Light light = (Light)list.getSelectedValue();
			return light;
		}

		private boolean isInvalidMouseClick(final MouseEvent event) {
			return SwingUtilities.isLeftMouseButton(event) && event.getClickCount()<=1;
		}});
	}
	
	@Override
	public String getName() {
		return "Blinking Lights";
	}
	
	public final class BlinkingLightsLabelProvider implements LabelProvider<Light> {
		
		Constant<Image> _error = new Constant<Image>(ERROR);
		Constant<Image> _warn = new Constant<Image>(WARN);
		Constant<Image> _info = new Constant<Image>(INFO);
		
		@Override
		public Signal<String> labelFor(Light light) {
			return new Constant<String>(light.message());
		}

		@Override
		public Signal<Image> imageFor(Light light) {
			if(light.isError()) 
				return _error;
			
			if(light.isWarn()) 
				return _warn;
			
			return _info;
		}
	}
}

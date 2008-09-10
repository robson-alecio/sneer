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
		_lightsList.getComponent().setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width,80 );
	}

	private void initWarnings() {
		_lightsList.getComponent().addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(final MouseEvent event) {
			
			if(isInvalidMouseClick(event)) 
				return;
			
			Light light = getClickedLight(event);
            
            if(isWarningLight(light)){
                JOptionPane.showMessageDialog(_container, light.message(), "Warning:", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(_container, getErrorMessage(light), "Error:", JOptionPane.ERROR_MESSAGE);
		}

		private String getErrorMessage(Light light) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(out);
			light.error().printStackTrace(ps);
			String stack = new String(out.toByteArray());
			ps.close();
			
			String msg = light.error().getMessage();
			msg = msg==null?"":msg + "\n ";
			
			return light.message() + "\n " + msg + stack;
		}

		private boolean isWarningLight(Light light) {
			return light.error()==null;
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
		
		@Override
		public Signal<String> labelFor(Light light) {
			return new Constant<String>(light.message());
		}

		@Override
		public Signal<Image> imageFor(Light light) {
			return light.error()==null?_warn:_error;
		}
	}
}

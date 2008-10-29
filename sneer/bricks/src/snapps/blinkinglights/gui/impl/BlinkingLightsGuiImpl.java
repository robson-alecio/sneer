package snapps.blinkinglights.gui.impl;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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
	
	@Override
	public void init(Container container) {
		_container = container;
		_lightsList = _rfactory.newList(_blinkingLights.lights(), new BlinkingLightsLabelProvider());
		iniGui();
		new AlertWindowSupport();
	}

	private void iniGui() {
		//Optimize set the scroll panel size to same size of window to prevent a BL label crop.
		//			 label now:        "bla, bla, bla, bla, bla, bla, b"  (crop: "la")  
		//			 label after fix: "bla, bla, bla, bla, bla, bla..."
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
	
	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 100 );
	}
	
	private String getName() {
		return "Blinking Lights";
	}
	
	private static Image loadImage(String fileName) {
		return Images.getImage(BlinkingLightsGuiImpl.class.getResource(fileName));
	}

	private Constant<Image> image(Light light) {
		return _images.get(light.type());
	}
	
	private final class AlertWindowSupport{
		private static final int HORIZONTAL_LIMIT = 600;
		
		private static final String HELP = "HELP";
		private static final String STACK_TRACE = "STACK_TRACE";
		
		private JDialog _window;
		private JTextPane _textPane;
		private JScrollPane _scroll;
		
		private AlertWindowSupport(){
			initGui();
			initMouseListener();
		}

		private void initGui() {
			_window = new JDialog((JFrame)SwingUtilities.windowForComponent(_container), false);
			
			_textPane = new JTextPane();
			_textPane.setOpaque(false);
			_textPane.setEditable(false);
			initDocumentStyles(_textPane.getStyledDocument());
			
			_scroll = new JScrollPane();
			_scroll.getViewport().add(_textPane);
			_scroll.setOpaque(false);
			
			Container panel = _window.getContentPane();
			panel.setLayout(new BorderLayout());		panel.add(_scroll, BorderLayout.CENTER);
			_scroll.setBorder(new EmptyBorder(5,5,5,5));
		}
		
		private void initMouseListener() {
			_lightsList.getComponent().addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(final MouseEvent event) {
				Light light = getClickedLight(event);
				if(light!=null)	show(light);
			}});
		}		

		private Light getClickedLight(final MouseEvent event) {
			JList list = (JList)event.getSource();
			list.setSelectedIndex(list.locationToIndex(event.getPoint()));
			Light light = (Light)list.getSelectedValue();
			return light;
		}
		
		private void show(Light light){
			setWindowTitle(light);
			setWindowsMessage(light);
			setWindowBounds(light);
			_window.setVisible(true);
		}

		private void setWindowTitle(Light light) {
			_window.setTitle(light.type().name());
			_window.setIconImage(image(light).currentValue());
		}
		
		private void setWindowsMessage(Light light) {
			_textPane.setText("");
			StyledDocument doc = _textPane.getStyledDocument();
			appendStyledText(doc, light.helpMessage(), HELP);
			
			if(light.error()==null) return;
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(out);
			light.error().printStackTrace(ps);
			String stack = new String(out.toByteArray());
			ps.close();
			
			appendStyledText(doc, "\n\n" + stack.trim(), STACK_TRACE);
		}	
		
		private void setWindowBounds(Light light) {
			int space = 20;
			_window.pack();
			
			int windowHeight = _container.getHeight();
			Point location = _container.getLocationOnScreen();
			int y = location.y;
			int x = location.x;
			if(light.error()!=null){
				y = y - windowHeight;
				windowHeight = windowHeight * 2;
			}
			int width = _window.getWidth() + space;
			if(width>HORIZONTAL_LIMIT) width = HORIZONTAL_LIMIT;
			_window.setBounds(x - width - space, y, width, windowHeight);
		}
		
		private void appendStyledText(StyledDocument doc, String msg, String style) {
			try {
				doc.insertString(doc.getLength(), msg, doc.getStyle(style));
			} catch (BadLocationException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
		}

		private void initDocumentStyles(StyledDocument doc) {
			Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );
		    
		    Style help = doc.addStyle( HELP, def );
		    StyleConstants.setBold(help, true);
		    StyleConstants.setFontSize( help, 14 );
		    doc.addStyle(HELP, help);
		    
		    Style stack = doc.addStyle( STACK_TRACE, def );
		    StyleConstants.setForeground(stack, Color.DARK_GRAY);
		    StyleConstants.setFontSize( stack, 11 );
		    doc.addStyle(STACK_TRACE, stack);
		}
	}
	
	private final class BlinkingLightsLabelProvider implements LabelProvider<Light> {
				
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
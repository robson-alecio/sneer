package sneer.bricks.snapps.gis.map.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.snapps.gis.location.Location;
import sneer.bricks.snapps.gis.location.Locations;
import sneer.bricks.snapps.gis.map.MapRenderer;
import sneer.bricks.snapps.gis.map.gui.MapGui;

class MapGuiImpl implements MapGui{

	private String _defaultAddress = "Florianópolis";

	private final JLabel _mapHolder = new JLabel();
	private final JTextField _address = new JTextField(_defaultAddress);
	private final JScrollPane _scroll = my(SynthScrolls.class).create();
	
	private Reception _locationReception;
	private Reception _imageReception;
	private int _zoom = 10;

	MapGuiImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	}
	
	private void updateAddress(final String address, final int zoom) {
		_address.setEnabled(false);
		_address.update(_address.getGraphics());
		
		if(_locationReception!=null)
			_locationReception.dispose();
		
		_locationReception = my(Signals.class).receive(my(Locations.class).find(address), 
			new Consumer<Location>(){ @Override public void consume(Location value) {
				renderLocation(value, zoom);
			}});
	}

	protected void renderLocation(Location location, int zoom) {

		if(zoom>17){
			zoom=17;
			_zoom=17;
		}
		
		if(zoom<0){
			zoom=0;
			_zoom=0;
		}
		
		if(location==null) return;
		
		if(_imageReception!=null)
			_imageReception.dispose();
	
		_imageReception = my(Signals.class).receive(my(MapRenderer.class).render(location, zoom), 
			new Consumer<Image>(){ @Override public void consume(Image value) {
				paintImage(value);
			}});
	}

	protected void paintImage(Image image) {

		if(image==null) 
			return;
		
		_mapHolder.setIcon(new ImageIcon(image));

		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			centerScrollBar(_scroll.getVerticalScrollBar(), _scroll.getSize().height/2);
			centerScrollBar(_scroll.getHorizontalScrollBar(), _scroll.getSize().width/2);
			_address.setEnabled(true);
		}});
	}
	
	private void centerScrollBar(JScrollBar bar, int offset) {
		BoundedRangeModel model = bar.getModel();
		int max = model.getMaximum();
		model.setValue(-offset+max/2);
	}
	
	@Override
	public int defaultHeight() {
		return 162;
	}

	@Override
	public void init(InstrumentPanel container) {
		container.contentPane().setLayout(new BorderLayout());
		container.contentPane().add(_address, BorderLayout.NORTH);
		container.contentPane().add(_scroll, BorderLayout.CENTER);

		_scroll.getViewport().add(_mapHolder);
		updateAddress(_defaultAddress, _zoom);
		
		JMenuItem zoomIn = new JMenuItem("Zoom In");
		container.actions().add(zoomIn);
		zoomIn.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			updateAddress(_address.getText(), _zoom++);
		}});
		
		JMenuItem zoomOut = new JMenuItem("Zoom Out");
		container.actions().add(zoomOut);
		zoomOut.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			updateAddress(_address.getText(), _zoom++);
		}});

		_address.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			updateAddress(_address.getText(), _zoom);
		}});

		_mapHolder.addMouseWheelListener(new MouseWheelListener(){ @Override public void mouseWheelMoved(MouseWheelEvent event) {
			_zoom = _zoom + event.getWheelRotation();
			updateAddress(_address.getText(), _zoom);
		}});
	}

	@Override
	public String title() {
		return "Location";
	}
}
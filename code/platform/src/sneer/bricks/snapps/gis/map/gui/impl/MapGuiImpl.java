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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.bricks.hardware.gui.Action;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.snapps.gis.location.Location;
import sneer.bricks.snapps.gis.location.Locations;
import sneer.bricks.snapps.gis.map.MapRenderer;
import sneer.bricks.snapps.gis.map.gui.MapGui;
import sneer.foundation.lang.Consumer;

class MapGuiImpl implements MapGui{

	private static final int MIN_ZOOM = 0;
	private static final int MAX_ZOOM = 17;
	private static final String DEFAULT_ADDRESS = "Florianópolis";

	private final JLabel _mapHolder = new JLabel();
	private final JTextField _address = new JTextField(DEFAULT_ADDRESS);
	private final JScrollPane _scroll = my(SynthScrolls.class).create();
	
	private int _zoom = 10;

	MapGuiImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	}
	
	private void updateAddress(final String address, final int zoom) {
		_address.setEnabled(false);
		_address.update(_address.getGraphics());
		
		my(Locations.class).find(address, 
			new Consumer<Location>(){ @Override public void consume(Location location) {
				renderLocation(location, zoom);
			}});
	}

	protected void renderLocation(Location location, int zoom) {
		if(zoom>MAX_ZOOM)  zoom=MAX_ZOOM;
		if(zoom<MIN_ZOOM)  zoom=MIN_ZOOM;
		_zoom=zoom;
		
		if(location==null) return;
		
		my(MapRenderer.class).render(
			new Consumer<Image>(){ @Override public void consume(Image image) {
				paintImage(image);
			}}, location, _zoom);
	}

	protected void paintImage(Image image) {
		if(image==null) return;
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
		updateAddress(DEFAULT_ADDRESS, _zoom);
		
		container.actions().addAction(new Action(){
			@Override public String caption() { return "Zoom In";}
			@Override public void run() { 
				_zoom=_zoom+1;
				updateAddress(_address.getText(), _zoom);
			}
		});
		
		container.actions().addAction(new Action(){
			@Override public String caption() { return "Zoom Out";}
			@Override public void run() { 
				_zoom=_zoom-1;
				updateAddress(_address.getText(), _zoom);
			}
		});
		
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
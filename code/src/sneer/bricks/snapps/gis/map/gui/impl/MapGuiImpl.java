package sneer.bricks.snapps.gis.map.gui.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

	private String _defaultAddress = "R Juquiá 114, São Paulo";

	private final JLabel _mapHolder = new JLabel();
	private final JTextField _address = new JTextField(_defaultAddress);
	private final JScrollPane _scroll = my(SynthScrolls.class).create();
	
	private Reception _locationReception;
	private Reception _imageReception;

	MapGuiImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	}
	
	private void updateAddress(final String address) {
		_address.setEnabled(false);
		_address.update(_address.getGraphics());
		
		if(_locationReception!=null)
			_locationReception.dispose();
		
		_locationReception = my(Signals.class).receive(my(Locations.class).find(address), 
			new Consumer<Location>(){ @Override public void consume(Location value) {
				renderLocation(value);
			}});
	}

	protected void renderLocation(Location location) {
		if(location==null) return;
		
		if(_imageReception!=null)
			_imageReception.dispose();
	
		_imageReception = my(Signals.class).receive(my(MapRenderer.class).render(location), 
			new Consumer<Image>(){ @Override public void consume(Image value) {
				paintImage(value);
			}});
	}

	protected void paintImage(Image image) {
		_address.setEnabled(true);

		if(image==null) 
			return;
		
		_mapHolder.setIcon(new ImageIcon(image));

		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			centerScrollBar(_scroll.getVerticalScrollBar(), _scroll.getSize().height/2);
			centerScrollBar(_scroll.getHorizontalScrollBar(), _scroll.getSize().width/2);
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
		updateAddress(_defaultAddress);

		_address.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			updateAddress(_address.getText());
		}});
		_mapHolder.setBackground(Color.white);
		_scroll.getViewport().add(_mapHolder);
		container.contentPane().add(_address, BorderLayout.NORTH);
		container.contentPane().add(_scroll, BorderLayout.CENTER);
		container.contentPane().setVisible(true);
	}

	@Override
	public String title() {
		return "Location";
	}
}
package sneer.bricks.snapps.maps.gui.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.threads.Threads;
import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.snapps.maps.LocationKeeper;
import sneer.bricks.snapps.maps.gui.LocationGui;

class LocationGuiImpl implements LocationGui{

	private final int _mapSize = 600;
	private int _zoom = 15;
	private final JLabel _mapHolder = new JLabel();
	private final JTextField _address = new JTextField();
	private final JScrollPane _scroll = my(SynthScrolls.class).create();
	private final LocationKeeper _keeper = my(LocationKeeper.class);

	LocationGuiImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	}
	
	private void updateAddress(final String address) {
		_address.setEnabled(false);
		_address.update(_address.getGraphics());
		String geocodeInfo[] = geocode(address).split(",");
		final String latitude = geocodeInfo[2];
		final String longitude = geocodeInfo[3];
		
		my(Threads.class).startDaemon("MapLoader",  new Runnable(){ @Override public void run() {
			_address.setEnabled(true);
			map(latitude, longitude, _mapSize, _mapSize, _zoom);
			_keeper.locationSetter().consume(address);
			_keeper.latitudeSetter().consume(latitude);
			_keeper.longitudeSetter().consume(longitude);
			
			my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
				centerScrollBar(_scroll.getVerticalScrollBar(), _scroll.getSize().height/2);
				centerScrollBar(_scroll.getHorizontalScrollBar(), _scroll.getSize().width/2);
			}
			private void centerScrollBar(JScrollBar bar, int offset) {
				BoundedRangeModel model = bar.getModel();
				int max = model.getMaximum();
				model.setValue(-offset+max/2);
			}});
		}});
	}

	private String geocode(String location) {
		try {
			String data = "http://maps.google.com/maps/geo?output=csv&key=ABQIAAAAipu2vgwNjShyGzhINGjXvRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTKgtleBywtdYBkXFEBvmkPMqvBzg&q="
					+ URLEncoder.encode(location, "UTF-8") + "";
			URL url = new URL(data);
			return new String(grabData(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void map(String latitude, String longitude, int width, int height, int zoom) {
		BufferedImage image = null;
		try {
			String data = "center=" + latitude + "," + longitude + "&zoom=" + zoom + "&size=" + width + "x" + height
							  + "&key=ABQIAAAAipu2vgwNjShyGzhINGjXvRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTKgtleBywtdYBkXFEBvmkPMqvBzg";
			URL url = new URL("http://maps.google.com/staticmap?" + data);
			byte[] imageData = grabData(url);
			ByteArrayInputStream imageIn = new ByteArrayInputStream(imageData);
			image = ImageIO.read(imageIn);
			Graphics2D g2d=((Graphics2D)image.getGraphics());
			g2d.setColor(Color.green);
			
			g2d.fillOval(width/2-5, height/2-5,10,10);
			g2d.setColor(Color.black);
			g2d.drawOval(width/2-5, height/2-5,10,10);
			
			_mapHolder.setIcon(new ImageIcon(image));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] grabData(URL url) throws Exception {
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		byte[] buffer = new byte[1000];
		int readed = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((readed = in.read(buffer)) != -1) {
			out.write(buffer, 0, readed);
		}
		return out.toByteArray();
	}

	@Override
	public int defaultHeight() {
		return 162;
	}

	@Override
	public void init(InstrumentPanel container) {
		container.contentPane().setLayout(new BorderLayout());
		String address=_keeper.location().currentValue();
		if (address == null) address = "R Juquiá 114, São Paulo";
		_address.setText(address);
		updateAddress(address);

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

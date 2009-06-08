package spikes.snapps.gis.ui.impl;

import static sneer.commons.environments.Environments.my;

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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;
import spikes.snapps.location.LocationKeeper;

public class LocationGuiImpl {

	private final LocationKeeper _keeper = my(LocationKeeper.class);

	private final Threads _threads = my(Threads.class);

	private final Stepper _refToAvoidGc;

	LocationGuiImpl() {
		_refToAvoidGc = new Stepper() { @Override public boolean step() {
			openGUI();
			return false;
		}};

		_threads.registerStepper(_refToAvoidGc);
	}

	private JLabel _mapHolder = new JLabel();
	private JTextField _address = new JTextField();
	private int _width=500;
	private int _height=550;
	private int _mapWidth=490;
	private int _mapHeight=500;
	

	private void openGUI() {
		String title = "Location";
		final JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel content = new JPanel(new BorderLayout());
		String address=_keeper.location().currentValue();
		if (address == null) address = "R Juquiá 114, São Paulo";
		_address.setText(address);
		updateAddress(address);

		_address.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			updateAddress(_address.getText());
		}});
		_mapHolder.setBackground(Color.white);
		content.add(_address, BorderLayout.NORTH);
		content.add(_mapHolder, BorderLayout.CENTER);
		frame.getContentPane().add(content, BorderLayout.CENTER);
		frame.setSize(_width, _height);
		frame.setVisible(true);
	}

	private void updateAddress(String address) {
		_address.setEnabled(false);
		_address.update(_address.getGraphics());
		String geocodeInfo[] = geocode(address).split(",");
		String latitude = geocodeInfo[2];
		String longitude = geocodeInfo[3];
		BufferedImage map = map(latitude, longitude, _mapWidth, _mapHeight, 16);
		_mapHolder.setIcon(new ImageIcon(map));
		_address.setEnabled(true);
		if (!address.equals(_keeper.location().currentValue()))
			_keeper.locationSetter().consume(address);
		if (!latitude.equals(_keeper.latitude().currentValue()))
			_keeper.latitudeSetter().consume(latitude);
		if (!longitude.equals(_keeper.longitude().currentValue()))
			_keeper.longitudeSetter().consume(longitude);
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

	private BufferedImage map(String latitude, String longitude, int width,
			int height, int zoom) {
		BufferedImage image = null;
		try {
			String data = "center="
					+ latitude
					+ ","
					+ longitude
					+ "&zoom="
					+ zoom
					+ "&size="
					+ width
					+ "x"
					+ height
					+ "&key=ABQIAAAAipu2vgwNjShyGzhINGjXvRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTKgtleBywtdYBkXFEBvmkPMqvBzg";
			URL url = new URL("http://maps.google.com/staticmap?" + data);
			byte[] imageData = grabData(url);
			ByteArrayInputStream imageIn = new ByteArrayInputStream(imageData);
			image = ImageIO.read(imageIn);
			Graphics2D g2d=((Graphics2D)image.getGraphics());
			g2d.setColor(Color.green);
			g2d.fillOval(_mapWidth/2-5,_mapHeight/2-5,10,10);
			g2d.setColor(Color.black);
			g2d.drawOval(_mapWidth/2-5,_mapHeight/2-5,10,10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
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

}

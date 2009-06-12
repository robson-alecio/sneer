package sneer.bricks.snapps.gis.map.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.log.Logger;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.threads.Threads;
import sneer.bricks.snapps.gis.location.Location;
import sneer.bricks.snapps.gis.map.MapRenderer;

class MapRendererImpl implements MapRenderer{

	private int _zoom = 10;
	private int _mapSize = 600;

	@Override
	public Signal<Image> render(Location location) {
		return render(location, _zoom);
	}
	
	@Override
	public Signal<Image> render(final Location location, final int zoom) {
		final Register<Image> register = my(Signals.class).newRegister(null);
		my(Threads.class).startDaemon("MapLoader",  new Runnable(){ @Override public void run() {
			try {
				map(location, register, zoom);
			} catch (IOException e) {
				my(Logger.class).log(e);
			}
		}});
		return register.output();
	}

	private void map(Location location, Register<Image> register, int zoom) throws IOException {
		BufferedImage image = null;
		String data = "center=" + location.latitude() + "," + location.longitude() + "&zoom=" + zoom + "&size=" + _mapSize + "x" + _mapSize
						  + "&key=ABQIAAAAipu2vgwNjShyGzhINGjXvRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTKgtleBywtdYBkXFEBvmkPMqvBzg";
		URL url = new URL("http://maps.google.com/staticmap?" + data);
		byte[] imageData = my(IO.class).streams().readBytesAndClose(url.openStream());
		ByteArrayInputStream imageIn = new ByteArrayInputStream(imageData);
		image = ImageIO.read(imageIn);
		Graphics2D g2d=((Graphics2D)image.getGraphics());
		g2d.setColor(Color.green);
		
		g2d.fillOval(_mapSize/2-5, _mapSize/2-5,10,10);
		g2d.setColor(Color.black);
		g2d.drawOval(_mapSize/2-5, _mapSize/2-5,10,10);
		
		register.setter().consume(image);
	}
}
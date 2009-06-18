package sneer.bricks.snapps.gis.location.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.log.Logger;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.snapps.gis.location.Location;

class LocationImpl implements Location {

	private final Register<Location> _register = my(Signals.class).newRegister(null);

	private String _latitude;
	private String _longitude;
	
	public LocationImpl(final String address) {
		my(Threads.class).startDaemon("LocationFinder", new Runnable(){ @Override public void run() {
			String geocodeInfo[];
			try {
				geocodeInfo = geocode(address).split(",");
				_latitude = geocodeInfo[2];
				_longitude = geocodeInfo[3];
				_register.setter().consume(LocationImpl.this);
			} catch (IOException e) {
				my(Logger.class).log(e);
			}
		}});
	}

	@Override
	public String latitude() {
		return _latitude;
	}

	@Override
	public String longitude() {
		return _longitude;
	}
	
	private String geocode(String location) throws IOException {
			String data = "http://maps.google.com/maps/geo?" +
								"output=csv&key=ABQIAAAAipu2vgwNjShyGzhINGjXvRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTKgtleBywtdYBkXFEBvmkPMqvBzg&q=" 
								+ URLEncoder.encode(location, "UTF-8") + "";
			byte[] bytes = my(IO.class).streams().readBytesAndClose(new URL(data).openStream());
			return new String(bytes);
	}
	
	Signal<Location> location(){
		return _register.output();
	}
}
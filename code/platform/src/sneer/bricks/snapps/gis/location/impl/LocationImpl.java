package sneer.bricks.snapps.gis.location.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.bricks.network.httpgateway.HttpGateway;
import sneer.bricks.snapps.gis.location.Location;
import sneer.foundation.lang.Consumer;

class LocationImpl implements Location {

	private String _latitude;
	private String _longitude;
	private Consumer<Location> _receiver;
	
	public LocationImpl(final String address, Consumer<Location> receiver) {
		_receiver = receiver;
		getDataForLocation(address);
	}

	@Override
	public String latitude() {
		return _latitude;
	}

	@Override
	public String longitude() {
		return _longitude;
	}
	
	private void getDataForLocation(String location)  {
			String url;
			try {
				url = "http://maps.google.com/maps/geo?" +
						"output=csv&key=ABQIAAAAipu2vgwNjShyGzhINGjXvRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxTKgtleBywtdYBkXFEBvmkPMqvBzg&q=" 
						+ URLEncoder.encode(location, "UTF-8") + "";
			} catch (UnsupportedEncodingException e) {
				my(ExceptionLogger.class).log(e);
				return;
			}
			
			my(HttpGateway.class).get(url, 
				new Consumer<byte[]>(){ @Override public void consume(byte[] value) {
					parseAndSetLocation(value);
				}});
	}
	
	private void parseAndSetLocation(byte[] value) {
		String geocodeInfo[] = new String(value).split(",");
		_latitude = geocodeInfo[2];
		_longitude = geocodeInfo[3];
		_receiver.consume(LocationImpl.this);
	}
}
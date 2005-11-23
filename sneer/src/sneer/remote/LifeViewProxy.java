package sneer.remote;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sneer.life.JpgImage;
import sneer.life.LifeView;
import wheelexperiments.Actor;
import wheelexperiments.Cool;
import wheelexperiments.reactive.SetSignal;
import wheelexperiments.reactive.Signal;

class LifeViewProxy implements LifeView, Actor, Serializable {

	transient private final QueryExecuter _queryExecuter;
	transient private boolean _scoutsSent = false;
	transient private boolean _active = false;
	transient private final Object _activeMonitor = new Object();
	
	private Date _lastSightingDate;
	
	private Map<String, LifeView> _contactCache = new HashMap<String, LifeView>();

	private LifeCache _cache;
	
	private final IndianForObject<String> _indianForName = new IndianForName();
	private final IndianForObject<String> _indianForThoughtOfTheDay = new IndianForThoughtOfTheDay();
	private final IndianForObject<JpgImage> _indianForPicture = new IndianForPicture();
	private final IndianForSet<String> _indianForNicknames = new IndianForNicknames();

	
	public LifeViewProxy(QueryExecuter queryExecuter) {
		_queryExecuter = queryExecuter;
	}

	private void sendScouts() throws IOException {
		if (_scoutsSent) return;

		_queryExecuter.execute(_indianForName);
		_queryExecuter.execute(_indianForThoughtOfTheDay);
		_queryExecuter.execute(_indianForPicture);
		_queryExecuter.execute(_indianForNicknames);
		_scoutsSent = true;
	}

	private Runnable updater() {
		return new Runnable() {
			public void run() {
				while (_active) {
					update();
//					Cool.sleep(1000 * 60);
					Cool.sleep(1000 * 3);
				}
				
				synchronized (_activeMonitor) {
					_activeMonitor.notify();
				}
			}
		};
	}

	private void update() {
		try {
			LifeCache newCache = _queryExecuter.execute(new LifeSightingQuery());
			if (newCache == null) return;
			if (newCache.lastSightingDate() == null) return;
			_cache = newCache;
			_lastSightingDate = new Date();

			sendScouts();
		} catch (ConnectException ignored) {
			_scoutsSent = false;
			//Simply ignore this exception, since the connection will try to reconnect anyway.
		} catch (IOException x) {
			_scoutsSent = false;
			x.printStackTrace(); //TODO Implement logging using wheel.environment.Environment.err().
		}
	}

	public Signal<String> name() {
		return _indianForName.localSourceToNotify();
	}

	public Signal<String> thoughtOfTheDay() {
		return _indianForThoughtOfTheDay.localSourceToNotify();
	}

	public SetSignal<String> nicknames() {
		return _indianForNicknames.localSetSourceToNotify();
	}
	
	public LifeView contact(String nickname) {
		if (!nicknames().currentElements().contains(nickname)) return null;

		LifeView cached = _contactCache.get(nickname);
		if (cached != null) return cached;
		
		LifeView proxy = new LifeViewProxy(new QueryRouter(nickname, _queryExecuter));
		_contactCache.put(nickname, proxy);
		return proxy;
	}

	public String contactInfo() {
		return _cache.contactInfo();
	}

	public Signal<JpgImage> picture() {
		return _indianForPicture.localSourceToNotify();
	}

	public Object thing(String name) {
		return _cache.thing(name);
	}

	public Map<String, Object> things() {
		return _cache.things();
	}

	public Date lastSightingDate() {
        return _lastSightingDate;
	}

	private static final long serialVersionUID = 1L;
	
	static private class IndianForName extends IndianForObject<String> {
		
		@Override
		protected Signal<String> signalToObserveOn(LifeView life) {
			return life.name();
		}
		
		private static final long serialVersionUID = 1L;
	}

	static private class IndianForThoughtOfTheDay extends IndianForObject<String> {
		
		@Override
		protected Signal<String> signalToObserveOn(LifeView life) {
			return life.thoughtOfTheDay();
		}
		
		private static final long serialVersionUID = 1L;
	}

	static private class IndianForNicknames extends IndianForSet<String> {

		@Override
		protected SetSignal<String> setSignalToObserveOn(LifeView life) {
			return life.nicknames();
		}

		private static final long serialVersionUID = 1L;
	}

	static private class IndianForPicture extends IndianForObject<JpgImage> {
		@Override
		protected Signal<JpgImage> signalToObserveOn(LifeView life) {
			return life.picture();
		}
		
		private static final long serialVersionUID = 1L;
	}

	public void start() {
		synchronized (_activeMonitor) {
			_active = true;
			Cool.startDaemon(updater());
		}
	}

	public void stop() {
		synchronized (_activeMonitor) {
			_active = false;
			Cool.wait(_activeMonitor);
		}
	}



}

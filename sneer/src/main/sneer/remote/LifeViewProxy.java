package sneer.remote;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sneer.life.JpgImage;
import sneer.life.LifeView;
import wheel.experiments.Cool;
import wheelexperiments.reactive.SetSignal;
import wheelexperiments.reactive.Signal;

class LifeViewProxy implements LifeView, Serializable {

	transient private final QueryExecuter _queryExecuter;
	transient private boolean _updaterStarted;
	transient private boolean _scoutsSent = false;
	
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

	private void sendScouts() {
		if (_scoutsSent) return;
		try {
			_queryExecuter.execute(_indianForName);
			_queryExecuter.execute(_indianForThoughtOfTheDay);
			_queryExecuter.execute(_indianForPicture);
			_queryExecuter.execute(_indianForNicknames);
			_scoutsSent = true;
		} catch (IOException ignored) {
//			ignored.printStackTrace();
			//Simply ignore this exception, since the connection will try to reconnect anyway.
		}
	}

	private Runnable updater() {
		return new Runnable() {
			public void run() {
				while (true) {
					update();
//					Cool.sleep(1000 * 60);
					Cool.sleep(1000 * 3);
				}
			}
		};
	}

	private void update() {
		try {
			sendScouts();

			LifeCache newCache = _queryExecuter.execute(new LifeSightingQuery());
			if (newCache == null) return;
			if (newCache.lastSightingDate() == null) return;
			_cache = newCache;
			_lastSightingDate = new Date();
		} catch (IOException ignored) {
			_scoutsSent = false;
			//Simply ignore this exception, since the connection will try to reconnect anyway.
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
		triggerUpdater();  //LifeViewProxies are recreated when the transactions get replayed. Only the ones that are actually used must start their thread.
        return _lastSightingDate;
	}

	private void triggerUpdater() {
		if (_updaterStarted) return;
		_updaterStarted = true;
		Cool.startDaemon(updater());
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

		private static final long serialVersionUID = 1L;

		@Override
		protected SetSignal<String> setSignalToObserveOn(LifeView life) {
			return life.nicknames();
		}
	}

	static private class IndianForPicture extends IndianForObject<JpgImage> {
		@Override
		protected Signal<JpgImage> signalToObserveOn(LifeView life) {
			return life.picture();
		}
		
		private static final long serialVersionUID = 1L;
	}


}

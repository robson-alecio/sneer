package sneer.remote;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import sneer.life.JpgImage;
import sneer.life.Life;
import sneer.life.LifeView;
import wheel.experiments.Cool;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.SourceImpl;

class LifeViewProxy implements LifeView, Serializable {

	transient private final QueryExecuter _queryExecuter;
	transient private boolean _updaterStarted;
	transient private boolean _scoutsSent = false;
	
	private Date _lastSightingDate;
	private Map<String, LifeView> _contactCache = new HashMap<String, LifeView>();
	private LifeCache _cache;
	private Source<String> _thoughtOfTheDay = new SourceImpl<String>();
	private Source<JpgImage> _picture = new SourceImpl<JpgImage>();

	public LifeViewProxy(QueryExecuter queryExecuter) {
		_queryExecuter = queryExecuter;
	}

	private void sendScouts() {
		if (_scoutsSent) return;
		try {
			_queryExecuter.execute(new IndianForThoughtOfTheDay(_thoughtOfTheDay));
			_queryExecuter.execute(new IndianForPicture(_picture));
			_scoutsSent = true;
		} catch (IOException ignored) {
			ignored.printStackTrace();
			//Simply ignore this exception, since the connection will try to reconnect anyway.
		}
	}

	private Runnable updater() {
		return new Runnable() {
			public void run() {
				while (true) {
					update();
//					Cool.sleep(1000 * 60);
//					Cool.sleep(1000 * 4);
					Cool.sleep(100);
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

	public String name() {
		return _cache.name();
	}

	public Signal<String> thoughtOfTheDay() {
		return _thoughtOfTheDay;
	}

	public Set<String> nicknames() {
		return _cache.nicknames();
	}

	public LifeView contact(String nickname) {
		if (!nicknames().contains(nickname)) return null;

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
		return _picture;
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

	
	static private class IndianForThoughtOfTheDay extends Indian<String> {
		private IndianForThoughtOfTheDay(Source<String> thoughtOfTheDay) {
			super(thoughtOfTheDay);
		}
		
		@Override
		protected Signal<String> signalToObserveOn(Life life) {
			return life.thoughtOfTheDay();
		}

		private static final long serialVersionUID = 1L;
	}

	static private class IndianForPicture extends Indian<JpgImage> {
		private IndianForPicture(Source<JpgImage> picture) {
			super(picture);
		}
		
		@Override
		protected Signal<JpgImage> signalToObserveOn(Life life) {
			return life.picture();
		}

		private static final long serialVersionUID = 1L;
	}

}

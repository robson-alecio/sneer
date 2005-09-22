package sneer.remote;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sneer.life.JpgImage;
import sneer.life.LifeView;
import wheel.experiments.Cool;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.SourceImpl;

class LifeViewProxy implements LifeView, Serializable {

	transient private final QueryExecuter _queryExecuter;
	private Date _lastSightingDate;
	private Map<String, LifeView> _contactCache = new HashMap<String, LifeView>();
	private LifeCache _cache;
	private Source<String> _thoughtOfTheDay = new SourceImpl<String>();

	public LifeViewProxy(QueryExecuter queryExecuter) {
		_queryExecuter = queryExecuter;
		Cool.startDaemon(updater());
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
			LifeCache newCache = _queryExecuter.execute(new LifeSightingQuery());
			if (newCache == null) return;
			if (newCache.lastSightingDate() == null) return;
			_cache = newCache;
			_lastSightingDate = new Date();
			_thoughtOfTheDay.supply(newCache.thoughtOfTheDay());
		} catch (IOException ignored) {
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

	public String profile() {
		return _cache.profile();
	}

	public String contactInfo() {
		return _cache.contactInfo();
	}

	public List<String> messagesSentToMe() {
		return _cache.messagesSentToMe();
	}

	public List<String> publicMessages() {
		return _cache.publicMessages();
	}

	public JpgImage picture() {
		return _cache.picture();
	}

	public Date lastSightingDate() {
        return _lastSightingDate;
	}

	private static final long serialVersionUID = 1L;


}

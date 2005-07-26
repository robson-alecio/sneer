package sneer.remote;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.prevayler.foundation.Cool;

import sneer.life.LifeView;

class LifeViewProxy implements LifeView {

	private final QueryExecuter _queryExecuter;
	private Date _lastSightingDate;
	private Map<String, LifeView> _contactCache = new HashMap<String, LifeView>();
	private LifeSighting _cache;

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
			LifeSighting newCache = _queryExecuter.execute(new LifeSightingQuery());
			if (newCache == null) return;
			if (newCache.lastSightingDate() == null) return;
			_cache = newCache;
			_lastSightingDate = new Date();
		} catch (IOException ignored) {
			//Simply ignore this exception, since the connection will try to reconnect anyway.
		}
	}

	public String name() {
		return _cache.name();
	}

	public String thoughtOfTheDay() {
		return _cache.thoughtOfTheDay();
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

	public List<String> messagesSentTo(String contact) {
		return _cache.messagesSentTo(contact);
	}

	public List<String> publicMessages() {
		return _cache.publicMessages();
	}

	public Date lastSightingDate() {
        return _lastSightingDate;
	}

}

package sneer.games.mediawars.mp3sushi;

import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import wheel.io.ui.Action;

public class Application extends SovereignApplicationAbstractImpl{

	private MP3SushiGameApp _delegate;

	@Override
	public String defaultName() {
		return "MP3 Sushi";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

	@Override
	public void start() {
		_delegate = new MP3SushiGameApp(_config);
	}

	@Override
	public List<Action> mainActions() {
		return _delegate.mainActions();
	}
	
}

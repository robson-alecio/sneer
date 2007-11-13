package frozenbubble;


import java.util.List;


import sneer.kernel.api.SovereignApplicationAbstractImpl;
import wheel.io.ui.Action;

public class Application extends SovereignApplicationAbstractImpl{

	private FrozenBubbleApp _delegate;

	@Override
	public String defaultName() {
		return "FrozenBubble";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

	@Override
	public void start() {
		_delegate = new FrozenBubbleApp(_config);
	}
	
	@Override
	public List<Action> mainActions() {
		return _delegate.mainActions();
	}

	@Override
	public Object initialState() {
		return null;
	}
	

}

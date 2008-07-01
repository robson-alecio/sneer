package sneer.skin.laf;

import wheel.io.ui.action.Action;

public interface LafSupport{

	Action getAction();

	void setLastUsedAction(Action last);
}
package frozenbubble;

import static wheel.i18n.Language.translate;

import java.util.Collections;
import java.util.List;

import frozenbubble.game.FrozenBubble;
import frozenbubble.game.FrozenFrame;

import sneer.kernel.api.SovereignApplicationNeeds;
import wheel.io.ui.Action;

public class FrozenBubbleApp {

	private FrozenFrame _frozenBubble;

	public FrozenBubbleApp(@SuppressWarnings("unused")
	SovereignApplicationNeeds config) {
	}

	public List<Action> mainActions() {
		return Collections.singletonList((Action)new Action(){
			public String caption() {
				return translate("Frozen Bubble");
			}
			public void run() {
				openGui();
			}
		});
	}
	
	private void openGui() {
		if ((_frozenBubble==null)||(!_frozenBubble.isVisible()))
			_frozenBubble = new FrozenFrame("Frozen Bubble v1.0.0", new FrozenBubble(), 640, 480);
	}

}

package sneerappsui.topten.impl.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.Inject;
import sneerappsui.topten.ToptenGui;
import spikes.legobricks.name.OwnNameKeeper;

public class ToptenGuiMain {

	public static void main(String[] args) {
		new ToptenGuiMain();
	}
	
	@SuppressWarnings("unused")
	@Inject
	private ToptenGui _topten;

	@Inject
	private OwnNameKeeper _nameKeeper;
	
	private ToptenGuiMain() {
		Container container = ContainerUtils.getContainer();
		container.inject(this);

		_nameKeeper.nameSetter().consume("Kalecser");
		_nameKeeper.nameSetter().consume("Kalecser Kurtz");
		_nameKeeper.nameSetter().consume("Kalecser Kurtz Kobain");
	}

	
}

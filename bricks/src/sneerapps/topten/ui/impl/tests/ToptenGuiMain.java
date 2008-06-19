package sneerapps.topten.ui.impl.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneerapps.topten.ui.ToptenGui;
import spikes.legobricks.name.OwnNameKeeper;

public class ToptenGuiMain {

	public static void main(String[] args) {
		new ToptenGuiMain();
	}
	
	@SuppressWarnings("unused")
	@Inject
	private ToptenGui _topten;

	@SuppressWarnings("unused")
	@Inject
	private Dashboard _dashboard;
	

	@Inject
	private OwnNameKeeper _nameKeeper;
	
	private ToptenGuiMain() {
		Container container = ContainerUtils.getContainer();
		container.inject(this);

		_nameKeeper.nameSetter().consume("1 Kalecser");
		_nameKeeper.nameSetter().consume("2 Kalecser Kurtz");
		_nameKeeper.nameSetter().consume("3 Kalecser Kurtz Kobain");
	}

	
}

package sneerapps.topten.ui.impl.tests;

import javax.swing.JFrame;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.Inject;
import sneer.skin.viewmanager.Snapp;
import sneer.skin.viewmanager.ViewManager;
import sneerapps.topten.ui.ToptenGui;

public class ToptenGuiMain {

	public static void main(String[] args) {
		new ToptenGuiMain();
	}

	@Inject
	private ViewManager _viewManager;
	
	@SuppressWarnings("unused")
	@Inject
	private ToptenGui _topten;

	private ToptenGuiMain() {
		Container container = ContainerUtils.getContainer();
		container.inject(this);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Snapp topTen = _viewManager.getOnlyOneSnappForNow();
		topTen.init(frame);
		
		frame.pack();
		frame.setVisible(true);
	}

	
}

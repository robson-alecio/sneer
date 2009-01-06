package spikes.priscila.go.gui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import sneer.kernel.container.Container;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;

public class GoScorePanel extends JPanel {

	public GoScorePanel(Signal<Integer> countCapturedBlack, Signal<Integer> countCapturedWhite) {
		//Fix Replace getContainer() from ContainerUtils
		//Container container = ContainerUtils.getContainer(); 
		//ReactiveWidgetFactory rfactory = container.produce(ReactiveWidgetFactory.class);

		//TextWidget<?> newLabelBlack = rfactory.newLabel(adaptToString(countCapturedBlack));
		//TextWidget<?> newLabelWhite = rfactory.newLabel(adaptToString(countCapturedWhite));
		
		JSeparator space= new JSeparator(SwingConstants.VERTICAL);
		space.setPreferredSize(new Dimension(8,0));
		add(new JLabel("Points:"));
		add(space);
		add(new JLabel("Black"));
		//add(newLabelWhite.getComponent());
		add(new JLabel("White"));
		//add(newLabelBlack.getComponent());

		setVisible(true);
	}

	private Signal<String> adaptToString(Signal<Integer> input) {
		return new Adapter<Integer, String>(input, new Functor<Integer, String>(){ @Override public String evaluate(Integer value) {
			return "" + value;
		}}).output();
	}
}

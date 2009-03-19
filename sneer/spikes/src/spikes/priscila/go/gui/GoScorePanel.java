package spikes.priscila.go.gui;

import static sneer.commons.environments.Environments.my;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import sneer.commons.lang.Functor;
import sneer.pulp.reactive.Signal;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.reactive.impl.Adapter;

public class GoScorePanel extends JPanel {

	public GoScorePanel(Signal<Integer> countCapturedBlack, Signal<Integer> countCapturedWhite) {
		ReactiveWidgetFactory rfactory = my(ReactiveWidgetFactory.class);

		TextWidget<?> newLabelBlack = rfactory.newLabel(adaptToString(countCapturedBlack));
		TextWidget<?> newLabelWhite = rfactory.newLabel(adaptToString(countCapturedWhite));
		
		JSeparator space= new JSeparator(SwingConstants.VERTICAL);
		space.setPreferredSize(new Dimension(8,0));
		add(new JLabel("Points:"));
		add(space);
		add(new JLabel("Black"));
		add(newLabelWhite.getComponent());
		add(new JLabel("White"));
		add(newLabelBlack.getComponent());

		setVisible(true);
	}

	private Signal<String> adaptToString(Signal<Integer> input) {
		return new Adapter<Integer, String>(input, new Functor<Integer, String>(){ @Override public String evaluate(Integer value) {
			return "" + value;
		}}).output();
	}
}

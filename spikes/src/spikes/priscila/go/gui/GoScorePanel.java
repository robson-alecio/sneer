package spikes.priscila.go.gui;

import static sneer.foundation.environments.Environments.my;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.bricks.skin.widgets.reactive.TextWidget;
import sneer.foundation.lang.Functor;

public class GoScorePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		return my(Signals.class).adapt(input, new Functor<Integer, String>(){ @Override public String evaluate(Integer value) {
			return "" + value;
		}});
	}
}

package wheel.io.ui.widgets;

import java.awt.Font;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ReactiveIntegerField extends ReactiveField<Integer>{
	
	public ReactiveIntegerField(Signal<Integer> source, Omnivore<Integer> setter, Font font) {
		super(source, setter, font);	
	}
	
	@Override
	public Omnivore<Integer> fieldReceiver() { return new Omnivore<Integer>(){
		public void consume(final Integer value) {
				setText(Integer.toString(value));
		}};
	}
	
	@Override
	public Omnivore<Pair<Integer, String>> textChangedReceiver() {
		return new Omnivore<Pair<Integer, String>>(){
			public void consume(Pair<Integer, String> value) {
				if (!value._a.equals(value._b))
					try {
						_setter.consume(new Integer(value._b));
					} catch (IllegalParameter ignored) {
					} catch (NumberFormatException ignored) {
					}
			}
		};
	}

	private static final long serialVersionUID = 1L;
}

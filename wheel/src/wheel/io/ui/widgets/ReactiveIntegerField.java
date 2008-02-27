package wheel.io.ui.widgets;

import java.awt.Font;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ReactiveIntegerField extends ReactiveField<Integer>{
	
	public ReactiveIntegerField(Signal<Integer> source, Consumer<Integer> setter, Signal<Font> font) {
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
				try {
					Integer number = new Integer(value._b);
					if (!value._a.equals(number))
						_setter.consume(number);
				} catch (IllegalParameter ignored) {
				} catch (NumberFormatException ignored) {
				}
			}
		};
	}

	private static final long serialVersionUID = 1L;
}

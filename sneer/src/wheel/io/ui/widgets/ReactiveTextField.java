package wheel.io.ui.widgets;

import java.awt.Font;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ReactiveTextField extends ReactiveField<String>{
	
	public ReactiveTextField(Signal<String> source, Omnivore<String> setter, Font font) {
		super(source, setter, font);	
	}
	
	@Override
	public Omnivore<String> fieldReceiver() { return new Omnivore<String>(){
		public void consume(final String text) {
			setText(text);
		}};
	}
	
	@Override
	public Omnivore<Pair<String, String>> textChangedReceiver() {
		return new Omnivore<Pair<String, String>>(){
			public void consume(Pair<String, String> value) {
				if (!value._a.equals(value._b))
					try {
						_setter.consume(value._b);
					} catch (IllegalParameter ignored) {
					}
			}
		};
	}

	private static final long serialVersionUID = 1L;
}

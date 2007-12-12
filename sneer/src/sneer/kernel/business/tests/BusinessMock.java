package sneer.kernel.business.tests;

import java.awt.Font;

import sneer.kernel.business.Business;
import sneer.kernel.business.contacts.ContactAttributes;
import wheel.graphics.JpgImage;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class BusinessMock {

	private static final long serialVersionUID = 1L;
	
	private ListSource<ContactAttributes> _attributes = new ListSourceImpl<ContactAttributes>();

	private Source<Integer> _port;
	
	private Source<String> _publicKey;

	private Business _output = new Business() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public ListSignal<ContactAttributes> contactAttributes() {
			return _attributes.output();
		}

		@Override
		public Signal<Font> font() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}

		@Override
		public Signal<String> language() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}

		@Override
		public Signal<String> msnAddress() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}

		@Override
		public Signal<String> ownName() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}

		@Override
		public Signal<JpgImage> picture() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}

		@Override
		public Signal<String> profile() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}

		@Override
		public Signal<String> publicKey() {
			return _publicKey.output();
		}

		@Override
		public Signal<Integer> sneerPort() {
			return _port.output();
		}

		@Override
		public Signal<String> thoughtOfTheDay() {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}
		
	};
	
	public BusinessMock(String publicKey, int port) {
		_publicKey = new SourceImpl<String>(publicKey);
		_port = new SourceImpl<Integer>(port);
	}
	
	public void addAttribute(ContactAttributes attr) {
		_attributes.add(attr);
	}
	

	public Business output() {
		return _output ;
	}

}

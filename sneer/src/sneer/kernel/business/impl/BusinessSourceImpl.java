package sneer.kernel.business.impl;

import java.awt.Font;

import javax.swing.JLabel;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactManager;
import sneer.kernel.business.contacts.impl.ContactManagerImpl;
import wheel.graphics.JpgImage;
import wheel.io.network.PortNumberSource;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;


public class BusinessSourceImpl implements BusinessSource  { //Refactor: Create a separate class for BusinessImpl.


	private final class MyOutput implements Business {

		private static final long serialVersionUID = 1L;

		@Override
		public ListSignal<ContactAttributes> contactAttributes() {
			return _contactManager.output();
		}

		@Override
		public Signal<String> ownName() {
			return _ownName.output();
		}
		
		@Override
		public Signal<String> language() {
			return _language.output();
		}
		
		@Override
		public Signal<Font> font() {
			return _font.output();
		}

		@Override
		public Signal<Integer> sneerPort() {
			return _sneerPortNumber.output();
		}

		@Override
		public Signal<String> publicKey() {
			return _publicKey.output();
		}
		
		@Override
		public Signal<String> thoughtOfTheDay() {
			return _thoughtOfTheDay.output();
		}
		
		@Override
		public Signal<JpgImage> picture() {
			return _picture.output();
		}
		
		@Override
		public Signal<String> profile() {
			return _profile.output();
		}

		@Override
		public Signal<String> msnAddress() {
			return _msnAddress.output();
		}

	}

	private Source<String> _ownName = new SourceImpl<String>("");
	private Source<String> _language = new SourceImpl<String>("");
	private Source<Font> _font = new SourceImpl<Font>(new JLabel().getFont());
	private final Source<String> _publicKey = new SourceImpl<String>("");
	
	private final Source<String> _thoughtOfTheDay = new SourceImpl<String>("");
	private final Source<JpgImage> _picture = new SourceImpl<JpgImage>(new JpgImage());
	private final Source<String> _profile = new SourceImpl<String>("");

	private final PortNumberSource _sneerPortNumber = new PortNumberSource(0);

	private final Source<String> _msnAddress = new SourceImpl<String>("");
	
	private final Business _output = new MyOutput();
	private final ContactManagerImpl _contactManager = new ContactManagerImpl();



	@Override
	public Omnivore<String> ownNameSetter() {
		return  _ownName.setter();
	}
	
	@Override
	public Omnivore<String> languageSetter() {
		return  _language.setter();
	}
	
	@Override
	public Omnivore<Font> fontSetter() {
		return  _font.setter();
	}
	
	@Override
	public Consumer<Integer> sneerPortSetter() {
		return _sneerPortNumber.setter();
	}
	
	@Override
	public Omnivore<String> thoughtOfTheDaySetter() {
		return  _thoughtOfTheDay.setter();
	}
	
	@Override
	public Omnivore<JpgImage> pictureSetter() {
		return  _picture.setter();
	}
	
	@Override
	public Omnivore<String> profileSetter() {
		return  _profile.setter();
	}


	@Override
	public Business output() {
		return _output;
	}

	@Override
	public Omnivore<String> msnAddressSetter() {
		return _msnAddress.setter();
	}

	@Override
	@Deprecated
	public Omnivore<sneer.kernel.business.contacts.OnlineEvent> contactOnlineSetter() {
		return new Omnivore<sneer.kernel.business.contacts.OnlineEvent>() { @Override public void consume(sneer.kernel.business.contacts.OnlineEvent ignored) {}};
	}
	
	public Omnivore<String> publicKeySetter() {
		return _publicKey.setter();
	}

	private static final long serialVersionUID = 1L;



	@Override
	public ContactManager contactManager() {
		return _contactManager;
	}





}

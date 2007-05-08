package sneer.kernel.business;

import java.io.Serializable;

public class BusinessFactory {

	public Business createBusiness() {
		return new BusinessImpl();
	}

}

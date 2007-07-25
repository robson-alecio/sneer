package sneer.kernel.business.impl;

import sneer.kernel.business.BusinessSource;

public class BusinessFactory {

	public BusinessSource createBusinessSource() {
		return new BusinessSourceImpl();
	}

}

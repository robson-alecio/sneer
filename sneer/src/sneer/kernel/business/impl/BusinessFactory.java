package sneer.kernel.business.impl;

import java.io.Serializable;

import sneer.kernel.business.BusinessSource;

public class BusinessFactory {

	public BusinessSource createBusinessSource() {
		return new BusinessSourceImpl();
	}

}

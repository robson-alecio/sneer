package sneer.kernel.business;

import java.io.Serializable;

public class BusinessFactory {

	public BusinessSource createBusinessSource() {
		return new BusinessSourceImpl();
	}

}

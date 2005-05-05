package sovereign;

public class NoneOfYourBusiness extends RuntimeException {

	public NoneOfYourBusiness() {}
	
	public NoneOfYourBusiness(NoneOfYourBusiness cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}

package addressbook.business;

import java.io.Serializable;

public class PersonInfo implements Serializable{

	public final String _name;
	public final String _address;
	public final String _phone;
	public final String _email;
	
	public PersonInfo(String name, String address, String phone, String email){
		_name = name;
		_address = address;
		_phone = phone;
		_email = email;
		
	}
	private static final long serialVersionUID = 1L;

}

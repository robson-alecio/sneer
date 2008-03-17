package sneer.contacts;


public interface ConnectionManager {
    
	boolean add(String nickame, String hostAddress, int sneerPort);
	
	ContactId currentContact(String nick);

}
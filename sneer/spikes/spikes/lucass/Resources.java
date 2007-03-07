package spikes.lucass;

import java.util.Vector;

/**
 * This class hold all string used in this plugin. It makes easier
 * to translate or modify captions from the menu.
 * TODO: Load from a file!!!
 * 
 * @author lcsantos
 *
 */
public class Resources {
	
	private static Vector<StringWithId> _stringsDictionary;
	
	public static int YES = 1;
	public static int NO = 2;
	public static int OPTIONS = 3;
	public static int CHANGE_GAME = 4;
	public static int CHESS = 5;
	public static int GO = 6;
	public static int ABOUT = 7;
	
	public static int ADD_PIECE = 8;
	public static int DELETE_PIECE = 9;
	
	//init resources
	static{
		_stringsDictionary= new Vector<StringWithId>();
		_stringsDictionary.add(new StringWithId(YES,"Sim"));
		_stringsDictionary.add(new StringWithId(NO,"Não"));
		_stringsDictionary.add(new StringWithId(OPTIONS,"Opções"));
		_stringsDictionary.add(new StringWithId(CHANGE_GAME,"Mudar jogo"));
		_stringsDictionary.add(new StringWithId(CHESS,"Xadrez"));
		_stringsDictionary.add(new StringWithId(GO,"Go"));
		_stringsDictionary.add(new StringWithId(ABOUT,"Sobre"));
		_stringsDictionary.add(new StringWithId(ADD_PIECE,"Adicionar peça"));
		_stringsDictionary.add(new StringWithId(DELETE_PIECE,"Remover peça"));
	}
	
	public static String getString(int id){
		for(int i=0; i<_stringsDictionary.size();i++){
			if(_stringsDictionary.elementAt(i).id()==id){
				return _stringsDictionary.elementAt(i).toString();
			}
		}
		return null;
	}
}

class StringWithId{
	private String _string;
	private int _id= 0;
	
	public StringWithId(int id, String string) {
		setId(id);
		setString(string);
	}
	
	@Override
	public boolean equals(Object compareTo) {
		if ( compareTo instanceof StringWithId) {
			return false;
		}
		
		return (((StringWithId)compareTo)._id == this._id);
	}
	
	public void setId(int id) {
		this._id = id;
	}

	public int id() {
		return _id;
	}

	public void setString(String string) {
		this._string = string;
	}

	@Override
	public String toString() {
		return _string;
	}
}
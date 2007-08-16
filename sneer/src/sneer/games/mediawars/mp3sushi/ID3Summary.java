package sneer.games.mediawars.mp3sushi;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.lyrics3.AbstractLyrics3;

public class ID3Summary {

	private String _fileName;
	private String _singer;
	private String _title;
	
	public ID3Summary(String fileName, String singer, String title) {
		super();
		this._fileName = fileName;
		this._singer = singer;
		this._title = title;
	}
	public static ID3Summary createFromFileName(String fileName) {
		MP3File mp3file;
		try {
			mp3file = new MP3File(fileName); //Fix: Read-only files cause Exception to be thrown. Fix jid3lib or remove read-only flag while reading info and set it back later.  
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		String title = null;
		String singer = null;
		AbstractID3v2 aid3V2 = mp3file.getID3v2Tag();
		
		if (aid3V2 != null) {
			title = aid3V2.getSongTitle(); //Fix Try and recognize different charsets. Use strangeCharsetName.mp3 in the tests package.
			if (aid3V2 != null) {
				if (title != null) title = title.trim();
				singer = aid3V2.getLeadArtist();
				if (singer != null) singer = singer.trim();
			}	
		}
		
		if ((title == null) || (title.length() == 0) || (singer == null) || (singer.length() == 0)) {
			ID3v1 aid3V1 = mp3file.getID3v1Tag();
			if (aid3V1 != null) {
				if ((title == null) || (title.length() == 0)) {
					title = aid3V1.getSongTitle();
					if (title != null) title = title.trim();
				}
				if ((singer == null) || (singer.length() == 0)) {			
					singer = aid3V1.getLeadArtist();
					if (singer != null) singer = singer.trim();
				}
			}
		}

		if ((title == null) || (title.length() == 0) || (singer == null) || (singer.length() == 0)) {
			AbstractLyrics3 aidLyr = mp3file.getLyrics3Tag();
			if (aidLyr != null) {
				if ((title == null) || (title.length() == 0)) {
					title = aidLyr.getSongTitle();
					if (title != null) title = title.trim();
				}
				if ((singer == null) || (singer.length() == 0)) {			
					singer = aidLyr.getLeadArtist();
					if (singer != null) singer = singer.trim();
				}
			}	
		}

		if ((title == null) || (title.length() == 0) || (singer == null) || (singer.length() == 0)) return null;
		return new ID3Summary(fileName, singer, title);
	}
	public String getSinger() {
		return _singer;
	}
	public String getTitle() {
		return _title;
	}
	
	@Override
	public String toString() {
		return _singer + " - " + _title;
	}
	public String getFileName() {
		return _fileName;
	}
}

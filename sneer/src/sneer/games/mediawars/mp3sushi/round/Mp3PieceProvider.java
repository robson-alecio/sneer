package sneer.games.mediawars.mp3sushi.round;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import sneer.games.mediawars.mp3sushi.BufferedInputStreamTransparent;
import sneer.games.mediawars.mp3sushi.ID3Summary;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

public class Mp3PieceProvider {

	public final static int BEGINING = 0;
	public final static int ENDING = 1;
	public final static int RANDOM = 2;

	private ArrayList<ID3Summary> _id3Summaries;
	private ArrayList<Integer> _id3sToPlay = new ArrayList<Integer>();
	Integer _pieceType;
	int _secondsOfMusic;
	private ID3Summary _actualId3;
	private Random _randomGenerator; 
	
	public Mp3PieceProvider(ArrayList<ID3Summary> id3Summaries, int pieceType, int secondsOfMusic) {
		super();
		_id3Summaries = id3Summaries;
		_randomGenerator = new Random(System.nanoTime());
		_pieceType = pieceType;
		_secondsOfMusic = secondsOfMusic;
		
		for (int i = 0; i < _id3Summaries.size(); i++) _id3sToPlay.add(i);
	}

	public byte[] nextMP3Piece() {
		while (!_id3sToPlay.isEmpty()) {
			int ndxToPlay = _randomGenerator.nextInt(_id3sToPlay.size());
			_actualId3 = _id3Summaries.get(_id3sToPlay.get(ndxToPlay)); 
			_id3sToPlay.remove(ndxToPlay);
			System.out.println("choose:" + _actualId3);
			byte[] bytesFromMP3 = bytesFromMP3(_actualId3);
			if (bytesFromMP3 != null) return bytesFromMP3;
		}
		return null;
	}
	
	private byte[] bytesFromMP3(ID3Summary id3Summary) {
		
		float totalMilisecondsFromMP3;
		byte[] bytesFromMP3 = null;
		float start = 0;
		float ending = 0;
		try {
			totalMilisecondsFromMP3 = totalMilisecondsFromMP3(id3Summary.getFileName());
			float piece = _secondsOfMusic * 1000;
			if (totalMilisecondsFromMP3 < piece) {
				start = 0;
				ending = totalMilisecondsFromMP3;
			} else 
			if (_pieceType.equals(BEGINING)) {
				start = 0;
				ending = piece;
			} else {
				if (_pieceType.equals(ENDING)) {
					ending = totalMilisecondsFromMP3;
					start = ending - piece;
				} else {
					start = _randomGenerator.nextFloat() * (totalMilisecondsFromMP3 - piece);
					ending = start + piece;
				}
			}
			bytesFromMP3 = bytesFromMP3(id3Summary.getFileName(),start,ending, totalMilisecondsFromMP3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytesFromMP3;
	}
	
	
	private float totalMilisecondsFromMP3(String fileName) throws FileNotFoundException, BitstreamException {
		File f = new File(fileName);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bif = new BufferedInputStream(fis);
		Bitstream bt = new Bitstream (bif);
		Header h = null; 
		float miliseconds = 0;
		do { 
				h = bt.readFrame();
				if (h != null) {
					miliseconds = miliseconds + h.ms_per_frame();
					bt.closeFrame();
				}
		} while (h!= null);
		
		return miliseconds;
	}

	private byte[] bytesFromMP3(String fileName, float startMiliseconds, float endMiliseconds, float totalMiliseconds) throws BitstreamException, IOException {
		if (endMiliseconds > totalMiliseconds) endMiliseconds = totalMiliseconds;
		if (startMiliseconds > endMiliseconds) startMiliseconds = endMiliseconds;
		File f = new File(fileName);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStreamTransparent bif = new BufferedInputStreamTransparent(fis);
		Bitstream bt = new Bitstream (bif);
			fis.getChannel();
			Header h = null;
			float miliseconds = 0; 		
		do { 
			h = bt.readFrame();
			if (h != null) {
				miliseconds = miliseconds + h.ms_per_frame();
				bt.closeFrame();
			}
		} while (h!= null && miliseconds < startMiliseconds);
		Long init = fis.getChannel().position() - bif.bufferLenght() + bif.position();
		do { 
			h = bt.readFrame();
			if (h != null) {
				miliseconds = miliseconds + h.ms_per_frame();
				bt.closeFrame();
			}
		} while (h!= null && miliseconds < endMiliseconds);
		Long end = fis.getChannel().position() - bif.bufferLenght() + bif.position();
		fis.getChannel().position(init);
		long size = end.longValue()- init.longValue();
		byte[] bytes = new byte[(int) size];
		fis.read(bytes);
		return bytes;
	}

	public boolean isGoodAnswer(ID3Summary answer) {
		return answer!=null && 
			answer.getTitle().equals(_actualId3.getTitle()) && 
			answer.getSinger().equals(_actualId3.getSinger());  
	}

}

package sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.xiph.speex.SpeexEncoder;

public class SpeexMicrophone extends Thread {
    private AudioFormat _format;
    private TargetDataLine _line;
    private boolean _running = true;
    private AudioCallback _callback;
    private SpeexEncoder _encoder = new SpeexEncoder();
    
    public SpeexMicrophone(AudioCallback callback){
        setDaemon(true);
        _callback=callback;
    }
    
    public void init() throws LineUnavailableException{
        _format=AudioUtil.getFormat();
        _line = AudioUtil.getTargetDataLine();
        _line.open(_format);
        _line.start();
        _encoder.init(0,8,(int)_format.getFrameRate(),_format.getChannels());
        start();
        while(!isAlive()){try{sleep(200);}catch(InterruptedException ie){}};
    }
    
    @Override
	public void run(){
        byte[] buffer = new byte[2 * _encoder.getFrameSize() * _encoder.getChannels()];
        while(_running){
            int readed = _line.read(buffer,0,buffer.length);
            if (_encoder.processData(buffer,0,readed)){
                int processed = _encoder.getProcessedData(buffer,0);
                _callback.audio(buffer,0,processed);
            }
        }
    }
    
    public interface AudioCallback{
        public void audio(byte[] buffer,int offset,int length);
    }
    
    public void close(){
        _line.drain();
        _line.close();
        _running=false;
    }
    
}

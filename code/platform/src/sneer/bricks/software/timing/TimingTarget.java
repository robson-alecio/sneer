package sneer.bricks.software.timing;

public interface TimingTarget {
    
    void timingEvent(float fraction);
    void begin();
    void end();
    void repeat();
    
}

package sneer.bricks.software.timing;

public interface Animator {

    static enum Direction {
    	FORWARD,
    	BACKWARD,
    };
    
    Direction startDirection();
    void setStartDirection(Direction startDirection) ;
    
    void addTarget(TimingTarget target) ;
    void removeTarget(TimingTarget target);
    
    void start();
    void stop();
    void cancel();
    void pause();
    void resume();
    
    long totalElapsedTime();
    long totalElapsedTime(long currentTime);

    long cycleElapsedTime();
    long cycleElapsedTime(long currentTime);

    public float timingFraction();
}

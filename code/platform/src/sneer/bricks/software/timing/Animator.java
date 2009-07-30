package sneer.bricks.software.timing;

public interface Animator {

    void stop();
    void pause();
    void resume();
    
    void playForward();
    void playBackward();    

    void addTarget(TimingTarget target) ;
    void removeTarget(TimingTarget target);
    
}

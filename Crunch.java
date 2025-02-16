
/**
 * Simply a sound of crunching on something...
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.0
 */
public class Crunch extends Sounds
{
    // The name of the file containing the crunching sound.
    private static final String CRUNCH_SOUND_FILE = "crunch.mp3";
    // Used to count the number of times the sound was asked to play.
    private static int COUNT = 0;
    // The number of times the method should be called to execute.
    private static final int PLAY_STEP = 15;

    /**
     * Constructor for objects of class Crunch
     */
    public Crunch()
    {
        
    }
    
    /**
     * Plays a sound that resembles crunching something every 
     * set number of times it's called. Counter gets incremented 
     * every time this method is called.
     */
    public void playSound() {
        COUNT++;
        if(COUNT % PLAY_STEP == 0) {
            playSoundEffects(CRUNCH_SOUND_FILE);
        }
    }
}

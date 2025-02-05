
/**
 * Indicates the day phases of any day in the game.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.0
 */
public enum DayPhases
{
    DAY("Day"), NIGHT("Night");
    // Name of the day phase in string.
    private String dayPhase;
    
    DayPhases (String dayPhaseName) {
        dayPhase = dayPhaseName;
    }
    
    @Override
    /**
     * Changes the day phase to it's string counterpart.
     */
    public String toString() {
        return dayPhase;
    }
}

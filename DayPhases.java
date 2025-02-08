
/**
 * Indicates the day phases of any day in the game.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.1
 */
public enum DayPhases
{
    DAY("Day"), NIGHT("Night");
    // Name of the day phase in string.
    private final String dayPhase;
    
    DayPhases (String dayPhaseName) {
        dayPhase = dayPhaseName;
    }
    
    @Override
    /**
     * Changes the day phase to its string counterpart.
     */
    public String toString() {
        return dayPhase;
    }
}

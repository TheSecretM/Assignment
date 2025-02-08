
/**
 * Time is a class that counts the number of days that has passed and 
 * stores the phase of the current day.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.0
 */
public class Time
{
    private DayPhases time;
    private int dayNumber;
    /**
     * Constructor for objects of class Time
     */
    public Time()
    {
        time = DayPhases.DAY;
        dayNumber = 1;
    }

    /**
     * Changes the time of the day to Night if it was day, or 
     * if it was Night, it changes the time of the day to Day 
     * and increments the number of days that passed.
     */
    public void incrementDay() {
        if(time == DayPhases.DAY) {
            time = DayPhases.NIGHT;
        }
        else if(time == DayPhases.NIGHT) {
            time = DayPhases.DAY;
            dayNumber++;
        }
    }

    /**
     * Arranges the day number and the day time in a single line for 
     * display and returns it as a string.
     * @return A string which displays the day number and the day time 
     * in a presentable manner.
     */
    public String getDetails() {
        return dayNumber + ", Day phase: " + time.toString();
    }

    /**
     * Resets the day number back to 1 and the day time back to Day.
     */
    public void reset() {
        dayNumber = 1;
        time = DayPhases.DAY;
    }

    /**
     * @return True if the time is Day, or False if the 
     * time is anything else.
     */
    public boolean isDay() {
        return time == DayPhases.DAY;
    }
}

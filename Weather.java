import java.util.Random;

/**
 * Weather class indicates the field weather conditions. Which 
 * affects the growth rate of Plants
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.1
 */
public class Weather
{
    // The current weather condition.
    private WeatherConditions currentWeather;
    // Random number generator.
    private Random rand = new Random();
    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        this.currentWeather = WeatherConditions.SUN;
    }
    
    /**
     * Changes the weather condition by a random choice, with 
     * the first weather (sunny) having a probability of 70%, while 
     * the other 3 weather conditions each have a probability of 
     * 10% of occurring.
     */
    public void change() {
        int num = rand.nextInt(0, 10);
        if(num == 0) {
            currentWeather = WeatherConditions.RAIN;
        }
        else if(num == 1) {
            currentWeather = WeatherConditions.SNOW;
        }
        else if(num == 2) {
            currentWeather = WeatherConditions.FOG;
        }
        else {
            currentWeather = WeatherConditions.SUN;
        }
    }
    
    /**
     * Returns the current weather condition as a string.
     * @return The weather condition.
     */
    public String getWeather() {
        return currentWeather.toString();
    }
    
    /**
     * Checks if the weather is snowy and returns true if so, 
     * otherwise returns false.
     * @return True if snowy weather, False if not snowy.
     */
    public boolean isSnowy() {
        return currentWeather == WeatherConditions.SNOW;
    }
    
    /**
     * Checks if the weather is snowy and returns true if so, 
     * otherwise returns false.
     * @return True if snowy weather, False if not snowy.
     */
    public boolean isRainy() {
        return currentWeather == WeatherConditions.RAIN;
    }
    
    /**
     * Checks if the weather is snowy and returns true if so, 
     * otherwise returns false.
     * @return True if snowy weather, False if not snowy.
     */
    public boolean isFoggy() {
        return currentWeather == WeatherConditions.FOG;
    }
}

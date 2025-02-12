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
    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        currentWeather = WeatherConditions.SUN;
    }
    
    /**
     * Changes the weather condition by a random choice, with 
     * the first weather (sunny) having a very high probability, while
     * the other weather conditions each have an equal and low
     * probability.
     */
    public void change() {
        Random rand = new Random();
        WeatherConditions[] weathers = WeatherConditions.values();
        int num = rand.nextInt(0, 10 + WeatherConditions.values().length);
        if(num < weathers.length) {
            currentWeather = weathers[num];
        }
        else {
            currentWeather = weathers[0];
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

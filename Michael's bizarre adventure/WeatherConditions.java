
/**
 * Indicates the Weather types allowed in the game.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.0
 */
public enum WeatherConditions
{
    SUN("sunny"), FOG("Foggy"), SNOW("Snowwy"), RAIN("Rainy");
    // Name of the weather condition in string.
    private String weatherCondition;
    
    WeatherConditions(String conditionName) {
        weatherCondition = conditionName;
    }
    
    @Override
    /**
     * Changes the weather condition to it's string counterpart.
     */
    public String toString() {
        return weatherCondition;
    }
}

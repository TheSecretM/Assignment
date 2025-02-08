
/**
 * Indicates the Weather types allowed in the game.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.1
 */
public enum WeatherConditions
{
    SUN("Sunny"), FOG("Foggy"), SNOW("Snowy"), RAIN("Rainy");
    // Name of the weather condition in string.
    private final String weatherCondition;
    
    WeatherConditions(String conditionName) {
        weatherCondition = conditionName;
    }
    
    @Override
    /**
     * Changes the weather condition to its string counterpart.
     * @return The weather condition's string name.
     */
    public String toString() {
        return weatherCondition;
    }
}

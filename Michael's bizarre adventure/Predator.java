import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a predator, based on the fox's class.
 * Predator age, move, eat prey, and die. They can also be infected by diseases transferred 
 * to them through adjacent preys.
 * 
 * @author David J. Barnes and Michael Kölling and Majed Alali and Vinushan Nagentherarajah
 * @version 9.0
 */
public abstract class Predator extends Characters
{
    // Characteristics shared by all predators (class variables).
    // The age at which a predator can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a predator can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a predator breeding.
    private static final double BREEDING_PROBABILITY = 0.06;
    // The maximum number of births.
    private static final int MAX_CHILD_SIZE = 2;
    // The food value of a single prey. In effect, this is the
    // number of steps a predator can go before it has to eat again.
    protected static final int PREY_FOOD_VALUE = 15;
    // Determines the value above which predator won't eat 
    // until it goes below that again.
    private static final int HUNGER_VALUE = 150;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).

    // The predator's age.
    private int age;
    // The predator's food level, which is increased by eating prey.
    protected int foodLevel;
    // The predator's wellness.
    private boolean sick;
    // Movement attempts counter (sick predators can't move well enough)
    private int move;

    /**
     * Create a predator. A predator can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param location The location within the field.
     */
    public Predator(boolean randomAge, Location location)
    {
        super(location);
        age = 0;
        sick = false;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        foodLevel = rand.nextInt(PREY_FOOD_VALUE);
    }
    
    /**
     * This is what the predator does during the day: it hunts for
     * preys. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public abstract void act(Field currentField, Field nextFieldState);
    
    /**
     * This is what the predator does at night.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public abstract void nightAct(Field currentField, Field nextFieldState);

    /**
     * Look for poops adjacent to the current location.
     * Only the first live poop is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findPoop(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Characters character = field.getCharacterAt(loc);
            if(character instanceof Poop poop) {
                if(poop.isAlive()) {
                    poop.setDead();
                    setSick();
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    @Override
    public String toString() {
        return "Predator{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }

    /**
     * Increase the age. This could result in the predator's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this predator more hungry. This could result in the predator's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for preys adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected abstract Location findFood(Field field);
    
    /**
     * Check whether this predator is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected abstract void giveBirth(Field nextFieldState, List<Location> freeLocations);
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_CHILD_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A predator can breed if it has reached the breeding age.
     * @return True if the predator can breed, or returns False if it cant.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * @return The age of the predator in int.
     */
    protected int getAge() {
        return age;
    }
}

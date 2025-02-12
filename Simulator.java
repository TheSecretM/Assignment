import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * predators and preys and plants.
 * 
 * @author David J. Barnes and Michael Kölling and Majed Alali and Vinushan Nagentherarajah
 * @version 10.1
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double PREDATOR_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given position.
    private static final double PREY_CREATION_PROBABILITY = 0.12;    
    // The probability that a fox will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.20;
    // The maximum number of Josh-type Predator allowed in the field.
    private static final int MAX_JOSH_NUMBER = 5;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;
    // The weather conditions of the field.
    private final Weather weather;
    // Day number and phase of day of the current simulation.
    private final Time time;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        weather = new Weather();
        time = new Time();

        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long 
     * period (700 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }

    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(100);         // adjust this to change execution speed
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each character.
     */
    public void simulateOneStep()
    {
        step++;
        // Might change the weather condition every 100 steps.
        if(step%100 == 0) {
            time.incrementDay();
            weather.change();
        }
        // Plants grow at different intervals based on the weather
        if(weather.isRainy()) {
            if(step%15 == 0) {
                growPlants();
            }
        }
        else if(weather.isSnowy()) {
            if(step%50 == 0) {
                growPlants();
            }
        }
        else if(weather.isFoggy()) {
            if(step%20 == 0) {
                growPlants();
            }
        }
        else {
            if(step%30 == 0) {
                growPlants();
            }
        }
        // Use a separate Field to store the starting state of
        // the next step.
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Characters> characters = field.getCharacters();
        if(time.isDay()) {
            for (Characters character : characters) {
                character.act(field, nextFieldState);
            }
        }
        else {
            for (Characters character : characters) {
                character.nightAct(field, nextFieldState);
            }
        }
        

        // Replace the old state with the new one.
        field = nextFieldState;

        reportStats();
        view.showStatus(step, field, weather.getWeather(), time);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        time.reset();
        populate();
        view.showStatus(step, field, weather.getWeather(), time);
    }

    /**
     * Randomly populate the field with predators, preys and plants.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        // Counts the current number of Josh-type Predator created.
        int CURRENT_JOSH = 0;
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= PREDATOR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    // Random creation of one of the Predators.
                    if(CURRENT_JOSH < MAX_JOSH_NUMBER) {
                        // Josh-type Predator has an upper limit of creations.
                        Josh predator = new Josh(true, location);
                        field.placeCharacter(predator, location);
                        CURRENT_JOSH++;
                    }
                    else {
                        Michael predator = new Michael(true, location);
                        field.placeCharacter(predator, location);
                    }
                }
                else if(rand.nextDouble() <= PREY_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    // Random creation of one of the 3 types of Preys.
                    int num = rand.nextInt(0, 5);
                    if(num <= 1) {
                        Luis prey = new Luis(true, location);
                        field.placeCharacter(prey, location);
                    }
                    else if(num == 2) {
                        Pierre prey = new Pierre(true, location);
                        field.placeCharacter(prey, location);
                    }
                    else {
                        Leonardo prey = new Leonardo(true, location);
                        field.placeCharacter(prey, location);
                    }

                }
                else if(rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(location);
                    field.placeCharacter(plant, location);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        //System.out.print("Step: " + step + " ");
        field.fieldStats();
    }

    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }

    /**
     * Grows Plants in empty Locations without resetting the field.
     */
    private void growPlants() {
        Random rand = Randomizer.getRandom();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                Characters character = field.getCharacterAt(location);
                if(character == null) {
                    // Only has a possibility of growing 
                    // plants were no character is present.
                    if(rand.nextBoolean()) {
                        Plant plant = new Plant(location);
                        field.placeCharacter(plant, location);
                    }
                }
            }
        }
    }
}

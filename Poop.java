
/**
 * Poop is a class of objects that appear in the field, they 
 * only age and do nothing else, except make whoever dares to 
 * eat them sick...
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.0
 */
public class Poop extends Characters
{
    private int age;
    /**
     * Constructor for objects of class Poop that initialises age 
     * to 0.
     */
    public Poop(Location location)
    {
        super(location);
        age = 0;
    }

    /**
     * Acts just like how a poop should act. By doing nothing and simply aging, 
     * but it magically disappears if it aged for a set number of times.
     */
    public void act(Field currentField, Field nextFieldState) {
        if(isAlive()) {
            age++;
            // Makes sure the object remains in place while not getting 
            // removed from the field.
            nextFieldState.placeCharacter(this, this.location);
            if(age >= 7) {
                setDead();
            }
        }
    }
    
    /**
     * No change to it's actions at night, so it simply 
     * does the usual act.
     */
    public void nightAct(Field currentField, Field nextFieldState) {
        act(currentField, nextFieldState);
    }

    /**
     * @return The age of the Poop as an int.
     */
    private int getAge() {
        return age;
    }
}

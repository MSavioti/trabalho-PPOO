import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Fox extends Animal
{
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with random age.
     */
    public Fox(int _maxAge, int _foodValue, int _breedingStartAge, double _breedProb, int _maxLinterSize)
    {
        super(_maxAge, _foodValue, _breedingStartAge, _breedProb, _maxLinterSize);
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     */
    public void hunt(Field currentField, Field updatedField, List newFoxes)
    {
        getOlder();
        if(isAlive()) {
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, getLocation());
            if(newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            } else { //found food - can breed
                // New foxes are born into adjacent locations.
                int births = getBirthsNumber();

                if (births > 0)
//                System.out.println(births + " novas raposas nasceram!");
                for(int b = 0; b < births; b++) {
                    Fox newFox = new Fox(Simulator.FOX_MAX_AGE, Simulator.RABBIT_FOOD_VALUE, Simulator.FOX_BREEDING_AGE,
                            Simulator.FOX_BREEDING_PROBABILITY, Simulator.FOX_MAX_LITTER_SIZE);
                    newFoxes.add(newFox);
                    Location loc = updatedField.randomAdjacentLocation(getLocation());
                    newFox.setLocation(loc);
                    updatedField.place(newFox, loc);
                }
            }
            if(newLocation != null) {
//                System.out.println("When fox is moving");
//                updatedField.percorrer();
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                // can neither move nor stay - overcrowding - all locations taken
                kill();
            }
            getHungrier();
        }
    }
    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     * @param field The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field, Location location)
    {
        Iterator adjacentLocations = field.adjacentLocations(location);
        boolean hasEatenYet = false;

        while(adjacentLocations.hasNext() && !hasEatenYet) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                hasEatenYet = true;
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) {
                  System.out.println("A raposa da posição " + getLocation() + " comeu o coelho da posição " + rabbit.getLocation());
                    rabbit.kill();
                    feed();
                    return where;
                }
            }
        }
        return null;
    }
}

import java.util.Iterator;
import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends Animal
{

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     */
    public Rabbit(int _maxAge, int _foodValue, int _breedingStartAge, double _breedProb, int _maxLinterSize)
    {
        super(_maxAge, _foodValue, _breedingStartAge, _breedProb, _maxLinterSize);
    }

    public void hunt(Field currentField, Field updatedField, List newRabbits)
    {
        getOlder();
        if(isAlive()) {
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, getLocation());
            if(newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            else { //found food
                int births = getBirthsNumber(); //only breed if have eaten
                if (births > 0)
                System.out.println(births + " novos coelhos nasceram!");

                for(int b = 0; b < births; b++) {
                    Rabbit newRabbit = new Rabbit(Simulator.RABBIT_MAX_AGE, Simulator.GRASS_FOOD_VALUE, Simulator.RABBIT_BREEDING_AGE,
                            Simulator.RABBIT_BREEDING_PROBABILITY, Simulator.RABBIT_MAX_LITTER_SIZE);
                    newRabbits.add(newRabbit);
                    Location loc = updatedField.freeAdjacentLocation(getLocation());

                    if (loc != null) {

                        newRabbit.setLocation(loc);
                        updatedField.place(newRabbit, loc);
                    }
//                    System.out.println("Coelho recém-nascido posicionado em " + newRabbit.getLocation());
                }
            }

            if(newLocation != null) {
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

    protected Location findFood(Field field, Location location)
    {
        Iterator adjacentLocations = field.adjacentLocations(location);
        boolean hasEatenYet = false;

        while(adjacentLocations != null && adjacentLocations.hasNext() && !hasEatenYet) {
            Location where = (Location) adjacentLocations.next();
            Object object = field.getObjectAt(where);
            if(object instanceof Grass) {
                hasEatenYet = true;
                Grass grass = (Grass) object;
                if(grass.isAlive()) {
                    //System.out.println("O coelho da posição " + getLocation() + " comeu a grama da posição " + grass.getLocation());
                    grass.kill();
                    feed();
                    return where;
                }
            }
        }
        return null;
    }
}

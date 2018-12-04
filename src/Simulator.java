import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a field containing
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator extends JFrame implements KeyListener
{
    // The private static final variables represent 
    // configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 15;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 15;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.04;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.15;
    // The probability that a rabbit will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.25;

    // The list of elements in the field
    private List elements;
    // The list of elements just born
    private List newElements;
    // The current state of the field.
    private Field field;
    // A second field, used to build the next stage of the simulation.
    private Field updatedField;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    private Random rand;

    @Override
    public void keyPressed(KeyEvent e)
    {
        simulateOneStep();
    }
    @Override
    public void keyReleased(KeyEvent e){}
    @Override
    public void keyTyped(KeyEvent e) {}
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
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        elements = new ArrayList();
        newElements = new ArrayList();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.addKeyListener(this);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Grass.class, Color.green);

        rand = new Random();
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * e.g. 500 steps.
     */
    public void runLongSimulation()
    {
        simulate(500);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        newElements.clear();
        for(Iterator iter = elements.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof Grass)
            {
                Grass grass = (Grass) object;
                if (grass.exists()) {
                    grass.refresh(field, updatedField, newElements);
                }
                else {
                    Grass newGrass = new Grass();
                    Location newGrassLocation = updatedField.freeAdjacentLocation(grass.getLocation());

                    if (newGrassLocation != null) {
                        newGrass.setLocation(newGrassLocation);
                        newElements.add(newGrass);
                        updatedField.place(newGrass, newGrassLocation);
                    }
                    iter.remove();
                }
            }
        }
        view.showStatus(step, updatedField);

        // let all elements act
        for(Iterator iter = elements.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if(object instanceof Rabbit) {
                Rabbit rabbit = (Rabbit)object;
                if(rabbit.isAlive()) {
                    rabbit.hunt(field, updatedField, newElements);
                }
                else {
                    iter.remove();   // remove dead rabbits from collection
                }
            }
            else if(object instanceof Fox) {
                Fox fox = (Fox)object;
                if(fox.isAlive()) {
                    fox.hunt(field, updatedField, newElements);
                }
                else {
                    iter.remove();   // remove dead foxes from collection
                }
            }
        }
//        System.out.println("Current:");
//        field.percorrer();
//        System.out.println("\nUpdated");
//        updatedField.percorrer();
        // add new born elements to the list of elements
        elements.addAll(newElements);
        


        //grass grows at every step, but only after everyone took its action
//        for(int row = 0; row < field.getDepth(); row++)
//        {
//            for(int col = 0; col < field.getWidth(); col++) {
////                System.out.print(updatedField.getObjectAt(row,col) + " ");
//                if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
//                    Location currentLocation = new Location(row, col);
//                    GameObject gameObj = (GameObject) updatedField.getObjectAt(row, col);
//                    //updatedField.percorrer();
//                    boolean positionTaken = false;
//
//                    if(gameObj != null)
//                        positionTaken = true;
//
//                    if (!positionTaken) {
//                        Grass grass = new Grass();
//                        grass.setLocation(currentLocation.getRow(), currentLocation.getCol());
//                        elements.add(grass);
//                        updatedField.place(grass, currentLocation);
//                    }
//                }
//            }
//            System.out.println("");
//        }
        // Swap the field and updatedField at the end of the step.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // display the new field on screen
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        elements.clear();
        field.clear();
        updatedField.clear();
        populate(field);
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Populate the field with foxes and rabbits.
     */
    private void populate(Field field)
    {
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(false);
                    elements.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(false);
                    elements.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                }
                else if (rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Grass grass = new Grass();
                    elements.add(grass);
                    grass.setLocation(row, col);
                    field.place(grass, row, col);
                }
                // else leave the location empty.
            }
        }
        Collections.shuffle(elements);
    }
}

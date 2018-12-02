import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.*;

import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

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
    private static final int DEFAULT_WIDTH = 50;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 50;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.07;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    
    private static final double FOOD_CREATION_PROBABILITY = 0.1;
    // The value in seconds that the grid will takes to refresh
    private static final int SECONDS_TO_REFRESH = 10;
    // The Timer object
    private Timer timer;
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

    private Random rand = new Random();
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        //this(DEFAULT_DEPTH, DEFAULT_WIDTH);
        this(20,25);
    }
    
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
        view.setColor(Food.class, Color.green);
        
        // Setup a valid starting point.
        reset();
        // After showing the first step, the grid will refresh at every SECONDS_TO_REFRESH seconds
        //runStepByStep();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * e.g. 500 steps.
     */
    public void runStepByStep()
    {
        class RemindTask extends TimerTask
        {
            public void run()
            {
                simulateOneStep();
            }
        }
        timer = new Timer();
        timer.schedule(new RemindTask(), SECONDS_TO_REFRESH * 1000, SECONDS_TO_REFRESH * 1000);
    }
    
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
        List busyLocations = new ArrayList<Field>();
        // let all elements act
        for(Iterator iter = elements.iterator(); iter.hasNext(); ) {
            Object element = iter.next();

            if(element instanceof Rabbit) {
                Rabbit rabbit = (Rabbit)element;
                if(rabbit.isAlive()) {
                    rabbit.hunt(field, updatedField, newElements);
                }
                else {
                    iter.remove();   // remove dead rabbits from collection
                }
            } else if(element instanceof Fox) {
                Fox fox = (Fox)element;
                if(fox.isAlive()) {
                    fox.hunt(field, updatedField, newElements);
                }
                else {
                    iter.remove();   // remove dead foxes from collection
                }
            } else if(element instanceof Food)
            {
                Food food = (Food)element;
                if(food.exists())
                {
                    food.refresh(field, updatedField);
                }else
                {
                    iter.remove();
                }
            }
        }

        // add new born elements to the list of elements
        elements.addAll(newElements);

        //try to instantiate grass on every location
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {

                if(rand.nextDouble() <= FOOD_CREATION_PROBABILITY) {
                    Location currentLocation = new Location(row, col);
                    boolean positionTaken = false;

                    // verifies if current location is already taken by any element
                    for(Object object : elements) {
                        if (object instanceof GameObject){
                            GameObject gameObject = (GameObject) object;
                            //System.out.println("GameObject X: " + gameObject.getLocation().getRow());
                            //System.out.println("GameObject Y: " + gameObject.getLocation().getRow());
                            if (gameObject.getLocation().equals(currentLocation)) {
                                positionTaken = true;
                            }
                        }
                    }

                    if (!positionTaken) {
                        System.out.println("creating grass");
                        Food food = new Food();
                        elements.add(food);
                        food.setLocation(currentLocation.getRow(), currentLocation.getCol());
                        field.place(food, currentLocation.getRow(), currentLocation.getCol());
                    }
                }
            }
        }
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
                } else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(false);
                    elements.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                } else if(rand.nextDouble() <= FOOD_CREATION_PROBABILITY) {
                    Food food = new Food();
                    elements.add(food);
                    food.setLocation(row, col);
                    //System.out.println("Imprimindo: " + food.getLocation());
                    field.place(food, row, col);
                }
               
                // else leave the location empty.
            }
        }
        Collections.shuffle(elements);
    }
}

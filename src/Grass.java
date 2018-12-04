
import java.util.List;
import java.util.Random;

public class Grass extends GameObject {
    private boolean exists;
    private static final double GRASS_CREATION_PROBABILITY = 0.175;
    private Random rand;
    public Grass()
    {
        exists = true;
        rand = new Random();
    }

    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public void setEaten()
    {
        exists = false;
    }
    public void refresh(Field currentField, Field updatedField, List newGrasses)
    {
        if (exists)
        {
            int births = 0;
            updatedField.place(this, location);
//            if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY)
//            {
//                births = rand.nextInt(4) + 1;
//            }
//            for(int b = 0; b < births; b++)
//            {
//                Grass newGrass = new Grass();
//                newGrasses.add(newGrass);
//                boolean encontradaPosicao = false;
//                int i = 0;
//                Location loc = null;
//                while(!encontradaPosicao && i < 6)
//                {
//                    System.out.println("location:" + location);
//                    loc = updatedField.randomAdjacentLocation(location);
//                    System.out.println("loc: " + loc);
//                    System.out.println("pegando current mimo:" + currentField.getObjectAt(loc));
//                    if(currentField.getObjectAt(loc) == null)
//                    {
//                        encontradaPosicao = true;
//                    }
//                    i++;
//                }
//                if(encontradaPosicao)
//                {
//                    newGrass.setLocation(loc);
//                    updatedField.place(newGrass, loc);
//                }
////                    System.out.println("Coelho recém-nascido posicionado em " + newRabbit.getLocation());
//            }
            
//            if(updatedField.getObjectAt(location) != null && !(updatedField.getObjectAt(location) instanceof Grass))
//            {
//                setEaten();
//            }else
//            {
//                updatedField.place(this, location);
//            }
        }
    }

    public boolean exists()
    {
        return exists;
    }
}

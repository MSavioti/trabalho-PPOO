
import java.util.List;

public class Food
{
    private Location location;
    private boolean exists;
    public Food()
    {
        exists = true;
    }
    
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }
    public void setEaten()
    {
        exists = false;
    }
    public void refresh(Field currentField, Field updatedField)
    {
        if (exists)
            updatedField.place(this, location);
    }
    
    public boolean exists()
    {
        return exists;
    }
}

public class Grass extends GameObject {
    private boolean exists;

    public Grass()
    {
        exists = true;
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

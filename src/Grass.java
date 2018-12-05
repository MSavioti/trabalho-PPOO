
public class Grass extends GameObject {
    public Grass()
    {
        super();
    }

    public void refresh(Field currentField, Field updatedField)
    {
        if (isAlive())
        {
            updatedField.place(this, getLocation());
        }
    }
}

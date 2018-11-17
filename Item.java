
/**
 * Class Item - an item in a room
 *
 * @author Fahim Mohammed
 * @version 2018.11.14
 */
public class Item
{
    private int weight;
    private String description;
    private String name;

    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String description, int weight)
    {
       this.name = name;
       this.description = description;
       this.weight = weight;
    }

    /**
     * @return The name of the item
     * (the one that was defined in the constructor).
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the weight of the current item
     *
     * @return    weight of the item
     */
    public int getWeight()
    {
       return weight;
    }
    
    /**
     * @return The short description of the item
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the item in the form:
     *     This is a potato.
     * @return A long description of this item
     */
    public String getLongDescription()
    {
        return "This is" + description;
    }

}

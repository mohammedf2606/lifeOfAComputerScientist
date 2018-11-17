import java.util.*;
/**
 * Class Player - creates a player in the game
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    private HashMap<String, Item> inventory;
    private Integer maxWeight;
    private Integer currentWeight = 0;
    public Room currentRoom;

    /**
     * Constructor for objects of class Player
     */
    public Player(Integer maxWeight)
    {
        this.maxWeight = maxWeight;
        inventory = new HashMap<>();
    }

    /**
     * Adds an item to player's inventory.
     * If max weight is exceeded, error message shown.
     */
    public void takeItem(Command command)
    {
        String itemName = command.getSecondWord();
        Item item = currentRoom.getItems().get(itemName);
        Integer weight = item.getWeight();
        if (currentWeight + weight <= maxWeight) {
            inventory.put(itemName, item);
            currentWeight += weight;
            System.out.println("Grabbed " + itemName);
        }
        else {
            System.out.println("No more space in bag. You gotta drop something...");
        }
    }
    
    /**
     * Removes an item from player's inventory.
     */
    public void dropItem(Command command)
    {
        String itemName = command.getSecondWord();
        inventory.remove(itemName);
        System.out.println("Dropped " + itemName);
    }
    
    /**
     * Returns items currently in player's inventory
     */
    public String getInventory()
    {
        String returnString = "Inventory:";
        Set<String> keys = inventory.keySet();
        for(String item : keys) {
            returnString += " " + item;
        }
        return returnString + "\nTotal weight: " + currentWeight.toString();
    }
}

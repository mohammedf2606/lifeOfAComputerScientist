import java.util.*;

/**
 * Class Player - creates a player in the game
 *
 * @author Fahim Mohammed
 * @version 2018.11.17
 */
public class Player {
    HashMap<String, Item> inventory;
    Integer maxWeight;
    Room currentRoom;
    Integer currentWeight = 0;

    /**
     * Constructor for objects of class Player
     * @param maxWeight The maximum weight a player can carry
     */
    public Player(Integer maxWeight) {
        this.maxWeight = maxWeight;
        inventory = new HashMap<>();
    }

    /**
     * Adds an item to player's inventory.
     * If max weight is exceeded, error message shown.
     */
    void takeItem(Command command)
    {
        String itemName = command.getSecondWord();
        Set<String> itemSet = currentRoom.getItems().keySet();
        if (itemSet.isEmpty()) {
            System.out.println("There's nothing to take from this room.");
        } 
        else if (!itemSet.contains(itemName)) {
            System.out.println("Take what?");
        } 
        else {
            Item item = currentRoom.getItems().get(itemName);
            Integer weight = item.getWeight();
            if (currentWeight + weight <= maxWeight) {
                if (weight == 0) {
                    System.out.println("You can't pick that up!");
                }
                else {
                    if (itemName.equals("backpack")) {
                        System.out.println("Looks like someone left their bag after the lecture. It's bigger than yours.");
                        System.out.println("Maybe you can make USE of it...\n");
                    }
                    inventory.put(itemName, item);
                    currentRoom.items.remove(itemName);
                    currentWeight += weight;
                    System.out.println("Grabbed " + itemName + "\nWeight: " + item.getWeight() + " g");
                }
            }
            else {
                System.out.println("No more space in bag. You gotta drop something...");
            }
        }
    }

    /**
     * Removes an item from player's inventory.
     */
    void dropItem(Command command, HashMap<String,Item> allItems) {
        String itemName = command.getSecondWord();
        Set<String> playerItemSet = inventory.keySet();
        if (!playerItemSet.contains(itemName)) {
          System.out.println("Drop what?");
        } 
        else {
          inventory.remove(itemName);
          currentRoom.items.put(itemName, allItems.get(itemName));
          System.out.println("Dropped " + itemName);
        }
    }
    
    /**
     * Returns items currently in player's inventory
     */
    String getInventory()
    {
        StringBuilder returnString = new StringBuilder("Inventory:");
        Set<String> keys = inventory.keySet();
        keys.forEach(i -> returnString.append(" ").append(i));
        return returnString + "\nTotal weight: " + currentWeight.toString()
                + " g" + "\nMax weight: " + maxWeight.toString() + " g";
    }
}

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
    private Integer currentWeight = 0;
    private Set<String> itemSet;
    private Game game;
    private Time time;

    /**
     * Constructor for objects of class Player
     */
    public Player(Integer maxWeight) {
        this.maxWeight = maxWeight;
        inventory = new HashMap<>();
    }

    /**
     * Adds an item to player's inventory.
     * If max weight is exceeded, error message shown.
     */
    public void takeItem(Command command) {
        String itemName = command.getSecondWord();
        itemSet = currentRoom.getItems().keySet();
        if (itemSet.isEmpty()) {
            System.out.println("There's nothing to take from this room.");
        } else if (!itemSet.contains(itemName)) {
            System.out.println("Take what?");
        } else {
            Item item = currentRoom.getItems().get(itemName);
            Integer weight = item.getWeight();
            if (currentWeight + weight <= maxWeight) {
                if (weight == 0) {
                    System.out.println("You can't pick that up!");
                } else {
                    if (itemName.equals("backpack")) {
                        System.out.println("Looks like someone left their bag after the lecture. It's bigger than yours.");
                        System.out.println("Maybe you can make USE of it...\n");
                    }
                    inventory.put(itemName, item);
                    currentRoom.items.remove(itemName);
                    currentWeight += weight;
                    System.out.println("Grabbed " + itemName);
                }
            } else {
                System.out.println("No more space in bag. You gotta drop something...");
            }
        }
    }

    /**
     * Returns items currently in player's inventory
     */
    public String getInventory() {
        String returnString = "Inventory:";
        Set<String> keys = inventory.keySet();
        for (String item : keys) {
            returnString += " " + item;
        }
        return returnString + "\nTotal weight: " + currentWeight.toString()
                + " g" + "\nMax weight: " + maxWeight.toString() + " g";
    }
}

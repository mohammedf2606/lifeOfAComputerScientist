/**
 * Class Item - an item in a room
 *
 * @author Fahim Mohammed
 * @version 2018.11.14
 */
public class Item {
  private Integer weight;
  private String description;
  private String name;

  /**
   * Constructor for objects of class Item
   */
  public Item(String name, String description, Integer weight) {
      this.name = name;
      this.description = description;
      this.weight = weight;
  }

  /**
   * @return The name of the item
   * (the one that was defined in the constructor).
   */
  public String getName() {
      return name;
  }

  /**
   * Returns the weight of the current item
   *
   * @return weight of the item
   */
  public Integer getWeight() {
      return weight;
  }

  /**
   * @return The short description of the item
   * (the one that was defined in the constructor).
   */
  public String getShortDescription() {
      return description + ", " + weight;
  }

}

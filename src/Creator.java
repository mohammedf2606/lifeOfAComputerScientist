import java.util.Collections;

/**
 * Class Creator - creates all the rooms in the GAME
 * @author Fahim Mohammed
 * @version 2018.11.29
 */


class Creator extends Actions {
  Room theatre, lab, classroom;
  private Room arcade;
  private Room pub;
  private Room library;
  private Room kitchen;

  /**
   * Create all the rooms and link their exits together.
   */
  void createRooms() {
    // create the rooms
    Room outside = new Room("outside", "outside the main entrance of the university");
    library = new Room("library", "in the Maughan Library");
    hall = new Room("hall", "in the exam hall");
    arcade = new Room("arcade", "in the Arcade");
    theatre = new Room("theatre", "in a lecture theatre");
    pub = new Room("pub", "at The Vault");
    kitchen = new Room("kitchen", "in the canteen");
    Room fifth = new Room("fifth", "on the fifth floor of Bush House");
    office = new Room("office", "in the Informatics departmental office");
    classroom = new Room("classroom", "in a classroom");
    Room sixth = new Room("sixth", "on the sixth floor of Bush House");
    lab = new Room("lab", "in a computing lab");
    Room random = new Room("random", "in a teleporter");

    Collections.addAll(roomList, outside, theatre, arcade, lab, hall, office, pub, fifth, sixth, library, kitchen, classroom);

    // initialise room exits
    outside.setExit("north", arcade);
    outside.setExit("east", library);

    library.setExit("west", outside);

    arcade.setExit("north", theatre);
    arcade.setExit("south", outside);
    arcade.setExit("east", pub);
    arcade.setExit("down", kitchen);
    arcade.setExit("up", fifth);

    theatre.setExit("south", arcade);
    theatre.setExit("east", random);

    pub.setExit("west", arcade);
    pub.setExit("south", kitchen);

    kitchen.setExit("up", arcade);
    kitchen.setExit("north", pub);

    fifth.setExit("down", arcade);
    fifth.setExit("up", sixth);
    fifth.setExit("east", classroom);
    fifth.setExit("north", office);

    office.setExit("south", fifth);

    classroom.setExit("west", fifth);

    sixth.setExit("down", fifth);
    sixth.setExit("north", lab);
    sixth.setExit("south", hall);

    hall.setExit("north", sixth);

    lab.setExit("south", sixth);

    createItems();
    createCharacters();

    player.currentRoom = outside;  // start game outside
  }

  /**
   * Creates all the items and places them in their designated room
   */
  private void createItems() {
    Item ppaBook = new Item("ppabook", "your PPA book", 1200);
    Item elaBook = new Item("elabook", "your ELA book", 1000);
    Item coffee = new Item("coffee", "a cup of coffee", 300);
    Item computer = new Item("computer", "a lab computer", 0);
    elaCW = new Item("elaCW", "your completed ELA coursework", 200);
    ppaCW = new Item("ppaCW", "your completed PPA coursework", 200);
    Item notebook = new Item("notebook", "your notebook", 400);
    Item drink = new Item("beer", "a pint of beer", 300);
    Item printer = new Item("printer", "a printer", 0);
    Item backpack = new Item("backpack", "a bigger backpack", 1);
    Item food = new Item("food", "some food", 400);
    Item exam = new Item("exam", "your final exam paper", 200);

    allItems.put(ppaBook.getName(), ppaBook);
    allItems.put(elaBook.getName(), elaBook);
    allItems.put(coffee.getName(), coffee);
    allItems.put(elaCW.getName(), elaCW);
    allItems.put(ppaCW.getName(), ppaCW);
    allItems.put(notebook.getName(), notebook);
    allItems.put(drink.getName(), drink);
    allItems.put(food.getName(), food);
    allItems.put(backpack.getName(), backpack);
    allItems.put(exam.getName(), exam);

    library.addItem(ppaBook);
    library.addItem(elaBook);

    arcade.addItem(coffee);

    lab.addItem(computer);
    lab.addItem(printer);

    pub.addItem(drink);

    hall.addItem(exam);

    kitchen.addItem(food);

    theatre.addItem(backpack);
    theatre.addItem(notebook);
  }

  /**
   * Creates the characters of the game and puts them in the correct place
   */
  private void createCharacters() {
    Character ppaLecturer = new Character("kolling");
    Character elaLecturer = new Character("rodrigues");
    Character cs1Lecturer = new Character("howard");
    Character fc1Lecturer = new Character("kurucz");

    Collections.addAll(charList, ppaLecturer, elaLecturer, fc1Lecturer, cs1Lecturer);
    charList.forEach(c -> office.addCharacter(c));
    charList.forEach(c -> c.changeRooms(office));
  }
}

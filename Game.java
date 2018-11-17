import java.util.Stack;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom = null;
    private TextReader textReader;
    private ArrayList<Room> roomList;
    private Stack<Room> backStack;
    private Room outside, theatre, arcade, lab, office, pub, fifth, sixth, library, kitchen, classroom, random;
    private Item ppa, ela, coffee, computer, elaCW, ppaCW, notebook, drink, printer;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        roomList = new ArrayList<>();
        backStack = new Stack<>();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        outside = new Room("outside","outside the main entrance of the university");
        library = new Room("library","in the Maughan Library");
        arcade = new Room("arcade","in the Arcade");
        theatre = new Room("theatre","in a lecture theatre");
        pub = new Room("pub","at The Vault");
        kitchen = new Room("kitchen","in the canteen");
        fifth = new Room("fifth","on the fifth floor of Bush House");
        office = new Room("office","in the Informatics departmental office");
        classroom = new Room("classroom","in a classroom");
        sixth = new Room("sixth","on the sixth floor of Bush House");
        lab = new Room("lab","in a computing lab");
        random = new Room("random","in a teleporter");

        Collections.addAll(roomList, outside, theatre, arcade, lab, office, pub, fifth, sixth, library, kitchen, classroom);

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

        lab.setExit("south", sixth);

        createItems();

        currentRoom = outside;  // start game outside
    }

    /**
     * Creates all the items and places them in their designated room
     */
    private void createItems()
    {
        ppa = new Item("ppa","your PPA book", 12);
        ela = new Item("ela","your ELA book", 10);
        coffee = new Item("coffee","a cup of coffee", 3);
        computer = new Item("computer","a lab computer", 0);
        elaCW = new Item("elaCW","your completed ELA coursework", 5);
        ppaCW = new Item("ppaCW","your completed PPA coursework", 5);
        notebook = new Item("notebook","your notebook", 7);
        drink = new Item("beer","a pint of beer", 3);
        printer = new Item("printer","a printer", 0);

        library.addItem(ppa);
        library.addItem(ela);

        arcade.addItem(coffee);
        
        lab.addItem(computer);
        lab.addItem(printer);
        lab.addItem(elaCW);
        lab.addItem(ppaCW);
        
        pub.addItem(drink);
    }

    /**
     * Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        textReader = new TextReader(null);
        textReader.reader();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("back")) {
            goBack();
        }
        else if (commandWord.equals("grab")) {
            
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        textReader = new TextReader("help");
        textReader.reader();
        parser.showCommands();
    }

    /**
     * Try to go to the previous room, else error message.
     */
    private void goBack()
    {
        if (backStack.empty() != true) {
            currentRoom = backStack.pop();
            System.out.println(currentRoom.getLongDescription());
        }
        
        else {
            System.out.println("You are back to the start!");
        }
    }

    /**
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        Random randomizer = new Random();

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            if (nextRoom.getName().equals("random")) {
                System.out.println("You are " + nextRoom.getShortDescription() + ".\n");
                while (nextRoom.getName().equals("random")) {
                    nextRoom = roomList.get(randomizer.nextInt(roomList.size()));
                }
            }
            previousRoom = currentRoom;
            backStack.push(previousRoom);
            currentRoom = nextRoom;
            System.out.println("\n" + currentRoom.getLongDescription());
        }
    }

    /**
     * Looks at the current item and carries out the appropriate action for it
     */
    private void itemCommands(Command command)
    {
        
    }
    
    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}

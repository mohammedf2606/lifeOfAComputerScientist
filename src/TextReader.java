import java.io.*;

/**
 * Reads the story from a text file
 *
 * @author Fahim Mohammed
 * @version 2018.11.12
 */
public class TextReader {
  private String fileName = "";
  private BufferedReader bufferedReader;

  /**
   * Constructor for objects of class TextReader
   * @param command A string that specifies what text file is needed
   */
  public TextReader(String command) {
    try {
      if (command == null) {
        fileName = "intro.txt";
      } else {
        switch (command) {
          case "help":
            fileName = "help.txt";
            break;
          case "turn":
            fileName = "failEndTurns.txt";
            break;
          case "timetable":
            fileName = "timetable.txt";
            break;
          case "time":
            fileName = "time.txt";
            break;
          case "win":
            fileName = "win.txt";
        }
      }
      FileReader fileReader = new FileReader(fileName);
      bufferedReader = new BufferedReader(fileReader);
    } catch (Exception e) {
      exceptionHandling(e);
    }
  }

  /**
   * Prints out each line of the text document with the story
   * If an exception is raised, it is passed for exception handling
   */
  void reader() {
    try {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
      }
      bufferedReader.close();
    } catch (Exception e) {
      exceptionHandling(e);
    }
  }

  /**
   * If any exceptions are detected, they are classified here
   */
  private void exceptionHandling(Exception e) {
    if (e instanceof FileNotFoundException) {
      System.out.println("Unable to open file '" + fileName + "'");
    }

    if (e instanceof IOException) {
      System.out.println("Error reading file '" + fileName + "'");
    }
  }
}

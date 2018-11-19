import java.io.*;
import java.lang.Math.*;
import java.util.*;

/**
 * Write a description of class Time here.
 *
 * @author Fahim Mohammed
 * @version 2018.11.19
 */
public class Time
{
  private String fileName = "";
  private FileReader fileReader;
  private BufferedReader bufferedReader;

  /**
   * Constructor for objects of class Time
   */
  public Time()
  {
    try {
      fileReader = new FileReader("time.txt");
      bufferedReader = new BufferedReader(fileReader);
    } catch(Exception e) {
      exceptionHandling(e);
    }
  }

  /**
   * Shows the current time to the user
   */
  public void showTime(int turns)
  {
    int currentTimeIndex = Math.round(turns / 10);

  }

  /**
   * If any exceptions are detected, they are classified here
   */
  public void exceptionHandling(Exception e){
    if(e instanceof FileNotFoundException) {
      System.out.println("Unable to open file '" + fileName + "'");
    }
    if(e instanceof IOException) {
      System.out.println("Error reading file '" + fileName + "'");
    }
  }
}

import java.io.*;

/**
 * Reads the story from a text file
 *
 * @author Fahim Mohammed
 * @version 2018.11.12
 */
public class TextReader
{
    private String fileName = "";
    private String line = null;
    private FileReader fileReader;
    private BufferedReader bufferedReader;

    /**
     * Constructor for objects of class TextReader
     */
    public TextReader(String command)
    {
        try {
            if (command == "help") {
                fileName = "help.txt";
            }
            if (command == null) {
                fileName = "intro.txt";
            }
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader); 
        }
        catch(Exception e) {
            exceptionHandling(e);
        }
    }
        
    /**
     * Prints out each line of the text document with the story
     * If an exception is raised, it is passed for exception handling
     */ 
    public void reader()
    {
        try {
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
         
            // Always close files.
            bufferedReader.close();
        }
        
        catch(Exception e) {
            exceptionHandling(e);
        }
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

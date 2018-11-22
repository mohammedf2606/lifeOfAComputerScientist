import java.lang.Math.*;

/**
 * Write a description of class Time here.
 *
 * @author Fahim Mohammed
 * @version 2018.11.19
 */
public class Time {
    private String fileName = "";
    private FileReader fr;
    private BufferedReader br;
    private String line = null;
    private int currentTimeIndex;

    /**
     * Constructor for objects of class Time
     */
    public Time() {
    }

    /**
     * Shows the current time to the user
     */
    public String getTime(int turns) {
        currentTimeIndex = getTimeIndex(turns);
        try {
            fr = new FileReader("time.txt");
            br = new BufferedReader(fr);
            for (int i = 0; i < currentTimeIndex; i++) {
                br.readLine();
            }
            line = br.readLine();
            return line;
        } catch (Exception e) {
            exceptionHandling(e);
            return "";
        }
    }

    public int getTimeIndex(int turns) {
        currentTimeIndex = Math.round(turns / 10);
        return currentTimeIndex;
    }

    /**
     * If any exceptions are detected, they are classified here
     */
    public void exceptionHandling(Exception e) {
        if (e instanceof FileNotFoundException) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        if (e instanceof IOException) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }
}

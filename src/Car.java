import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
3/13    [chris]     - added minimum field variables to test gui, used UML
3/14    [chris]     - added getNextPosition() method, no code
3/15    [chris]     - added test code to getNextPosition() to test updating position in GUI
3/18    [Kat]       - added constructor to allow LinkedList to be passed in from importCarFromFile
3/21    [chris]     - implemented UML design
3/22    [tre]       - add method for setting the cars current path index

 */
public class Car extends JLabel {
    /* ___ FIELD VARIABLES ___ */
    /* Name assigned to each Car or players name */
    private String name;
    /* Calculated value to be displayed as user feedback */
    private double speed;
    /* Series of points car must reach to complete race */
    private Point[] checkpoints;
    /* Current point car is heading towards */
    private int checkpointIndex;
    /* Holds the index value of the Track.path point the car is currently at */
    private int currentPointOnPathIndex;
    /* Catch-all value for car "alterations"  */
    private int modifier;
    /* Cars x position on raceTrack panel */
    private int posX;
    /* Cars y position on raceTrack panel */
    private int posY;


    /* ___ CONSTRUCTORS ___ */
    // TODO: 3/22/2023 Constructor setting variables to null and 0 may be redundant unless done for the sake of being explicit.
    //  Need to look at this more closely first.
    public Car() {
        this.name = null;
        this.speed = 0;
        this.checkpoints = null;
        this.checkpointIndex = 0;
        this.modifier = 0;
        this.posX = 0;
        this.posY = 0;
    }
    public Car(String name, Image carImage) {
        this();
        this.name = name;
        this.setIcon(new ImageIcon(carImage));
    }

    public Car(LinkedList<String> data) {
        this();
        String spriteLocation = data.get(0);
    }

    /* ___ FUNCTIONS ___ */

    /* ___ ACCESSORS / MUTATORS ___ */
    public Point getPosition() {
        return new Point(posX, posY);
    }
    public void setPosition(Point position) {
        this.posX = position.x;
        this.posY = position.y;

    }
    public void setNextPosition(Point p) {
        this.posX = p.x;
        this.posY = p.y;
    }

    public int getCurrentPointOnPathIndex() {
        return this.currentPointOnPathIndex;
    }

    public void setCurrentIndexAlongTrackPath(int index) {
        this.currentPointOnPathIndex = index;
    }
}


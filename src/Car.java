import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

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
3/26    [Kat]       - added getter for checkpointIndex, added lastCheckpoint field to hold the index of the last
                      checkpoint passed for carpanel info

 */
public class Car extends JLabel {

    private int baseSpeed;

    public int getBaseSpeed() {
        return baseSpeed;
    }

    /* ___ FIELD VARIABLES ___ */
    enum direction {UP, DOWN, LEFT, RIGHT}
    private direction currDir;
    /* Name assigned to each Car or players name */
    private String name;
    /* Calculated value to be displayed as user feedback */
    private int speed;
    /* Series of points car must reach to complete race */
    private Point[] checkpoints;
    /* Current point car is heading towards */
    private int checkpointIndex;
    // last checkpoint Car passed
    private int lastCheckpoint;
    /* Holds the index value of the Track.path point the car is currently at */
    private int currentIndexOnTrackPointPath;
    /* Catch-all value for car "alterations"  */
    private int modifier;
    /* Cars x position on raceTrack panel */
    private int posX;
    /* Cars y position on raceTrack panel */
    private int posY;
    private boolean wasRotated;

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

    public Car(String name, Image carImage, Point[] checkpoints, int startingX, int startingY) {
        this();
        this.name = name;
        this.checkpoints = checkpoints;
        this.posY = startingY;
        this.posX = startingX;
        this.setIcon(new ImageIcon(carImage));
        this.currDir = direction.UP;
        this.wasRotated = false;
        determineCarSpeed();
        speed = baseSpeed;
    }

    /* ___ FUNCTIONS ___ */

    private void determineCarSpeed() {
        /* TODO: call this method each time getSpeed() is called
         * then either add or subtract 1 or 2 from the cars speed
         * every X number of iterations.
         */
        switch(this.name) {
            case "blue":
            case "purple":
                baseSpeed = 3;
                break;
            case "green":
            case "red":
                baseSpeed = 4;
                break;
            case "orange":
            case "yellow":
                baseSpeed = 5;
                break;
        }
    }
    /* ___ ACCESSORS / MUTATORS ___ */

    public int getLastCheckpoint() {
        return this.lastCheckpoint;
    }

    public void setLastCheckpoint(int lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }
    public Point getPosition() {
        return new Point(this.posX, this.posY);
    }
    public void setNextPosition(Point p) {
        if(this.posX == 0 && this.posY == 0) {
            this.posX = p.x;
            this.posY = p.y;
            return;
        }
        checkForRotation(p);
        this.posX = p.x;
        this.posY = p.y;
    }
    public void checkForRotation(Point next) {
        // if orientation = down and x changes =>  turn left or turn right
        // change sprite
        if( (next.y - this.posY) >= 1)  { // Moving downwards
            if(!(currDir.equals(direction.DOWN))) {
                this.currDir = direction.DOWN;
                this.wasRotated = true;
            }
        }
        if( (next.y - this.posY) < 0)  { // Moving upwards
            if(!(currDir.equals(direction.UP))) {
                this.currDir = direction.UP;
                this.wasRotated = true;
            }

        }
        if( (next.x - this.posX) >= 1)  { // Moving right
            if(!(currDir.equals(direction.RIGHT))) {
                this.currDir = direction.RIGHT;
                this.wasRotated = true;
            }

        }
        if( (next.x - this.posX) < 0)  { // Moving left
            if(!(currDir.equals(direction.LEFT))) {
                this.currDir = direction.LEFT;
                this.wasRotated = true;
            }

        }

    }

    public int getCurrentIndexOnTrackPointPath() {
        return currentIndexOnTrackPointPath;
    }

    public void setCurrentIndexOnTrackPointPath(int index) {
        this.currentIndexOnTrackPointPath = index;
    }

    public void incrementCurrentIndexOnTrackPointPath(int amount) {
        currentIndexOnTrackPointPath += amount;
    }
    public int getCheckpointIndex() {
        return checkpointIndex;
    }

    public void checkIndexRange(int length) {
        if(this.currentIndexOnTrackPointPath >= length) {
            setCurrentIndexOnTrackPointPath((this.currentIndexOnTrackPointPath - length));
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int value) {
        this.speed = value;
    }

    public int getCurrDir() {
        return currDir.ordinal();
    }

    public boolean getWasRotated() {
        return wasRotated;
    }
    public void setWasRotated(boolean b) {
        this.wasRotated = b;
    }

    public String getName() {
        return this.name;
    }
}


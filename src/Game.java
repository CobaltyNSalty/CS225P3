import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Scanner;


/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
                    - wrote play() method and added field variable declarations
                    - added default constructor
3/13    [chris]     - added testing code for gui.
3/14    [chris]     - testing gameLoop method for desired functionality, added updateCarPosition() from the UML
3/17    [joey]      - updated gameLoop to incorporate the timer
3/20    [joey]      - added new Instant attribute "startTime"
3/21    [joey]      - added getCurrentTime() method
3/18    [Kat]       - adding code for importing Car and Track data from files
3/21    [tre]       - replace timer delay with constant
3/22    [tre]       - implement updateCarPositions() which now has the cars loop indefinitely around the track
3/22    [chris]     - added initializeGameWindow() to create game from options selected in application
3/23    [chris]     - added control functions array, merged branches and troubleshot, implemented game start method
                    - sequence using actionListener and controlFunction.
3/25    [chris]     - moved initialization of startTime to when the gameClock control boolean 'play' is set to true.
                    - made the continue button not enabled until at least 1 car and 1 track are selected
3/26    [Kat]       - changed updateCarPosition to now check for if the car passed through a checkpoint
3/26    [chris]     - added comments to updateCarPosition and reorganized car.position() get/set calls

 */
public class Game implements ActionListener {
    /* The delay in milliseconds of the game clock timer */
    public static final int TIMER_DELAY = 15;
    /* 2-D image of racetrack and the path the raceCars move along */
    private Track raceTrack;
    /* The raceCars drawn on the raceTrack */
    private Car[] racers;
    /* Graphical User Interface for Application */
    private GUI gui;
    /* Functions performed by Game class that are triggered
    * by user interaction with GUI. i.e. buttons */
    private Object[] controlFunctions;
    /* Used to provide feedback to user of elapsed time */
    private Instant startTime;
    /* Control variable to start game */
    private boolean play;


    public Game() {
        this.raceTrack = null;
        this.racers = null;
        this.gui = null;
        this.play = false;
        this.controlFunctions = new Object[2];
        initControlFunctions();
    }

    /**
     * Launches the application by initializing the User interface
     */
    public void play() {
        this.gui = new GUI(this.controlFunctions);
    }

    /**
     * Primary method that calls recurring game functions. Limited implementation
     * only includes animating car movement.
     */
    private void gameLoop() {
        if(play) {
            /* Method triggers every TIMER_DELAY to animate car movement */
            Timer gameClock = new Timer(TIMER_DELAY, e -> {
                updateCarPositions();
                this.gui.updateTimer(getGameDuration().getSeconds());
            });
            gameClock.start();
        }
    }

    /**
     * Determines the cars next position along the track and moves the car to that position.
     */
    private void updateCarPositions() {
        for (Car car : this.racers) {
            // save current position value
            Point current = car.getPosition();
            // add speed to current index position
            car.incrementCurrentIndexOnTrackPointPath(car.getSpeed());
            // wrap around array total path if necessary
            car.checkIndexRange(this.raceTrack.getPath().size());
            // get Point value from new index position
            Point next = this.raceTrack.getPointAtIndex(car.getCurrentIndexOnTrackPointPath());
            // update tracker
            car.setNextPosition(next);
            // redraw
            this.gui.drawNewCarPositions();

            // Determine if any car crossed any checkpoint
            int checkPoint = this.raceTrack.CheckpointCrossedIndex(current, next);
            if (checkPoint >= 0) {
                car.setLastCheckpoint(checkPoint);
            }
        }
    }

    /* Helper methods */

    /**
     * Initialize control buttons for actions that affect the state of the game and must be handled
     * by Game class but require a graphical trigger so must be passed to GUI.
     */
    private void initControlFunctions() {
        JButton continueButton = new JButton();
        continueButton.setEnabled(false);
        continueButton.addActionListener(this);
        this.controlFunctions[0] = continueButton;
    }

    /* Class Functions */
    /**
     * Calculate time between start of game play and now.
     * @return - time
     */
    public Duration getGameDuration(){
        Instant currentTime = Instant.now();
        return Duration.between(this.startTime,currentTime);
    }

    public Track importTrackFromFile(String fileName) throws IOException {
        Track importTrack;
        LinkedList<String> data;

        data = importData(fileName);
        importTrack = new Track(data);

        return importTrack;
    }

    public LinkedList<String> importData(String fileName) throws IOException {
        FileInputStream inFS = null;
        LinkedList<String> data;
        try {
            inFS = new FileInputStream(fileName);
            data = extractInfoFromFile(inFS);
        } catch (FileNotFoundException e) {
            data = new LinkedList<String>();
        } finally {
            if (inFS != null) {
                inFS.close();
            }
        }
        return data;
    }

    public LinkedList<String> extractInfoFromFile(FileInputStream fIS) {
        LinkedList<String> entryList = new LinkedList<String>();
        Scanner scnr = new Scanner(fIS);
        while (scnr.hasNextLine()) {
            entryList.add(scnr.nextLine());
        }
        return entryList;
    }
    public void initializeGameWindow(JButton pressed) {
        // Get track and racers from selection window chosen by user
        Object[] args = this.gui.extractGameArgs(pressed);
        this.racers = (Car[]) args[0];
        String trackFileName = ((String) args[1]);

        // Initialize Track object from filename
        try {
            this.raceTrack = importTrackFromFile(trackFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // initialize Gameplay window with user selection
        Object[] assets = new Object[] { this.racers, this.raceTrack};
        this.gui.gameAssetsSelected(assets);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /* The only control function is the 'continue' button to move
         * from racer and track selection to the game play window.
         */
        initializeGameWindow((JButton) e.getSource());
        this.play = true;
        this.startTime = Instant.now();
        gameLoop();
    }
}

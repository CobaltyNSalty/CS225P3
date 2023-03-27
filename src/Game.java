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
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


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
3/26    [tre]       - implemented variable random speed algorithm
3/26    [Kat]       - added assignCheckpoints function, edited checkpoint checking functionality to check if the
                      checkpoint crossed was the next one in the car's list
3/26    [chris]     - added end game functionality and method

 */

/**
 * Handler class for running the Game. Orchestrates game state changes and interactions between gameAssets
 */
public class Game implements ActionListener {
    /* The delay in milliseconds of the game clock timer */
    public static final int TIMER_DELAY = 15;
    // number of checkpoints the race consists of
    private static final int NUM_CHECKPOINTS = 8;
    /**
     * The delay (in multiples of <code>TIMER_DELAY</code> milliseconds) before changing a cars speed.
     * A value of 100 equals <code>100 * TIMER_DELAY = 1500</code> milliseconds.
     */
    public static final int SPEED_CHANGE_DELAY = 100;
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
    /* Frame rate of game to update car positions and game clock */
    private Timer gameClock;

    public Game() {
        this.raceTrack = null;
        this.racers = null;
        this.gui = null;
        this.play = false;
        this.controlFunctions = new Object[2];
        initControlFunctions();
    }

    /**
     * Gets a random racers car and multiplies the speed of that care by either 1 or 2
     * to add speed variation to cars.
     */
    private void changeSpeedOfRandomCar() {
        /* get a random car */
        Car car = racers[(int) (Math.random() * racers.length)];
        /* these are the values that will be multiplied by the cars speed */
        int[] multipliers = new int[]{-2,-1,0,1,2};
        /* Calculate the cars new speed by multiplying one of the multipliers by the cars base speed. */
        int newSpeed = car.getSpeed() * multipliers[(int) (Math.random() * multipliers.length)];
        /* To avoid setting the cars speed to a negative number and having the car go backwards, a check
        * is made to ensure that the cars speed is never less than it's base speed. */
        car.setSpeed(newSpeed < 1 ? car.getBaseSpeed() : newSpeed);
    }

    /* Game control methods */
    /**
     * Launches the application by initializing the User interface
     */
    public void play() {
        this.gui = new GUI(this.controlFunctions);
    }

    /**
     * Start point for game window. Houses the timer method which is called on intervals
     * of TIMER_DELAY to update game state.
     */
    private void gameLoop() {
        assignCheckpoints();
        /* The value to check against the speed change rate to determine whether
         * a cars speed should be changed this tick. Making it atomic is necessary for concurrency. */
        AtomicInteger count = new AtomicInteger();
        this.gameClock = new Timer(TIMER_DELAY, e -> {
            /* If a racer has reached their final checkpoint, end the game */
            if(!this.play) {endGame(); }
            /* See if count is divisible by the change rate and if it is then a cars speed should
             * get changed this tick. */
            if (count.getAndIncrement() % SPEED_CHANGE_DELAY == 0) {
                changeSpeedOfRandomCar();
            }
            /* Until a racer reaches their last checkpoint, update car positions */
            if(this.play) {
                if (updateCarPositions()) {
                    /* racer has reach their last checkpoint, end game */
                    this.play = false;
                } else {
                    /* only update gui elements if the game hasn't ended */
                    this.gui.updateTimer(getGameDuration().getSeconds());
                }
            }
        });
        this.gameClock.start();

    }

    /**
     * Called to end the game when a racer achieves victory(reached all checkpoints)
     */
    private void endGame() {
        this.gameClock.stop();
        this.gui.endGame();
    }

    /**
     * Method to give each car in the race a discrete set of checkpoints and pass it to their checkpoints field.
     */
    private void assignCheckpoints() {
        int[] checkpointList;
        for (Car c : this.racers) {
            checkpointList = new int[NUM_CHECKPOINTS];
            for (int i = 0; i < NUM_CHECKPOINTS; i ++) {
                int checkpoint = (int)(Math.random() * 3);
                checkpointList[i] = checkpoint;
            }
            c.setCheckpoints(checkpointList);
        }
    }

    /**
     * Updates the cars next position along the track and redraws the car at that position. Checks
     * if a raceCar has crossed a checkpoint. Checks if a car has reached their final checkpoint and
     * won the race.
     */
    private boolean updateCarPositions() {
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

            // Determine if any car crossed any checkpoint
            int checkpoint = this.raceTrack.CheckpointCrossedIndex(current, next);
            if (checkpoint >= 0) {
                if (checkpoint == car.getCheckpoints()[car.getCheckpointIndex()]) {
                    if (car.incrementCheckpointIndex()) {
                        return true; // final checkpoint has been reached by racer
                    }
                }
            }
        }

        // redraw
        this.gui.drawNewCarPositions();

        return false;
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

    /**
     * Creates Track object from data stored in fileName
     * @param fileName - Track data file
     * @return Track object used as the games raceTrack
     * @throws IOException - if fileName doesn't exist
     */
    public Track importTrackFromFile(String fileName) throws IOException {
        Track importTrack;
        LinkedList<String> data;

        data = importData(fileName); // Get data from file
        importTrack = new Track(data); // create Track

        return importTrack;
    }

    /**
     * Convert data file into usable data
     * @param fileName - Track data file
     * @return - List of lines of data used to initialise Tiles
     * @throws IOException - if fileName doesn't exist
     */
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

    /**
     * Read text from data file and store in a List data structure
     * @param fIS - stream to read from, Track data file
     * @return - data
     */
    public LinkedList<String> extractInfoFromFile(FileInputStream fIS) {
        LinkedList<String> entryList = new LinkedList<String>();
        Scanner scnr = new Scanner(fIS);
        while (scnr.hasNextLine()) {
            entryList.add(scnr.nextLine());
        }
        return entryList;
    }

    /**
     * After user selects cars and Track, this method initializes gameAssets and passes them to GUI
     * @param pressed
     */
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

    /**
     * control function button pressed
     * @param e - 'continue' button
     */
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

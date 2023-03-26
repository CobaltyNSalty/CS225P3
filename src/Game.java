import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
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

 */
public class Game implements ActionListener {
    /**
     * The delay in milliseconds of the game clock timer.
     */
    public static final int TIMER_DELAY = 15;
    private Track raceTrack;
    private Car[] racers;
    private GUI gui;
    private Timer gameClock;
    private Object[] controlFunctions;
    //test
    private Instant startTime;
    private boolean play;


    public Game() {
        this.raceTrack = null;
        this.racers = null;
        this.gui = null;
        this.play = false;
        this.controlFunctions = new Object[2];
        initControlFunctions();
    }

    /* Game control methods */
    public void play() {
        this.gui = new GUI(this.controlFunctions);
    }

    private void gameLoop() {
        if(play) {
            gameClock = new Timer(TIMER_DELAY, e -> {
                updateCarPositions();
                this.gui.updateTimer(getCurrentTime().getSeconds());
            });
            gameClock.start();
        }
    }

    /**
     * Determines the cars next position along the track and moves the car to that position.
     *
     */
    private void updateCarPositions() {
        for(Car car: this.racers) {
            Point next = this.raceTrack.getNextPointOnPath(car.getCurrentIndexOnTrackPointPath());
            car.setNextPosition(next);
            car.checkIndexRange(this.raceTrack.getPath().size()); // [chris] added check to have path array be circular
            car.incrementCurrentIndexOnTrackPointPath(car.getSpeed()); // [chris] added a simple speed variance between cars
            this.gui.drawNewCarPositions();
        }
    }

    /* Helper methods */
    private void initControlFunctions() {
        JButton continueButton = new JButton();
        continueButton.setEnabled(false);
        continueButton.addActionListener(this);
        this.controlFunctions[0] = continueButton;
    }

    /* Class Functions */
    public Duration getCurrentTime(){
        Instant currentTime = Instant.now();
        return Duration.between(startTime,currentTime);
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
        Object[] args = this.gui.extractGameArgs(pressed);
        this.racers = (Car[]) args[0];
        String filename = ((String) args[1]);
        try {
            this.raceTrack = importTrackFromFile(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Object[] assets = new Object[] { this.racers, this.raceTrack};
        this.gui.gameAssetsSelected(assets);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: for now, there is only one control button
        initializeGameWindow((JButton) e.getSource());
        this.play = true;
        this.startTime = Instant.now();
        gameLoop();
    }
}

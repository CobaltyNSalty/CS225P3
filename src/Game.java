import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
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
3/18    [Kat]       - adding code for importing Car and Track data from files
3/21    [Tre]       - Replace timer delay with constant


 */
public class Game implements ActionListener {
    /**
     * The delay in milliseconds of the game clock timer.
     */
    public static final int TIMER_DELAY = 50;
    private Track raceTrack;
    private Car[] racers;
    private GUI gui;
    private Timer gameClock;

    public Game() {
        this.raceTrack = null;
        this.racers = null;
        this.gui = null;
    }

    public void play() {
        try {
            raceTrack = importTrackFromFile("Tracks\\Track1.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO: Replace this code block with inputs from filereader
        this.racers = new Car[] { new Car(), new Car() };
        Image car1, car2;
        try {
            car1 = ImageIO.read(new File("Sprites\\carSprites\\blueCar.png"));
            car2 = ImageIO.read(new File("Sprites\\carSprites\\orangeCar.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.racers[0] = new Car("Tester 1", car1, new Point[]{new Point(50, 50), new Point(100, 100)}, 0, 50);
        this.racers[1] = new Car("Tester 2", car2, new Point[]{new Point(50, 50), new Point(100, 100)}, 100, 50);
        // TODO: end of code block to remove

        Object[] gameAssets = new Object[] {this.raceTrack, this.racers, this};
        this.gui = new GUI(gameAssets);
        gameLoop();
    }

    private void gameLoop() {
        // TODO: clock starts prior to game start
        gameClock = new Timer(TIMER_DELAY, e -> {
            updateCarPositions();
        });
        gameClock.start();
    }

    private void updateCarPositions() {
        // TODO: pending pathing implementation
        for(Car car: this.racers) {
            // TODO: this hasn't been tested, needs the generation of Track.path to be completed
            car.setNextPosition(this.raceTrack.getNextPointOnPath(car.getCurrentPointOnPathIndex()));
            int nextPathIndex = car.getCurrentPointOnPathIndex() + 1;
            car.setCurrentPointOnPathIndex(nextPathIndex >= raceTrack.getPath().size() - 1 ? 0 : nextPathIndex);
            this.gui.drawNewCarPositions();
        }
    }

    public Car importCarFromFile(String fileName) throws IOException {
        Car importCar;
        LinkedList<String> data;

        data = importData(fileName);
        importCar = new Car(data);

        return importCar;
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

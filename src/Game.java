import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
                    - wrote play() method and added field variable declarations
                    - added default constructor
3/13    [chris]     - added testing code for gui.
3/14    [chris]     - testing gameLoop method for desired functionality, added updateCarPosition() from the UML


 */
public class Game {

    // TODO: 3/21/2023 Add documentation.
    public static Game instance;

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
        // TODO: Replace this code block with inputs from filereader
        this.raceTrack = new Track();
        this.raceTrack.createHardCodedTrackTileData();
        this.racers = new Car[] { new Car(), new Car() };
        Image car1, car2;
        try {
            car1 = ImageIO.read(new File("Sprites\\carSprites\\blueCar.png"));
            car2 = ImageIO.read(new File("Sprites\\carSprites\\orangeCar.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.racers[0].setSprite(car1);
        this.racers[1].setSprite(car2);
        // TODO: end of code block to remove

        Object[] gameAssets = new Object[] {this.raceTrack, this.racers};
        this.gui = new GUI(gameAssets);
        gameLoop();
    }

    private void gameLoop() {
        gameClock = new Timer(50, e -> {
            updateCarPositions();
        });
        gameClock.start();
    }

    private void updateCarPositions() {
        for(Car car: this.racers) {
            // TODO: next positions should be the next point in the path array in raceTrack variable
            car.getNextPosition();
            this.gui.drawNewCarPositions();
        }
    }

    public Track getTrack() {
        return raceTrack;
    }

    public static Game getInstance() {
        return instance;
    }
}

/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
3/13    [chris]     - implemented minimum field variables to create gui, used UML
                    - wrote basic constructors and a test method with hardcoded Tiles variable
                    - wrote loadTrackTiles() method


 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class Track {
    // Tile array maximum size: width: 14, height: 10
    private Tile[][] raceTrack;
    private Image[] trackTileSprites;

    public Track() {
        this.raceTrack = null;
        this.trackTileSprites = null;
    }

    public Track(int width, int height) {
        this.raceTrack = new Tile[height][width];
    }

    public Track(Tile[][] tiles) {
        this.raceTrack = tiles;
    }

    /**
     * This method is to create a Track object without importing data from a file. This
     * is a method used to test the gui system.
     * TODO: Remove this method
     */
    public void createHardCodedTrackTileData() {
        this.raceTrack = new Tile[9][13];
        loadTrackTiles();
        for (int row = 0; row < raceTrack.length; row++) {
            for (int col = 0; col < raceTrack[row].length; col++) {
                this.raceTrack[row][col] = new Tile(trackTileSprites[0]); // empty tile
            }
        }
        /*     0   1   2   3   4   5   6   7   8   9   10  11  12  13
            0
            1      6   2   4           6   2   2   2   2   2   4
            2      1       1           1                       1
            3      1       5   2   2   3                       1
            4      1                                           1
            5      1                                   6   2   3
            6      5   2   4                           1
            7              1                           1
            8              5   2   2   2   2   2   2   3
            9
         */
        this.raceTrack[1][1] = new Tile(trackTileSprites[6]);
        this.raceTrack[2][1] = new Tile(trackTileSprites[1]);
        this.raceTrack[3][1] = new Tile(trackTileSprites[1]);
        this.raceTrack[4][1] = new Tile(trackTileSprites[1]);
        this.raceTrack[5][1] = new Tile(trackTileSprites[1]);
        this.raceTrack[6][1] = new Tile(trackTileSprites[5]);
        this.raceTrack[6][2] = new Tile(trackTileSprites[2]);
        this.raceTrack[6][3] = new Tile(trackTileSprites[4]);
        this.raceTrack[7][3] = new Tile(trackTileSprites[1]);
        this.raceTrack[8][3] = new Tile(trackTileSprites[5]);
        this.raceTrack[8][4] = new Tile(trackTileSprites[2]);
        this.raceTrack[8][5] = new Tile(trackTileSprites[2]);
        this.raceTrack[8][6] = new Tile(trackTileSprites[2]);
        this.raceTrack[8][7] = new Tile(trackTileSprites[2]);
        this.raceTrack[8][8] = new Tile(trackTileSprites[2]);
        this.raceTrack[8][9] = new Tile(trackTileSprites[2]);
        this.raceTrack[8][10] = new Tile(trackTileSprites[3]);
        this.raceTrack[7][10] = new Tile(trackTileSprites[1]);
        this.raceTrack[6][10] = new Tile(trackTileSprites[1]);
        this.raceTrack[5][10] = new Tile(trackTileSprites[6]);
        this.raceTrack[5][11] = new Tile(trackTileSprites[2]);
        this.raceTrack[5][12] = new Tile(trackTileSprites[3]);
        this.raceTrack[4][12] = new Tile(trackTileSprites[1]);
        this.raceTrack[3][12] = new Tile(trackTileSprites[1]);
        this.raceTrack[2][12] = new Tile(trackTileSprites[1]);
        this.raceTrack[1][12] = new Tile(trackTileSprites[4]);
        this.raceTrack[1][11] = new Tile(trackTileSprites[2]);
        this.raceTrack[1][10] = new Tile(trackTileSprites[2]);
        this.raceTrack[1][9] = new Tile(trackTileSprites[2]);
        this.raceTrack[1][8] = new Tile(trackTileSprites[2]);
        this.raceTrack[1][7] = new Tile(trackTileSprites[2]);
        this.raceTrack[1][6] = new Tile(trackTileSprites[6]);
        this.raceTrack[2][6] = new Tile(trackTileSprites[1]);
        this.raceTrack[3][6] = new Tile(trackTileSprites[3]);
        this.raceTrack[3][5] = new Tile(trackTileSprites[2]);
        this.raceTrack[3][4] = new Tile(trackTileSprites[2]);
        this.raceTrack[3][3] = new Tile(trackTileSprites[5]);
        this.raceTrack[2][3] = new Tile(trackTileSprites[1]);
        this.raceTrack[1][3] = new Tile(trackTileSprites[4]);
        this.raceTrack[1][2] = new Tile(trackTileSprites[2]);
    }

    /**
     * Import default tile set for Track tiles
     * @return if images loaded successfully, 0 = fail, 1 = success
     */
    private int loadTrackTiles() {
        File imageDirectory = new File("Sprites\\TrackTiles");
        String[] imageNames = imageDirectory.list();
        if (imageNames == null) {
            return 0;
        }

        this.trackTileSprites = new Image[imageNames.length];
        int index = 0;

        for (String imageName : imageNames) {
            String filepath = "Sprites\\TrackTiles\\\\" + imageName;
            Image trackSprite;
            try {
                trackSprite = ImageIO.read(new File(filepath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.trackTileSprites[index] = trackSprite;
            index++;
        }

        return 1;
    }

    public Tile[][] getRaceTrack() {
        return raceTrack;
    }

    public Image getTileSpriteAt(int x, int y) {
        return this.raceTrack[x][y].getSprite();
    }
}

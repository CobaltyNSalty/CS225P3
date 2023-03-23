/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
3/13    [chris]     - implemented minimum field variables to create gui, used UML
                    - wrote basic constructors and a test method with hardcoded Tiles variable
                    - wrote loadTrackTiles() method
3/14    [chris]     - testing a total track path implementation with createPath() method
3/20    [Kat]       - wrote constructor for passing in LinkedList from importTrackfromFile function
3/21    [chris]     - updated documentation for data file constructor


 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 *  Tile array maximum size: width: 14, height: 10. Display window is fixed size so this is the
 *  maximum allowable grid dimensions.
 */
public class Track {
    /* ___ FIELD VARIABLES ___ */
    /* The background image of the racetrack, constructed via smaller images in a 2D grid */
    private Tile[][] raceTrack;
    /* The Images used by 'raceTrack' to initialize Tiles */
    private Image[] trackTileSprites;
    /* The sequence of (x,y) coordinates that Car objects will follow to "stay on the road" */
    private Point[] path;
    private String filename;

    /* ___ CONSTRUCTORS ___ */
    public Track() {
        this.raceTrack = null;
        this.trackTileSprites = null;
        this.path = null;
    }

    public Track(String filename) {

    }

    /**
     * Creates a new Track object from data file
     * First line of data file: [0] = height, [1] = width, [2] = 0(nothing)
     * All following lines: [0] = Row, [1] = Column, [2] = ImageID integer
     * ImageID is the integer value at the beginning of the selected Tiles filename in the default tile set.
     * @param data - List of Strings imported from the data File
     */
    public Track(LinkedList<String> data) {
        this();
        /* Import all default Tile sprite images from "Sprites\\TrackTiles" */
        loadTrackTiles();
        /* Holds integer values from data file once imported and converted */
        int[] tileArgs = new int[3];                                 // each line is 1 Tile and has 3 entries
        String[] tileArgsStringArray = (data.pop()).split(",");// get first line from data file
        tileArgs[0] = Integer.parseInt(tileArgsStringArray[0]);      // first line entry 1 = height of track
        tileArgs[1] = Integer.parseInt(tileArgsStringArray[1]);      // first line entry 2 = width of track
        this.raceTrack = new Tile[tileArgs[0]][tileArgs[1]];         // initialize raceTrack with given dimensions

        /* Initialize all elements in raceTrack to a non-null Tile(0 - Empty Tile) */
        for (int row = 0; row < this.raceTrack.length; row++) {
            for (int col = 0; col < this.raceTrack[row].length; col++) {
                this.raceTrack[row][col] = new Tile(this.trackTileSprites[0]);
            }
        }

        /* Modify Tiles in raceTrack with new Tiles from data File */
        while (!(data.isEmpty())) {
            tileArgsStringArray = (data.pop()).split(",");
            for (int x = 0; x < tileArgsStringArray.length; x++) { // [chris] swapped integer value for stringArray.length
                tileArgs[x] = Integer.parseInt(tileArgsStringArray[x]);
            }
            this.raceTrack[tileArgs[0]][tileArgs[1]] = new Tile(this.trackTileSprites[tileArgs[2]], tileArgs[2], tileArgs[0], tileArgs[1]);
        }
    }

    /* ___ FUNCTIONS ___ */
    /*
     * This method is to create a Track object without importing data from a file. This
     * is a method used to test the gui system.
     * TODO: Remove this method after filereader implemented

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
            1      6   2   4           6   2   2   8   2   2   4
            2      1       1           1                       1
            3      1       5   2   2   3                       1
            4      7                                           1
            5      1                                   6   2   3
            6      5   2   4                           1
            7              1                           1
            8              5   2   2   2   8   2   2   3
            9

        this.raceTrack[1][1] = new Tile(trackTileSprites[6], 6, 1, 1);
        this.raceTrack[2][1] = new Tile(trackTileSprites[1], 1, 2, 1);
        this.raceTrack[3][1] = new Tile(trackTileSprites[1], 1, 3, 1);
        this.raceTrack[4][1] = new Tile(trackTileSprites[7], 7, 4, 1);
        this.raceTrack[5][1] = new Tile(trackTileSprites[1], 1, 5, 1);
        this.raceTrack[6][1] = new Tile(trackTileSprites[5], 5, 6, 1);
        this.raceTrack[6][2] = new Tile(trackTileSprites[2], 2, 6, 2);
        this.raceTrack[6][3] = new Tile(trackTileSprites[4], 4, 6, 3);
        this.raceTrack[7][3] = new Tile(trackTileSprites[1], 1, 7, 3);
        this.raceTrack[8][3] = new Tile(trackTileSprites[5], 5, 8, 3);
        this.raceTrack[8][4] = new Tile(trackTileSprites[2], 2, 8, 4);
        this.raceTrack[8][5] = new Tile(trackTileSprites[2], 2, 8, 5);
        this.raceTrack[8][6] = new Tile(trackTileSprites[2], 2, 8, 6);
        this.raceTrack[8][7] = new Tile(trackTileSprites[8], 8, 8, 7);
        this.raceTrack[8][8] = new Tile(trackTileSprites[2], 2, 8, 8);
        this.raceTrack[8][9] = new Tile(trackTileSprites[2], 2, 8, 9);
        this.raceTrack[8][10] = new Tile(trackTileSprites[3], 3, 8, 10);
        this.raceTrack[7][10] = new Tile(trackTileSprites[1], 1, 7, 10);
        this.raceTrack[6][10] = new Tile(trackTileSprites[1], 1, 6, 10);
        this.raceTrack[5][10] = new Tile(trackTileSprites[6], 6, 5, 10);
        this.raceTrack[5][11] = new Tile(trackTileSprites[2], 2, 5, 11);
        this.raceTrack[5][12] = new Tile(trackTileSprites[3], 3, 5, 12);
        this.raceTrack[4][12] = new Tile(trackTileSprites[1], 1, 4, 12);
        this.raceTrack[3][12] = new Tile(trackTileSprites[1], 1, 3, 12);
        this.raceTrack[2][12] = new Tile(trackTileSprites[1], 1, 2, 12);
        this.raceTrack[1][12] = new Tile(trackTileSprites[4], 4, 1, 12);
        this.raceTrack[1][11] = new Tile(trackTileSprites[2], 2, 1, 11);
        this.raceTrack[1][10] = new Tile(trackTileSprites[2], 2, 1, 10);
        this.raceTrack[1][9] = new Tile(trackTileSprites[8], 8, 1, 9);
        this.raceTrack[1][8] = new Tile(trackTileSprites[2], 2, 1, 8);
        this.raceTrack[1][7] = new Tile(trackTileSprites[2], 2, 1, 7);
        this.raceTrack[1][6] = new Tile(trackTileSprites[6], 6, 1, 6);
        this.raceTrack[2][6] = new Tile(trackTileSprites[1], 1, 2, 6);
        this.raceTrack[3][6] = new Tile(trackTileSprites[3], 3, 3, 6);
        this.raceTrack[3][5] = new Tile(trackTileSprites[2], 2, 3, 5);
        this.raceTrack[3][4] = new Tile(trackTileSprites[2], 2, 3, 4);
        this.raceTrack[3][3] = new Tile(trackTileSprites[5], 5, 3, 3);
        this.raceTrack[2][3] = new Tile(trackTileSprites[1], 1, 2, 3);
        this.raceTrack[1][3] = new Tile(trackTileSprites[4], 4, 1, 3);
        this.raceTrack[1][2] = new Tile(trackTileSprites[2], 2, 1, 2);
    }
    */

    /**
     * Returns the next point on the path given an index value for the current path
     * @param index - Index of current point on path
     * @return - next point on path
     */
    public Point getNextPointOnPath(int index) {
        return path[(index +1)];
    }

    /**
     * Import default tile set for Track tiles
     * @return if images loaded successfully
     */
    private boolean loadTrackTiles() {
        File imageDirectory = new File("Sprites\\TrackTiles");
        String[] imageNames = imageDirectory.list();
        if (imageNames == null) {
            return false;
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

        return true;
    }

    /**
     * Extract the Path from each tile, correct points to the (x,y) position of the parent container,
     * then string together each tiles path to create a sequence of coordinates that represent the
     * entire racetrack's path.
     */
    private void createPath() {
        // navigate 2D array based on Tile type
        // get first road tile
        Tile start = getNonEmptyTile();
        int rowIndex = start.getIndexPosRow();
        int colIndex = start.getIndexPosCol();
        Point startPoint = start.getPath()[0];
        int posX = (int)((colIndex * 50) + startPoint.getX());
        int posY = (int)((rowIndex * 50) + startPoint.getY());
        // TODO: add all points from path using offset values, create offset values to replace first factor in posX,posY
        Point lastPoint;


    }

    /**
     * Search through Tile[][] until a tile that is not an empty tile is found, this becomes
     * the start of the racetrack path creation.
     * @return first non-empty Tile found in Tile[][]
     */
    private Tile getNonEmptyTile() {
        for(int a = 0; a < this.raceTrack.length; a++) {
            for(int b = 0; b < this.raceTrack[0].length; b++) {
                if(this.raceTrack[a][b].getTileIDNum() > 0 ) {
                    return this.raceTrack[a][b];
                }
            }
        }
        return null;
    }


    /* ___ ACCESSORS / MUTATORS ___ */
    public Tile[][] getRaceTrack() {
        return raceTrack;
    }

    public Tile getTileAtPoint(int x, int y) {
        return this.raceTrack[x][y];
    }

    public String getFilename() {
        return filename;
    }
}

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
3/22    [tre]       - change type of path field to List<Point>
                    - change tileArgs size to 4 to account for the path index of each track tile
                    - add method getPath() for getting the path field

 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Point> path;

    /* ___ CONSTRUCTORS ___ */
    public Track() {
        this.raceTrack = null;
        this.trackTileSprites = null;
        this.path = new ArrayList<>();
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
            Tile tile = new Tile(this.trackTileSprites[tileArgs[2]], tileArgs[2], tileArgs[0], tileArgs[1]);
            this.raceTrack[tileArgs[0]][tileArgs[1]] = tile;
            Point[] tilePath = tile.getPath();
            /* multiply the location of each point on the tile by the tiles column and row index
            * to properly position the point on the racetrack */
            for (Point point : tilePath) {
                point.translate(tile.getIndexPosCol() * GUI.TILE_SIZE, tile.getIndexPosRow() * GUI.TILE_SIZE);
            }
            /* convert tilePath Point array to a List so that it can be easily added
            * to the tracks list of Point's then add it to the tracks List<Point> path */
            path.addAll(Arrays.stream(tilePath).collect(Collectors.toList()));
        }
    }

    /**
     * Returns the next point on the path given an index value for the current path
     * @param index - Index of current point on path
     * @return - next point on path
     */
    public Point getNextPointOnPath(int index) {
        return path.get(index + 1);
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
        int posX = (int)((colIndex * 50) + startPoint.getX()); // TODO: 3/21/2023 Magic numbers could be replaced with constant variable.
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

    public List<Point> getPath() {
        return path;
    }
}

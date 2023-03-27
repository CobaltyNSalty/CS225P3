/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
3/13    [chris]     - implemented minimum field variables to create gui, used UML
                    - wrote basic constructors and a test method with hardcoded Tiles variable
                    - wrote loadTrackTiles() method
3/14    [chris]     - testing a total track path implementation with createPath() method
3/20    [Kat]       - wrote constructor for passing in LinkedList from importTrackFromFile function
3/21    [chris]     - updated documentation for data file constructor
3/22    [tre]       - change type of path field to List<Point>
                    - change tileArgs size to 4 to account for the path index of each track tile
                    - add method getPath() for getting the path field
3/24    [chris]     - added isCheckPoint boolean and related methods to identify Tiles that contain checkpoints
3/25    [chris]     - wrote reverse path algorithm
3/26    [Kat]       - wrote determineCheckpoint function to populate checkpoints with Point values of checkpoints,
                      and CheckpointCrossedIndex to return which checkpoint was crossed between two car positions, and
                      isCheckpointCrossed to do the math to actually determine if there was a crossing
3/26    [chris]     - Updated documentation and deleted obsolete methods
3/26    [Kat]       - Added checkPoint getter

 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  A Track object is used to load tile sprite images and construct a 2-D array of Tile objects
 *  as well as create a circular array of the (x,y) coordinates of the 'path' that the cars will
 *  follow as they race around the racetrack.
 *  Limitations: Tile array maximum size: width: 14, height: 10. Display window is fixed size so this is the
 *  maximum allowable grid dimensions.
 */
public class Track {
    /* ___ FIELD VARIABLES ___ */
    /* The background image of the racetrack, constructed via smaller images in a 2-D grid */
    private Tile[][] raceTrack;
    /* The Images used by 'raceTrack' to initialize Tiles */
    private Image[] trackTileSprites;
    /* The sequence of (x,y) coordinates that Car objects will follow to "stay on the road" */
    private List<Point> path;
    /* A series of (x,y) points along the 'path' of special importance to Car objects */
    private List<Point> checkpoints;

    /* ___ CONSTRUCTORS ___ */
    public Track() {
        this.raceTrack = null;
        this.trackTileSprites = null;
        this.path = new ArrayList<>();
        this.checkpoints = new ArrayList<>();
    }

    /**
     * First line of data file: [0] = height, [1] = width, [2] = 0(nothing)
     * All following lines: [0] = Row, [1] = Column, [2] = ImageID value
     * ImageID is the integer value at the beginning of the selected Tiles filename in the default tile set.
     * @param data - List of Strings imported from the data File
     */
    public Track(LinkedList<String> data) {
        this();
        /* Import default Tile set */
        loadTrackTiles();
        /* Convert data List to initialize 'raceTrack' and 'path' variables */
        initRaceTrackAndPath(data);
        /* Identify & init 'checkpoints' with the center point of the checkpoint-tiles in 'raceTrack' */
        determineCheckpoints();
    }

    /**
     * Convert text data from data List into Track dimensions and tile init arguments to initialize
     * 'raceTrack' and 'path' variables. Tiles in data file must be listed in a consecutive order
     * meaning each line must be a tile that borders the line above and below it.
     * @param data - Text from the data File as a List
     */
    private void initRaceTrackAndPath(LinkedList<String> data) {
        /* Holds the rowIndex, colIndex and ImageID of each tile from data file */
        int[] nextTileArgs = new int[3];

        /* Initialize 'raceTrack' dimensions */
        String[] tileArgsStringArray = (data.pop()).split(",");
        nextTileArgs[0] = Integer.parseInt(tileArgsStringArray[0]);
        nextTileArgs[1] = Integer.parseInt(tileArgsStringArray[1]);
        this.raceTrack = new Tile[nextTileArgs[0]][nextTileArgs[1]];

        /* Initialize all elements in raceTrack to a non-null Tile(0 - Empty Tile) */
        for (int row = 0; row < this.raceTrack.length; row++) {
            for (int col = 0; col < this.raceTrack[row].length; col++) {
                this.raceTrack[row][col] = new Tile(this.trackTileSprites[0]);
            }
        }

        /* Modify blank tiles in 'raceTrack' with new Tiles from data file */
        boolean firstTile = true;
        int lastX = 0;
        int lastY = 0;

        /* Read each line of tile arguments from data file */
        while (!(data.isEmpty())) {
            /* Convert text into integer arguments */
            tileArgsStringArray = (data.pop()).split(",");
            for (int x = 0; x < tileArgsStringArray.length; x++) { // [chris] swapped integer value for stringArray.length
                nextTileArgs[x] = Integer.parseInt(tileArgsStringArray[x]);
            }

            /* next and last values are used to determine if the default path direction of the Tile object
            * needs to be swapped. For example, vertical tile paths start at the top[0] and end at the bottom[49].
            * In order to create a continuous and consecutive series of points for the total path('path') as the
            * individual tile paths are combined, it may be necessary to add the tile path to the total path
            * from bottom to top. Therefore, its order must be reversed prior to being added to the 'path'. */
            if(firstTile) {
                // last tile = this tile
                lastY = nextTileArgs[0];
                lastX = nextTileArgs[1];
                firstTile = false;
            }
            int nextY = nextTileArgs[0];
            int nextX = nextTileArgs[1];

            /* Initialize tiles with the extracted tile arguments */
            boolean isCheckpoint = (nextTileArgs[2] == 7) || (nextTileArgs[2] == 8); // [chris] imageID 7 & 8 are checkpoints
            Tile tile = new Tile(this.trackTileSprites[nextTileArgs[2]], nextTileArgs[2], nextTileArgs[0], nextTileArgs[1], isCheckpoint);
            this.raceTrack[nextTileArgs[0]][nextTileArgs[1]] = tile;

            /* Swap individual tile path direction if necessary */
            Point[] tilePath;
            boolean reverse = determineReversePath(lastX, lastY, nextX, nextY, nextTileArgs[2]);
            if(reverse) {
                tilePath = tile.getReversePath();
            } else {
                tilePath = tile.getPath();
            }
            /* update 'last' tile to be the now complete 'next' tile */
            lastY = nextTileArgs[0];
            lastX = nextTileArgs[1];

            /* Convert the series of points along an individual tile to the global (x,y) coordinates
            * of the entire image of 'raceTrack'. */
            for (Point point : tilePath) { // [chris] +10 and +20, centers the sprite on the track.
                point.translate(((tile.getIndexPosCol() * GUI.TILE_SIZE)+10), ((tile.getIndexPosRow() * GUI.TILE_SIZE+20)));
            }
            /* convert 'tilePath' to a List so that it can be added to total 'path' */
            this.path.addAll(Arrays.stream(tilePath).collect(Collectors.toList()));
        }
    }

    /**
     * Calculate the direction moved from 'last' to 'next' {up, down, left, right} and use this
     * to determine if the first entry of the 'next' tiles path variable is the next point after
     * 'last' tiles last point. ie. last.path[49] = (100, 100) DOES next.path[0] = (100, 101)? or
     * (101, 100)? or is the default path for next not in the right order and instead next.path[49]
     * = (101, 100)?. In this case reverse the path.
     * @param lastX x coordinate of last tile
     * @param lastY y coordinate of last tile
     * @param nextX x coordinate of next tile
     * @param nextY y coordinate of next tile
     * @param imageType Each image type has a unique Point[] path variable create upon initialization
     * @return - If the default or the reverse tile path should be used.
     */
    private boolean determineReversePath(int lastX, int lastY, int nextX, int nextY, int imageType) {
        if(imageType == 7) { // vertical checkpoint
            imageType = 1;   // vertical tile
        }
        if(imageType == 8) { // horizontal checkpoint
            imageType = 2;   // horizontal tile
        }
        if        ((nextX - lastX ==   0) && (nextY - lastY ==  -1)) { // next tile is above last tile.
            // Tile types that could be the next tile:vertical, rightBot, leftBot
            // reverse all of these tiles
            return true;
        } else if ((nextX - lastX ==  -1) && (nextY - lastY ==   0)) {  // next tile is left of the last tile.
            // Tile types that could be the next tile: horizontal, rightTop, rightBot
            // if(horizontal tile) -> reverse, otherwise don't
            return (imageType == 2);
        } else if ((nextX - lastX ==   0) && (nextY - lastY ==   1)) {  // next tile is below the last tile.
            // Tile types that could be the next tile:vertical, left top, right top
            // if(vertical tile) -> dont reverse, otherwise do
            return (imageType != 1);
        } else if ((nextX - lastX ==   1) && (nextY - lastY ==   0)) {  // next tile is to the right of the last tile.
            // Tile types that could be the next tile:horizontal, left top, left bot
            // don't reverse any of these tiles
            return false;
        }
        return false;
    }

    /**
     * Returns the point on the path at the given index position
     * @param index - Index of current point on path
     * @return - Point at that index position
     */
    public Point getPointAtIndex(int index) {
        return path.get(index);
    }

    /**
     * Import default tile set for Track tiles
     */
    private void loadTrackTiles() {
        File imageDirectory = new File("Sprites\\TrackTiles");
        String[] imageNames = imageDirectory.list();
        if (imageNames == null) {
            return;
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

    }

    /**
     * Determine the coordinates of the checkpoints in 'raceTrack'
     */
    public void determineCheckpoints() {
        int rowIndex;
        int colIndex;
        Point checkpoint;
        int posX;
        int posY;

        /* Check every tile in 'raceTrack' for checkpoint tiles */
        for (Tile[] tA: raceTrack) {
            for (Tile t: tA) {
                if (t.isCheckpoint()) {
                    rowIndex = t.getIndexPosRow();
                    colIndex = t.getIndexPosCol();
                    posX = (colIndex * 50) + 25;
                    posY = (rowIndex * 50) + 25;
                    checkpoint = new Point(posX, posY);
                    checkpoints.add(checkpoint);
                }
            }
        }
    }

    /**
     * Function to return which checkpoint was crossed between two car positions, if any
     */
    public int CheckpointCrossedIndex(Point current, Point next) {
        for (int i = 0; i < checkpoints.size(); i++) {
            if(isCheckpointCrossed(current, next, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Function to return if a checkpoint was crossed between two car positions
     * @param current - car location
     * @param next - car location
     * @param i - checkpoint index
     * @return - if a checkpoint was crossed between the two positions
     */
    private boolean isCheckpointCrossed(Point current, Point next, int i) {
        int alpha = 50;
        if ((Math.abs(checkpoints.get(i).x - next.x) < alpha) && ((current.y < checkpoints.get(i).y) == (checkpoints.get(i).y <= next.y))) {
            return true;
        } else if ((Math.abs(checkpoints.get(i).y - next.y) < alpha) && ((current.x < checkpoints.get(i).x) == (checkpoints.get(i).x <= next.x))) {
            return true;
        } else {
            return false;
        }
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

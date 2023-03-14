import java.awt.*;

/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
3/13    [chris]     - Implemented minimum field variables to create gui, used UML
                    - added (Image image) constructor
3/14    [chris]     - wrote createPath() method

 */
public class Tile {
    /* ___ FIELD VARIABLES ___ */
    /* Display image */
    private Image sprite;
    /* Sequence of points from entry point to exit point that Car objects travel across this Tile */
    private Point[] path;
    /* A value to represent the specific type of Tile, correlates to TrackTile filename of image */
    private int tileIDNum;
    /* The row index of this Tile within the array housing it
       used to access neighboring Tiles in array */
    private int indexPosRow;
    /* The column index of this Tile within the array housing it
       used to access neighboring Tiles in array */
    private int indexPosCol;

    /* ___ CONSTRUCTORS ___ */
    public Tile(Image image) {
        this.sprite = image;
    }

    public Tile(Image image, int imageIndexValue) {
        this.sprite = image;
        this.tileIDNum = imageIndexValue; // integer value at start of image file name
        createPath(imageIndexValue);
    }

    public Tile(Image image, int imageIndexValue, int row, int col) {
        this.sprite = image;
        this.tileIDNum = imageIndexValue; // integer value at start of image file name
        this.indexPosRow = row;
        this.indexPosCol = col;
        createPath(imageIndexValue);
    }


    /* ___ FUNCTIONS ___ */
    /**
     * Create a sequence of (x,y) coordinates that represent the path of a Car object traversing
     * over this Tile. Path is based on the type of Tile which is indicated by the index value.
     * @param index - value correlating the TrackTile filename used to create this Tile's sprite.
     */
    private void createPath(int index) {
        path = new Point[50]; // TODO: needs testing
        if(index == 7) { index = 1; } // same tile but with checkpoint
        if(index == 8) { index = 2; } // same tile but with checkpoint
        switch(index) { // TODO: needs testing
            case 1: // 1 Straight vertical
                for(int i = 0; i < 50; i++) {
                    path[i] = new Point(25, i);
                }
                break;
            case 2: // 2 Straight horizontal
                for(int i = 0; i < 50; i++) {
                    path[i] = new Point(i, 25);
                }
                break;
            case 3: // 3 Corner between left edge and top edge
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point(i, 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (25-j));
                }
                break;
            case 4: // 4 Corner between left edge and bottom edge
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point(i, 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (j+25));
                }
                break;
            case 5: // 5 Corner between right edge and top edge
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point((50-i), 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (25-j));
                }
                break;
            case 6: // 6 Corner between right edge and bottom edge
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point((50-i), 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (j+25));
                }
                break;
            default: // empty, no path
                break;
        }
    }


    /* ___ ACCESSORS / MUTATORS ___ */
    public Image getSprite() {
        return sprite;
    }
    public int getTileIDNum() {
        return tileIDNum;
    }
    public Point[] getPath() {
        return path;
    }
    public int getIndexPosRow() {
        return indexPosRow;
    }
    public int getIndexPosCol() {
        return indexPosCol;
    }
}

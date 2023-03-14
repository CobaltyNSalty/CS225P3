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
    private Image sprite;
    private Point[] path;

    public Tile(Image image) {
        this.sprite = image;
    }

    public Tile(Image image, int imageIndexValue) {
        this.sprite = image;
        createPath(imageIndexValue);
    }

    private void createPath(int index) {
        path = new Point[50];
        if(index == 7) { index = 1; } // same tile but with checkpoint
        if(index == 8) { index = 2; } // same tile but with checkpoint
        switch(index) {
            case 1:
                for(int i = 0; i < 50; i++) {
                    path[i] = new Point(25, i);
                }
                break;
            case 2:
                for(int i = 0; i < 50; i++) {
                    path[i] = new Point(i, 25);
                }
                break;
            case 3:
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point(i, 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (25-j));
                }
                break;
            case 4:
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point(i, 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (j+25));
                }
                break;
            case 5:
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point((50-i), 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (25-j));
                }
                break;
            case 6:
                for(int i = 0; i < 25; i++) {
                    path[i] = new Point((50-i), 25);
                }
                for(int j = 0; j < 25; j++) {
                    path[(j+25)] = new Point(25, (j+25));
                }
                break;
            default:
                break;
        }
    }

    public Image getSprite() {
        return sprite;
    }
}

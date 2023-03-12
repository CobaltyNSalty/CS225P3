/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
                    - wrote play() method and added field variable declarations
                    - added default constructor


 */
public class Game {
    private Track raceTrack;
    private Car[] racers;
    private GUI gui;

    public Game() {
        this.raceTrack = null;
        this.racers = null;
        this.gui = null;
    }
    public void play() {
        Object[] gameAssets = new Object[] {this.raceTrack, this.racers};
        GUI gui = new GUI(gameAssets);

    }
}

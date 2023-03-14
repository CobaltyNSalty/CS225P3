import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* Work Log: add your name in brackets, the date, and a brief summary of what you contributed that day.
The assignment details that code you wrote requires a comment with your name above it. We will implement
that using this process. Log your work up here, and if you make a revision to existing code then add your
name in a comment on the same line to not interfere with other important documentation requirements.

3/11    [chris]     - Created class, added work log comment.
                    - added field variables, wrote constructors and createGUI method
                    - Worked on the gameWindow in createGUI method
3/13    [chris]     - worked on getting tiles to display in gameWindowPanel
3/14    [chris]     - added JLayeredPane and carPanel implementation.


 */
public class GUI {
    /* ___ FIELD VARIABLES ___ */
    /* Base frame for the application */
    private JFrame rootFrame;
    /* Content pane for rootFrame */
    private JPanel contentPanel;
    /* Start menu */
    private JPanel menuWindowPanel;
    /* Window to display game, track, cars, and movement */
    private JPanel gameWindowPanel;
    /* Objects used by 'Game' that have a graphical component */
    private Object[] gameAssets;

    /* ___ CONSTRUCTORS ___ */
    public GUI() {
        this.rootFrame = new JFrame();
        this.contentPanel = new JPanel();
        this.menuWindowPanel = new JPanel();
        this.gameWindowPanel = new JPanel();
        this.gameAssets = null;
    }

    /**
     * Objects used by 'Game' class that need to be displayed as part of the
     * game window.
     * @param assets - [0] Track, [1] Cars[]
     */
    public GUI(Object[] assets) {
        this();
        this.gameAssets = assets;
        createGUI();
    }

    /* ___ FUNCTIONS ___ */

    /**
     * Initialize and compose the graphical interface of the application
     */
    private void createGUI() {
        this.rootFrame.setContentPane(this.contentPanel);
        this.rootFrame.setPreferredSize(new Dimension(1000, 600));
        this.rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.rootFrame.setTitle("Track Star Racer! 9001");
        this.rootFrame.setResizable(false);

        // TODO: This likely needs to be moved somewhere else, import all images at same time
        BufferedImage checkeredTile;
        try {
            checkeredTile = ImageIO.read(new File("Sprites\\Checkered.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* _Game Window_ */
        this.gameWindowPanel.setLayout(new BorderLayout());

        // checkered flag side panels
        JPanel leftRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        leftRootPanel.setBackground(Color.BLACK);

        JLabel leftImageLabel = new JLabel();
        leftImageLabel.setIcon(new ImageIcon(checkeredTile));
        leftRootPanel.add(leftImageLabel);

        JPanel rightRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rightRootPanel.setPreferredSize(new Dimension(100, 600));
        rightRootPanel.setBackground(Color.BLACK);

        JLabel rightImageLabel = new JLabel();
        rightImageLabel.setIcon(new ImageIcon(checkeredTile));
        rightRootPanel.add(rightImageLabel);

        // Where the game will be displayed
        JLayeredPane centerPanel = new JLayeredPane();
        centerPanel.setBorder(new LineBorder(Color.BLACK));
        centerPanel.setPreferredSize(new Dimension(800, 600));

        JPanel blackBackgroundPanel = new JPanel();
        blackBackgroundPanel.setBackground(Color.BLACK);
        blackBackgroundPanel.setBounds(0, 0, 800, 600);

        // TODO: should gameAssets be unpackaged into field variables?
        Track raceTrack = ((Track)this.gameAssets[0]);

        JPanel gameTilePanel = new JPanel(new GridBagLayout());
        gameTilePanel.setBackground(new Color(67, 174, 32));
        gameTilePanel.setBounds(50, 25, 700, 500);
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < raceTrack.getRaceTrack().length; row++) {
            for (int col = 0; col < raceTrack.getRaceTrack()[0].length; col++) {
                constraints.gridx = col;
                constraints.gridy = row;
                JLabel trackTileLabel = new JLabel(new ImageIcon(raceTrack.getTileSpriteAt(row, col)));
                gameTilePanel.add(trackTileLabel, constraints);
            }
        }

        Car[] raceCars = ((Car[])this.gameAssets[1]);
        // TODO: Remove
        raceCars[0].setPosX(75);
        raceCars[0].setPosY(100);
        raceCars[1].setPosX(75);
        raceCars[1].setPosY(200);
        // TODO: end Remove

        JPanel carPanel = new JPanel();
        carPanel.setOpaque(false);
        carPanel.setBounds(50, 25, 700, 500);
        carPanel.setLayout(null);
        JLabel carLabel1 = new JLabel(new ImageIcon(raceCars[0].getSprite()));
        carLabel1.setBounds(raceCars[0].getPosX(), raceCars[0].getPosY(), 50, 50);
        JLabel carLabel2 = new JLabel(new ImageIcon(raceCars[1].getSprite()));
        carLabel2.setBounds(raceCars[1].getPosX(), raceCars[1].getPosY(), 50, 50);
        carPanel.add(carLabel1);
        carPanel.add(carLabel2);

        centerPanel.add(blackBackgroundPanel, new Integer(0));
        centerPanel.add(gameTilePanel, new Integer(1));
        centerPanel.add(carPanel, new Integer(2));


        this.gameWindowPanel.add(leftRootPanel, BorderLayout.WEST);
        this.gameWindowPanel.add(centerPanel, BorderLayout.CENTER);
        this.gameWindowPanel.add(rightRootPanel, BorderLayout.EAST);

        // TODO: this will initially be the menuWindow not the gameWindowPanel
        this.contentPanel.add(this.gameWindowPanel);

        this.rootFrame.pack();
        this.rootFrame.setVisible(true);

    }

    /* ___ ACCESSORS / MUTATORS ___ */

}

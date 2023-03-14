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
        leftRootPanel.setBackground(Color.GRAY);

        JLabel leftImageLabel = new JLabel();
        leftImageLabel.setIcon(new ImageIcon(checkeredTile));
        leftRootPanel.add(leftImageLabel);

        JPanel rightRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rightRootPanel.setPreferredSize(new Dimension(100, 600));
        rightRootPanel.setBackground(Color.GRAY);

        JLabel rightImageLabel = new JLabel();
        rightImageLabel.setIcon(new ImageIcon(checkeredTile));
        rightRootPanel.add(rightImageLabel);

        // Where the game will be displayed
        JPanel centerRootPanel = new JPanel(new FlowLayout());
        centerRootPanel.setBorder(new LineBorder(Color.BLACK));
        centerRootPanel.setPreferredSize(new Dimension(800, 600));
        centerRootPanel.setBackground(Color.BLACK);

        // TODO: should gameAssets be unpackaged into field variables?
        Track raceTrack = ((Track)this.gameAssets[0]);

        JPanel gameTilePanel = new JPanel(new GridBagLayout());
        gameTilePanel.setBackground(new Color(67, 174, 32));
        gameTilePanel.setPreferredSize(new Dimension(700, 500));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        for (int row = 0; row < raceTrack.getRaceTrack().length; row++) {
            for (int col = 0; col < raceTrack.getRaceTrack()[0].length; col++) {
                constraints.gridx = col;
                constraints.gridy = row;
                JLabel trackTileLabel = new JLabel(new ImageIcon(raceTrack.getTileSpriteAt(row, col)));
                gameTilePanel.add(trackTileLabel, constraints);
            }
        }

        JPanel bufferPanel = new JPanel();
        bufferPanel.setPreferredSize(new Dimension(600, 15));
        bufferPanel.setBackground(Color.BLACK);

        centerRootPanel.add(bufferPanel);
        centerRootPanel.add(gameTilePanel);

        this.gameWindowPanel.add(leftRootPanel, BorderLayout.WEST);
        this.gameWindowPanel.add(centerRootPanel, BorderLayout.CENTER);
        this.gameWindowPanel.add(rightRootPanel, BorderLayout.EAST);

        // TODO: this will initially be the menuWindow not the gameWindowPanel
        this.contentPanel.add(this.gameWindowPanel);

        this.rootFrame.pack();
        this.rootFrame.setVisible(true);

    }

    /* ___ ACCESSORS / MUTATORS ___ */
    public JFrame getApplicationWindowFrame() {
        return this.rootFrame;
    }


}

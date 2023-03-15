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
3/15    [chris]     - tested how to get cars to move, no dedicated path. Got it working
                    - added empty bottom panel to display information about racers to user


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

    private Track gameTrack;
    private Car[] gameCars;
    /* Testing if Cars should extend JLabel */ // TODO: Remove
    private JLabel[] carLabels;

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
        this.gameTrack = ((Track)this.gameAssets[0]);
        this.gameCars = ((Car[])this.gameAssets[1]);
        createGUI();
    }

    /* ___ FUNCTIONS ___ */

    /**
     * Initialize and compose the graphical interface of the application
     */
    private void createGUI() {
        this.rootFrame.setContentPane(this.contentPanel);
        this.rootFrame.setPreferredSize(new Dimension(1000, 700));
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
        JPanel topGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topGamePanel.setBorder(new LineBorder(Color.BLACK));
        topGamePanel.setBackground(Color.BLACK);
        JPanel bottomGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomGamePanel.setBounds(0, 0, 700, 200);

        // checkered flag side panels
        JPanel leftRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        leftRootPanel.setBackground(Color.BLACK);

        JLabel leftImageLabel = new JLabel();
        leftImageLabel.setIcon(new ImageIcon(checkeredTile));
        leftRootPanel.add(leftImageLabel);

        JPanel rightRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rightRootPanel.setBackground(Color.BLACK);

        JLabel rightImageLabel = new JLabel();
        rightImageLabel.setIcon(new ImageIcon(checkeredTile));
        rightRootPanel.add(rightImageLabel);

        // Where the game will be displayed
        JLayeredPane centerPanel = new JLayeredPane();
        centerPanel.setPreferredSize(new Dimension(800, 500));

        // Panel to house the racetrack Tile sprites
        JPanel gameTilePanel = new JPanel(new GridBagLayout());
        gameTilePanel.setBackground(new Color(67, 174, 32));
        gameTilePanel.setBounds(50, 0, 700, 500);
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < this.gameTrack.getRaceTrack().length; row++) {
            for (int col = 0; col < this.gameTrack.getRaceTrack()[0].length; col++) {
                constraints.gridx = col;
                constraints.gridy = row;
                JLabel trackTileLabel = new JLabel(new ImageIcon(this.gameTrack.getTileSpriteAt(row, col)));
                gameTilePanel.add(trackTileLabel, constraints);
            }
        }

        // TODO: Remove, initializing car starting position
        this.gameCars[0].setPosX(75);
        this.gameCars[0].setPosY(100);
        this.gameCars[1].setPosX(75);
        this.gameCars[1].setPosY(200);
        this.carLabels = new JLabel[2];
        // TODO: end Remove

        // transparent panel for cars to move across using (x,y) coordinate values, cars are drawn over
        // the racetrack sprites.
        JPanel carPanel = new JPanel();
        carPanel.setOpaque(false);
        carPanel.setBounds(50, 0, 700, 500);
        carPanel.setLayout(null);
        JLabel carLabel1 = new JLabel(new ImageIcon(this.gameCars[0].getSprite()));
        carLabel1.setBounds(this.gameCars[0].getPosX(), this.gameCars[0].getPosY(), 50, 50);
        carLabel1.setDoubleBuffered(true);
        JLabel carLabel2 = new JLabel(new ImageIcon(this.gameCars[1].getSprite()));
        carLabel2.setBounds(this.gameCars[1].getPosX(), this.gameCars[1].getPosY(), 50, 50);
        carLabel2.setDoubleBuffered(true);
        this.carLabels[0] = carLabel1;
        this.carLabels[1] = carLabel2;
        carPanel.add(carLabel1);
        carPanel.add(carLabel2);

        // Compose gameplay area
        centerPanel.add(gameTilePanel, new Integer(1));
        centerPanel.add(carPanel, new Integer(2));

        // info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(1000, 200));
        infoPanel.setBackground(Color.PINK);
        infoPanel.setBorder(new LineBorder(Color.RED));


        // Compose overall game window
        topGamePanel.add(leftRootPanel, BorderLayout.WEST);
        topGamePanel.add(centerPanel, BorderLayout.CENTER);
        topGamePanel.add(rightRootPanel, BorderLayout.EAST);

        bottomGamePanel.add(infoPanel);

        this.gameWindowPanel.add(topGamePanel, BorderLayout.NORTH);
        this.gameWindowPanel.add(bottomGamePanel, BorderLayout.SOUTH);

        // TODO: this will initially be the menuWindow not the gameWindowPanel
        this.contentPanel.add(this.gameWindowPanel);

        this.rootFrame.pack();
        this.rootFrame.setVisible(true);
    }

    public void drawNewCarPositions() {
        for(int i = 0; i < 2; i++) {
            this.carLabels[i].setBounds(this.gameCars[i].getPosX(), this.gameCars[i].getPosY(), 50, 50);
        }
    }

    /* ___ ACCESSORS / MUTATORS ___ */

}

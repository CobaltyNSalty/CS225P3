import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                    - rearranged code by adding createWindow methods to simplify createGUI()
3/20    [Kat]       - added Car display panels to bottom of UI showing current position

 */
public class GUI implements ActionListener{
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
    private JPanel carCreationWindowPanel;
    private JPanel trackCreationWindowPanel;
    private JPanel startGameOptionsWindowPanel;
    private Object[] gameAssets;

    private Image[] images;
    private Track gameTrack;
    private Car[] gameCars;
    private ActionListener listener;

    JLabel[][] carPanelSpeedLabels;
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
     * @param assets - [0] Track, [1] Cars[], [2] ActionListener
     */
    public GUI(Object[] assets) {
        this();
        this.gameAssets = assets;
        this.gameTrack = ((Track)this.gameAssets[0]);
        this.gameCars = ((Car[])this.gameAssets[1]);
        this.listener = ((ActionListener)this.gameAssets[2]);
        createGUI();
    }

    /* ___ FUNCTIONS ___ */

    /**
     * Initialize and compose the graphical interface of the application
     */
    private void createGUI() {
        //this.rootFrame.setContentPane(this.contentPanel);
        this.rootFrame.setPreferredSize(new Dimension(1000, 700));
        this.rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.rootFrame.setTitle("Track Star Racer! 9001");
        this.rootFrame.setResizable(false);

        loadImages();
        createMenuWindow();
        createGameWindow();

        // TODO: this will initially be the menuWindow not the gameWindowPanel
        //this.rootFrame.setContentPane(this.menuWindowPanel);
        this.rootFrame.setContentPane(this.gameWindowPanel);

        this.rootFrame.pack();
        this.rootFrame.setVisible(true);
    }

    private void loadImages() {
        this.images = new Image[2];
        try {
            this.images[0] = ImageIO.read(new File("Sprites\\Checkered.png"));
            this.images[1] = ImageIO.read(new File("Sprites\\MenuImage.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void swapWindow(JPanel newWindow) {
        //this.contentPanel.add(newWindow);
        //this.contentPanel.remove(0);
        this.rootFrame.setContentPane(newWindow);
        this.rootFrame.pack();
        this.rootFrame.revalidate();
        this.rootFrame.repaint();
        this.rootFrame.setVisible(true);
    }
    private void createTrackCreationWindow() {

    }
    private void createCarCreationWindow() {

    }
    private void createMenuWindow() {
        this.menuWindowPanel.setPreferredSize(new Dimension(1000, 700));
        this.menuWindowPanel.setLayout(new BorderLayout());

        JLayeredPane menuRootPane = new JLayeredPane();

        // Background image pane
        JLabel backgroundImage = new JLabel(new ImageIcon(this.images[1]));
        JPanel backgroundImagePane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        backgroundImagePane.setBounds(0, 0, 1000, 700);
        backgroundImagePane.setOpaque(false);
        backgroundImagePane.add(backgroundImage);

        // button pane
        JPanel buttonPane = new JPanel(new GridBagLayout());
        buttonPane.setBounds(0, 0, 1000, 700);
        buttonPane.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15, 0, 15, 0);

        JButton startButton = new JButton("Start New Game");
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension(400, 40));
        constraints.gridy = 0;
        buttonPane.add(startButton, constraints);

        JButton createCarButton = new JButton("Design a Car");
        createCarButton.addActionListener(this);
        createCarButton.setPreferredSize(new Dimension(400, 40));
        constraints.gridy = 1;
        buttonPane.add(createCarButton, constraints);

        JButton createTrackButton = new JButton("Create a Track");
        createTrackButton.addActionListener(this);
        createTrackButton.setPreferredSize(new Dimension(400, 40));

        constraints.gridy = 2;
        buttonPane.add(createTrackButton, constraints);

        menuRootPane.add(backgroundImagePane, new Integer(0));
        menuRootPane.add(buttonPane, new Integer(1));
        this.menuWindowPanel.add(menuRootPane, BorderLayout.CENTER);
    }
    private void createGameWindow() {
        /* _Game Window_ */
        this.gameWindowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.gameWindowPanel.setPreferredSize(new Dimension(1000,700));

        JPanel topGamePanel = new JPanel();
        topGamePanel.setLayout(new BoxLayout(topGamePanel, BoxLayout.X_AXIS));
        topGamePanel.setBackground(Color.BLACK);
        JPanel bottomGamePanel = createGameWindowInfoPanel();

        // checkered flag side panels
        JPanel leftRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        leftRootPanel.setBounds(0, 0, 100, 500);
        leftRootPanel.setBackground(Color.BLACK);

        JLabel leftImageLabel = new JLabel();
        leftImageLabel.setIcon(new ImageIcon(this.images[0]));
        leftRootPanel.add(leftImageLabel);

        JPanel rightRootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rightRootPanel.setBackground(Color.BLACK);

        JLabel rightImageLabel = new JLabel();
        rightImageLabel.setIcon(new ImageIcon(this.images[0]));
        rightRootPanel.add(rightImageLabel);

        // Where the game will be displayed
        JLayeredPane centerPanel = new JLayeredPane();
        centerPanel.setPreferredSize(new Dimension(800, 500));

        // Panel to house the racetrack Tile sprites
        JPanel gameTilePanel = new JPanel(new GridBagLayout());
        gameTilePanel.setBackground(new Color(67, 174, 32));
        gameTilePanel.setBounds(50, 20, 700, 500);
        GridBagConstraints constraints2 = new GridBagConstraints();
        for (int row = 0; row < this.gameTrack.getRaceTrack().length; row++) {
            for (int col = 0; col < this.gameTrack.getRaceTrack()[0].length; col++) {
                constraints2.gridx = col;
                constraints2.gridy = row;
                // JLabel trackTileLabel = new JLabel(new ImageIcon(this.gameTrack.getTileSpriteAt(row, col)));
                gameTilePanel.add(this.gameTrack.getTileAtPoint(row, col), constraints2);
            }
        }

        // transparent panel for cars to move across using (x,y) coordinate values, cars are drawn over
        // the racetrack sprites.
        JPanel carPanel = new JPanel();
        carPanel.setOpaque(false);
        carPanel.setBounds(50, 0, 700, 500);
        carPanel.setLayout(null);
        // TODO: add cars

        // Compose gameplay area
        centerPanel.add(gameTilePanel, new Integer(1));
        centerPanel.add(carPanel, new Integer(2));

        // car specific panels in info panel
        // TODO: make dynamic
        JPanel[] carPanels = new JPanel[2];
        JLabel[] carPanelTitles = new JLabel[2];
        carPanelSpeedLabels = new JLabel[2][2];

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(10,10,10,10);
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;

        for (int i = 0; i < 2; i++) {
            carPanels[i] = new JPanel(new GridBagLayout());
            carPanels[i].setBorder(new LineBorder(Color.RED));
            carPanels[i].setPreferredSize(new Dimension(250, 100));
            carPanelTitles[i] = new JLabel("Car " + (i + 1));
            carPanelSpeedLabels[0][i] = new JLabel("50");
            //carPanelTextFields[0][i].setColumns(10);

            carPanelSpeedLabels[0][i].setPreferredSize(new Dimension(50, 50));
            carPanelSpeedLabels[1][i] = new JLabel("" + gameCars[i].getPosition().y);

            layoutConstraints.gridy = 0;
            layoutConstraints.gridx = 0;
            layoutConstraints.gridwidth = 2;
            carPanels[i].add(carPanelTitles[i], layoutConstraints);

            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridy = 1;
            carPanels[i].add(new JLabel("X position:"), layoutConstraints);

            layoutConstraints.gridx = 1;
            carPanels[i].add( carPanelSpeedLabels[0][i], layoutConstraints);

            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 2;
            carPanels[i].add(new JLabel("Y position:"), layoutConstraints);

            layoutConstraints.gridx = 1;
            carPanels[i].add( carPanelSpeedLabels[1][i], layoutConstraints);

            // BottomGamePanel created in createGameWindowInfoPanel() and has a JPanel added to it
            ((JPanel)bottomGamePanel.getComponent(0)).add(carPanels[i]);
        }

        // Compose overall game window
        topGamePanel.add(leftRootPanel);
        topGamePanel.add(centerPanel);
        topGamePanel.add(rightRootPanel);

        this.gameWindowPanel.add(topGamePanel);
        this.gameWindowPanel.add(bottomGamePanel);
    }

    private JPanel createGameWindowInfoPanel() {
        JPanel bottomGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        // info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 4));
        infoPanel.setPreferredSize(new Dimension(1000, 200));
        infoPanel.setBackground(Color.PINK); // REMOVE
        infoPanel.setBorder(new LineBorder(Color.RED)); // REMOVE

        bottomGamePanel.add(infoPanel);

        return bottomGamePanel;
    }

    public void drawNewCarPositions() {
        for(int i = 0; i < 2; i++) {
            this.gameCars[i].setBounds(this.gameCars[i].getPosition().x, this.gameCars[i].getPosition().y, 50, 50);
            carPanelSpeedLabels[0][i].setText("" + gameCars[i].getPosition().x);
            carPanelSpeedLabels[1][i].setText("" + gameCars[i].getPosition().y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = ((JButton)e.getSource()).getText();
        switch(text) {
            case "Start New Game":
                //swapWindow(this.startGameOptionsWindowPanel);
                swapWindow(this.gameWindowPanel);
                break;
            case "Design a Car":
                swapWindow(this.carCreationWindowPanel);
                break;
            case "Create a Track":
                swapWindow(this.trackCreationWindowPanel);
                break;
            case "Start Game":
                swapWindow(this.gameWindowPanel);
                break;
            default:
                swapWindow(this.menuWindowPanel);
                break;
        }



    }

    /* ___ ACCESSORS / MUTATORS ___ */

}

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Object gameClass;

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
        this.gameClass = this.gameAssets[2];
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
        createGameOptionsWindow();
        createGameWindow();

        // TODO: this will initially be the menuWindow not the gameWindowPanel
        // this.rootFrame.setContentPane(this.menuWindowPanel);
        // this.rootFrame.setContentPane(this.gameWindowPanel);
        this.rootFrame.setContentPane(this.startGameOptionsWindowPanel);

        this.rootFrame.pack();
        this.rootFrame.setVisible(true);
    }

    private void loadImages() {
        this.images = new Image[12];
        try {
            this.images[0] = ImageIO.read(new File("Sprites\\Checkered.png"));
            this.images[1] = ImageIO.read(new File("Sprites\\MenuImage.png"));
            this.images[2] = ImageIO.read(new File("Sprites\\carSprites\\blueCar.png"));
            this.images[3] = ImageIO.read(new File("Sprites\\carSprites\\greenCar.png"));
            this.images[4] = ImageIO.read(new File("Sprites\\carSprites\\orangeCar.png"));
            this.images[5] = ImageIO.read(new File("Sprites\\carSprites\\purpleCar.png"));
            this.images[6] = ImageIO.read(new File("Sprites\\carSprites\\redCar.png"));
            this.images[7] = ImageIO.read(new File("Sprites\\carSprites\\yellowCar.png"));
            this.images[8] = ImageIO.read(new File("Sprites\\TrackIcons\\Track1Icon.png"));
            this.images[9] = ImageIO.read(new File("Sprites\\TrackIcons\\Track2Icon.png"));
            this.images[10] = ImageIO.read(new File("Sprites\\TrackIcons\\Track3Icon.png"));
            this.images[11] = ImageIO.read(new File("Sprites\\TrackIcons\\Track4Icon.png"));

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
    private void createGameOptionsWindow() {
        // TODO: disable continue until at least 2 cars and 1 track are selected
        // TODO: Add information to center
        // TODO: add center panel
        // TODO: this.gameClass is the Game object that created this GUI, use it to call importTrack and update game Asset Track

        this.startGameOptionsWindowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.startGameOptionsWindowPanel.setPreferredSize(new Dimension(1000, 700));

        JPanel optionsRootPanel = new JPanel(new BorderLayout());
        optionsRootPanel.setPreferredSize(new Dimension(1000, 700));
        optionsRootPanel.setBackground(Color.BLUE);

        JPanel topPanel = new JPanel();     // feedback
        topPanel.setPreferredSize(new Dimension(1000, 100));

        JPanel leftPanel = new JPanel(new GridLayout(3, 2, 10, 10));    // display cars
        leftPanel.setPreferredSize(new Dimension(300, 500));
        leftPanel.setBorder(new LineBorder(Color.green));

        JPanel rightPanel = new JPanel(new GridLayout(2, 2, 10, 10));   // display tracks
        rightPanel.setPreferredSize(new Dimension(350, 500));
        rightPanel.setBorder(new LineBorder(Color.RED));

        JPanel bottomPanel = new JPanel();  // buttons
        bottomPanel.setPreferredSize(new Dimension(1000, 100));

        // bottom panel components and settings
        JButton startButton = new JButton("Continue");
        startButton.setPreferredSize(new Dimension(200, 40));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(this);
        bottomPanel.add(startButton);

        // right panel components and settings
        JButton track1Btn = makeTrackOptionButton(1);
        JButton track2Btn = makeTrackOptionButton(2);
        JButton track3Btn = makeTrackOptionButton(3);
        JButton track4Btn = makeTrackOptionButton(4);
        rightPanel.add(track1Btn);
        rightPanel.add(track2Btn);
        rightPanel.add(track3Btn);
        rightPanel.add(track4Btn);

        // Left panel components and settings
        JButton car1Btn = makeCarOptionsButton(1);
        JButton car2Btn = makeCarOptionsButton(2);
        JButton car3Btn = makeCarOptionsButton(3);
        JButton car4Btn = makeCarOptionsButton(4);
        JButton car5Btn = makeCarOptionsButton(5);
        JButton car6Btn = makeCarOptionsButton(6);
        leftPanel.add(car1Btn);
        leftPanel.add(car2Btn);
        leftPanel.add(car3Btn);
        leftPanel.add(car4Btn);
        leftPanel.add(car5Btn);
        leftPanel.add(car6Btn);

        // Compose Window elements
        optionsRootPanel.add(topPanel, BorderLayout.NORTH);
        optionsRootPanel.add(leftPanel, BorderLayout.WEST);
        optionsRootPanel.add(rightPanel, BorderLayout.EAST);
        optionsRootPanel.add(bottomPanel, BorderLayout.SOUTH);
        this.startGameOptionsWindowPanel.add(optionsRootPanel);
    }
    private JButton makeCarOptionsButton(int index) {
        JButton button = new JButton("Car " + index);
        button.setDoubleBuffered(true);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setIcon(new ImageIcon(this.images[(index + 1)]));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.addActionListener(this);
        return button;
    }
    private JButton makeTrackOptionButton(int index) {
        JButton button = new JButton("Track " + index);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setIcon(new ImageIcon(this.images[(index + 7)]));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.addActionListener(this);
        return button;
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

    private void cycleButtonHighlight(JButton button) {
        Color color = new Color(92, 255, 63, 126);
        if(button.getBackground().equals(color)) {
            button.setBackground(null);
        } else {
            button.setBackground(color);
        }
    }
    private void singleSelectionOfButtons(JButton button) {
        for(Component c : button.getParent().getComponents()) {
            ((JButton)c).setBackground(null);
        }
        button.setBackground(new Color(92, 255, 63, 126));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = ((JButton)e.getSource());
        String text = pressed.getText();
        switch(text) {
            case "Start New Game":
                 swapWindow(this.gameWindowPanel);
                // swapWindow(this.startGameOptionsWindowPanel);
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
            case "Car 1":
                cycleButtonHighlight(pressed);
                break;
            case "Car 2":
                cycleButtonHighlight(pressed);
                break;
            case "Car 3":
                cycleButtonHighlight(pressed);
                break;
            case "Car 4":
                cycleButtonHighlight(pressed);
                break;
            case "Car 5":
                cycleButtonHighlight(pressed);
                break;
            case "Car 6":
                cycleButtonHighlight(pressed);
                break;
            case "Track 1":
                singleSelectionOfButtons(pressed);
                break;
            case "Track 2":
                singleSelectionOfButtons(pressed);
                break;
            case "Track 3":
                singleSelectionOfButtons(pressed);
                break;
            case "Track 4":
                singleSelectionOfButtons(pressed);
                break;
            default:
                swapWindow(this.menuWindowPanel);
                break;
        }



    }

    /* ___ ACCESSORS / MUTATORS ___ */

}

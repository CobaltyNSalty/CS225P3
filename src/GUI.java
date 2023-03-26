import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
3/17    [Joey]      - added updateTimer() method
3/20    [Joey]      - Updated updateTimer() to keep time better
3/20    [Kat]       - added Car display panels to bottom of UI showing current position - only shows 2 right now
3/21    [tre]       - Replace tile width and tile height with constants.
3/22    [tre]       - replace TILE_WIDTH and TILE_HEIGHT constants with TILE_SIZE
                    - add cars to carPanel
                    - replace magic numbers of value 50 with TILE_SIZE constant
3/22    [chris]     - wrote a couple methods to extract game options from selection window
                    - then trigger the Game class to create the new game window and display it.
3/23    [chris]     - added controlFunctions support
3/25    [Kat]       - reconfigured carPanels to display appropriate number of cars
3/25    [chris]     - added turning methods to swap sprites based on direction
3/25    [Kat]       - changed carPanel to pull name of car for identification, also changed to displaying speed and
                      last checkpoint Passed
3/26    [Kat]       - changed carPanel to show speed (still), checkpoint list, and checkpoint Index


 */
public class GUI implements ActionListener{
    // TODO: 3/21/2023 TILE_SIZE may need to be moved to another file.
    /**
     * The width of game tiles in pixels.
     */
    public static final int TILE_SIZE = 50;

    /* ___ FIELD VARIABLES ___ */
    /* Base frame for the application */
    private JFrame rootFrame;
    /* Start menu */
    private JPanel menuWindowPanel;
    /* Window to display game, track, cars, and movement */
    private JPanel gameWindowPanel;
    private JPanel startGameOptionsWindowPanel;
    /* Objects used by 'Game' that have a graphical component */
    private Object[] gameAssets;
    private Image[] images;
    private Track gameTrack;
    private Car[] gameCars;
    private JLabel[][] carPanelLabels;
    private JLabel timeLabel;
    private Object[] controls;
    private JLayeredPane centerPanel;
    private int carsSelected;
    private int trackSelected;

    /* ___ CONSTRUCTORS ___ */
    public GUI(Object[] controls) {
        this.rootFrame = new JFrame();
        this.menuWindowPanel = new JPanel();
        this.gameWindowPanel = new JPanel();
        this.gameAssets = null;
        this.controls = controls;
        this.carsSelected = 0;
        this.trackSelected = 0;
        createGUI();
    }

    /* ___ FUNCTIONS ___ */
    /**
     * initialize and create the portions of the gui necessary to start the application and
     * launch the start window(menuWindowPanel).
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

        this.rootFrame.setContentPane(this.menuWindowPanel); // starting window
        this.rootFrame.pack();
        this.rootFrame.setVisible(true);
    }
    /* Window Creation Methods */
    private void createGameOptionsWindow() {
        // TODO: disable continue until at least 2 cars and 1 track are selected
        // TODO: Add information to center

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
        JButton startButton = (JButton) this.controls[0];
        startButton.setText("Continue");
        startButton.setPreferredSize(new Dimension(200, 40));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        bottomPanel.add(startButton);

        // TODO: track button text must match track file name not including .csv, this is added later.
        // right panel components and settings
        JButton track1Btn = makeTrackOptionButton(1);
        JButton track2Btn = makeTrackOptionButton(2);
        track2Btn.setEnabled(false);
        JButton track3Btn = makeTrackOptionButton(3);
        track3Btn.setEnabled(false);
        JButton track4Btn = makeTrackOptionButton(4);
        track4Btn.setEnabled(false);
        rightPanel.add(track1Btn);
        rightPanel.add(track2Btn);
        rightPanel.add(track3Btn);
        rightPanel.add(track4Btn);

        // Left panel components and settings
        JButton car1Btn = makeCarOptionsButton(2);
        car1Btn.setText("blue");
        JButton car2Btn = makeCarOptionsButton(6);
        car2Btn.setText("green");
        JButton car3Btn = makeCarOptionsButton(10);
        car3Btn.setText("orange");
        JButton car4Btn = makeCarOptionsButton(14);
        car4Btn.setText("purple");
        JButton car5Btn = makeCarOptionsButton(18);
        car5Btn.setText("red");
        JButton car6Btn = makeCarOptionsButton(22);
        car6Btn.setText("yellow");
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
        JPanel bottomGamePanel = createGameWindowInfoPanel(this.gameCars.length);

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
        this.centerPanel = new JLayeredPane();
        this.centerPanel.setPreferredSize(new Dimension(800, 500));

        // Panel to house the racetrack Tile sprites
        JPanel gameTilePanel = new JPanel(new GridBagLayout());
        gameTilePanel.setBackground(new Color(67, 174, 32));
        gameTilePanel.setBounds(50, 20, 700, 500);
        GridBagConstraints constraints2 = new GridBagConstraints();
        for (int row = 0; row < this.gameTrack.getRaceTrack().length; row++) {
            for (int col = 0; col < this.gameTrack.getRaceTrack()[0].length; col++) {
                constraints2.gridx = col;
                constraints2.gridy = row;
                gameTilePanel.add(this.gameTrack.getTileAtPoint(row, col), constraints2);
            }
        }

        /*  transparent panel for cars to move across using (x,y) coordinate values, cars are drawn over
         *  the racetrack sprites.
         */
        JPanel carPanel = new JPanel();
        carPanel.setOpaque(false);
        carPanel.setBounds(50, 0, 700, 500);
        carPanel.setLayout(null);

        for(Car car : this.gameCars) {
            // TODO: set starting positions for each car to be random
            Random rng = new Random();

            car.incrementCurrentIndexOnTrackPointPath(rng.nextInt(this.gameTrack.getPath().size()));
            carPanel.add(car);
        }
        // Compose gameplay area
        this.centerPanel.add(gameTilePanel, new Integer(1));
        this.centerPanel.add(carPanel, new Integer(2));

        // feedback panel for race timer
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setPreferredSize(new Dimension(250, 100));
        this.timeLabel = new JLabel("00:00:00");
        this.timeLabel.setBounds(0,5,100,50);
        this.timeLabel.setFont(new Font("Helvetica", Font.PLAIN,25 ));
        feedbackPanel.add(this.timeLabel);
        ((JPanel)bottomGamePanel.getComponent(0)).add(feedbackPanel);

        // car specific panels in info panel
        // TODO: make dynamic - this needs to display the number of cars not a fixed number, i.e. 2
        JPanel[] carInfoPanels = new JPanel[gameCars.length];
        JLabel[] carNameLabels = new JLabel[gameCars.length];
        this.carPanelLabels = new JLabel[gameCars.length][3]; // [# of cars][ x and y values ]

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        //layoutConstraints.insets = new Insets(0,0,0,0);
        layoutConstraints.weightx = 1.0;
        layoutConstraints.weighty = 1.0;

        for (int i = 0; i < gameCars.length; i++) {
            carInfoPanels[i] = new JPanel(new GridBagLayout());
            carInfoPanels[i].setBorder(new LineBorder(Color.green));
            //carInfoPanels[i].setPreferredSize(new Dimension(250, 100));

            carNameLabels[i] = new JLabel(gameCars[i].getName());
            this.carPanelLabels[i][0] = new JLabel();
            this.carPanelLabels[i][0].setBorder(new LineBorder(Color.red));
            //this.carPanelSpeedLabels[i][0].setPreferredSize(new Dimension(50, 50));
            this.carPanelLabels[i][1] = new JLabel();
            this.carPanelLabels[i][1].setBorder(new LineBorder(Color.MAGENTA));
            this.carPanelLabels[i][2]= new JLabel();

            layoutConstraints.gridy = 0;
            layoutConstraints.gridx = 0;
            layoutConstraints.gridwidth = 2;
            carInfoPanels[i].add(carNameLabels[i], layoutConstraints);

            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridy = 1;

            JLabel speedLabel = new JLabel("Current Speed: ");
            speedLabel.setBorder(new LineBorder(Color.BLUE));
            carInfoPanels[i].add(speedLabel, layoutConstraints);

            layoutConstraints.gridx = 1;
            carInfoPanels[i].add(carPanelLabels[i][0], layoutConstraints);

            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 2;
            JLabel pathLabel = new JLabel("Checkpoint Path: ");
            pathLabel.setBorder(new LineBorder(Color.YELLOW));
            carInfoPanels[i].add(pathLabel, layoutConstraints);

            layoutConstraints.gridx = 1;
            carInfoPanels[i].add(carPanelLabels[i][1], layoutConstraints);

            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 3;
            JLabel indexLabel = new JLabel("Checkpoint Index: ");
            indexLabel.setBorder(new LineBorder(Color.MAGENTA));
            carInfoPanels[i].add(indexLabel, layoutConstraints);

            layoutConstraints.gridx = 1;
            carInfoPanels[i].add(carPanelLabels[i][2], layoutConstraints);


            // BottomGamePanel created in createGameWindowInfoPanel() and has a JPanel added to it
            ((JPanel)bottomGamePanel.getComponent(0)).add(carInfoPanels[i]);
        }

        // Compose overall game window
        topGamePanel.add(leftRootPanel);
        topGamePanel.add(this.centerPanel);
        topGamePanel.add(rightRootPanel);

        this.gameWindowPanel.add(topGamePanel);
        this.gameWindowPanel.add(bottomGamePanel);
    }

    /* Window creation helper methods */
    private JButton makeCarOptionsButton(int index) {
        JButton button = new JButton();
        button.setDoubleBuffered(true);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setIcon(new ImageIcon(this.images[(index + 1)])); // TODO: UPDATE to include sprite rotation and Tile orientation
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.addActionListener(this);
        return button;
    }
    private JButton makeTrackOptionButton(int index) {
        JButton button = new JButton("Track" + index);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setIcon(new ImageIcon(this.images[(index + 25)]));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.addActionListener(this);
        return button;
    }
    private void cycleButtonHighlight(JButton button) {
        Color color = new Color(92, 255, 63, 126);
        if((this.carsSelected >= 3) && (!(button.getBackground().equals(color)))) {
            return;
        }
        if (button.getBackground().equals(color)) {
            button.setBackground(null);
            button.setSelected(false);
            this.carsSelected--;
        } else {
            button.setBackground(color);
            button.setSelected(true);
            this.carsSelected++;
        }

    }

    private void checkForStartConditions() {
        if((this.carsSelected >= 1) && (this.carsSelected < 4) && (this.trackSelected == 1)) {
            ((JButton)this.controls[0]).setEnabled(true);
        } else {
            ((JButton)this.controls[0]).setEnabled(false);
        }
    }

    private void singleSelectionOfButtons(JButton button) {
        this.trackSelected = 1;
        for(Component c : button.getParent().getComponents()) {
            c.setBackground(null);
            ((JButton)c).setSelected(false);
        }
        button.setBackground(new Color(92, 255, 63, 126));
        button.setSelected(true);
    }
    private JPanel createGameWindowInfoPanel(int length) {
        JPanel bottomGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel infoPanel = new JPanel(new GridLayout(1, (length + 1), 10, 10));
        bottomGamePanel.add(infoPanel);
        return bottomGamePanel;
    }

    /* Class Functions */
    private void loadImages() {
        this.images = new Image[30];
        // TODO: modify images to be a 2d array and put each direction of car into its own array
        try {
            this.images[0] = ImageIO.read(new File("Sprites\\Checkered.png"));
            this.images[1] = ImageIO.read(new File("Sprites\\MenuImage.png"));

            this.images[2] = ImageIO.read(new File("Sprites\\carSprites\\blueCarUp.png"));
            this.images[3] = ImageIO.read(new File("Sprites\\carSprites\\blueCarDown.png"));
            this.images[4] = ImageIO.read(new File("Sprites\\carSprites\\blueCarLeft.png"));
            this.images[5] = ImageIO.read(new File("Sprites\\carSprites\\blueCarRight.png"));

            this.images[6] = ImageIO.read(new File("Sprites\\carSprites\\greenCarUp.png"));
            this.images[7] = ImageIO.read(new File("Sprites\\carSprites\\greenCarDown.png"));
            this.images[8] = ImageIO.read(new File("Sprites\\carSprites\\greenCarLeft.png"));
            this.images[9] = ImageIO.read(new File("Sprites\\carSprites\\greenCarRight.png"));

            this.images[10] = ImageIO.read(new File("Sprites\\carSprites\\orangeCarUp.png"));
            this.images[11] = ImageIO.read(new File("Sprites\\carSprites\\orangeCarDown.png"));
            this.images[12] = ImageIO.read(new File("Sprites\\carSprites\\orangeCarLeft.png"));
            this.images[13] = ImageIO.read(new File("Sprites\\carSprites\\orangeCarRight.png"));

            this.images[14] = ImageIO.read(new File("Sprites\\carSprites\\purpleCarUp.png"));
            this.images[15] = ImageIO.read(new File("Sprites\\carSprites\\purpleCarDown.png"));
            this.images[16] = ImageIO.read(new File("Sprites\\carSprites\\purpleCarLeft.png"));
            this.images[17] = ImageIO.read(new File("Sprites\\carSprites\\purpleCarRight.png"));

            this.images[18] = ImageIO.read(new File("Sprites\\carSprites\\redCarUp.png"));
            this.images[19] = ImageIO.read(new File("Sprites\\carSprites\\redCarDown.png"));
            this.images[20] = ImageIO.read(new File("Sprites\\carSprites\\redCarLeft.png"));
            this.images[21] = ImageIO.read(new File("Sprites\\carSprites\\redCarRight.png"));

            this.images[22] = ImageIO.read(new File("Sprites\\carSprites\\yellowCarUp.png"));
            this.images[23] = ImageIO.read(new File("Sprites\\carSprites\\yellowCarDown.png"));
            this.images[24] = ImageIO.read(new File("Sprites\\carSprites\\yellowCarLeft.png"));
            this.images[25] = ImageIO.read(new File("Sprites\\carSprites\\yellowCarRight.png"));

            this.images[26] = ImageIO.read(new File("Sprites\\TrackIcons\\Track1Icon.png"));
            this.images[27] = ImageIO.read(new File("Sprites\\TrackIcons\\Track2Icon.png"));
            this.images[28] = ImageIO.read(new File("Sprites\\TrackIcons\\Track3Icon.png"));
            this.images[29] = ImageIO.read(new File("Sprites\\TrackIcons\\Track4Icon.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void swapWindow(JPanel newWindow) {
        this.rootFrame.setContentPane(newWindow);
        this.rootFrame.pack();
        this.rootFrame.revalidate();
        this.rootFrame.repaint();
        this.rootFrame.setVisible(true);
    }
    public void drawNewCarPositions() {
        for(int i = 0; i < this.gameCars.length; i++) {
            this.gameCars[i].setBounds(this.gameCars[i].getPosition().x, this.gameCars[i].getPosition().y, TILE_SIZE, TILE_SIZE);
            if(this.gameCars[i].getWasRotated()) {
                swapCarSprite(i);
            }
            this.carPanelLabels[i][0].setText(gameCars[i].getSpeed() * 15 + " mph");
            this.carPanelLabels[i][1].setText("" + makeCheckpointsReadable(gameCars[i].getCheckpoints()));
            this.carPanelLabels[i][2].setText("" + gameCars[i].getCheckpointIndex());
        }
    }

    private void swapCarSprite(int i) {
        this.gameCars[i].setWasRotated(false);
        int dir = this.gameCars[i].getCurrDir();
        switch(this.gameCars[i].getName()) {
            case "blue":
                this.gameCars[i].setIcon(new ImageIcon(this.images[(2 + dir)]));
                break;
            case "green":
                this.gameCars[i].setIcon(new ImageIcon(this.images[(6 + dir)]));
                break;
            case "orange":
                this.gameCars[i].setIcon(new ImageIcon(this.images[(10 + dir)]));
                break;
            case "purple":
                this.gameCars[i].setIcon(new ImageIcon(this.images[(14 + dir)]));
                break;
            case "red":
                this.gameCars[i].setIcon(new ImageIcon(this.images[(18 + dir)]));
                break;
            case "yellow":
                this.gameCars[i].setIcon(new ImageIcon(this.images[(22 + dir)]));
                break;
        }

    }

    public Object[] extractGameArgs(JButton component) {
        Object[] args = new Object[2];
        // get parent root panel
        JPanel panel = (JPanel) component.getParent();
        JPanel rootPanel = (JPanel) panel.getParent();
        // local variables containing game argument selection panels
        Component[] cars = ((JPanel) rootPanel.getComponent(1)).getComponents();
        Component[] tracks = ((JPanel) rootPanel.getComponent(2)).getComponents();

        ArrayList<Car> racers = new ArrayList<Car>();
        for(Component car : cars ) {
            JButton carBtn = (JButton) car;
            if(carBtn.isSelected()) {
                String name = carBtn.getText();
                Image image = getCarSpriteFromText(name); // Car default image is 'Up'
                racers.add(new Car(name, image, null, 0, 0)); // TODO: needs testing
            }
        }
        this.gameCars = new Car[racers.size()];
        args[0] = racers.toArray(this.gameCars);

        for(Component track : tracks ) {
            JButton trackBtn = (JButton) track;
            if(trackBtn.isSelected()) {
                String file = ("Tracks\\" + trackBtn.getText() + ".csv");
                args[1] = file;
            }
        }

        return args;
    }
    private Image getCarSpriteFromText(String name) {
        switch(name) {
            // default sprite is 'Up'
            case "blue":
                return images[2];
            case "green":
                return images[3];
            case "orange":
                return images[4];
            case "purple":
                return images[5];
            case "red":
                return images[6];
            case "yellow":
                return images[7];

        }
        return null;
    }
    public void gameAssetsSelected(Object[] gameAssets) {
        this.gameAssets = gameAssets;
        this.gameCars = ((Car[])this.gameAssets[0]);
        this.gameTrack = ((Track)this.gameAssets[1]);
        createGameWindow();
        swapWindow(this.gameWindowPanel);
    }
    public void updateTimer(double elapsedSeconds){
        int hours = (int) (elapsedSeconds / 3600);
        int minutes = (int) (elapsedSeconds / 60);
        int seconds = (int) (elapsedSeconds % 60);
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        this.timeLabel.setText(timeString);

    }

    private String makeCheckpointsReadable(int[] checkpointList) {
        String output = "";
        for (int checkpointIndex : checkpointList) {
            output += checkpointIndex;
        }
        return output;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = ((JButton)e.getSource());
        String text = pressed.getText();
        switch(text) {
            // TODO: 3/21/2023 Magic strings should probably be replaced with constants.
            case "Start New Game":
                swapWindow(this.startGameOptionsWindowPanel);
                // swapWindow(this.startGameOptionsWindowPanel);
                break;
            case "Start Game":
                swapWindow(this.gameWindowPanel);
                break;
            case "blue":
                cycleButtonHighlight(pressed);
                break;
            case "green":
                cycleButtonHighlight(pressed);
                break;
            case "orange":
                cycleButtonHighlight(pressed);
                break;
            case "purple":
                cycleButtonHighlight(pressed);
                break;
            case "red":
                cycleButtonHighlight(pressed);
                break;
            case "yellow":
                cycleButtonHighlight(pressed);
                break;
            case "Track1":
                singleSelectionOfButtons(pressed);
                break;
            case "Track2":
                singleSelectionOfButtons(pressed);
                break;
            case "Track3":
                singleSelectionOfButtons(pressed);
                break;
            case "Track4":
                singleSelectionOfButtons(pressed);
                break;
            default:
                swapWindow(this.menuWindowPanel);
                break;
        }
        checkForStartConditions();
    }

}

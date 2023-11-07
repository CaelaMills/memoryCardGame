import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;


public class FlipperGame extends JFrame implements ActionListener {
    private JPanel panel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private ImageIcon[] icons;
    private int[] iconnumbers;
    private int[] cardNumbers;
    private int[] selectedCards;
    private int[] matchedCards;
    private int numCards;
    private int numMatches;
    private int numAttempts;
    private Timer timer;
    private JLabel timerLabel;
    private int timeLeft;
    private ImageIcon frontIcon;
    private Image frontImage;
    private ArrayList<String> backImage; // Added declaration -- fix

    public FlipperGame(int numCards) {
        this.numCards = numCards;
        this.numMatches = 0;
        this.numAttempts = 0;
        this.timeLeft = 300; //300 seconds = 5 minutes
        this.icons = new ImageIcon[numCards];
        this.cardNumbers = new int[numCards];
        this.selectedCards = new int[2];
        this.matchedCards = new int[numCards];
        this.timer = new Timer(1000, this);

        // Create panel and buttons
        this.panel = new JPanel(new GridLayout(0, 4));
        this.buttons = new JButton[numCards];
        for (int i = 0; i < numCards; i++) {
            this.buttons[i] = new JButton();
            this.buttons[i].addActionListener(this);
            this.panel.add(this.buttons[i]);
        }

        // Set up timer
        this.timerLabel = new JLabel("Time left: " + this.timeLeft + " seconds");
        this.timerLabel.setHorizontalAlignment(JLabel.CENTER);
        this.timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(this.timerLabel, BorderLayout.NORTH);

        // Set up frame
        setTitle("Memory Card Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.add(this.panel, BorderLayout.CENTER);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        titleLabel = new JLabel("Welcome to Memory Card Game!");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel);

        // Start game
        startGame();
    }

    public void startGame() {
        // Define back images of cards, add more back images if needed
        backImage = new ArrayList<>();
        backImage.add("back1.png");
        backImage.add("back2.png");
        backImage.add("back3.png");
        backImage.add("back4.png");
        backImage.add("back5.png");
        backImage.add("back6.png");
        backImage.add("back7.png");
        backImage.add("back8.png");
        backImage.add("back9.png");
        backImage.add("back10.png");
        backImage.add("back11.png");
        backImage.add("back12.png");

        // Load card images
        frontIcon = new ImageIcon("front.png");
        frontImage = frontIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        for (int i = 0; i < numCards / 2; i++) {
            ImageIcon cardIcon = new ImageIcon(backImage.get(i));
            Image cardImage = cardIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            this.icons[i] = new ImageIcon(cardImage);

            ImageIcon cardIcon2 = new ImageIcon(backImage.get(i));
            Image cardImage2 = cardIcon2.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            this.icons[i + numCards / 2] = new ImageIcon(cardImage2);

//            ImageIcon backIcon = new ImageIcon(backImage.get(i));
//            Image backImage = backIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//            this.icons[i] = new ImageIcon(backImage);

        }

        for (int i = 0; i < numCards; i++) {
            this.cardNumbers[i] = i;
        }

        // Shuffle cards
        Random rand = new Random();
        for (int i = 0; i < numCards; i++) {
            int j = rand.nextInt(numCards);
            int temp = this.cardNumbers[i];
            this.cardNumbers[i] = this.cardNumbers[j];
            this.cardNumbers[j] = temp;
//            this.iconnumbers[i] = i / 2;
        }

        // Reset game variables
        Arrays.fill(this.matchedCards, 0);
        Arrays.fill(this.selectedCards, -1);
        this.numMatches = 0;
        this.numAttempts = 0;
        this.timeLeft = 300;
        this.timer.start();

        // Update buttons
        for (int i = 0; i < numCards; i++) {
            this.buttons[i].setIcon(new ImageIcon(frontImage));
            this.buttons[i].setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            // Handle button click
            JButton button = (JButton) e.getSource();
            int index = Arrays.asList(this.buttons).indexOf(button);
            if (this.matchedCards[index] == 0 && this.selectedCards[0] != index) {
                if (this.selectedCards[0] == -1) {
                    this.selectedCards[0] = index;
                    this.buttons[index].setIcon(this.icons[this.cardNumbers[index]]);
                } else {
                    this.selectedCards[1] = index;
                    this.buttons[index].setIcon(this.icons[this.cardNumbers[index]]);
                    ImageIcon firstcard = this.icons[this.selectedCards[0]];
                    ImageIcon secondcard = this.icons[this.selectedCards[1]];
                    this.numAttempts++;
                    if (this.cardNumbers[this.selectedCards[0]] %(numCards / 2) == this.cardNumbers[this.selectedCards[1]] %(numCards / 2)) {
                        // Match found
                        this.matchedCards[this.selectedCards[0]] = 1;
                        this.matchedCards[this.selectedCards[1]] = 1;
                        this.selectedCards[0] = -1;
                        this.selectedCards[1] = -1;
                        this.numMatches++;
                        if (this.numMatches == this.numCards / 2) {
                            // Game over
                            this.timer.stop();
                            JOptionPane.showMessageDialog(this, "Congratulations! You won in " + (300 - this.timeLeft) + " seconds with " + this.numAttempts + " attempts.");
                            startGame();
                        }
                    } else {
                        // No match found
                        this.buttons[this.selectedCards[0]].setEnabled(false);
                        this.buttons[this.selectedCards[1]].setEnabled(false);
                        Timer flipTimer = new Timer(2000, new ActionListener() {
                            // delay originally set to 1000 ms with the help of my tutor Jonathan -- I changed to 1500 then 2000
                            public void actionPerformed(ActionEvent e) {
                                buttons[selectedCards[0]].setIcon(new ImageIcon(frontImage));
                                buttons[selectedCards[1]].setIcon(new ImageIcon(frontImage));
                                buttons[selectedCards[0]].setEnabled(true);
                                buttons[selectedCards[1]].setEnabled(true);
                                selectedCards[0] = -1;
                                selectedCards[1] = -1;
                            }
                        });
                        flipTimer.setRepeats(false);
                        flipTimer.start();
                    }
                }
            }
        } else if (e.getSource() == this.timer) {
            // Handle timer tick
            this.timeLeft--;
            this.timerLabel.setText("Time left: " + this.timeLeft + " seconds");
            if (this.timeLeft == 0) {
                // Game over
                this.timer.stop();
                JOptionPane.showMessageDialog(this, "Time's up! You had " + this.numMatches + " matches and " + this.numAttempts + " attempts.");
                startGame();
            }
        }
    }

    public static void main(String[] args) {
        int numCards = Integer.parseInt(JOptionPane.showInputDialog("Enter number of cards (12 or 24): "));
        if (numCards != 12 && numCards != 24) {
            JOptionPane.showMessageDialog(null, "Invalid number of cards. Defaulting to 12.");
            numCards = 12;
        }
        new FlipperGame(numCards);
    }
}

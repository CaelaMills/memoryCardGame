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
    private int[] cardNumbers;
    private int[] selectedCards;
    private int[] matchedCards;
    private int numCards;
    private int numMatches;
    private int numAttempts;
    private Timer timer;
    private JLabel timerLabel;
    private int timeLeft;

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
        // Load card images
        for (int i = 0; i < numCards / 2; i++) {
            this.icons[i] = new ImageIcon("cards_front/front.png");
            this.icons[i + numCards / 2] = new ImageIcon("cards_back/back " + i + ".png");
        }

        // Shuffle cards
        Random rand = new Random();
        for (int i = 0; i < numCards; i++) {
            int j = rand.nextInt(numCards);
            int temp = this.cardNumbers[i];
            this.cardNumbers[i] = this.cardNumbers[j];
            this.cardNumbers[j] = temp;
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
            this.buttons[i].setIcon(this.icons[this.cardNumbers[i]]);
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
                    this.numAttempts++;
                    if (this.cardNumbers[this.selectedCards[0]] == this.cardNumbers[this.selectedCards[1]]) {
                        // Match found
                        this.matchedCards[this.selectedCards[0]] = 1;
                        this.matchedCards[this.selectedCards[1]] = 1;
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
                        Timer flipTimer = new Timer(5000, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                buttons[selectedCards[0]].setIcon(icons[cardNumbers[selectedCards[0]] + numCards / 2]);
                                buttons[selectedCards[1]].setIcon(icons[cardNumbers[selectedCards[1]] + numCards / 2]);
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

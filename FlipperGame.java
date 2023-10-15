import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




public class FlipperGame extends JFrame implements ActionListener
{




    private static final String[] CARD_NAMES = {"cow.png", "cutebird.png", "fox.png", "meow.png",
            "OVO.png", "squirrel.png", "teddybear.png", "woof.png"};




    private int numMoves;
    private int numMatches;




    private Card[] cards;
    private JPanel cardsPanel;
    private JLabel movesLabel;
    private JLabel matchesLabel;




    private Card firstOpenedCard;
    private Card secondOpenedCard;




    private JButton continueButton;
    private JButton exitButton;




    public FlipperGame()
    //Initialize the board, the cards, and the labels
    {
        setTitle("Memory Card Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 500);
        setLocationRelativeTo(null);








        movesLabel = new JLabel();
        matchesLabel = new JLabel();




        JPanel gameStatsPanel = new JPanel(new FlowLayout());
        gameStatsPanel.add(movesLabel);
        gameStatsPanel.add(matchesLabel);
        add(BorderLayout.NORTH, gameStatsPanel);







        //Initializes cards to be in pairs
        cards = new Card[CARD_NAMES.length * 2];




        for (int i = 0, j = 0; i < CARD_NAMES.length; i++)
        {
            cards[j++] = new Card("cardimages/" + CARD_NAMES[i]);
            cards[j++] = new Card("cardimages/" + CARD_NAMES[i]);
        }







        //Initializes buttons
        continueButton = new JButton("Continue");
        exitButton = new JButton("Exit");




        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(continueButton);
        buttonsPanel.add(exitButton);
        add(BorderLayout.SOUTH, buttonsPanel);




        continueButton.addActionListener(this);
        exitButton.addActionListener(this);







        // starts a new game
        initializeNewGame();




    }




    private void initializeNewGame()
    //Shuffles cards and puts them into the cards panel
    {




        if (cardsPanel != null)
        //Remove cards in user interface
        {
            remove(cardsPanel);
        }




        continueButton.setEnabled(false);




        numMoves = 0;
        numMatches = 0;




        movesLabel.setText("Attempts: 0");
        matchesLabel.setText("Matches: 0");




        for (int i = 0; i < cards.length; i++)
            //Shuffle the cards, s variable for "shuffle"
        {
            int s = i + (int) (Math.random() * (cards.length - i));
            Card temp = cards[s];
            cards[s] = cards[i];
            cards[i] = temp;
        }




        cardsPanel = new JPanel(new GridLayout(cards.length / 4, cards.length / 4));




        for (Card card : cards)
        {
            card.addActionListener(this);
            card.hideCard();
            cardsPanel.add(card);
        }




        add(BorderLayout.CENTER, cardsPanel);




        firstOpenedCard = null;
        secondOpenedCard = null;




        this.repaint();
        this.revalidate();
    }




    private void evaluateScore()        //Evaluates the score of the user
    {
        int score = 0;




        if (numMoves > 0)
        {
            score = (int) (((double) numMatches / (double) numMoves) * 100);
        }




        String result = "In " + numMoves + " move(s), "+ " your score was " + score + ", ";


        if (score == 100)
        {
            result += "Congratulations! YOU WON";
        } else if (score > 97)
        {
            result += "AMAZING!";
        } else if (score > 80)
        {
            result += "Excellent!";
        } else if (score > 60)
        {
            result += "Great";
        } else if (score > 30)
        {
            result += "Not too bad";
        } else if (score > 10)
        {
            result += "Let's aim higher";
        } else
        {
            result += "Better luck next time";
        }




        JOptionPane.showMessageDialog(this, result);
    }








    private void evaluateOpenedCards()          //Evaluate the flipped cards and keep track of matches
    {
        if (firstOpenedCard != null && secondOpenedCard != null)
        {
            numMoves++;
            if (firstOpenedCard.equals(secondOpenedCard))           //Check for matches
            {
                numMatches++;




                firstOpenedCard = null;
                secondOpenedCard = null;
            } else {
                continueButton.setEnabled(true);            //Continue button only works for mismatched cards
            }
            movesLabel.setText("Moves: " + numMoves);      //Update the stats
            matchesLabel.setText("Matches: " + numMatches);
            if (numMatches >= CARD_NAMES.length)            //If all were matched then evaluate the score
            {
                evaluateScore();
            }
        }




    }




    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof Card)
        {
            if ((firstOpenedCard != null) && (secondOpenedCard != null))
            {
                return;
            }




            if (secondOpenedCard == null)
            {
                secondOpenedCard = (Card) e.getSource();
                secondOpenedCard.showCard();
                secondOpenedCard.removeActionListener(this);
            } else if (null == firstOpenedCard) {
                firstOpenedCard = (Card) e.getSource();
                firstOpenedCard.showCard();
                firstOpenedCard.removeActionListener(this);
            }




            evaluateOpenedCards();
        } else if (e.getSource() == continueButton)
        {
            firstOpenedCard.hideCard();
            secondOpenedCard.hideCard();




            firstOpenedCard.addActionListener(this);
            secondOpenedCard.addActionListener(this);




            firstOpenedCard = null;
            secondOpenedCard = null;




            continueButton.setEnabled(false);
        } else if (e.getSource() == exitButton)
            //Asks for confirmation if you want to close the gaming program
        {
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game?") != JOptionPane.YES_OPTION)
            {
                return;
            }



            // Evaluates card matches
            if (numMatches < CARD_NAMES.length)
            {
                evaluateScore();
            }




            setVisible(false);
            dispose();
        }
    }


    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Prompts the user for an input value of 12 or 24 (cards) for specified grid size
                String gridSizeInput = JOptionPane.showInputDialog("Grid size/number of cards: Enter '12' or '24':");
                int gridSize = Integer.parseInt(gridSizeInput);








                if (gridSize != 12 && gridSize != 24) {
                    JOptionPane.showInputDialog("Invalid grid size. Only enter 12 or 24. ");
                    return;
                }


                new FlipperGame().setVisible(true);
            }
        });
    }
}


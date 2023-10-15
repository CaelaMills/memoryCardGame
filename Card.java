import javax.swing.*;
import java.awt.*;

public class Card extends JButton
{

    private ImageIcon imageIcon;
    private String imagePath;

    public Card()
    // Creates an empty card
    {
        imageIcon = null;
        imagePath = "";
    }

    public Card(String imagePath) //Initialize images
    {
        this.imagePath = imagePath;
        imageIcon = new ImageIcon(imagePath);
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_FAST));
    }

    public void hideCard()
    //Hides card
    {
        setIcon(null);
    }

    public void showCard()
    //Shows card
    {
        setIcon(imageIcon);
    }

    @Override
    public boolean equals(Object m)
    //Checks for matches, "m" variable representing "match"
    {
        if (!(m instanceof Card))
        {
            return false;
        }

        Card other = (Card) m;
        return imagePath.equals(other.imagePath);
    }
}
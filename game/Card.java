package game;

import javax.swing.*;
import java.awt.Image; // Import the Image class


//Card represents a single card in the game. It extends JButton to be clickable.
//This class is a child of JButton, helping to fulfill the parent/child class requirement.
public class Card extends JButton {

    private final int id;
    private boolean isFlipped = false;
    private boolean isMatched = false;
    private final ImageIcon frontIcon;
    private final ImageIcon backIcon;

    private static final int CARD_WIDTH = 91;  
    private static final int CARD_HEIGHT = 131;

  
    //Main constructor for the Card class.
    public Card(int id) {
        this.id = id;
        this.frontIcon = getScaledIcon("resources/images/card_" + id + ".png");
        this.backIcon = getScaledIcon("resources/images/card_back.png");
        setIcon(backIcon);
        // Set the button's preferred size to match the image dimensions
        setPreferredSize(new java.awt.Dimension(CARD_WIDTH, CARD_HEIGHT));
        // Remove default button styling to make it look cleaner
        setBorder(null);
        setMargin(new java.awt.Insets(0, 0, 0, 0));
    }

    
    //Requirement: An overloaded constructor.
    //This constructor allows setting the initial flipped state.
    public Card(int id, boolean isFlipped) {
        this(id); // Calls the main constructor
        this.isFlipped = isFlipped;
        if(isFlipped) {
            setIcon(frontIcon);
        }
    }

    //Helper method to load and scale an image icon.
    private ImageIcon getScaledIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }


    public int getId() {
        return id;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        this.isMatched = matched;
        setEnabled(!matched); // Disable the button once a match is found.
    }

    //Flips the card over, changing its icon.
    public void flip() {
        isFlipped = !isFlipped;
        if (isFlipped) {
            setIcon(frontIcon);
        } else {
            setIcon(backIcon);
        }
    }

    
    //Resets the card to its initial face-down and unmatched state.
    public void reset() {
        isFlipped = false;
        isMatched = false;
        setIcon(backIcon);
        setEnabled(true);
    }
}



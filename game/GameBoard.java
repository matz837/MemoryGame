package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//GameBoard represents the main panel where the memory match game is played.
//This class uses JPanel and demonstrates handling events and game state.
public class GameBoard extends JPanel {

    private static final int GRID_SIZE = 4;
    private static final int NUM_CARDS = GRID_SIZE * GRID_SIZE;
    private static final int NUM_PAIRS = NUM_CARDS / 2;

    private List<Card> cards;
    private Card selectedCard = null;
    private int pairsFound = 0;
    private int attempts = 0;
    private JLabel attemptsLabel;
    private Timer mismatchTimer; // This fulfills the requirement for using threading/animation.

    public GameBoard() {
        setLayout(new BorderLayout());

        // Top Panel for score/attempts
        JPanel topPanel = new JPanel();
        attemptsLabel = new JLabel("Attempts: 0 | High Score: " + readHighScore()); // JLabel is a required GUI element.
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(attemptsLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel for the cards
        JPanel cardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 5, 5)); 
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        initializeCards();
        for (Card card : cards) {
            cardPanel.add(card);
        }
        add(cardPanel, BorderLayout.CENTER);

        // Bottom Panel for the reset button
        JPanel bottomPanel = new JPanel();
        ResetButton resetButton = new ResetButton("Reset Game"); // Custom child class of JButton
        resetButton.addActionListener(e -> resetGame());
        bottomPanel.add(resetButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Timer to handle delay for mismatched cards
        mismatchTimer = new Timer(1000, new MismatchListener());
        mismatchTimer.setRepeats(false);
    }

    
    //Initializes the list of cards, creating pairs and shuffling them.
    private void initializeCards() {
        cards = new ArrayList<>();
        for (int i = 1; i <= NUM_PAIRS; i++) {
            // Requirement: Use of Images.
            Card c1 = new Card(i);
            Card c2 = new Card(i);

            // Add action listener to each card
            ActionListener cardClickListener = e -> onCardClicked((Card) e.getSource());
            c1.addActionListener(cardClickListener);
            c2.addActionListener(cardClickListener);

            cards.add(c1);
            cards.add(c2);
        }
        Collections.shuffle(cards); // Requirement: Use of a data structure (ArrayList).
    }
    
    //Handles the logic when a card is clicked.
    private void onCardClicked(Card clickedCard) {
        if (mismatchTimer.isRunning() || clickedCard.isMatched() || clickedCard == selectedCard) {
            return; // Ignore clicks if timer is running, card is matched, or same card is clicked twice.
        }

        try {
            SoundPlayer.playSound("resources/sounds/flip.wav"); // Requirement: Use of Audio.
        } catch (NoSoundFileException e) {
            // Requirement: Custom exception is caught and handled without crashing.
            System.err.println(e.getMessage());
        }

        clickedCard.flip();

        if (selectedCard == null) {
            // This is the first card of a potential pair.
            selectedCard = clickedCard;
        } else {
            // This is the second card. Check for a match.
            attempts++;
            updateAttemptsLabel();
            if (selectedCard.getId() == clickedCard.getId()) {
                // Match found!
                selectedCard.setMatched(true);
                clickedCard.setMatched(true);
                pairsFound++;
                selectedCard = null; // Reset for the next pair.
                checkForWin();
            } else {
                // Mismatch. Start the timer to flip them back.
                mismatchTimer.start();
            }
        }
    }

    
    //Checks if the game has been won (all pairs found).
    private void checkForWin() {
        if (pairsFound == NUM_PAIRS) {
            try {
                SoundPlayer.playSound("resources/sounds/win.wav");
                int currentHighScore = readHighScore();
                if (attempts < currentHighScore || currentHighScore == 0) {
                    writeHighScore(attempts);
                }
            } catch (NoSoundFileException e) {
                System.err.println(e.getMessage());
            }
            JOptionPane.showMessageDialog(this, "You won! It took you " + attempts + " attempts.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    //Resets the game to its initial state.
    private void resetGame() {
        pairsFound = 0;
        attempts = 0;
        selectedCard = null;
        updateAttemptsLabel();
        mismatchTimer.stop();

        // Re-shuffle and reset all cards
        Collections.shuffle(cards);
        JPanel cardPanel = (JPanel) this.getComponent(1); // Assuming cardPanel is the second component
        cardPanel.removeAll();
        for(Card card : cards) {
            card.reset();
            cardPanel.add(card);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }


    private void updateAttemptsLabel() {
        attemptsLabel.setText("Attempts: " + attempts + " | High Score: " + readHighScore());
    }

    //Reads the high score from a file.
    //This fulfills the requirement to read from a file.
    private int readHighScore() {
        File file = new File("highscore.txt");
        if (file.exists()) {
            // Using try-with-resources ensures the scanner is automatically closed.
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextInt()) {
                    return scanner.nextInt();
                }
            } catch (IOException e) {
                System.err.println("Could not read high score: " + e.getMessage());
            }
        }
        return 0;
    }

    
    //Writes a new high score to a file.
    //This fulfills the requirement to write to a file.
    private void writeHighScore(int score) {
        try (FileWriter writer = new FileWriter("highscore.txt")) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }

    
    //ActionListener for the mismatch timer. Flips the cards back.
    private class MismatchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCard != null) {
                // Find the other flipped card that is not the selected one
                for (Card card : cards) {
                    if (card.isFlipped() && !card.isMatched() && card != selectedCard) {
                        card.flip();
                        break;
                    }
                }
                selectedCard.flip();
                selectedCard = null;
            }
        }
    }
}



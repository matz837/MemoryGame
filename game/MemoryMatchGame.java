package game;

import javax.swing.*;
import java.awt.*;

//MemoryMatchGame is the main class that sets up the game window.
//This fulfills the requirement for a GUI application using a JFrame. 
public class MemoryMatchGame {

    public static void main(String[] args) {
        // Swing GUI components should be created and updated on the Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Memory Match Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLayout(new BorderLayout()); // This fulfills the requirement to use a Layout Manager.

            // Add the main game board panel to the center of the frame.
            frame.add(new GameBoard(), BorderLayout.CENTER);

            frame.pack(); // Sizes the frame so all its contents are at or above their preferred sizes.
            frame.setLocationRelativeTo(null); // Center the window on the screen.
            frame.setVisible(true);
        });
    }
}

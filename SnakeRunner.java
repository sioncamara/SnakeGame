import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
public class SnakeRunner {
    public static void main (String[] args)
    {
        JFrame snakeFrame = new JFrame ("Snake");
        SnakePanel snakePanel = new SnakePanel();
        snakeFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        snakeFrame.getContentPane().add(snakePanel);
        snakeFrame.pack();
        snakeFrame.setVisible(true);


        do {
            snakePanel.move(); // moves snake
            snakePanel.checkIfFoodEaton();
            snakePanel.repaint();

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);


    }

}

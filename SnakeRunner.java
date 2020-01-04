import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
public class SnakeRunner {
    public static void main (String[] args)
    {
        JFrame snakeFrame = new JFrame ("Snake");
        SnakePanel panel = new SnakePanel();
        snakeFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        snakeFrame.getContentPane().add(panel);
        snakeFrame.pack();
        snakeFrame.setVisible(true);






        while(true) {
            panel.move();
            panel.checkEaton();
            panel.repaint();

            try {
                Thread.sleep(60);
            } catch (InterruptedException ex) {
                Logger.getLogger(SnakeRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

}

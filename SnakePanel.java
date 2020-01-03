import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SnakePanel extends JPanel {

    private static  final int WINDOW_WIDTH = 1460;
    private static final int WINDOW_HEIGHT = 920;
    private final int BLOCK_WIDTH = 35;
    private final int BLOCK_HEIGHT = 35;
    private final int DISTANCE_TO_MOVE = 40;
    private final int ADD_TO_NEW_BLOCK = 10;
    private boolean Left, Up, Down, Right;
    private Font font = new Font("Roman", Font.PLAIN, 38);
    private Food food = new Food();

    // x and y coordinated for the start and end of game play area
    static int gameAreaStartX = 40;
    static int gameAreaStartY = 40;
    static int gameAreaEndx = WINDOW_WIDTH - 60;
    static int gameAreaEndY = WINDOW_HEIGHT - 80;

    private ArrayList<Block> BlockList = new ArrayList<>();
    private Direction direction;
    private JButton playButton;


    /**
     *
     */
     SnakePanel() {

        setBackground(Color.getHSBColor(23, .5f, .9f));

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        BlockList.add(new Block(400, 400));
        addKeyListener(new BlockListener());
        this.setFocusable(true);
        playButton = new JButton("Play Again");
        this.add(playButton);
        playButton.setVisible(false);

    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        Graphics2D snakeInterface = (Graphics2D) page;
        snakeInterface.setColor(Color.getHSBColor(.623f, .9f, .5f)); // set color of snake area
        snakeInterface.fillRect(gameAreaStartX, gameAreaStartY, gameAreaEndx, gameAreaEndY); // create snake area
        snakeInterface.setColor(Color.yellow); // set color of snake
        // generate current snake
        BlockList.forEach(block -> snakeInterface.fillRect(block.x(), block.y(), BLOCK_WIDTH, BLOCK_HEIGHT));

        snakeInterface.setColor(Color.red); // set color of food
        snakeInterface.fillRect(food.getX(), food.getY(), BLOCK_WIDTH, BLOCK_HEIGHT); // draw food

        int length = BlockList.size();

        // generate display of length
        snakeInterface.setColor(Color.white);
        snakeInterface.setFont(font);
        snakeInterface.drawString("Length: " + length, WINDOW_WIDTH - 350, WINDOW_HEIGHT - 60);



    }

     void eatfood() {
        int x = BlockList.get(0).x(); // front of snake x coordinate
        int y = BlockList.get(0).y(); // fornt of snake y coordinate
        int snakeEnd = BlockList.size() - 1;
        if (x == food.getX() && y == food.getY()) {
            for (int i = 0; i < 5; i++) {
                if(snakeEnd < 10){
                    if (direction == Direction.DOWN) {
                        BlockList.add(new Block(BlockList.get(snakeEnd).x(), BlockList.get(snakeEnd).y() - DISTANCE_TO_MOVE));

                    } else if (direction == Direction.UP) {
                        BlockList.add(new Block(BlockList.get(snakeEnd).x(), BlockList.get(snakeEnd).y() + DISTANCE_TO_MOVE));

                    } else if (direction == Direction.LEFT) {
                        BlockList.add(new Block(BlockList.get(snakeEnd).x() + DISTANCE_TO_MOVE, BlockList.get(snakeEnd).y()));

                    } else {
                        BlockList.add(new Block(BlockList.get(snakeEnd).x() - DISTANCE_TO_MOVE, BlockList.get(snakeEnd).y()));
                    }
                }
                else
                if (direction == Direction.DOWN) {
                    BlockList.add(new Block(BlockList.get(ADD_TO_NEW_BLOCK).x(), BlockList.get(ADD_TO_NEW_BLOCK).y() - DISTANCE_TO_MOVE));

                } else if (direction == Direction.UP) {
                    BlockList.add(new Block(BlockList.get(ADD_TO_NEW_BLOCK).x(), BlockList.get(ADD_TO_NEW_BLOCK).y() + DISTANCE_TO_MOVE));

                } else if (direction == Direction.LEFT) {
                    BlockList.add(new Block(BlockList.get(ADD_TO_NEW_BLOCK).x() + DISTANCE_TO_MOVE, BlockList.get(ADD_TO_NEW_BLOCK).y()));

                } else {
                    BlockList.add(new Block(BlockList.get(ADD_TO_NEW_BLOCK).x() - DISTANCE_TO_MOVE, BlockList.get(ADD_TO_NEW_BLOCK).y()));
                }

            }
            food.generateNewFood();
            boolean isAlone = true;
            for (Block block : BlockList) {
                if (food.getX() == block.x() && food.getY() == block.y()) {
                    isAlone = false;
                }
            }
            while (!isAlone) {
                food.generateNewFood();
                isAlone = true;
                for (Block block : BlockList) {
                    if (food.getX() == block.x() && food.getY() == block.y()) {
                        isAlone = false;
                    }
                }

            }

        }
    }

    private void crash(){
        playButton.setVisible(true);
        playButton.setBounds(1150, 700, 400, 100);
        playButton.addActionListener(this::actionPerformed);

    }
    private boolean boundry() {
        int x = BlockList.get(0).x();
        int y = BlockList.get(0).y();
        if (x > gameAreaEndx + 40 || x < gameAreaStartX) {
            return true;
        } else return y > gameAreaEndY + 40 || y < gameAreaStartY;

    }

    private boolean hitbody() {
        int x = BlockList.get(0).x();
        int y = BlockList.get(0).y();
        for (int i = 1; i < BlockList.size(); i++) {

            if (x == BlockList.get(i).x() && y == BlockList.get(i).y()) {
                return true;
            }

        }
        return false;
    }

    void move() {

        if (!hitbody() && !boundry()) {

            int x = BlockList.get(0).x();
            int y = BlockList.get(0).y();


            if (direction == Direction.UP) {
                BlockList.add(0, new Block(x, y - DISTANCE_TO_MOVE));
                BlockList.remove(BlockList.size() - 1);
            }

            if (direction == Direction.DOWN) {
                BlockList.add(0, new Block(x, y + DISTANCE_TO_MOVE));
                BlockList.remove(BlockList.size() - 1);
            }
            if (direction == Direction.LEFT) {
                BlockList.add(0, new Block(x - DISTANCE_TO_MOVE, y));
                BlockList.remove(BlockList.size() - 1);
            }

            if (direction == Direction.RIGHT) {
                BlockList.add(0, new Block(x + DISTANCE_TO_MOVE, y));
                BlockList.remove(BlockList.size() - 1);
            }
        }
        else crash();

    }

    private void actionPerformed(ActionEvent e) {
        playButton.setVisible(false);
        BlockList.clear();
        BlockList.add(new Block(400, 400));
        direction = Direction.nothing;
    }

    public enum Direction {
        RIGHT, LEFT, UP, DOWN, nothing

    }
    private class BlockListener implements KeyListener {

        public void keyPressed(KeyEvent e) {

            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(SnakeRunner.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && (direction != Direction.LEFT || BlockList.size() == 1) ) {
                direction = Direction.RIGHT;


            }

            else if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && (direction != Direction.RIGHT || BlockList.size() ==1)) {
                direction = Direction.LEFT;
            }

            else if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && (direction != Direction.DOWN || BlockList.size() == 1)) {
                direction = Direction.UP;

            }

            else  if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && (direction != Direction.UP || BlockList.size() == 1)) {
                direction = Direction.DOWN;

            }

        }

        public void keyRealesed(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {

        }
    }


}

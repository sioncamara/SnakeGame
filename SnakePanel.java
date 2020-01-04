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
    private final int nextIndex = 10;
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

        setBackground(Color.getHSBColor(23, .5f, .9f)); // color of panel

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        BlockList.add(new Block(400, 400)); // create snake
        addKeyListener(new BlockListener()); // listens for directional changes
        this.setFocusable(true); // allows snake to move
        playButton = new JButton("Play Again");
        this.add(playButton);
        playButton.setVisible(false);

    }

    /**
     * paints the interface where the game is played
     * @param page
     */
    protected void paintComponent(Graphics page) {
        super.paintComponent(page);
        Graphics2D snakeInterface = (Graphics2D) page;
        snakeInterface.setColor(Color.getHSBColor(.623f, .9f, .5f)); // set color of area snake can go
        snakeInterface.fillRect(gameAreaStartX, gameAreaStartY, gameAreaEndx, gameAreaEndY); // create snake area
        snakeInterface.setColor(Color.yellow); // set color of snake
        // generate current snake
        BlockList.forEach(block -> snakeInterface.fillRect(block.getX(), block.getY(), BLOCK_WIDTH, BLOCK_HEIGHT));

        snakeInterface.setColor(Color.red); // set color of food
        snakeInterface.fillRect(food.getX(), food.getY(), BLOCK_WIDTH, BLOCK_HEIGHT); // draw food

        // generate length of snake display
        snakeInterface.setColor(Color.white);
        snakeInterface.setFont(font);
        snakeInterface.drawString("Length: " + BlockList.size(), WINDOW_WIDTH - 350, WINDOW_HEIGHT - 60);



    }

     void checkEaton() {
        int x = BlockList.get(0).getX(); // front of snake x coordinate
        int y = BlockList.get(0).getY(); // fornt of snake y coordinate
        int snakeEnd = BlockList.size() - 1;
        if (x == food.getX() && y == food.getY()) {
            for (int i = 0; i < 5; i++) {
                if(snakeEnd < 10){
                    if (direction == Direction.DOWN) {
                        BlockList.add(new Block(BlockList.get(snakeEnd).getX(), BlockList.get(snakeEnd).getY() - DISTANCE_TO_MOVE));

                    } else if (direction == Direction.UP) {
                        BlockList.add(new Block(BlockList.get(snakeEnd).getX(), BlockList.get(snakeEnd).getY() + DISTANCE_TO_MOVE));

                    } else if (direction == Direction.LEFT) {
                        BlockList.add(new Block(BlockList.get(snakeEnd).getX() + DISTANCE_TO_MOVE, BlockList.get(snakeEnd).getY()));

                    } else {
                        BlockList.add(new Block(BlockList.get(snakeEnd).getX() - DISTANCE_TO_MOVE, BlockList.get(snakeEnd).getY()));
                    }
                }
                else
                if (direction == Direction.DOWN) {
                    BlockList.add(new Block(BlockList.get(nextIndex).getX(), BlockList.get(nextIndex).getY() - DISTANCE_TO_MOVE));

                } else if (direction == Direction.UP) {
                    BlockList.add(new Block(BlockList.get(nextIndex).getX(), BlockList.get(nextIndex).getY() + DISTANCE_TO_MOVE));

                } else if (direction == Direction.LEFT) {
                    BlockList.add(new Block(BlockList.get(nextIndex).getX() + DISTANCE_TO_MOVE, BlockList.get(nextIndex).getY()));

                } else {
                    BlockList.add(new Block(BlockList.get(nextIndex).getX() - DISTANCE_TO_MOVE, BlockList.get(nextIndex).getY()));
                }

            }
           // food.generateNewFood();
            boolean foodNotOnSnake = true;

            do {
                food.generateNewFood();
                for (Block block : BlockList)
                    if (food.getX() == block.getX() && food.getY() == block.getY()) foodNotOnSnake = false;

            } while(!foodNotOnSnake);

        }
    }

    private void crash(){
        playButton.setVisible(true);
        playButton.setBounds(gameAreaStartX * 14, gameAreaStartY * 5, 400, 100);
        playButton.addActionListener(e -> actionPerformed());

    }
    private boolean boundry() {
        int x = BlockList.get(0).getX();
        int y = BlockList.get(0).getY();
        if (x > gameAreaEndx + 40 || x < gameAreaStartX) {
            return true;
        } else return y > gameAreaEndY + 40 || y < gameAreaStartY;

    }

    private boolean hitbody() {
        int x = BlockList.get(0).getX();
        int y = BlockList.get(0).getY();
        for (int i = 1; i < BlockList.size(); i++) {

            if (x == BlockList.get(i).getX() && y == BlockList.get(i).getY()) {
                return true;
            }

        }
        return false;
    }

    void move() {

        if (!hitbody() && !boundry()) {

            int x = BlockList.get(0).getX();
            int y = BlockList.get(0).getY();


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

    private void actionPerformed() {
        playButton.setVisible(false);
        BlockList.clear();
        BlockList.add(new Block(400, 400));
        direction = Direction.nothing;
    }

    public enum Direction {
        RIGHT, LEFT, UP, DOWN, nothing

    }
    private class BlockListener implements KeyListener {

        public void keyPressed(KeyEvent keyPress) {

            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(SnakeRunner.class.getName()).log(Level.SEVERE, null, ex);
            }

            // change direction to right if right arrow key or D key is pressed. Do not change if curr direction is
            // left
            if ((keyPress.getKeyCode() == KeyEvent.VK_RIGHT || keyPress.getKeyCode() == KeyEvent.VK_D) && (direction != Direction.LEFT || BlockList.size() == 1) ) {
                direction = Direction.RIGHT;
            }

            // change direction to left if left arrow key or A key is pressed. Do not change if curr direction is right
            else if ((keyPress.getKeyCode() == KeyEvent.VK_LEFT || keyPress.getKeyCode() == KeyEvent.VK_A) && (direction != Direction.RIGHT || BlockList.size() ==1)) {
                direction = Direction.LEFT;
            }
            // change direction to up if up arrow key or W key is pressed. Do not change if curr direction is down
            else if ((keyPress.getKeyCode() == KeyEvent.VK_UP || keyPress.getKeyCode() == KeyEvent.VK_W) && (direction != Direction.DOWN || BlockList.size() == 1)) {
                direction = Direction.UP;

            }
            // change direction to down if left arrow key or s key is pressed. Do not change if curr direction is up
            else  if ((keyPress.getKeyCode() == KeyEvent.VK_DOWN || keyPress.getKeyCode() == KeyEvent.VK_S) && (direction != Direction.UP || BlockList.size() == 1)) {
                direction = Direction.DOWN;

            }

        }

        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {

        }
    }


}

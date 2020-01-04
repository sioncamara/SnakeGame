import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class SnakePanel extends JPanel {

    private static final int WINDOW_WIDTH = 1460;
    private static final int WINDOW_HEIGHT = 920;
    private final int BLOCK_WIDTH = 35;
    private final int BLOCK_HEIGHT = 35;
    private final int spaceBtwBlocks = 40;
    private final int nextIndex = 10;
    private boolean Left;
    private boolean Up;
    private boolean Down;
    private boolean Right;
    private Font font = new Font("Roman", Font.PLAIN, 38);
    private Food food = new Food();

    // x and y coordinated for the start and end of game play area
    static int gameAreaStartX = 40;
    static int gameAreaStartY = 40;
    static int gameAreaEndx = WINDOW_WIDTH - 100;
    static int gameAreaEndY = WINDOW_HEIGHT - 120;

    private ArrayList<Block> BlockList2 = new ArrayList<>(1000);
    private LinkedList<Block> BlockList = new LinkedList<>();
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
     *
     * @param page user interface to build on top off
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
        snakeInterface.drawString("Length: " + BlockList.size(), gameAreaEndx - 130, gameAreaEndY + 90);


    }



    void checkIfFoodEaton() {
        if (BlockList.getFirst().getX() == food.getX() &&  BlockList.getFirst().getY() == food.getY()) {
            if (direction == Direction.DOWN) {
                growSnake(0, -spaceBtwBlocks);

            } else if (direction == Direction.UP) {
                growSnake(0, spaceBtwBlocks);

            } else if (direction == Direction.LEFT) {
                growSnake(spaceBtwBlocks, 0);
            } else {
                growSnake(-spaceBtwBlocks, 0);
            }
            generateNewFood();

        }
    }

    // delay between each move of the snake
    private void pause(int speed){
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * helper method that grows snake body
     */
    private void growSnake(int addToX, int addToY){

        int index = 0;
        for (int count = 0; count < 5; count++){

            BlockList.add(1, new Block(BlockList.get(index).getX() + addToX,
                    BlockList.get(index).getY() + addToY));
            //pause(100);
        }


    }

    /**
     * generated a new food item for the snake to eat
     */
    private void generateNewFood() {
        AtomicBoolean foodNotOnSnake = new AtomicBoolean(false);

        do {
            food.generateNewFood();
            foodNotOnSnake.set(true);
            for (Block block : BlockList) {
                if (food.getX() == block.getX() && food.getY() == block.getY()) {
                    foodNotOnSnake.set(false);
                    break;
                }
            }

        } while (!foodNotOnSnake.get());
    }


    private void endTurn() {
        playButton.setVisible(true);
        playButton.setBounds(gameAreaStartX * 14, gameAreaStartY * 5, 400, 100);
        playButton.addActionListener(e -> actionPerformed());

    }

    /**
     * check if the snake has hit a boundary
     *
     * @return true if snake head is in a boundary
     */
    private boolean hitBoundary() {
        int x = BlockList.getFirst().getX();
        int y = BlockList.getFirst().getY();
        return y > gameAreaEndY || y < gameAreaStartY || x > gameAreaEndx || x < gameAreaStartX;
    }

    private boolean hitBody() {
        int headX = BlockList.getFirst().getX(); // x coordinate of head block
        int headY = BlockList.getFirst().getY(); // y coordinate of head block
        return IntStream.range(1, BlockList.size()).anyMatch(i -> headX == BlockList.get(i).getX() && headY == BlockList.get(i).getY());
    }

    void move() {

        if (!hitBody() && !hitBoundary()) {

            int x = BlockList.get(0).getX();
            int y = BlockList.get(0).getY();


            if (direction == Direction.UP) {
                BlockList.addFirst(new Block(x, y - spaceBtwBlocks));
                BlockList.removeLast();
            }

            if (direction == Direction.DOWN) {
                BlockList.addFirst(new Block(x, y + spaceBtwBlocks));
                BlockList.removeLast();
            }
            if (direction == Direction.LEFT) {
                BlockList.addFirst(new Block(x - spaceBtwBlocks, y));
                BlockList.removeLast();
            }

            if (direction == Direction.RIGHT) {
                BlockList.addFirst(new Block(x + spaceBtwBlocks, y));
                BlockList.removeLast();
            }
        } else endTurn();

    }

    private void actionPerformed() {
        playButton.setVisible(false);
        BlockList.clear();
        BlockList.add(new Block(400, 400));
        direction = Direction.NOTHING;
    }

    public enum Direction {
        RIGHT, LEFT, UP, DOWN, NOTHING

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
            if ((keyPress.getKeyCode() == KeyEvent.VK_RIGHT || keyPress.getKeyCode() == KeyEvent.VK_D) && (direction != Direction.LEFT || BlockList.size() == 1)) {
                direction = Direction.RIGHT;
            }

            // change direction to left if left arrow key or A key is pressed. Do not change if curr direction is right
            else if ((keyPress.getKeyCode() == KeyEvent.VK_LEFT || keyPress.getKeyCode() == KeyEvent.VK_A) && (direction != Direction.RIGHT || BlockList.size() == 1)) {
                direction = Direction.LEFT;
            }
            // change direction to up if up arrow key or W key is pressed. Do not change if curr direction is down
            else if ((keyPress.getKeyCode() == KeyEvent.VK_UP || keyPress.getKeyCode() == KeyEvent.VK_W) && (direction != Direction.DOWN || BlockList.size() == 1)) {
                direction = Direction.UP;

            }
            // change direction to down if left arrow key or s key is pressed. Do not change if curr direction is up
            else if ((keyPress.getKeyCode() == KeyEvent.VK_DOWN || keyPress.getKeyCode() == KeyEvent.VK_S) && (direction != Direction.UP || BlockList.size() == 1)) {
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

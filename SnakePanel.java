import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private LinkedList<Block> snake = new LinkedList<>(); // linked list representing snake
    private Direction direction;
    private JButton playButton;


    /**
     * Instance object that contains the panel that will open when user want to play
     */
    SnakePanel() {

        setBackground(Color.getHSBColor(23, .5f, .9f)); // color of panel (Salmon)
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // size of panel on screen
        snake.add(new Block(400, 400)); // create snake head
        addKeyListener(new BlockListener()); // listens for directional changes
        this.setFocusable(true); // allows snake to move
        playButton = new JButton("Play Again"); // button shown if snake dies
        this.add(playButton);
        playButton.setVisible(false);

    }

    /**
     * creates the area where the snake can move, along with the snake itself
     *
     * @param page user interface to build on top off
     */
    protected void paintComponent(Graphics page) {
        super.paintComponent(page);
        Graphics2D snakeGameArea = (Graphics2D) page;
        snakeGameArea.setColor(Color.getHSBColor(.623f, .9f, .5f)); // set color of area snake can go
        snakeGameArea.fillRect(gameAreaStartX, gameAreaStartY, gameAreaEndx, gameAreaEndY); // create snake area



        // generate current snake
        snakeGameArea.setColor(Color.yellow); // set color of snake
        snake.forEach(block -> snakeGameArea.fillRect(block.getX(), block.getY(), BLOCK_WIDTH, BLOCK_HEIGHT));

        // generate food location
        snakeGameArea.setColor(Color.red); // set color of food
        snakeGameArea.fillRect(food.getX(), food.getY(), BLOCK_WIDTH, BLOCK_HEIGHT); // draw food

        // generate length of snake display
        snakeGameArea.setColor(Color.white);
        snakeGameArea.setFont(font);
        snakeGameArea.drawString("Length: " + snake.size(), gameAreaEndx - 130, gameAreaEndY + 90);


    }



    void checkIfFoodEaton() {
        if (snake.getFirst().getX() == food.getX() &&  snake.getFirst().getY() == food.getY()) {
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

    /**
     * helper method that grows snake body
     */
    private void growSnake(int addToX, int addToY){
        int index;
    if(snake.size() ==1 ){
         index = 0;
    } else{
        index = 1;
    }

        for (int count = 0; count < 5; count++){

            snake.add(1, new Block(snake.get(index).getX() + addToX,
                    snake.get(index).getY() + addToY));
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
            for (Block block : snake) {
                if (food.getX() == block.getX() && food.getY() == block.getY()) {
                    foodNotOnSnake.set(false);
                    break;
                }
            }

        } while (!foodNotOnSnake.get());
    }

    /**
     * ask the player if they would like to play again
     */
    private void playAgain() {
        playButton.setVisible(true);
        playButton.setBounds(gameAreaStartX * 14, gameAreaStartY * 5, 400, 100);
        playButton.addActionListener(e -> actionPerformed());

    }

    /**
     * resets game if playAgain button is pushed
     */
    private void actionPerformed() {
        playButton.setVisible(false);
        snake.clear();
        snake.add(new Block(400, 400));
        food.generateNewFood();
        direction = Direction.NOTHING;
    }



    /**
     * moves the snake in the direction determined my the previous directional key
     */
    void move() {

        if (!hitBody() && !hitBoundary()) {

            int x = snake.get(0).getX();
            int y = snake.get(0).getY();


            if (direction == Direction.UP) {
                snake.addFirst(new Block(x, y - spaceBtwBlocks));
                snake.removeLast();
            }

            if (direction == Direction.DOWN) {
                snake.addFirst(new Block(x, y + spaceBtwBlocks));
                snake.removeLast();
            }
            if (direction == Direction.LEFT) {
                snake.addFirst(new Block(x - spaceBtwBlocks, y));
                snake.removeLast();
            }

            if (direction == Direction.RIGHT) {
                snake.addFirst(new Block(x + spaceBtwBlocks, y));
                snake.removeLast();
            }
        } else playAgain();

    }

    /**
     * check if the snake has hit a boundary
     *
     * @return true if snake head is in a boundary
     */
    private boolean hitBoundary() {
        int x = snake.getFirst().getX();
        int y = snake.getFirst().getY();
        return y > gameAreaEndY || y < gameAreaStartY || x > gameAreaEndx || x < gameAreaStartX;
    }

    /**
     * checks if the snake ran into itself
     * @return true if snake head is at location of any part of body
     */
    private boolean hitBody() {
        int headX = snake.getFirst().getX(); // x coordinate of head block
        int headY = snake.getFirst().getY(); // y coordinate of head block
        return IntStream.range(1, snake.size()).anyMatch(i -> headX == snake.get(i).getX() && headY == snake.get(i).getY());
    }



    public enum Direction {
        RIGHT, LEFT, UP, DOWN, NOTHING

    }

    private class BlockListener implements KeyListener {

        public void keyPressed2(KeyEvent keyPress) {

            if(snake.size() == 1 || (snake.getFirst().getX() - spaceBtwBlocks != snake.get(1).getX())) {
                System.out.println("confused");
            }


            // change direction to right if right arrow key or D key is pressed. Do not change if curr direction is
            // left
            if ((keyPress.getKeyCode() == KeyEvent.VK_RIGHT || keyPress.getKeyCode() == KeyEvent.VK_D) && (direction != Direction.LEFT || snake.size() == 1)) {
                direction = Direction.RIGHT;
            }

            // change direction to left if left arrow key or A key is pressed. Do not change if curr direction is right
            else if ((keyPress.getKeyCode() == KeyEvent.VK_LEFT || keyPress.getKeyCode() == KeyEvent.VK_A) && (direction != Direction.RIGHT || snake.size() == 1)) {
                direction = Direction.LEFT;
            }
            // change direction to up if up arrow key or W key is pressed. Do not change if curr direction is down
            else if ((keyPress.getKeyCode() == KeyEvent.VK_UP || keyPress.getKeyCode() == KeyEvent.VK_W) && (direction != Direction.DOWN || snake.size() == 1)) {
                direction = Direction.UP;

            }
            // change direction to down if left arrow key or s key is pressed. Do not change if curr direction is up
            else if ((keyPress.getKeyCode() == KeyEvent.VK_DOWN || keyPress.getKeyCode() == KeyEvent.VK_S) && (direction != Direction.UP || snake.size() == 1)) {
                direction = Direction.DOWN;
            }

        }

        public void keyPressed(KeyEvent keyPress) {

//            try {
//                Thread.sleep(25);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            // change direction to right if right arrow key or D key is pressed. Do not change if curr direction is
            // left
            if ((keyPress.getKeyCode() == KeyEvent.VK_RIGHT || keyPress.getKeyCode() == KeyEvent.VK_D) ) {
                if(snake.size() == 1 || (snake.getFirst().getX() + spaceBtwBlocks != snake.get(1).getX())) {
                    direction = Direction.RIGHT;
                }

            }

            // change direction to left if left arrow key or A key is pressed. Do not change if curr direction is right
            else if ((keyPress.getKeyCode() == KeyEvent.VK_LEFT || keyPress.getKeyCode() == KeyEvent.VK_A) ) {
                if(snake.size() == 1 || (snake.getFirst().getX() - spaceBtwBlocks != snake.get(1).getX())) {
                    direction = Direction.LEFT;
                }

            }
            // change direction to up if up arrow key or W key is pressed. Do not change if curr direction is down
            else if ((keyPress.getKeyCode() == KeyEvent.VK_UP || keyPress.getKeyCode() == KeyEvent.VK_W) ) {
                if(snake.size() == 1 || (snake.getFirst().getY() - spaceBtwBlocks != snake.get(1).getY())) {
                    direction = Direction.UP;
                }


            }
            // change direction to down if left arrow key or s key is pressed. Do not change if curr direction is up
            else if ((keyPress.getKeyCode() == KeyEvent.VK_DOWN || keyPress.getKeyCode() == KeyEvent.VK_S) ) {
                if(snake.size() == 1 || (snake.getFirst().getY() + spaceBtwBlocks != snake.get(1).getY())) {
                    direction = Direction.DOWN;
                }


            }

        }



        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {

        }
    }


}

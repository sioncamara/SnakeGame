import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Snake extends JPanel {

    private final int WINDOW_WIDTH = 1460;
    private final int WINDOW_HEIGHT = 900;
    private final int BLOCK_WIDTH = 35;
    private final int BLOCK_HEIGHT = 35;
    private final int DISTANCE_TO_MOVE = 40;
    private final int ADD_TO_NEW_BLOCK = 10;
    private boolean Left, Up, Down, Right;
    private Font font = new Font("Roman", Font.PLAIN, 38);
    private Food f = new Food();

    // x and y corrdinated for the start and end of game play area
    private int gameAreaStartX = 50;
    private int gameAreaStartY = 50;
    private int gameAreaEndx = WINDOW_WIDTH - 120;
    private int gameAreadEndY = WINDOW_HEIGHT - 100;

    private ArrayList<Block> BlockList = new ArrayList<>();
    private Direction direction;
    private JButton play;


     Snake() {

        setBackground(Color.getHSBColor(23, .5f, .9f));

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        BlockList.add(new Block(400, 400));
        addKeyListener(new BlockListener());
        this.setFocusable(true);
        play = new JButton("Play Again");
        add(play);
        play.setVisible(false);

    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        Graphics2D g = (Graphics2D) page;
        g.setColor(Color.getHSBColor(.623f, .9f, .5f));
        g.fillRect(gameAreaStartX, gameAreaStartY, gameAreaEndx, gameAreadEndY);
        g.setColor(Color.yellow);
        for (Block drawBlock : BlockList) {
            g.fillRect(drawBlock.x(), drawBlock.y(), BLOCK_WIDTH, BLOCK_HEIGHT);

        }
        g.setColor(Color.red);
        g.fillRect(f.x(), f.y(), BLOCK_WIDTH, BLOCK_HEIGHT);
        int length = BlockList.size();
        g.setColor(Color.black);
        g.setFont(font);
        g.drawString("Length: " + length, WINDOW_WIDTH - 350, WINDOW_HEIGHT - 60);



    }

     void eatfood() {
        int x = BlockList.get(0).x();
        int y = BlockList.get(0).y();
        int add2 = BlockList.size() - 1;
        if (x == f.x() && y == f.y()) {
            for (int i = 0; i < 5; i++) {
                if(add2 < 10){
                    if (direction == Direction.DOWN) {
                        BlockList.add(new Block(BlockList.get(add2).x(), BlockList.get(add2).y() - DISTANCE_TO_MOVE));

                    } else if (direction == Direction.UP) {
                        BlockList.add(new Block(BlockList.get(add2).x(), BlockList.get(add2).y() + DISTANCE_TO_MOVE));

                    } else if (direction == Direction.LEFT) {
                        BlockList.add(new Block(BlockList.get(add2).x() + DISTANCE_TO_MOVE, BlockList.get(add2).y()));

                    } else {
                        BlockList.add(new Block(BlockList.get(add2).x() - DISTANCE_TO_MOVE, BlockList.get(add2).y()));
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
            f.newpoints();
            boolean isAlone = true;
            for (Block block : BlockList) {
                if (f.x() == block.x() && f.y() == block.y()) {
                    isAlone = false;
                }
            }
            while (!isAlone) {
                f.newpoints();
                isAlone = true;
                for (Block block : BlockList) {
                    if (f.x() == block.x() && f.y() == block.y()) {
                        isAlone = false;
                    }
                }

            }

        }
    }

    private void crash(){
        play.setVisible(true);
        play.setBounds(1150, 700, 400, 100);
        play.addActionListener(this::actionPerformed);

    }
    private boolean boundry() {
        int x = BlockList.get(0).x();
        int y = BlockList.get(0).y();
        if (x > gameAreaEndx + 40 || x < gameAreaStartX) {
            return true;
        } else return y > gameAreadEndY + 40 || y < gameAreaStartY;

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
        play.setVisible(false);
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

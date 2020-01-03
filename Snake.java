import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Snake extends JPanel {

    private final int WIDTH = 1460, HEIGHT = 900, bwidth = 35, bheight = 35, bd = 40, add = 10;
    private boolean Left, Up, Down, Right;
    private Font font = new Font("Roman", Font.ROMAN_BASELINE, 38);;
    private Food f = new Food();

    // x and y corrdinated for the start and end of game play area
    private int gameAreaStartX = 80, gameAreaStartY = 80, gameAreaEndx = 860, gameAreadEndY = 360;
    private ArrayList<Block> BlockList = new ArrayList<Block>();
    private Direction d;
    private JButton play;


    public Snake() {

        setBackground(Color.getHSBColor(23, .5f, .9f));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
            g.fillRect(drawBlock.x, drawBlock.y, bwidth, bheight);

        }
        g.setColor(Color.red);
        g.fillRect(f.x(), f.y(), bwidth, bheight);
        int length = BlockList.size();
        g.setColor(Color.black);
        g.setFont(font);
        g.drawString("Length: " + length, WIDTH - 350, HEIGHT - 60);



    }

    public void eatfood() {
        int x = BlockList.get(0).x;
        int y = BlockList.get(0).y;
        int add2 = BlockList.size() - 1;
        if (x == f.x() && y == f.y()) {
            for (int i = 0; i < 5; i++) {
                if(add2 < 10){
                    if (d == Direction.DOWN) {
                        BlockList.add(new Block(BlockList.get(add2).x, BlockList.get(add2).y - bd));

                    } else if (d == Direction.UP) {
                        BlockList.add(new Block(BlockList.get(add2).x, BlockList.get(add2).y + bd));

                    } else if (d == Direction.LEFT) {
                        BlockList.add(new Block(BlockList.get(add2).x + bd, BlockList.get(add2).y));

                    } else {
                        BlockList.add(new Block(BlockList.get(add2).x - bd, BlockList.get(add2).y));
                    }
                }
                else
                if (d == Direction.DOWN) {
                    BlockList.add(new Block(BlockList.get(add).x, BlockList.get(add).y - bd));

                } else if (d == Direction.UP) {
                    BlockList.add(new Block(BlockList.get(add).x, BlockList.get(add).y + bd));

                } else if (d == Direction.LEFT) {
                    BlockList.add(new Block(BlockList.get(add).x + bd, BlockList.get(add).y));

                } else {
                    BlockList.add(new Block(BlockList.get(add).x - bd, BlockList.get(add).y));
                }

            }
            f.newpoints();
            boolean isAlone = true;
            for (int i = 0; i < BlockList.size(); i++) {
                if (f.x() == BlockList.get(i).x && f.y() == BlockList.get(i).y) {
                    isAlone = false;
                }
            }
            while (!isAlone) {
                f.newpoints();
                isAlone = true;
                for (int i = 0; i < BlockList.size(); i++) {
                    if (f.x() == BlockList.get(i).x && f.y() == BlockList.get(i).y) {
                        isAlone = false;
                    }
                }

            }

        }
    }

    public void crash(){
        play.setVisible(true);
        play.setBounds(1150, 700, 400, 100);
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                play.setVisible(false);
                BlockList.clear();
                BlockList.add(new Block(400,400));
                d = Direction.nothing;
            }
        });

    }
    public boolean boundry() {
        int x = BlockList.get(0).x;
        int y = BlockList.get(0).y;
        if (x > gameAreaEndx + 40 || x < gameAreaStartX) {
            return true;
        } else if (y > gameAreadEndY + 40 || y < gameAreaStartY) {
            return true;
        } else {
            return false;
        }

    }

    public boolean hitbody() {
        int x = BlockList.get(0).x;
        int y = BlockList.get(0).y;
        for (int i = 1; i < BlockList.size(); i++) {

            if (x == BlockList.get(i).x && y == BlockList.get(i).y) {
                return true;
            }

        }
        return false;
    }

    public void move() {

        if (hitbody() == false && boundry() == false) {

            int x = BlockList.get(0).x;
            int y = BlockList.get(0).y;


            if (d == Direction.UP) {
                BlockList.add(0, new Block(x, y - bd));
                BlockList.remove(BlockList.size() - 1);
            }

            if (d == Direction.DOWN) {
                BlockList.add(0, new Block(x, y + bd));
                BlockList.remove(BlockList.size() - 1);
            }
            if (d == Direction.LEFT) {
                BlockList.add(0, new Block(x - bd, y));
                BlockList.remove(BlockList.size() - 1);
            }

            if (d == Direction.RIGHT) {
                BlockList.add(0, new Block(x + bd, y));
                BlockList.remove(BlockList.size() - 1);
            }
        }
        else crash();

    }

    public enum Direction {
        RIGHT, LEFT, UP, DOWN, nothing;

    }

    private class BlockListener implements KeyListener {

        public void keyPressed(KeyEvent e) {

            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(SnakeRunner.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && (d != Direction.LEFT || BlockList.size() == 1) ) {
                d = Direction.RIGHT;


            }

            else if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && (d != Direction.RIGHT || BlockList.size() ==1)) {
                d = Direction.LEFT;
            }

            else if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && (d != Direction.DOWN || BlockList.size() == 1)) {
                d = Direction.UP;

            }

            else  if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && (d != Direction.UP || BlockList.size() == 1)) {
                d = Direction.DOWN;

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

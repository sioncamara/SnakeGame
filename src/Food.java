import java.util.concurrent.ThreadLocalRandom;

public class Food {

    private int x;
    private int y;
    private int bd = 40;
    private int areaStartX = SnakePanel.gameAreaStartX;
    private int areaStartY = SnakePanel.gameAreaStartY;
    private int areaEndx = SnakePanel.gameAreaEndx;
    private int areadEndY = SnakePanel.gameAreaEndY;



    public Food() {
        x = ThreadLocalRandom.current().nextInt(areaStartX, areaEndx);
        y = ThreadLocalRandom.current().nextInt(areaStartY, areadEndY);

        while (x % 40 != 0) {
            x = ThreadLocalRandom.current().nextInt(areaStartX, areadEndY);
        }

        while (y % 40 != 0) {

            y = ThreadLocalRandom.current().nextInt(areaStartY, areadEndY);
        }


    }



    public void generateNewFood() {
        x = ThreadLocalRandom.current().nextInt(areaStartX, areaEndx);
        y = ThreadLocalRandom.current().nextInt(areaStartY, areadEndY);

        while (x % 40 != 0) {
            x = ThreadLocalRandom.current().nextInt(areaStartX, areaEndx);

        }

        while (y % 40 != 0) {
            y = ThreadLocalRandom.current().nextInt(areaStartY, areadEndY);
        }



    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

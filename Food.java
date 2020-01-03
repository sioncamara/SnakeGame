import java.util.concurrent.ThreadLocalRandom;

public class Food {

    private int x;
    private int y;
    private int bd = 40;
    private int areaStartX = Snake.gameAreaStartX;
    private int areaStartY = Snake.gameAreaStartY;
    private int areaEndx = Snake.gameAreaEndx;
    private int areadEndY = Snake.gameAreadEndY;



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



    public void newpoints() {
        x = ThreadLocalRandom.current().nextInt(areaStartX, areaEndx);
        y = ThreadLocalRandom.current().nextInt(areaStartY, areadEndY);

        while (x % 40 != 0) {
            x = ThreadLocalRandom.current().nextInt(areaStartX, areaEndx);

        }

        while (y % 40 != 0) {
            y = ThreadLocalRandom.current().nextInt(areaStartY, areadEndY);
        }



    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}

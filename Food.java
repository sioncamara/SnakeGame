import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Food {

    private int x;
    private int y;
    private int bd = 40;
    private int rxs = 80;
    private int rxe = 2560;
    private int rys = 80;
    private int rye = 1360;



    public Food() {
        x = ThreadLocalRandom.current().nextInt(rxs, rxe);
        y = ThreadLocalRandom.current().nextInt(rys, rye);

        while (x % 40 != 0) {
            x = ThreadLocalRandom.current().nextInt(rxs, rye);
        }

        while (y % 40 != 0) {

            y = ThreadLocalRandom.current().nextInt(rys, rye);
        }


    }



    public void newpoints() {
        x = ThreadLocalRandom.current().nextInt(rxs, rxe);
        y = ThreadLocalRandom.current().nextInt(rys, rye);

        while (x % 40 != 0) {
            x = ThreadLocalRandom.current().nextInt(rxs, rxe);

        }

        while (y % 40 != 0) {
            y = ThreadLocalRandom.current().nextInt(rys, rye);
        }



    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}

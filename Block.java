
public class Block {
    private int getX;
    private int y;

     Block(int xcoordinate, int ycoordinate){
        getX = xcoordinate;
        y = ycoordinate;

    }
     int x(){
        return getX;
    }

     int y(){
        return y;
    }

    public void addx(int increment){
        getX += increment;
    }

    public void addy(int increment){
        y+= increment;
    }

    public void subtractx(int increment){
        getX -= increment;
    }
    public void subtracty(int increment){
        y-= increment;
    }

    public void setx(int value){
        getX = value;
    }

    public void sety(int value){
        y = value;
    }

    public void set(Block b)
    {
        this.getX = b.getX;
        this.y = b.y;
    }


}
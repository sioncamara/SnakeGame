public class Block {
    public int x;
    public int y;

    public Block(int xcoordinate, int ycoordinate){
        x = xcoordinate;
        y = ycoordinate;

    }
    public int x(){
        return x;
    }

    public int y(){
        return y;
    }

    public void addx(int increment){
        x+= increment;
    }

    public void addy(int increment){
        y+= increment;
    }

    public void subtractx(int increment){
        x-= increment;
    }
    public void subtracty(int increment){
        y-= increment;
    }

    public void setx(int value){
        x = value;
    }

    public void sety(int value){
        y = value;
    }

    public void set(Block b)
    {
        this.x = b.x;
        this.y = b.y;
    }


}
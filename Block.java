import java.awt.*;

public class Block {
    private Point topLeft, btmRight;

    public Block(int topLeftX, int topLeftY, int btmRightX, int btmRightY) {
        this.topLeft = new Point(topLeftX, topLeftY);
        this.btmRight = new Point(btmRightX, btmRightY);
    }

    public boolean isOk() {
        return topLeft.x <= btmRight.x && topLeft.y <= btmRight.y;
    }

    public double distance(Block blk) {
        return this.topLeft.distance(blk.getTopLeft());
    }

    /************************************************************
     *                 Getter/Setter/Override                   *
     ************************************************************/

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBtmRight() {
        return btmRight;
    }

    public int getHeight() {
        return topLeft.x - btmRight.x;
    }

    public int getWidth() {
        return topLeft.y - btmRight.y;
    }

    @Override
    public Block clone() {
        return new Block(this.topLeft.x, this.topLeft.y, this.btmRight.x, this.btmRight.y);
    }

    @Override
    public int hashCode() {
        final int PRIME = 97;
        int sum = 1;

        sum = PRIME*sum + this.topLeft.x;
        sum = PRIME*sum + this.topLeft.y;
        sum = PRIME*sum + this.btmRight.x;
        sum = PRIME*sum + this.btmRight.y;

        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Block)) // This checks for null too.
            return false;
        else if(obj == this)
            return true;
        else {
            return this.topLeft.equals(((Block) obj).topLeft) && this.btmRight
                    .equals(((Block) obj).btmRight);
        }
    }

    @Override
    public String toString() {
        return "TL: (" + topLeft.x + ", " + topLeft.y + ")\tBR: (" + btmRight.x + " ," + btmRight.y + ")";
    }
}
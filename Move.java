public class Move {
    private Block block;
    private String direction;

    public Move(Block block, String direction) {
        this.block = block;
        this.direction = direction;
    }

    public Block getBlock()
    {
        return block;
    }

    public String getDirection()
    {
        return direction;
    }

    @Override
    public String toString() {
        int x = this.block.getTopLeft().x;
        int y = this.block.getTopLeft().y;
        int newX = x, newY = y;

        switch (this.direction) {
            case "up":
                --newX;
                break;
            case "down":
                ++newX;
                break;
            case "left":
                --newY;
                break;
            case "right":
                ++newY;
        }
        return x + " " + y + " " + newX + " " + newY;
    }
}

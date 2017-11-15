import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Board implements Comparable<Board>{
    private boolean[][] board;
    public HashSet<Block> blocks;
    private Board parent;
    private String move;
    private double rank;

    public Board(int length, int width) {
        this.board = new boolean[length][width];
        this.blocks = new HashSet<>();
        this.rank = -1;
    }

    public Board(String[] args) throws IllegalStateException {
        int initialConfigFileIndex;

        if(args.length == 2)
            initialConfigFileIndex = 0;
        else if (args.length == 3)
            initialConfigFileIndex = 1;
        else {
            throw new IllegalStateException("Invalid number of files argument!");
        }

        this.blocks = new HashSet<>();
        this.rank = -1;
        Input in = new Input(args[initialConfigFileIndex]);

        while(true) {
            String line = in.readLine();

            if(line == null)
                return;

            StringTokenizer token = new StringTokenizer(line);
            if(line.matches("[0-9]+ [0-9]+")) {
                board = new boolean[Integer.parseInt(token.nextToken())][Integer.parseInt((token.nextToken()))];
            } else if(line.matches("[0-9]+ [0-9]+ [0-9]+ [0-9]+")) {
                int width = Integer.parseInt(token.nextToken());
                int height = Integer.parseInt(token.nextToken());
                int TLx = Integer.parseInt(token.nextToken());
                int TLy = Integer.parseInt(token.nextToken());
                int BLx = TLx + width - 1;
                int BLy = TLy + height - 1;
                this.addBlock(new Block(TLx, TLy, BLx, BLy));
            }
        }
    }

    public void addBlock(Block blk) {
        int TLx = blk.getTopLeft().x;
        int TLy = blk.getTopLeft().y;
        int BRx = blk.getBtmRight().x;
        int BRy = blk.getBtmRight().y;

        for(int i = TLx; i <= BRx; i++)
            for(int j = TLy; j <= BRy; j++)
                this.board[i][j] = true;

        blocks.add(blk);
    }

    public ArrayList<Move> possibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();

        for(Block b : this.blocks) {
            boolean U = true, D = true, L = true, R = true;
            final int TLx = b.getTopLeft().x;
            final int TLy = b.getTopLeft().y;
            final int BRx = b.getBtmRight().x;
            final int BRy = b.getBtmRight().y;

            if(TLx == 0)
                U = false;
            if(BRx == this.board.length - 1)
                D = false;
            if(TLy == 0)
                L = false;
            if(BRy == this.board[0].length - 1)
                R = false;

            for(int i = TLy; i <= BRy; i++) {
                if(U && TLx - 1 >= 0 && this.board[TLx - 1][i])
                    U = false;
                if(D && BRx + 1 < this.board.length && this.board[BRx + 1][i])
                    D = false;
            }

            for(int i = TLx; i <= BRx; i++) {
                if(L && TLy - 1 >= 0 && this.board[i][TLy - 1])
                    L = false;
                if(R && BRy + 1 < this.board[i].length && this.board[i][BRy + 1])
                    R = false;
            }

            if(U)
                moves.add(new Move(b, "up"));
            if(D)
                moves.add(new Move(b, "down"));
            if(L)
                moves.add(new Move(b, "left"));
            if(R)
                moves.add(new Move(b, "right"));

        }
        return moves;
    }

    public Board makeMove(Move mv) {
        Board copy = new Board(this.board.length, this.board[0].length);

        for(int i = 0; i < this.board.length; i++)
            for(int j = 0; j < this.board[0].length; j++)
                copy.board[i][j] = this.board[i][j];

        for(Block b : this.blocks) {
            if(b.equals(mv.getBlock())) {
                int TLx = b.getTopLeft().x;
                int TLy = b.getTopLeft().y;
                int BRx = b.getBtmRight().x;
                int BRy = b.getBtmRight().y;

                switch(mv.getDirection()) {
                    case "up":
                        --TLx;
                        --BRx;
                        copy.moveBlockVertical(b, -1);
                        break;
                    case "down":
                        ++TLx;
                        ++BRx;
                        copy.moveBlockVertical(b, 1);
                        break;
                    case "left":
                        --TLy;
                        --BRy;
                        copy.moveBlockHorizontal(b, -1);
                        break;
                    case "right":
                        ++TLy;
                        ++BRy;
                        copy.moveBlockHorizontal(b, 1);
                }
                copy.blocks.add(new Block(TLx, TLy, BRx, BRy));
            } else {
                copy.blocks.add(b.clone());
            }
        }
        return copy;
    }

    public void moveBlockVertical(Block blk, int dir) {
        final int TLx = blk.getTopLeft().x;
        final int TLy = blk.getTopLeft().y;
        final int BRx = blk.getBtmRight().x;
        final int BRy = blk.getBtmRight().y;

        for(int i = TLx; i <= BRx; i++)
            for(int j = TLy; j <= BRy; j++)
                this.board[i][j] = false;

        for(int i = TLx+dir; i <= BRx+dir; i++)
            for(int j = TLy; j <= BRy; j++)
                this.board[i][j] = true;
    }

    public void moveBlockHorizontal(Block blk, int dir) {
        final int TLx = blk.getTopLeft().x;
        final int TLy = blk.getTopLeft().y;
        final int BRx = blk.getBtmRight().x;
        final int BRy = blk.getBtmRight().y;

        for(int i = TLx; i <= BRx; i++)
            for(int j = TLy; j <= BRy; j++)
                board[i][j] = false;

        for(int i = TLx; i <= BRx; i++)
            for(int j = TLy+dir; j <= BRy+dir; j++)
                board[i][j] = true;
    }

    public void setRank(ArrayList<Block> goal) {
        double rank = 0;

        for(Block a : this.blocks) {
            for(Block b : goal) {
                if(a.getHeight() == b.getHeight() && a.getWidth() == b.getWidth()) {
                    rank += a.distance(b);
                }
            }
        }
        this.rank = rank;
    }

    public void isOk() throws IllegalStateException {
        boolean[][] existingBoard = new boolean[this.board.length][this.board[0].length];

        for(Block b : blocks) {
            if(!b.isOk())
                throw new IllegalStateException("Block: " + b + " is malformed");

            int TLx = b.getTopLeft().x;
            int TLy = b.getTopLeft().y;
            int BRx = b.getBtmRight().x;
            int BRy = b.getBtmRight().y;

            if(TLx < 0 || TLy < 0 || BRx < 0 || BRy < 0 ||
                    TLx > this.board.length || TLy > this.board[0].length ||
                    BRx > this.board.length || BRy > this.board[0].length)
                throw new IllegalStateException("Block: " + b + " index out of bound");

            for(int i = TLx; i <= BRx; i++) {
                for(int j = TLy; j <= BRy; j++) {
                    if(!existingBoard[i][j])
                        existingBoard[i][j] = true;
                    else
                        throw new IllegalStateException("Block: " + b + " overlaps with another block");
                }
            }
        }

        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[0].length; j++) {
                if(this.board[i][j] != existingBoard[i][j])
                    throw new IllegalStateException("Block HashSet does not match Board matrix at: row = " + i + ", col = " + j);
            }
        }
    }

    public void printBoard()
    {
        String separator = "";
        String data = "";
        char cell;

        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[0].length; j++) {
                if(this.board[i][j])
                    cell = 'B';
                else
                    cell = ' ';

                separator += "+---";
                data += "| " + cell + " ";
            }

            separator += "+";
            data += "|";
            if(i == 0)
                System.out.println(separator);
            System.out.println(data);
            System.out.println(separator);
            separator = data = "";
        }
    }

    /************************************************************
     *                 Getter/Setter/Override                   *
     ************************************************************/

    public Board getParent() {
        return parent;
    }

    public void setParent(Board parent) {
        this.parent = parent;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public double getRank() {
        return rank;
    }

    public int compareTo(Board brd) {
        if(this.rank == brd.rank)
            return 0;
        else if(this.rank < brd.rank)
            return -1;
        else
            return 1;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        for(Block b : this.blocks) {
            hash += b.hashCode()*3;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Board))
            return false;
        else if(obj == this)
            return true;
        else
            return this.blocks.equals(((Board) obj).blocks);
    }
}

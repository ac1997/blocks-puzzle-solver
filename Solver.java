import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Solver {
    private HashSet<Board> visited;
    private PriorityQueue<Board> boardQueue;
    private boolean debuggingSolver1, debuggingSolver2, debuggingBoard;
    public ArrayList<Block> goalConfig = new ArrayList<Block>();

    public Solver(String[] args) throws IllegalStateException {
        if(args.length > 3){
            System.err.println("Invalid number of arguments passed.");
            System.exit(1);
        }

        visited = new HashSet<>();
        boardQueue = new PriorityQueue<>();
        boardQueue.add(new Board(args));
        getFinalConfig(args);

        switch (args[0])
        {
            case "-odebuggingSolver1":
                debuggingSolver1 = true;
                break;
            case "-odebuggingSolver2":
                debuggingSolver2 = true;
                break;
            case "-odebuggingBoard":
                debuggingBoard = true;
                break;
            case "-ooptions":
                System.out.println("-odebuggingSolver1: Prints all moves that have been tried so far");
                System.out.println("-odebuggingSolver2: Prints all moves that are added to the priority queue and prints current board");
                System.out.println("-odebuggingBoard: Calls isOk methods on board and block and prints current board and boards added");
                break;
//            default:
//                System.err.println("Invalid debugging option.");
//                System.exit(1);
        }
    }

    private void getFinalConfig(String[] args) throws IllegalStateException {
        int finalConfigFileIndex;

        if(args.length == 2)
            finalConfigFileIndex = 1;
        else if (args.length == 3)
            finalConfigFileIndex = 2;
        else
            throw new IllegalStateException("Invalid number of files argument!");

        Input in = new Input(args[finalConfigFileIndex]);
        while(true) {
            String line = in.readLine();

            if(line == null)
                return;
            else if(line.matches("[0-9]+ [0-9]+ [0-9]+ [0-9]+")) {
                StringTokenizer token = new StringTokenizer(line);
                int width = Integer.parseInt(token.nextToken());
                int height = Integer.parseInt(token.nextToken());
                int TLx = Integer.parseInt(token.nextToken());
                int TLy = Integer.parseInt(token.nextToken());
                int BLx = TLx + width - 1;
                int BLy = TLy + height - 1;
                goalConfig.add(new Block(TLx, TLy, BLx, BLy));
            }
        }
    }

    public void solve() throws IllegalStateException {
        while(!boardQueue.isEmpty()) {
            Board current = boardQueue.poll();

            if(debuggingSolver1)
                System.out.println("Move: " + current.getMove());
            else if(debuggingBoard) {
                System.out.println("Current board is:");
                current.printBoard();
                System.out.println();
            }

            if(this.isGoal(current)) {
                Board copy = current;
                String output = "";


                //System.out.println("Solution found! Final config:");
                //current.printBoard();

                while(copy.getParent() != null) {
                    output = copy.getMove() + "\n" + output;
                    copy = copy.getParent();
                }

                System.out.println(output.trim());
                return;

            }else if(visited.add(current)) {
                ArrayList<Move> legitMoves = current.possibleMoves();

                if(debuggingSolver2) {
                    System.out.println("Looking for moves for current board:");
                    current.printBoard();
                    System.out.println();
                }

                for(Move m : legitMoves) {
                    Board altered = current.makeMove(m);

                    if(debuggingSolver2)
                        System.out.println("Move added: " + m);
                    else if(debuggingBoard) {
                        altered.isOk();
                        System.out.println("Board added:");
                        altered.printBoard();
                        System.out.println();
                    }

                    altered.setRank(this.goalConfig);
                    boardQueue.add(altered);
                    altered.setParent(current);
                    altered.setMove(m.toString());
                }
            }
        }
        System.err.println("No solution found.");
        System.exit(1);
    }

    public boolean isGoal(Board brd) {
        for (Block b : goalConfig)
            if (!brd.blocks.contains(b))
                return false;
        return true;
    }

    public static void main(String[] args) {
        try {
            Solver s = new Solver(args);
            s.solve();
        } catch(Exception e) {
            System.out.println("!!!Error");
        }

    }
}

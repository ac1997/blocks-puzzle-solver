# Blocks Puzzle Solver
## Design and Data Structure ##
* This Sliding Block Puzzle primarily consists of 3 main classes: **Board**, **Block** and **Solver**. Each class keeping track of variables that are necessary to solve the puzzle or traverse back to the initial configuration after the puzzle is solved.

* The **Board** class consists of several crucial data structures that were chosen carefully to optimize the runtime of this program. It is also the class that initialize a board and blocks using the given data in initial configuration file. The **Board** class implements Comaparable to support comparator in PriorityQueue used in **Block** class. The *hashCode* method of this class is also overridden to avoid clustering in HashSet implemented in **Solver** class. Below are the data structures contained in **Board** class:
  * `board`: A 2D Array boolean matrix which is used to store which coordinates are occupied or unoccupied by a **Block** instance at any given moment. This data structure was chosen because it has constant lookup time given a pair of indices. The constructor of the Board class initialize this Array to the given size in the first line of initial configuration file. 
  * `blocks`: A HashSet of **Block** instances placed on the board. The *hashCode* method for **Block** was overridden in order to achieve constant lookup and insert time for `blocks` HashSet.
  * `parent`: A **Board** instance used to traverse back to the original configuration after a solution is found.
  * `move`: A string containing 4 integers stating which move was taken to reach the current board.
  * `rank`: A double used in PriorityQueue. The value of the `rank` is calculated using the sum of distances of blocks with matching dimensions to the blocks in final configuration board.

* The **Block** class is considerably simpler than the other 2 classes. It consists of mainly 2 built-in **Point** instances called `topLeft` and `btmRight`. These 2 variables store the coordinate of the upper left corner and lower right corner of a block. The `x` and `y` variables in the **Point** object correspond to the row and the column of the corner (opposite of how coordinate is defined on Cartesian Plane). This class also consists of several getter methods that return Point object or the width and height of the block. 4 of the methods inherited from **Object** class are also overridden. The *hashCode* method is overridden to avoid clustering in the **Block** hashSet initialized in **Board** class. The *equals* method is overridden so that 2 blocks having same `topLeft` and `btmRight` coordinates are consider the same.

* The **Solver** class is the heart of this Sliding Puzzle Solver program. It consists of *solve* method which search for solution for given initial board configuration and final board configuration. Below are the data structures contained in **Solver** class.
  * `visited`: A HashSet of **Board** instances already visited by the solving algorithm. The *hashCode* method in Board class was overridden in order to achieve constant lookup and insert time which is crucial to meet the time constraints. 
  * `boardQueue`: A PriorityQueue of Board that stores all board configuration after proceeding all possible moves for a given board. The PirorityQueue is organized using `rank` variable and *compareTo* method in **Board** class.
  * `goalConfig`: An ArrayList of **Block** instances of the goal configuration initialized in *getFinalConfig* method.

* The **Move** class is a helper class. It consist of 2 variables, `block`, a **Block** instance and `direction`, a String of either `up`, `down`, `left` or `right` indicating the direction of the movement of the `block`.

## Solving Algorithm ##
* The **Solver** class consists of `solve` method where the heuristic greedy search algorithm is implemented. This method consists of 2 loops: the outer loop runs until the `boardQueue` is empty and the inner loop stops when all possible moves for the current board is processed. Inside the first loop, the algorithm first check if the current board reached final goal configuration, if it is, the program prints the solution (move) by tracing back through `parent` variable and exit with `status 0`. If the goal has not been achieved and the board is not in `visited` HashSet, the algorithm gets all possible move for current board by calling `possibleMoves` method in **Board** class. The `possibleMoves` method returns an **Move** instance ArrayList. For each **Move** in the ArrayList, the move is proccessed by calling `makeove` method in **Board** class. The resulting board after making the move is then added to the `boardQueue` with its `rank` calculated. The `parent` of the resulting board is also set to the current board. The algorithm then proceeds to the next iteration of the outer loop until a solution is found or if all possible boards are visited, the algorithm ends and program exits with `status 1`.

## Alternating Strategies and Tradeoffs ##
* For storing the blocks of a **Board** object, HashSet was the most appropriate choice because it has constant lookup and insert time given that there is no clustering. Although a HashSet would also introduced memory issue if the board is big, but we only concern about the runtime for this program. Other structure such as ArrayList would not support a constant time lookup although it might help with the memory usage.
  
* For the **Board** structure, a boolean 2D-Array is chosen over an ArrayList of cells because the lookup time for an boolean 2D-Array is constant as the structure indices matched the `x` and `y` coordinates of the blocks.

* Although the traceback algorithm to print the steps to reach the goal configuration (after a solution is found) can be implemented using other data structures such as Stack or Queue, it would likely to slow the program down as inserting element to those data structures take time. Hence, a pointer to the parent board is the more ideal solution and only one simple loop tracing through the `parent` variable is required to print the solution by getting the `move` string of the **Board** instance.

* For storing the visited board, HashSet is again the best choice because it support constant lookup and insert time given that an ideal `hashCode` method is provided. Other data structures would significantly slow down the program as it would take more calculation or comparison to check if it contains the given element. Again, although this HashSet introduced memory allocation problem to the program, but we only concern about the runtime of this program and certainly the tradeoff of memory for runtime is more ideal (memory can be retrieved back but time cannot!).

* The breadth-first greedy search algorithm was chosen over depth-first greedy search algorithm because the implementation is relatively simpler and it provides a better performance. A PriorityQueue data structure was chosen over regular Queue structure to optimize the runtime of the program. The `rank` variable was also introduced for heuristic comparison.

* To store the goal configuration, a simple ArrayList of blocks is more ideal than a HashSet. Although a HashSet has a better lookup time, the `hashCode` method written for **Block** HashSet in **Board** class is not ideal at here (too many empty spaces). Since the goal configuration is relatively small, the difference of the lookup time is negligible.

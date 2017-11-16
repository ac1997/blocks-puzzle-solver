# Blocks Puzzle Solver
## Design and Data Structure ##
* This Sliding Block Puzzle primarily consists of 3 main classes: **Board**, **Block** and **Sovler**. Each class keeping track of variables that are neccessary to solve the puzzle or traverse back to the initial configuration after the puzzle is solved.

* The **Board** class consists of several crucial data structures that were chosen carefully to optimize the run time of this program. Below are the data structures contained in **Board** class:
  * 'board': A 2D Array boolean matrix which is used to store which coordinates are occupied or unoccupied by a **Block** instance at any given moment. This data structure was chosen because it has constant 'O(1)' lookup time given a pair of index. The constructor of the Board class initialize this Array to the given size in the first line of initial configuration file. 
  * 'blocks': A HashSet of **Block** instances placed on the 'Board'. The 'hashCode' method for **Block** was overriden in order to achieve constant lookup and insert time for 'blocks' HashSet, which will be explained in more detail in **Block** class.

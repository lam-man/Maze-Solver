/* grid.java */

import java.io.*;
import java.util.Random;

class grid {
  protected int width;
  protected int height;
  protected int[][] board;
  protected position start;
  protected position dest;
  protected int obsPercent;

  /* constructor to generate a grid
  * @param - int width, width of game board, column of the 2-D vector
  * @param - int height, height of game board, row of the 2-D vector
  * @return - return a game board
  **/
  public grid(int width, int height, int obsPercent,
              int startX, int startY,
              int destX, int destY) {
    this.width = width;
    this.height = height;
    this.obsPercent = obsPercent;
    this.board = new int[width][height];
    this.start = new position(startX, startY);
    this.dest = new position(destX, destY);
    this.gridInitializer();
    this.obstacleCreator(this.obsPercent);
  }

  /* default constructor without parameter */ 
  public grid() {
    this(30, 30, 30, 0, 0, 29, 29);
  }
    
  /* boardInitializer(): initialize the input all cells
   * in the game board (2-D vector) to 1, which means available.  
   * @param - int[][] gameBoard, 2-D vector game board. 
   */
  public void gridInitializer() {
      
    for (int row=0; row<this.height; row++) {
      for (int col=0; col<this.width; col++) {
        this.board[col][row] = 1;
      }
    }
  }

  /* obstacleCreator(int obstacleNum) creates certain
   * number of obstacles in the game board.
   * @param int obstacleNum, number of obstacles to create
   **/
  public void obstacleCreator(int obsPercent) {
    Random rand = new Random();
    int obsNum = (this.height * this.width * obsPercent) / 100;
    for (int count=0; count < obsNum;) {
      int obsY = rand.nextInt(height);  // row of obstacle
      int obsX = rand.nextInt(width);   // height of obstacle
      position noOverlay = new position(obsX, obsY);

      if (this.board[obsX][obsY] != 0 &&
          !noOverlay.equal(start) && !noOverlay.equal(dest)) {
        this.board[obsX][obsY] = 0;
        count++;
      }
    }   
  }

  /* getValue(position indexPos) with the index of the position return the
   * value of the index position in grid.
   * @indexPos position used to index
   * @return value in the index position
   **/
  public int getValue(position indexPos) {
    int col = indexPos.x;
    int row = indexPos.y;
    return this.board[col][row];
  }

  // Print the maze grid.
  public void printGrid() {
    for (int i=0; i<this.height; i++) {
      for (int j=0; j<this.width; j++) {
        System.out.print(this.board[j][i] + " ");
      }
      System.out.print("\n");
    }
  }
    
  /* main function is used to test whether the grid is work or
   * not. If it works, it will print out the grid we created. If
   * not, return an error message. 
   */
  public static void main(String[] args) {

    int gridWidth = 0;
    int gridHeight = 0;
    int obsPercent = 0;
    int startX = 0;
    int startY = 0;
    int destX = 0;
    int destY = 0;
    grid newGrid = new grid();

    // Create grid
    if (args.length==0) {
      newGrid = new grid();
    } else if (args.length != 7) {
      System.out.println("Wrong number of arguments:width, height, " +
                         "number of obstacles, startx, startY, destX, and destY");
      System.exit(0);
    } else {
      gridWidth = Integer.parseInt(args[0]);
      gridHeight = Integer.parseInt(args[1]);
      obsPercent = Integer.parseInt(args[2]);
      startX = Integer.parseInt(args[3]);
      startY = Integer.parseInt(args[4]);
      destX = Integer.parseInt(args[5]);
      destY = Integer.parseInt(args[6]);
      
      newGrid = new grid(gridWidth, gridHeight, obsPercent, startX, startY, destX, destY);
    }

    newGrid.printGrid();
  }
    
}
    

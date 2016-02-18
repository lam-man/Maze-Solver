/* robot.java */

import java.util.LinkedList;

/* moveDirection is a class used to decide the directions,
 * including left, front, right, a robot can move.
 * Directions are decided according to the input angle.
 */
class moveDirection {
  protected position left;
  protected position front;
  protected position right;

  /* dirUpdate(int angle): Update the directions according to the angle.
   * @param int angle, input angle
   */
  public void dirUpdate(int angle) {
    switch (angle) {
      case 0:
        left = new position(1, -1);
        front = new position(1, 0);
        right = new position(1, 1);
        break;
      case 45:
        left = new position(1, 0);
        front = new position(1, 1);
        right = new position(0, 1);
        break;
      case 90:
        left = new position(1, 1);
        front = new position(0, 1);
        right = new position(-1, 1);
        break;
      case 135:
        left = new position(0, 1);
        front = new position(-1, 1);
        right = new position(-1, 0);
        break;
      case 180:
        left = new position(-1, 1);
        front = new position(-1, 0);
        right = new position(-1, -1);
        break;
      case 225:
        left = new position(-1, 0);
        front = new position(-1, -1);
        right = new position(0, -1);
        break;
      case 270:
        left = new position(-1, -1);
        front = new position(0, -1);
        right = new position(1, -1);
        break;
      case 315:
        left = new position(0, -1);
        front = new position(1, -1);
        right = new position(1, 0);
        break;
      default:
        left = new position(0, 0);
        front = new position(0, 0);
        right = new position(0, 0);
        break;
    }       
  }
    
}

class robot {
  protected int height, width, perofObs;
  protected int startPx, startPy, destPx, destPy;
  protected sensor discovery;
  protected position currentPos;
  protected int currentAngle;
  protected moveDirection direction = new moveDirection();
  protected int angleCounter;
  protected LinkedList<position> path;
  protected position original;
  protected grid finalGrid;
  protected Visualize sol;
  protected boolean runFlag = true;


  final int tried = 5;
    
  /* constructor to create a new robot.
   * @param int width, maze grid width
   * @param int height, maze grid height
   * @param int perofObs, percentage of obstacles
   * @param int startPx, startPy, start position
   * @param int destPx, destPy, destination position
   */
  public robot(int width, int height, int perofObs, int startPx,
               int startPy, int destPx, int destPy) {
    this.height = width;
    this.width = height;
    this.perofObs = perofObs;
    this.startPx = startPx;
    this.startPy = startPy;
    this.destPx = this.destPx;
    this.destPy = destPy;
    this.discovery =
        new sensor(width, height, perofObs, startPx, startPy, destPx, destPy);
    this.currentPos = new position(startPx, startPy);
    this.discovery.mazeGrid.board[this.startPx][this.startPy] = tried;
    this.angleComputer();
    direction.dirUpdate(this.currentAngle);
    this.angleCounter = 0;
    this.path = new LinkedList<position>();
    this.original = new position(startPx, startPy);
    this.path.add(this.original);
  }

  /* Compute the angle according to the start position and 
   * destination position.*/
  public void angleComputer() {
    if (this.discovery.mazeGrid.start.x < this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y < this.discovery.mazeGrid.dest.y)
      this.currentAngle = 45;
    else if (this.discovery.mazeGrid.start.x < this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y > this.discovery.mazeGrid.dest.y)
      this.currentAngle = 315;
    else if (this.discovery.mazeGrid.start.x == this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y < this.discovery.mazeGrid.dest.y)
      this.currentAngle = 90;
    else if (this.discovery.mazeGrid.start.x == this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y > this.discovery.mazeGrid.dest.y)
      this.currentAngle = 270;
    else if (this.discovery.mazeGrid.start.x > this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y > this.discovery.mazeGrid.dest.y)
      this.currentAngle = 225;
    else if (this.discovery.mazeGrid.start.x > this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y < this.discovery.mazeGrid.dest.y)
      this.currentAngle = 135;
    else if (this.discovery.mazeGrid.start.x < this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y == this.discovery.mazeGrid.dest.y)
      this.currentAngle = 0;
    else if (this.discovery.mazeGrid.start.x > this.discovery.mazeGrid.dest.x &&
        this.discovery.mazeGrid.start.y == this.discovery.mazeGrid.dest.y)
      this.currentAngle = 180;
  } 

  /* A function used to change the angle of robot.
   * It will be called when the current angle offers no choice.
   */
  public void angleTuner() {
    if (this.angleCounter < 7) {
      if (this.currentAngle == 0) {
        this.currentAngle = 360 - 45;
        this.angleCounter += 1;
      } else {
        this.currentAngle -= 45;
        this.angleCounter += 1;
      }
    }
    this.direction.dirUpdate(this.currentAngle);
  }


  /* pathMarker Mark the cell as an element of path when robot moves to it.
   * Change the value of the cell to 5, which means tried.
   * Add the current cell position to solution. Get new angle according to
   * the position and desination. Update directions.
   */
  public void pathMarker() {
    this.angleCounter=0;
    this.discovery.mazeGrid.start.assign(this.currentPos);
    this.discovery.mazeGrid.board[this.currentPos.x][this.currentPos.y] = tried;
    this.path.add(new position(this.currentPos.x, this.currentPos.y));
    this.angleComputer();
    this.direction.dirUpdate(this.currentAngle);
  }
    

  /* Print the elements in the solution path. */
  public void pathPrinter() {
    int steps = this.path.size();
    System.out.println("Stupid maze runner takes " + steps + " steps to get out.");
    for (int i=0; i<steps; i++) {
      position pathEle = this.path.get(i);
      pathEle.printer();
    }
  }

  /* pathFinder used to find a path for the robot to get out of a maze.
   * Including all the rules in this maze runner expert system.
   * One and only one rule will be fired every time when the condition
   * of the rule is satisfied. 
   */
  public void pathFinder() {

    this.discovery.frontCells.clear();
    this.discovery.leftCells.clear();
    this.discovery.rightCells.clear();
    this.discovery.posToCheck.assign(this.currentPos);
    this.discovery.frontChecker(this.direction.front);
    this.discovery.leftChecker(this.direction.left);
    this.discovery.rightChecker(this.direction.right);

    // case 1: destination found
    if (this.currentPos.equal(this.discovery.mazeGrid.dest) ||
        this.discovery.mazeGrid.getValue(this.discovery.mazeGrid.dest) == 5) {
      System.out.println("Robot hits destination, solution found.");
      this.discovery.mazeGrid.printGrid();
      this.pathPrinter();
      this.finalGrid = this.discovery.mazeGrid;
      this.sol = new Visualize(this.finalGrid);
      this.runFlag = false;
      //return this.finalGrid;
      //System.exit(0);
      return;
    }

    // case 2: no solution
    if (this.angleCounter == 7 &&
        this.discovery.allEmpty() &&
        this.currentPos.equal(this.original)) {
      System.out.println("Current position of robot: " +
                         this.currentPos.x + " " + this.currentPos.y);
      System.out.println("Robot cannot find a solution, " +
                         "the maze may have no solution.");
      this.discovery.mazeGrid.printGrid();
      this.finalGrid = this.discovery.mazeGrid;
      this.sol = new Visualize(this.finalGrid);
      this.runFlag = false;
      //return this.finalGrid;
      //System.exit(0);
      return;
    }

    // case 3: destination in front cell
    if (this.runFlag && this.discovery.destInFront()) {
      this.currentPos.move(this.direction.front);  // move step
      this.pathMarker();
      this.pathFinder();
    }

    // case 4: destination in left cell
    if (this.runFlag && this.discovery.destInLeft()) {
      this.currentPos.move(this.direction.left); // move step
      this.pathMarker();
      this.pathFinder();
    }

    // case 5: destination in right cell
    if (this.runFlag && this.discovery.destInRight()){
      this.currentPos.move(this.direction.right);
      this.pathMarker();
      this.pathFinder();
    }

    // case 6: destination not in cell, move front if front cell not empty
    if (this.runFlag && this.discovery.destNotIn() && 
      !this.discovery.frontIsBlocked()) {
      this.currentPos.move(this.direction.front);
      this.pathMarker();
      this.pathFinder();
    }

    // case 7: desination not in cell, front is empty, left available and move left
    if (this.runFlag && this.discovery.destNotIn() && 
      this.discovery.frontIsBlocked() &&
        !this.discovery.leftIsBlocked()) {
      this.currentPos.move(this.direction.left);
      this.pathMarker();
      this.pathFinder();
    }

    // case 8: dest not in cell, front and left are empty, right available and move right
    if (this.runFlag && this.discovery.destNotIn() && 
        this.discovery.frontIsBlocked() &&
        this.discovery.leftIsBlocked() && !this.discovery.rightIsBlocked()) {
      this.currentPos.move(this.direction.right);
      this.pathMarker();
      this.pathFinder();
    }

    // case 9: all cell empty, then change angle
    if (this.runFlag && this.discovery.allEmpty() && this.angleCounter < 7) {
      this.angleTuner();
      System.out.println("Current size of path: " + this.path.size());
      this.pathFinder();
    }

    // case 10: the cell has no choice but go back, then go back
    if (this.runFlag && this.path.size() > 1 && this.discovery.allEmpty() && this.angleCounter == 7) {
      this.path.removeLast();
      this.currentPos.assign(this.path.getLast());
      this.angleComputer();
      this.angleCounter = 0;
      this.direction.dirUpdate(this.currentAngle);
      this.pathFinder();
    }
    return ;
  }

  /* main method to run a robot. */
  public static void main(String[] args) {
    robot mazeRunner = new robot(30, 30, 40, 0, 0, 29, 29);
    mazeRunner.discovery.mazeGrid.printGrid();
    // mazeRunner.pathFinder();
    mazeRunner.pathFinder();
    //System.exit(0);
    //Visualize sol = new Visualize(finalGrid);
    //sol.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //sol.setTitle("Maze runner path."); 
  } 
}


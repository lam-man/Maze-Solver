/* robot.java */

import java.util.LinkedList;

/** moveDirection is a class used to decide the directions,
 *  including left, front, right, a robot can move.
 *  Directions are decided according to the input angle.
 */
class MoveDirection {
  protected Position left;
  protected Position front;
  protected Position right;

  /** dirUpdate(int angle): Update the directions according to the angle.
   *  @param int angle, input angle
   */
  public void dirUpdate(int angle) {
    switch (angle) {
      case 0:
        left = new Position(1, -1);
        front = new Position(1, 0);
        right = new Position(1, 1);
        break;
      case 45:
        left = new Position(1, 0);
        front = new Position(1, 1);
        right = new Position(0, 1);
        break;
      case 90:
        left = new Position(1, 1);
        front = new Position(0, 1);
        right = new Position(-1, 1);
        break;
      case 135:
        left = new Position(0, 1);
        front = new Position(-1, 1);
        right = new Position(-1, 0);
        break;
      case 180:
        left = new Position(-1, 1);
        front = new Position(-1, 0);
        right = new Position(-1, -1);
        break;
      case 225:
        left = new Position(-1, 0);
        front = new Position(-1, -1);
        right = new Position(0, -1);
        break;
      case 270:
        left = new Position(-1, -1);
        front = new Position(0, -1);
        right = new Position(1, -1);
        break;
      case 315:
        left = new Position(0, -1);
        front = new Position(1, -1);
        right = new Position(1, 0);
        break;
      default:
        left = new Position(0, 0);
        front = new Position(0, 0);
        right = new Position(0, 0);
        break;
    }       
  }
    
}

class Robot {
  protected int height, width, perofObs;
  protected int startPx, startPy, destPx, destPy;
  protected Sensor discovery;
  protected Position currentPos;
  protected int currentAngle;
  protected MoveDirection direction = new MoveDirection();
  protected int angleCounter;
  protected LinkedList<Position> path;
  protected Position original;
  protected Grid finalGrid;
  protected Visualize sol;
  protected boolean runFlag = true;


  final int tried = 5;
    
  /** constructor to create a new robot.
   *  @param int width, maze grid width
   *  @param int height, maze grid height
   *  @param int perofObs, percentage of obstacles
   *  @param int startPx, startPy, start position
   *  @param int destPx, destPy, destination position
   */
  public Robot(int width, int height, int perofObs, int startPx,
               int startPy, int destPx, int destPy) {
    this.height = width;
    this.width = height;
    this.perofObs = perofObs;
    this.startPx = startPx;
    this.startPy = startPy;
    this.destPx = this.destPx;
    this.destPy = destPy;
    this.discovery =
        new Sensor(width, height, perofObs, startPx, startPy, destPx, destPy);
    this.currentPos = new Position(startPx, startPy);
    this.discovery.mazeGrid.board[this.startPx][this.startPy] = tried;
    this.angleComputer();
    direction.dirUpdate(this.currentAngle);
    this.angleCounter = 0;
    this.path = new LinkedList<Position>();
    this.original = new Position(startPx, startPy);
    this.path.add(this.original);
  }

  /** Compute the angle according to the start position and 
   *  destination position.
   */
  public void angleComputer() {
    if (discovery.mazeGrid.start.x < discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y < discovery.mazeGrid.dest.y)
      currentAngle = 45;
    else if (discovery.mazeGrid.start.x < discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y > discovery.mazeGrid.dest.y)
     currentAngle = 315;
    else if (discovery.mazeGrid.start.x == discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y < discovery.mazeGrid.dest.y)
      currentAngle = 90;
    else if (discovery.mazeGrid.start.x == discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y > discovery.mazeGrid.dest.y)
      currentAngle = 270;
    else if (discovery.mazeGrid.start.x > discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y > discovery.mazeGrid.dest.y)
      currentAngle = 225;
    else if (discovery.mazeGrid.start.x > discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y < discovery.mazeGrid.dest.y)
      currentAngle = 135;
    else if (discovery.mazeGrid.start.x < discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y == discovery.mazeGrid.dest.y)
      currentAngle = 0;
    else if (discovery.mazeGrid.start.x > discovery.mazeGrid.dest.x &&
        discovery.mazeGrid.start.y == discovery.mazeGrid.dest.y)
      currentAngle = 180;
  } 

  /** A function used to change the angle of robot.
   *  It will be called when the current angle offers no choice.
   */
  public void angleTuner() {
    if (angleCounter < 7) {
      if (currentAngle == 0) {
        currentAngle = 360 - 45;
        angleCounter += 1;
      } else {
        currentAngle -= 45;
        angleCounter += 1;
      }
    }
    direction.dirUpdate(currentAngle);
  }


  /** pathMarker Mark the cell as an element of path when robot moves to it.
   *  Change the value of the cell to 5, which means tried.
   *  Add the current cell position to solution. Get new angle according to
   *  the position and desination. Update directions.
   */
  public void pathMarker() {
    angleCounter=0;
    discovery.mazeGrid.start.assign(currentPos);
    discovery.mazeGrid.board[currentPos.x][currentPos.y] = tried;
    path.add(new Position(currentPos.x, currentPos.y));
    angleComputer();
    direction.dirUpdate(currentAngle);
  }
    

  /* Print the elements in the solution path. */
  public void pathPrinter() {
    int steps = path.size();
    System.out.println("Stupid maze runner takes " + steps + " steps to get out.");
    for (int i=0; i<steps; i++) {
      Position pathEle = path.get(i);
      pathEle.printer();
    }
  }

  /** pathFinder used to find a path for the robot to get out of a maze.
   *  Including all the rules in this maze runner expert system.
   *  One and only one rule will be fired every time when the condition
   *  of the rule is satisfied. 
   */
  public void pathFinder() {

    discovery.frontCells.clear();
    discovery.leftCells.clear();
    discovery.rightCells.clear();
    discovery.posToCheck.assign(currentPos);
    discovery.frontChecker(direction.front);
    discovery.leftChecker(direction.left);
    discovery.rightChecker(direction.right);

    // case 1: destination found
    if (currentPos.equal(discovery.mazeGrid.dest) ||
        discovery.mazeGrid.getValue(discovery.mazeGrid.dest) == 5) {
      System.out.println("Robot hits destination, solution found.");
      discovery.mazeGrid.printGrid();
      pathPrinter();
      finalGrid = discovery.mazeGrid;
      sol = new Visualize(finalGrid);
      runFlag = false;
      return;
    }

    // case 2: no solution
    if (angleCounter == 7 &&
        discovery.allEmpty() &&
        currentPos.equal(original)) {
      System.out.println("Current position of robot: " +
                         currentPos.x + " " + currentPos.y);
      System.out.println("Robot cannot find a solution, " +
                         "the maze may have no solution.");
      discovery.mazeGrid.printGrid();
      finalGrid = discovery.mazeGrid;
      sol = new Visualize(finalGrid);
      runFlag = false;
      return;
    }

    // case 3: destination in front cell
    if (runFlag && discovery.destInFront()) {
      currentPos.move(direction.front);  // move step
      pathMarker();
      pathFinder();
    }

    // case 4: destination in left cell
    if (runFlag && discovery.destInLeft()) {
      currentPos.move(direction.left); // move step
      pathMarker();
      pathFinder();
    }

    // case 5: destination in right cell
    if (runFlag && discovery.destInRight()){
      currentPos.move(direction.right);
      pathMarker();
      pathFinder();
    }

    // case 6: destination not in cell, move front if front cell not empty
    if (runFlag && discovery.destNotIn() && 
      !discovery.frontIsBlocked()) {
      currentPos.move(direction.front);
      pathMarker();
      pathFinder();
    }

    // case 7: desination not in cell, front is empty, left available and move left
    if (runFlag && discovery.destNotIn() && 
      discovery.frontIsBlocked() &&
        !discovery.leftIsBlocked()) {
      currentPos.move(direction.left);
      pathMarker();
      pathFinder();
    }

    // case 8: dest not in cell, front and left are empty, right available and move right
    if (runFlag && discovery.destNotIn() && 
        discovery.frontIsBlocked() &&
        discovery.leftIsBlocked() && !discovery.rightIsBlocked()) {
      currentPos.move(direction.right);
      pathMarker();
      pathFinder();
    }

    // case 9: all cell empty, then change angle
    if (runFlag && discovery.allEmpty() && angleCounter < 7) {
      angleTuner();
      System.out.println("Current size of path: " + path.size());
      pathFinder();
    }

    // case 10: robot has no choice but go back, then go back
    if (runFlag && path.size() > 1 && discovery.allEmpty() && angleCounter == 7) {
      path.removeLast();
      currentPos.assign(path.getLast());
      angleComputer();
      angleCounter = 0;
      direction.dirUpdate(currentAngle);
      pathFinder();
    }
    return ;
  }

  /* main method to run a robot. */
  public static void main(String[] args) {
    Robot mazeRunner = new Robot(30, 30, 40, 0, 0, 29, 29);
    mazeRunner.discovery.mazeGrid.printGrid();
    mazeRunner.pathFinder();
  } 
}


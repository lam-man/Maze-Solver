/* sensor.java */

import java.util.*;

class Sensor {

  protected int height, width, obsPertage;
  protected int startX, startY, destX, destY;
    
  protected LinkedList<Position> leftCells;
  protected LinkedList<Position> frontCells;
  protected LinkedList<Position> rightCells;
  protected Position posToCheck;
  protected Grid mazeGrid;

  /* Create a sensor*/
  public Sensor(int width, int height, int obsPertage,
                int startX, int startY, int destX, int destY) {
    this.width = width;
    this.height = height;
    this.startX = startX;
    this.startY = startY;
    this.destX = destX;
    this.destY = destY;
    this.obsPertage = obsPertage;
    leftCells = new LinkedList<Position> ();
    frontCells = new LinkedList<Position> ();
    rightCells = new LinkedList<Position> ();
    posToCheck = new Position(startX, startY);
    mazeGrid = new Grid(width, height, obsPertage, startX, startY, destX, destY); 
  }

  /* Check if sensor's current position in maze */
  public boolean inMaze() {
    if (posToCheck.x < 0 || posToCheck.y < 0 ||
        posToCheck.x >= mazeGrid.width ||
        posToCheck.y >= mazeGrid.height)
        return false;
    else
        return true;
  }

  /* Check if sensor's current position is obstacle or not. */
  public boolean notObstacle() {
    if (mazeGrid.getValue(posToCheck) == 0 || mazeGrid.getValue(posToCheck) == 5)
      return false;
    else
      return true;
  }
    
  /** Check available moves in left direction.
   *  @param step, direction move
   *  Add all available moves to the left cell
   */
  public void leftChecker(Position step) {

    posToCheck.move(step);

    if (inMaze() && notObstacle()) {
      leftCells.add(new Position (posToCheck.x, posToCheck.y));
      leftChecker(step);
    }
    posToCheck.assign(mazeGrid.start);
  }

  /** Check available moves in front direction.
   *  @param step, direction move
   *  Add all available moves to the front cell
   */
  public void frontChecker(Position step) {

    posToCheck.move(step);

    if (inMaze() && notObstacle()) {
      frontCells.add(new Position (posToCheck.x, posToCheck.y));
      frontChecker(step);
    }
    posToCheck.assign(mazeGrid.start);
  }

  /** Check available moves in right direction.
   *  @param step, direction move
   *  Add all available moves to the right cell
   */
  public void rightChecker(Position step) {

    posToCheck.move(step);

    if (inMaze() && notObstacle()) {
      rightCells.add(new Position (posToCheck.x, posToCheck.y));
      rightChecker(step);
    }
    posToCheck.assign(mazeGrid.start);
  }

  // Check whether left cell is empty.
  public boolean leftIsBlocked() {
    if (leftCells.size() == 0)
      return true;
    else
      return false;
  }

  // Check whether front cell is empty.
  public boolean frontIsBlocked() {
    if (frontCells.size() == 0)
      return true;
    else
      return false;
  }
    
  // Check whether right cell is empty.
  public boolean rightIsBlocked() {
    if (this.rightCells.size() == 0)
      return true;
    else
      return false;
  }

  // Check whether desination is in left cell or not. 
  public boolean destInLeft() {
    int length = leftCells.size();
    if (!leftIsBlocked()) {     
      for (int i=0; i<=length-1; i++) {
        Position temp = leftCells.get(i);
        if (temp.equal(mazeGrid.dest)) 
          return true;
      }
    }
    return false;
  }
    
  // Check whether desination is in front cell or not. 
  public boolean destInFront() {
    int length = frontCells.size();
    if (!frontIsBlocked()) {
      for (int i=0; i<=length-1; i++) {
        Position temp = frontCells.get(i);
        if (temp.equal(mazeGrid.dest)) 
          return true;
      }
    }
    return false;
  }


  // Check whether desination is in right cell or not.     
  public boolean destInRight() {
    int length = rightCells.size();
    if (!rightIsBlocked()) {
      for (int i=0; i<=length-1; i++) {
        Position temp = rightCells.get(i);
        if (temp.equal(mazeGrid.dest)) 
          return true;
      }
    }
    return false;
  }

  // Indicates destination not in the current three direction cells.
  public boolean destNotIn() {
    if (!destInFront() && !destInLeft() && !destInRight())
      return true;
    else
      return false;
  }

  // Indicates current three directions have no available moves. 
  public boolean allEmpty() {
    if (frontCells.size() == 0 &&
        leftCells.size() == 0 &&
        rightCells.size() == 0)
      return true;
    else
      return false;
  }

  // Print all available moves in the current three direction cells. 
  public void cellPrinter(){
    int tempLen;
    tempLen = frontCells.size();
    System.out.println("Elements in front cell: ");
    for (int i=0; i<tempLen; i++) {
      Position tempEle = frontCells.get(i);
      tempEle.printer();
    }

    tempLen = leftCells.size();
    System.out.println("Elements in left cell: ");
    for (int i=0; i<tempLen; i++) {
      Position tempEle = leftCells.get(i);
      tempEle.printer();
    }

    tempLen = rightCells.size();
    System.out.println("Elements in right cell: ");
    for (int i=0; i<tempLen; i++){
      Position tempEle = rightCells.get(i);
      tempEle.printer();
    }
  }

  /* Class test code to make sure sensor class works. */
  public static void main(String[] args) {
    Sensor pinner = new Sensor(10, 10, 10, 1, 1, 9, 9);
    pinner.mazeGrid.printGrid();
    System.out.println("Dest of grid is: " + pinner.mazeGrid.dest.x + " " + pinner.mazeGrid.dest.y);
    Position test0 = new Position(1, 1);
    Position test1 = new Position(1, 0);
    Position test2 = new Position(0, 1);
    pinner.frontChecker(test0);
    pinner.leftChecker(test1);
    pinner.rightChecker(test2);
    pinner.cellPrinter();
    System.out.println("The size of front cell is: " + pinner.frontCells.size());
    Position retest = pinner.frontCells.getLast();
    System.out.println("The last element in front cell is: " + retest.x + " " + retest.y);
    System.out.println(pinner.destInLeft());
    System.out.println(pinner.destInRight());
    System.out.println(pinner.destInFront());
    System.out.println(pinner.destNotIn());
  }
}


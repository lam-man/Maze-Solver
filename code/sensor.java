/* sensor.java */

import java.util.*;

class sensor {

  protected int height, width, obsPertage;
  protected int startX, startY, destX, destY;
    
  protected LinkedList<position> leftCells;
  protected LinkedList<position> frontCells;
  protected LinkedList<position> rightCells;
  protected position posToCheck;
  protected grid mazeGrid;

  /* Create a sensor*/
  public sensor(int width, int height, int obsPertage,
                int startX, int startY, int destX, int destY) {
    this.width = width;
    this.height = height;
    this.startX = startX;
    this.startY = startY;
    this.destX = destX;
    this.destY = destY;
    this.obsPertage = obsPertage;
    leftCells = new LinkedList<position> ();
    frontCells = new LinkedList<position> ();
    rightCells = new LinkedList<position> ();
    posToCheck = new position(startX, startY);
    mazeGrid = new grid(width, height, obsPertage, startX, startY, destX, destY); 
  }

  /* Check if sensor's current position in maze */
  public boolean inMaze() {
    if (this.posToCheck.x < 0 ||
        this.posToCheck.y < 0 ||
        this.posToCheck.x >= mazeGrid.width ||
        this.posToCheck.y >= mazeGrid.height)
        return false;
    else
        return true;
  }

  /* Check if sensor's current position is obstacle or not. */
  public boolean notObstacle() {
    if (mazeGrid.getValue(this.posToCheck) == 0 || mazeGrid.getValue(this.posToCheck) == 5)
      return false;
    else
      return true;
  }
    
  /* Check available moves in left direction.
   * @param step, direction move
   * Add all available moves to the left cell
   */
  public void leftChecker(position step) {

    this.posToCheck.move(step);

    if (this.inMaze() && this.notObstacle()) {
      leftCells.add(new position (this.posToCheck.x, this.posToCheck.y));
      this.leftChecker(step);
    }
    this.posToCheck.assign(mazeGrid.start);
  }

  /* Check available moves in front direction.
   * @param step, direction move
   * Add all available moves to the front cell
   */
  public void frontChecker(position step) {

    this.posToCheck.move(step);

    if (this.inMaze() && this.notObstacle()) {
      frontCells.add(new position (this.posToCheck.x, this.posToCheck.y));
      this.frontChecker(step);
    }
    this.posToCheck.assign(mazeGrid.start);
  }

  /* Check available moves in right direction.
   * @param step, direction move
   * Add all available moves to the right cell
   */
  public void rightChecker(position step) {

    this.posToCheck.move(step);

    if (this.inMaze() && this.notObstacle()) {
      rightCells.add(new position (this.posToCheck.x, this.posToCheck.y));
      this.rightChecker(step);
    }
    this.posToCheck.assign(mazeGrid.start);
  }

  // Check whether left cell is empty.
  public boolean leftIsBlocked() {
    if (this.leftCells.size() == 0)
      return true;
    else
      return false;
  }

  // Check whether front cell is empty.
  public boolean frontIsBlocked() {
    if (this.frontCells.size() == 0)
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
    int length = this.leftCells.size();
    if (!this.leftIsBlocked()) {     
      for (int i=0; i<=length-1; i++) {
        position temp = this.leftCells.get(i);
        if (temp.equal(this.mazeGrid.dest)) 
          return true;
      }
    }
    return false;
  }
    
  // Check whether desination is in front cell or not. 
  public boolean destInFront() {
    int length = this.frontCells.size();
    if (!this.frontIsBlocked()) {
      for (int i=0; i<=length-1; i++) {
        position temp = this.frontCells.get(i);
        if (temp.equal(this.mazeGrid.dest)) 
          return true;
      }
    }
    return false;
  }


  // Check whether desination is in right cell or not.     
  public boolean destInRight() {
    int length = this.rightCells.size();
    if (!this.rightIsBlocked()) {
      for (int i=0; i<=length-1; i++) {
        position temp = this.rightCells.get(i);
        if (temp.equal(this.mazeGrid.dest)) 
          return true;
      }
    }
    return false;
  }

  // Indicates destination not in the current three direction cells.
  public boolean destNotIn() {
    if (!this.destInFront() && !this.destInLeft() && !this.destInRight())
      return true;
    else
      return false;
  }

  // Indicates current three directions have no available moves. 
  public boolean allEmpty() {
    if (this.frontCells.size() == 0 &&
        this.leftCells.size() == 0 &&
        this.rightCells.size() == 0)
      return true;
    else
      return false;
  }

  // Print all available moves in the current three direction cells. 
  public void cellPrinter(){
    int tempLen;
    tempLen = this.frontCells.size();
    System.out.println("Elements in front cell: ");
    for (int i=0; i<tempLen; i++) {
      position tempEle = frontCells.get(i);
      tempEle.printer();
    }

    tempLen = this.leftCells.size();
    System.out.println("Elements in left cell: ");
    for (int i=0; i<tempLen; i++) {
      position tempEle = leftCells.get(i);
      tempEle.printer();
    }

    tempLen = this.rightCells.size();
    System.out.println("Elements in right cell: ");
    for (int i=0; i<tempLen; i++){
      position tempEle = rightCells.get(i);
      tempEle.printer();
    }
  }

  /* Class test code to make sure sensor class works. */
  public static void main(String[] args) {
    sensor pinner = new sensor(10, 10, 10, 1, 1, 9, 9);
    pinner.mazeGrid.printGrid();
    System.out.println("Dest of grid is: " + pinner.mazeGrid.dest.x + " " + pinner.mazeGrid.dest.y);
    position test0 = new position(1, 1);
    position test1 = new position(1, 0);
    position test2 = new position(0, 1);
    pinner.frontChecker(test0);
    pinner.leftChecker(test1);
    pinner.rightChecker(test2);
    pinner.cellPrinter();
    System.out.println("The size of front cell is: " + pinner.frontCells.size());
    position retest = pinner.frontCells.getLast();
    System.out.println("The last element in front cell is: " + retest.x + " " + retest.y);
    System.out.println(pinner.destInLeft());
    System.out.println(pinner.destInRight());
    System.out.println(pinner.destInFront());
    System.out.println(pinner.destNotIn());
  }
}


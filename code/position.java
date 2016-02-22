/* position.java */ 

class Position {
  protected int x;
  protected int y;

  // position constructor
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // default constructor
  public Position() {
    this(0, 0);
  }

  // move and update position
  public Position move(Position step) {
    x = x + step.x;
    y = y + step.y;
    return this;
  }

  /* Check if two positions are the same.
   * @param position posToCompare, input position need be checked.
   * @reutrn if same, reutrn true, otherwise return false.
   */
  public boolean equal(Position posToCompare) {
    if (x == posToCompare.x &&
        y == posToCompare.y)
      return true;
    else
      return false;
  }

  /* Assign a position to another. 
   * @param position assignTo
   */
  public void assign(Position assignTo) {
    this.x = assignTo.x;
    this.y = assignTo.y;
  }

  // print the information of a position object.
  public void printer() {
    System.out.println(x + " " + y);
  }

  
  /* class test in main funciton */
  public static void main(String[] args) {
    Position start = new Position(1,1);
    System.out.println("Start: " + start.x + " " + start.y);
    Position end = new Position(10,10);
    Position step = new Position(1, 1);

    Position second = start.move(step);
    System.out.println(step.equal(second));
    
    System.out.println("End: " + end.x + " " + end.y);
    System.out.println("Second: " + second.x + " " + second.y);
  }
}


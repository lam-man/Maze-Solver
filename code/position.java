/* position.java */ 

class position {
  protected int x;
  protected int y;

  // position constructor
  public position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // default constructor
  public position() {
    this(0, 0);
  }

  // move and update position
  public position move(position step) {
    this.x = this.x + step.x;
    this.y = this.y + step.y;
    return this;
  }

  /* Check if two positions are the same.
   * @param position posToCompare, input position need be checked.
   * @reutrn if same, reutrn true, otherwise return false.
   */
  public boolean equal(position posToCompare) {
    if (this.x == posToCompare.x &&
        this.y == posToCompare.y)
      return true;
    else
      return false;
  }

  /* Assign a position to another. 
   * @param position assignTo
   */
  public void assign(position assignTo) {
    this.x = assignTo.x;
    this.y = assignTo.y;
  }

  // print the information of a position object.
  public void printer() {
    System.out.println(this.x + " " + this.y);
  }

  
  /* class test in main funciton */
  public static void main(String[] args) {
    position start = new position(1,1);
    System.out.println("Start: " + start.x + " " + start.y);
    position end = new position(10,10);
    position step = new position(1, 1);

    position second = start.move(step);
    System.out.println(step.equal(second));
    
    System.out.println("End: " + end.x + " " + end.y);
    System.out.println("Second: " + second.x + " " + second.y);
  }
}


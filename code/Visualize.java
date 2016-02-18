/* Visualize.java */
import java.awt.*;
import javax.swing.*;

/* Visualize is a class used to visualize the path of robot,
 * Red: obstacles;
 * White: availables;
 * Green: path robot choose.
 */
class Visualize extends JFrame {
  JPanel panel;
  int size = 900;
  JButton buttons[] = new JButton[900];
  grid maze;

  public Visualize(grid input) {
    this.maze = input;
    setSize(600, 600);
    panel = new JPanel();
    panel.setLayout(new GridLayout(30, 30));
    for (int i=0; i < size; i++) {
      buttons[i] = new JButton();
      int y = i / 30;
      int x = i % 30;
      position temp = new position(x, y);
      if (this.maze.getValue(temp) == 0) {
        buttons[i].setBackground(Color.red);
        buttons[i].setOpaque(true);
        buttons[i].setBorderPainted(false);
      } else if (this.maze.getValue(temp) == 5) {
        buttons[i].setBackground(Color.green);
        buttons[i].setOpaque(true);
        buttons[i].setBorderPainted(false);
      }
      panel.add(buttons[i]);
    }
    add(panel);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Maze runner path!");    
  }
  

  
  public static void main(String args[]) {
    grid newGrid = new grid(30, 30, 30, 0, 0, 29, 29);
    Visualize sol = new Visualize(newGrid);
  }
    
}

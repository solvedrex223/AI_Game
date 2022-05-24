package game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton{
    
    public int distance,pos;
    public boolean mapped,moved,finish;
    public Button up,down,left,right;

    Button(ActionListener act){
        super();
        this.setBackground(Color.WHITE);
        this.addActionListener(act);
        this.distance = 0;
        this.mapped = false;
        this.moved = false;
    }
}

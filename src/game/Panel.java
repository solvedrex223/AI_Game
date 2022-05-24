package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
//import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedOutputStream;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Panel extends JPanel implements ActionListener{

    Button button;
    int grid_side;
    Button [] grid;
    Random r;
    FileWriter fw;
    Panel() throws IOException{
        super();
        grid_side = 10;
        int sqr_size = (int) Math.pow(grid_side, 2);
        grid = new Button[sqr_size];
        r = new Random();
        this.setPreferredSize(new Dimension(500,500));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(grid_side,grid_side));
        fw = new FileWriter(new File("res/moves.txt"));
        //System.out.println(System.getProperty("user.dir"));

        for (int i = 0; i < sqr_size; i++) {
            button = new Button(this);
            button.setPreferredSize(new Dimension(500 / grid_side, 500 / grid_side));
            grid[i] = button;
            button.pos = i;
            this.add(button);
            button.left = (i - 1 < 0 || (i - 1) % grid_side == (grid_side - 1)) ? null : grid[i - 1];
            if (button.left != null){
                button.left.right = button;
            }
            button.right = null;
            button.up = i - grid_side < 0 ? null : grid[i - grid_side];
            if(button.up != null){
                button.up.down = button;
            }
            button.down = null;
        }
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int j = (int) Math.pow(grid_side, 2);
        for (int i = 0; i < j; i++) {
            if (this.grid[i] == e.getSource()){
                int x = (i / grid_side) + 1, y = (i % grid_side) + 1;
                System.out.println(x + "," + y);
                try {
                    fw.append(x + "," + y + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Button button = (Button) e.getSource();
                button.moved = true;
                if (i == grid_side-1){
                    button.setBackground(Color.GREEN);
                    JOptionPane.showMessageDialog(this, "You Win", "CONGRATULATIONS", JOptionPane.PLAIN_MESSAGE);
                    try {
                        fw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.exit(0);
                }
                else{
                    button.setBackground(Color.YELLOW);
                    try {
                        randomMove(button);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    public void randomMove(Button button) throws IOException{
        Button pressed;
        boolean check = false;
        ArrayList <Button> moves = new ArrayList<>(4);
        moves.add(button.up);
        moves.add(button.down);
        moves.add(button.left);
        moves.add(button.right);
        do{
            pressed = moves.get(r.nextInt(moves.size()));
            try {
                check = pressed.moved;
            } catch (Exception e) {
                check = true;
            }

            if (pressed == null || check){
                moves.remove(pressed);
            }

            if (moves.size() == 0){
                JOptionPane.showMessageDialog(this, "You lost", "GAME OVER", JOptionPane.WARNING_MESSAGE);
                fw.close();
                System.exit(0);
            }
        }
        while(pressed == null || check);
        pressed.doClick();
    }

    public void checkDistance(Button button){
        try {
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

}
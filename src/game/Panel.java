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
    Button [] grid = new Button[25];
    Random r;
    FileWriter fw;
    Panel() throws IOException{
        super();
        r = new Random();
        this.setPreferredSize(new Dimension(500,500));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(5,5));
        fw = new FileWriter(new File("res/moves.txt"));
        //System.out.println(System.getProperty("user.dir"));

        for (int i = 0; i < 25; i++) {
            button = new Button(this);
            grid[i] = button;
            button.pos = i;
            this.add(button);
            button.left = (i - 1 < 0 || (i - 1) % 5 == 4) ? null : grid[i - 1];
            if (button.left != null){
                button.left.right = button;
            }
            button.right = null;
            button.up = i - 5 < 0 ? null : grid[i - 5];
            if(button.up != null){
                button.up.down = button;
            }
            button.down = null;
        }
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 25; i++) {
            if (this.grid[i] == e.getSource()){
                int x = (i / 5) + 1, y = (i % 5) + 1;
                System.out.println(x + "," + y);
                try {
                    fw.append(x + "," + y + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Button button = (Button) e.getSource();
                button.moved = true;
                if (i == 4){
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
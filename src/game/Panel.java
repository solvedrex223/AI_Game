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

public class Panel extends JPanel implements ActionListener,Runnable{

    Button button;
    int grid_side, finish_pos;
    Button [] grid;
    Random r;
    FileWriter fw;
    Thread thread;
    Panel() throws IOException{
        super();
        grid_side = 10;
        int sqr_size = (int) Math.pow(grid_side, 2);
        grid = new Button[sqr_size];
        r = new Random();
        this.setPreferredSize(new Dimension(700,700));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(grid_side,grid_side));
        fw = new FileWriter(new File("res/moves.txt"));
        //System.out.println(System.getProperty("user.dir"));
        this.finish_pos = r.nextInt(sqr_size);
        //System.out.println(finish_pos);
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
            if (i == finish_pos){
                button.finish = true;
                button.distance = 100;
                button.setBackground(Color.GREEN);
            }
        }
        // Commented for exam 1
        /*for (Button button : grid) {
            checkDistance(button);
        }*/  
        thread = new Thread(this);
        exam1();      
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
                if (i == finish_pos){
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
                    distanceMove(button);
                    /*try {
                        randomMove(button);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }*/
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

    public void distanceMove(Button button){
        ArrayList <Button> dis = new ArrayList<>();
        if (button.up != null){
            dis.add(button.up);
        }
        if (button.down != null){
            dis.add(button.down);
        }
        if (button.left != null){
            dis.add(button.left);
        }
        if (button.right != null){
            dis.add(button.right);
        }
        int min = Integer.MAX_VALUE, x = 0;
        for (int i = 0; i < dis.size(); i++) {
            if (dis.get(i).distance <= min){
                min = dis.get(i).distance;
                x = i;
            }
        }

        dis.get(x).doClick();
    }

    public void checkDistance(Button button){
        if (button.finish){
            button.setText(Integer.toString(0));
        }
        else {
            int [] finish = {this.finish_pos / grid_side, this.finish_pos % grid_side};
            int [] button_pos = {button.pos / grid_side, button.pos % grid_side};
            button.distance = (Math.abs(finish[0] - button_pos[0]) + Math.abs(finish[1] - button_pos[1]));
            button.setText(Integer.toString(button.distance));
        }
    }

    //Codigo de examen 1
    public void exam1(){
        this.setVisible(false);
        for (int i = 0; i < 100; i++) {
            startGame();
            do {
                randomMoveExam();
            } while (button.finish != true);
        }
        String grid_layout = "[";
        grid_layout += Integer.toString(button.distance) + ",";
        for (Button button : grid) {
            if (button.pos % grid_side == grid_side - 1){
                grid_layout += Integer.toString(button.distance) + "\n";
            }
            else{
                grid_layout += Integer.toString(button.distance) + ",";
            }
        }
        grid_layout += "]";
        System.out.println(grid_layout);
        for (Button button : grid) {
            button.setText(Integer.toString(button.distance));
        }
        this.setVisible(true);
        startGame();
        thread.start();
    }

    public void randomMoveExam(){
        ArrayList <Button> directions = new ArrayList<>(4);
        directions.add(button.up);
        directions.add(button.down);
        directions.add(button.left);
        directions.add(button.right);
        Button random_dir;
        do{
            random_dir = directions.get(r.nextInt(4));
        }
        while (random_dir == null);
        button.distance++;
        button = random_dir;
    }

    public void startGame(){
        do{
            button = grid[r.nextInt((int) Math.pow(grid_side, 2))];
        }
        while(button.finish);
    }

    public void distanceMoveExam(){
        ArrayList <Button> dis = new ArrayList<>();
        if (button.up != null){
            dis.add(button.up);
        }
        if (button.down != null){
            dis.add(button.down);
        }
        if (button.left != null){
            dis.add(button.left);
        }
        if (button.right != null){
            dis.add(button.right);
        }
        int min = Integer.MAX_VALUE, x = 0;
        for (int i = 0; i < dis.size(); i++) {
            if (dis.get(i).moved){
                dis.get(i).moved = false;
            }
            else{
                if (dis.get(i).distance <= min){
                    min = dis.get(i).distance;
                    x = i;
                }
            }
        }
        Button tmp = dis.get(x);
        tmp.setBackground(Color.YELLOW);
        button.setBackground(Color.WHITE);
        button.moved = true;
        button = tmp;
    }
    @Override
    public void run() {
        do {
            distanceMoveExam();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!button.finish);
        System.out.println("You Win");
        System.exit(0);     
    }
}
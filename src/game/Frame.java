package game;

import java.io.IOException;

import javax.swing.JFrame;

public class Frame extends JFrame {

    Frame() throws IOException{
        super("AI Game");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(new Panel());
        this.pack();
    }
    public static void main(String[] args) throws Exception {
        new Frame();
    }
}

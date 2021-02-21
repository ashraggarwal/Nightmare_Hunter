import javax.swing.JFrame;
import java.io.*;
public class Test {
    public static void main(String args[]) throws IOException {
        //System.out.println(new RectPrism(new Vector(0.0,0.0,0.0),new Vector(1.0,0.0,0.0),new Vector(0.0,1.0,0.0),new Vector(0.0,0.0,1.0),255,0,0).colliding(new RectPrism(new Vector(0.9,0.7,0.8),new Vector(5.0,4.0,0.0),new Vector(4.0,-5.0,0.0),10.0,255,0,0)));
        JFrame frame = new JFrame("Client");
        Screen sc = new Screen();
        frame.add(sc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        sc.animate();
    }
}
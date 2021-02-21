import java.awt.Color;
public class Coord{
    private double z;
    private int r,g,b,a;
    private Color c;
    public Coord(double z,int r,int g,int b){
        this.z=z;
        this.r=r;
        this.g=g;
        this.b=b;
        c=new Color(r,g,b);
    }
    public Coord(double z,Color c){
        this.z=z;
        this.c=c;
    }
    public double getZ(){
        return z;
    }
    public Color getColor(){
        return c;
    }
}
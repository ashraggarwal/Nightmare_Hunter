import java.util.ArrayList;
import java.io.Serializable;
public class Sword implements Shape,Serializable{
    private static final long serialVersionUID = 1L;
    private Cylinder hilt,blade;
    private Hand hand;
    private double size;
    private double length;
    private int hiltTexture,bladeTexture;
    private ArrayList<Triangle>triangles;
    private ArrayList<Sphere>spheres;
    private Vector pos;
    private double scale;
    private Vector center;
    public Sword(Hand hand,double scale,Vector center,double size,double length,int hiltTexture,int bladeTexture){
        this.hand=hand;
        this.center=center;
        this.scale=scale;
        this.pos=hand.multiply(scale).plus(center);
        this.size=size;
        this.length=length;
        this.hiltTexture=hiltTexture;
        this.bladeTexture=bladeTexture;
        create();
    }
    public void create(){
        this.pos=hand.multiply(scale).plus(center);
        spheres=new ArrayList<Sphere>();
        triangles=new ArrayList<Triangle>();
        spheres.add(new Sphere(pos.plus(hand.getZ().multiply(0.15*size)),0.040*size,100,100,200));
        spheres.add(new Sphere(pos.minus(hand.getZ().multiply(0.15*size)),0.03*size,20,0,20));
        hilt=new Cylinder(pos.minus(hand.getZ().multiply(0.15*size)),pos.plus(hand.getZ().multiply(0.15*size)),hand.getX().multiply(-0.015*size),hand.getY().multiply(0.03*size),hiltTexture);
        blade=new Cylinder(pos.plus(hand.getZ().multiply(0.15*size)),pos.plus(hand.getZ().multiply(size*length)),0.1,hand.getX().multiply(0.015*size),hand.getY().multiply(0.03*size),bladeTexture);
        triangles.addAll(hilt.getTriangles());
        triangles.addAll(blade.getTriangles());
    }
    public RectPrism getHitBox(){
        Vector pos1=pos.minus(hand.getZ().multiply(0.15*size));
        Vector pos2=pos.plus(hand.getZ().multiply(size*length));
        Vector x=hand.getX().multiply(0.015*size);
        Vector y=hand.getY().multiply(0.03*size);
        return new RectPrism(pos1.minus(x),x.multiply(2),y.multiply(2),pos2.minus(pos1));
    }
    public ArrayList<Triangle>getTriangles(){
        return triangles;
    }
    public ArrayList<Sphere>getSpheres(){
        return spheres;
    }
}
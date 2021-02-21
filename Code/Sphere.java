import java.io.Serializable;
public class Sphere implements Serializable{
    private static final long serialVersionUID = 1L;
    private Vector c;
    private double radius;
    private int r,g,b;
    private Vector[]bounds;
    public Sphere(Vector c,double radius,int r,int g,int b){
        this.c=c;
        this.radius=radius;
        this.r=r;
        this.g=g;
        this.b=b;
        this.bounds=new Vector[8];
        bounds[0]=new Vector(c.x()-radius,c.y()-radius,c.z()-radius);
        bounds[1]=new Vector(c.x()+radius,c.y()-radius,c.z()-radius);
        bounds[2]=new Vector(c.x()-radius,c.y()+radius,c.z()-radius);
        bounds[3]=new Vector(c.x()-radius,c.y()-radius,c.z()+radius);
        bounds[4]=new Vector(c.x()+radius,c.y()+radius,c.z()-radius);
        bounds[5]=new Vector(c.x()-radius,c.y()+radius,c.z()+radius);
        bounds[6]=new Vector(c.x()+radius,c.y()-radius,c.z()+radius);
        bounds[7]=new Vector(c.x()+radius,c.y()+radius,c.z()+radius);
    }
    public Vector getC(){
        return c;
    }
    public double getRadius(){
        return radius;
    }
    public int getR(){
        return r;
    }
    public int getG(){
        return g;
    }
    public int getB(){
        return b;
    }
    public Vector[]getBounds(){
        return bounds;
    }
}
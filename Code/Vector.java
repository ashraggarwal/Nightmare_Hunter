import java.io.Serializable;
public class Vector implements Serializable{
    private static final long serialVersionUID = 1L;
    private double x, y, z;
    public Vector(double x,double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public double x(){
        return x;
    }
    public void x(double x){
        this.x=x;
    }
    public double y(){
        return y;
    }
    public void y(double y){
        this.y=y;
    }
    public double z(){
        return z;
    }
    public void z(double z){
        this.z=z;
    }
    public Vector cross(Vector v){
        return new Vector(y*v.z()-z*v.y(),z*v.x()-x*v.z(),x*v.y()-y*v.x());
    }
    public double dot(Vector v){
        return v.x()*x+v.y()*y+v.z()*z;
    }
    public Vector neg(){
        return new Vector(-x,-y,-z);
    }
    public Vector plus(Vector v){
        return new Vector(x+v.x(),y+v.y(),z+v.z());
    }
    public Vector minus(Vector v){
        return new Vector(x-v.x(),y-v.y(),z-v.z());
    }
    public double[] toArray(){
        return new double[]{x,y,z};
    }
    public double mag(){
        return Math.sqrt(dot(this));
    }
    public Vector multiply(double m){
        return new Vector(m*x,m*y,m*z);
    }
    public Vector unit(){
        return new Vector(x/mag(),y/mag(),z/mag());
    }
    public void set(Vector v){
        x=v.x();
        y=v.y();
        z=v.z();
    }
    public String toString(){
        return x+" "+y+" "+z;
    }
}
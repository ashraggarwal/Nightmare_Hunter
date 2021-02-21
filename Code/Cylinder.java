import java.util.ArrayList;
import java.io.Serializable;
public class Cylinder implements Shape,Serializable{
    private static final long serialVersionUID = 1L;
    private Vector pos1, pos2, x, y, del;
    private double radius;
    private int r,g,b;
    private ArrayList<Triangle>t;
    private ArrayList<Rectangle>rects;
    public Cylinder(Vector pos1,Vector pos2,double radius,int r,int g,int b){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.radius=radius;
        this.r=r;
        this.g=g;
        this.b=b;
        this.del=pos2.minus(pos1);
        if(Math.abs(del.x())>Math.abs(del.y())){
            this.x=new Vector(del.z(),0.0,-del.x()).unit().multiply(radius);
            this.y=this.x.cross(del).unit().multiply(radius);
        }else{
            this.x=new Vector(0.0,del.z(),-del.y()).unit().multiply(radius);
            this.y=this.x.cross(del).unit().multiply(radius);
        }
        int n=8;
        for(int i=0;i<n;i++){
            //System.out.println(x.mag()+" "+y.mag());
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n)));
            //System.out.println(v);
            //System.out.println(v2);
            //System.out.println(v2.minus(v).mag());
            Rectangle temp=new Rectangle(v,del,v2.minus(v),r,g,b);
            rects.add(temp);
            t.addAll(temp.getTriangles());
        }
    }
    public Cylinder(Vector pos1,Vector pos2,Vector x,Vector y,int r,int g,int b){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.r=r;
        this.g=g;
        this.b=b;
        this.del=pos2.minus(pos1);
        this.x=x;
        this.y=y;
        int n=8;
        for(int i=0;i<n;i++){
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n)));
            Rectangle temp=new Rectangle(v,del,v2.minus(v),r,g,b);
            rects.add(temp);
            t.addAll(temp.getTriangles());
        }
    }
    public Cylinder(Vector pos1,Vector pos2,double radius,int texture){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.radius=radius;
        this.del=pos2.minus(pos1);
        if(Math.abs(del.x())>Math.abs(del.y())){
            this.x=new Vector(del.z(),0.0,-del.x()).unit().multiply(radius);
            this.y=this.x.cross(del).unit().multiply(radius);
        }else{
            this.x=new Vector(0.0,del.z(),-del.y()).unit().multiply(radius);
            this.y=this.x.cross(del).unit().multiply(radius);
        }
        int n=8;
        for(int i=0;i<n;i++){
            //System.out.println(x.mag()+" "+y.mag());
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n)));
            //System.out.println(v);
            //System.out.println(v2);
            //System.out.println(v2.minus(v).mag());
            Rectangle temp=new Rectangle(v,del,v2.minus(v),texture);
            rects.add(temp);
            t.addAll(temp.getTriangles());
        }
    }
    public Cylinder(Vector pos1,Vector pos2,Vector x,Vector y,int texture){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.del=pos2.minus(pos1);
        this.x=x;
        this.y=y;
        int n=8;
        for(int i=0;i<n;i++){
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n)));
            Rectangle temp=new Rectangle(v,del,v2.minus(v),texture);
            rects.add(temp);
            t.addAll(temp.getTriangles());
        }
    }
    public Cylinder(Vector pos1,Vector pos2,Vector x,Vector y,int texture,int n){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.del=pos2.minus(pos1);
        this.x=x;
        this.y=y;
        for(int i=0;i<n;i++){
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n+Math.PI/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n+Math.PI/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n+Math.PI/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n+Math.PI/n)));
            Rectangle temp=new Rectangle(v,del,v2.minus(v),texture);
            rects.add(temp);
            t.addAll(temp.getTriangles());
        }
    }
    public Cylinder(Vector pos1,Vector pos2,double skew,Vector x,Vector y,int texture){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.del=pos2.minus(pos1);
        this.x=x;
        this.y=y;
        int n=8;
        for(int i=0;i<n;i++){
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n+Math.PI/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n+Math.PI/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n+Math.PI/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n+Math.PI/n)));
            Vector v3=this.pos2.plus(this.x.multiply(skew*Math.cos(2*Math.PI*i/n+Math.PI/n))).plus(this.y.multiply(skew*Math.sin(2*Math.PI*i/n+Math.PI/n)));
            Vector v4=this.pos2.plus(this.x.multiply(skew*Math.cos(2*Math.PI*(i+1)/n+Math.PI/n))).plus(this.y.multiply(skew*Math.sin(2*Math.PI*(i+1)/n+Math.PI/n)));
            t.add(new Triangle(v,v2,v3,texture));
            t.add(new Triangle(v4,v3,v2,texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(1.0-u1)),(int)((h-1)*(1.0-u2))})));
        }
    }
    public Cylinder(Vector pos1,Vector pos2,double skewX,double skewY,Vector x,Vector y,int texture){
        this.t=new ArrayList<Triangle>();
        this.rects=new ArrayList<Rectangle>();
        this.pos1=pos1;
        this.pos2=pos2;
        this.del=pos2.minus(pos1);
        this.x=x;
        this.y=y;
        int n=8;
        for(int i=0;i<n;i++){
            Vector v=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*i/n+Math.PI/n))).plus(this.y.multiply(Math.sin(2*Math.PI*i/n+Math.PI/n)));
            Vector v2=this.pos1.plus(this.x.multiply(Math.cos(2*Math.PI*(i+1)/n+Math.PI/n))).plus(this.y.multiply(Math.sin(2*Math.PI*(i+1)/n+Math.PI/n)));
            Vector v3=this.pos2.plus(this.x.multiply(skewX*Math.cos(2*Math.PI*i/n+Math.PI/n))).plus(this.y.multiply(skewY*Math.sin(2*Math.PI*i/n+Math.PI/n)));
            Vector v4=this.pos2.plus(this.x.multiply(skewX*Math.cos(2*Math.PI*(i+1)/n+Math.PI/n))).plus(this.y.multiply(skewY*Math.sin(2*Math.PI*(i+1)/n+Math.PI/n)));
            t.add(new Triangle(v,v2,v3,texture));
            t.add(new Triangle(v4,v3,v2,texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(1.0-u1)),(int)((h-1)*(1.0-u2))})));
        }
    }
    public ArrayList<Triangle>getTriangles(){
        return t;
    }
    public ArrayList<Sphere>getSpheres(){
        return new ArrayList<Sphere>();
    }
}
import java.io.Serializable;
public class Limb implements Serializable{
    private static final long serialVersionUID = 1L;
    private Joint v1,v2;
    private double size,skewX,skewY;
    private int type;
    private Vector x,y;
    public Limb(Joint v1,Joint v2,double size){
        this.v1=v1;
        this.v2=v2;
        this.size=size;
        this.type=1;
        Vector del=v2.minus(v1);
        if(Math.abs(del.x())>Math.abs(del.y())){
            this.x=new Vector(del.z(),0.0,-del.x()).unit().multiply(size);
            this.y=this.x.cross(del).unit().multiply(size);
        }else{
            this.x=new Vector(0.0,del.z(),-del.y()).unit().multiply(size);
            this.y=this.x.cross(del).unit().multiply(size);
        }
    }
    public Limb(Joint v1,Joint v2,Vector x,Vector y){
        this.v1=v1;
        this.v2=v2;
        this.x=x;
        this.y=y;
        this.type=2;
    }
    public Limb(Joint v1,Joint v2,Vector x,Vector y,double skewX,double skewY){
        this.v1=v1;
        this.v2=v2;
        this.x=x;
        this.y=y;
        this.type=3;
        this.skewX=skewX;
        this.skewY=skewY;
    }
    public RectPrism getHitBox(Vector pos,double scale){
        return new RectPrism(this.v1.minus(this.x).minus(this.y).multiply(scale).plus(pos),this.x.multiply(2*scale),this.y.multiply(2*scale),this.v2.minus(this.v1).multiply(scale));
    }
    public Cylinder getCylinder(Vector pos,double scale,int r,int g,int b){
        if(type==1){
            return new Cylinder(v1.multiply(scale).plus(pos),v2.multiply(scale).plus(pos),scale*size,r,g,b);
        }
        return new Cylinder(v1.multiply(scale).plus(pos),v2.multiply(scale).plus(pos),x.multiply(scale),y.multiply(scale),r,g,b);
    }
    public Cylinder getCylinder(Vector pos,double scale,int texture){
        if(type==1){
            return new Cylinder(v1.multiply(scale).plus(pos),v2.multiply(scale).plus(pos),scale*size,texture);
        }else if(type==3){
            return new Cylinder(v1.multiply(scale).plus(pos),v2.multiply(scale).plus(pos),skewX,skewY,x.multiply(scale),y.multiply(scale),texture);
        }
        return new Cylinder(v1.multiply(scale).plus(pos),v2.multiply(scale).plus(pos),x.multiply(scale),y.multiply(scale),texture);
    }
}
import java.io.Serializable;
import java.awt.Color;
public class Triangle implements Serializable{
    private static final long serialVersionUID = 1L;
    private int texture;
    private int r,g,b;
    private Integer a;
    private Vector[]v;
    private Vector n,grad;
    private double averageHeight;
    private TextureMap function;
    public Triangle(Vector v1,Vector v2,Vector v3,int r,int g,int b){
        this.v=new Vector[]{v1,v2,v3};
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=255;
        this.n=v[1].minus(v[0]).cross(v[2].minus(v[0])).unit();
        this.averageHeight=(v[0].z()+v[1].z()+v[2].z())/3;
        this.texture=-1;
        if(n.z()==0.0){
            this.grad=new Vector(0.0,0.0,1.0);
        }else{
            this.grad=new Vector(-n.x()/n.z(),-n.y()/n.z(),0.0);
        }
    }
    public Triangle(Vector v1,Vector v2,Vector v3,int r,int g,int b,int a){
        this.v=new Vector[]{v1,v2,v3};
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
        this.averageHeight=(v[0].z()+v[1].z()+v[2].z())/3;
        this.n=v[1].minus(v[0]).cross(v[2].minus(v[0])).unit();
        this.texture=-1;
        if(n.z()==0.0){
            this.grad=new Vector(0.0,0.0,1.0);
        }else{
            this.grad=new Vector(-n.x()/n.z(),-n.y()/n.z(),0.0);
        }
    }
    public Triangle(Vector v1,Vector v2,Vector v3,Color c,Vector n){
        this.v=new Vector[]{v1,v2,v3};
        this.r=c.getRed();
        this.g=c.getGreen();
        this.b=c.getBlue();
        this.a=c.getAlpha();
        this.averageHeight=(v[0].z()+v[1].z()+v[2].z())/3;
        this.n=n;
        this.texture=-1;
        if(n.z()==0.0){
            this.grad=new Vector(0.0,0.0,1.0);
        }else{
            this.grad=new Vector(-n.x()/n.z(),-n.y()/n.z(),0.0);
        }
    }
    public Triangle(Vector v1,Vector v2,Vector v3,int texture){
        this.v=new Vector[]{v1,v2,v3};
        this.r=0;
        this.g=0;
        this.b=0;
        this.a=255;
        this.texture=texture;
        this.averageHeight=(v[0].z()+v[1].z()+v[2].z())/3;
        this.n=v[1].minus(v[0]).cross(v[2].minus(v[0])).unit();
        if(n.z()==0.0){
            this.grad=new Vector(0.0,0.0,1.0);
        }else{
            this.grad=new Vector(-n.x()/n.z(),-n.y()/n.z(),0.0);
        }
    }
    public Triangle(Vector v1,Vector v2,Vector v3,int texture,TextureMap function){
        this.v=new Vector[]{v1,v2,v3};
        this.r=0;
        this.g=0;
        this.b=0;
        this.a=255;
        this.texture=texture;
        this.averageHeight=(v[0].z()+v[1].z()+v[2].z())/3;
        this.n=v[1].minus(v[0]).cross(v[2].minus(v[0])).unit();
        if(n.z()==0.0){
            this.grad=new Vector(0.0,0.0,1.0);
        }else{
            this.grad=new Vector(-n.x()/n.z(),-n.y()/n.z(),0.0);
        }
        this.function=function;
    }
    public Triangle(Vector v1,Vector v2,Vector v3,Triangle old){
        this.v=new Vector[]{v1,v2,v3};
        if(old.hasTexture()){
            this.texture=old.getTexture();
        }else{
            this.r=old.getR();
            this.g=old.getG();
            this.b=old.getB();
            this.a=old.getA();
            this.texture=-1;
        }
        this.averageHeight=(v[0].z()+v[1].z()+v[2].z())/3;
        this.n=v[1].minus(v[0]).cross(v[2].minus(v[0])).unit();
        if(n.z()==0.0){
            this.grad=new Vector(0.0,0.0,1.0);
        }else{
            this.grad=new Vector(-n.x()/n.z(),-n.y()/n.z(),0.0);
        }
    }
    public boolean hasTexture(){
        return texture!=-1;
    }
    public int getTexture(){
        return texture;
    }
    public int[] getXY(double u1,double u2,int w,int h){
        if(u1>1.0){
            u1=1.0;
        }
        if(u2>1.0){
            u2=1.0;
        }
        if(u1<0.0){
            u1=0.0;
        }
        if(u2<0.0){
            u2=0.0;
        }
        if(function==null){
            return new int[]{(int)(u1*(w-1)),(int)(u2*(h-1))};
        }else{
            return function.position(u1,u2,w,h);
        }
    }
    public void setTexture(int texture){
        this.texture=texture;
    }
    public Vector[] getV(){
        return v;
    }
    public int getR(){
        return r;
    }
    public int getB(){
        return b;
    }
    public int getG(){
        return g;
    }
    public int getA(){
        return a;
    }
    public void forceUp(){
        if(n.z()<0){
            n=n.neg();
        }
    }
    public void forceDir(Vector dir){
        if(n.dot(dir)<0){
            n=n.neg();
        }
    }
    public Vector getN(){
        return n;
    }
    public double getAverageHeight(){
        return averageHeight;
    }
    public Vector getAverage(){
        return new Vector((v[0].x()+v[1].x()+v[2].x())/3,(v[0].y()+v[1].y()+v[2].y())/3,(v[0].z()+v[1].z()+v[2].z())/3);
    }
    public Vector getGrad(){
        return grad;
    }
    public Color getColor(){
        if(a==null){
            return new Color(r,g,b);
        }
        return new Color(r,g,b,a);
    }
}
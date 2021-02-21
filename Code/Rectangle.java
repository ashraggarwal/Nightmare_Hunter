import java.util.ArrayList;
import java.io.Serializable;
public class Rectangle implements Shape,Serializable{
    private static final long serialVersionUID = 1L;
    private Vector pos,x,y;
    private int r,g,b;
    private ArrayList<Triangle>t;
    public Rectangle(Vector pos,Vector x,Vector y,int r,int g,int b){
        t=new ArrayList<Triangle>();
        this.r=r;
        this.g=g;
        this.b=b;
        this.pos=pos;
        this.x=x;
        this.y=y;
        t.add(new Triangle(pos,pos.plus(x),pos.plus(y),r,g,b));
        t.add(new Triangle(pos.plus(x).plus(y),pos.plus(y),pos.plus(x),r,g,b));
    }
    public Rectangle(Vector pos,Vector x,Vector y,Vector dir,int r,int g,int b){
        t=new ArrayList<Triangle>();
        this.r=r;
        this.g=g;
        this.b=b;
        this.pos=pos;
        this.x=x;
        this.y=y;
        Triangle t1=new Triangle(pos,pos.plus(x),pos.plus(y),r,g,b);
        t1.forceDir(dir);
        Triangle t2=new Triangle(pos.plus(x).plus(y),pos.plus(y),pos.plus(x),r,g,b);
        t2.forceDir(dir);
        t.add(t1);
        t.add(t2);
    }
    public Rectangle(Vector pos,Vector x,Vector y,int r,int g,int b,int detail){
        t=new ArrayList<Triangle>();
        this.r=r;
        this.g=g;
        this.b=b;
        this.pos=pos;
        this.x=x;
        this.y=y;
        Vector xDetail=this.x.multiply(1.0/detail);
        Vector yDetail=this.y.multiply(1.0/detail);
        for(int i=0;i<detail;i++){
            for(int j=0;j<detail;j++){
                Vector posDetail=pos.plus(xDetail.multiply(i*1.0)).plus(yDetail.multiply(j*1.0));
                t.add(new Triangle(posDetail,posDetail.plus(xDetail),posDetail.plus(yDetail),r,g,b));
                t.add(new Triangle(posDetail.plus(xDetail).plus(yDetail),posDetail.plus(yDetail),posDetail.plus(xDetail),r,g,b));
            }
        }   
    }
    public Rectangle(Vector pos,Vector x,Vector y,Vector dir,int r,int g,int b,int detail){
        t=new ArrayList<Triangle>();
        this.r=r;
        this.g=g;
        this.b=b;
        this.pos=pos;
        this.x=x;
        this.y=y;
        Vector xDetail=this.x.multiply(1.0/detail);
        Vector yDetail=this.y.multiply(1.0/detail);
        for(int i=0;i<detail;i++){
            for(int j=0;j<detail;j++){
                Vector posDetail=pos.plus(xDetail.multiply(i*1.0)).plus(yDetail.multiply(j*1.0));
                Triangle t1=new Triangle(posDetail,posDetail.plus(xDetail),posDetail.plus(yDetail),r,g,b);
                Triangle t2=new Triangle(posDetail.plus(xDetail).plus(yDetail),posDetail.plus(yDetail),posDetail.plus(xDetail),r,g,b);
                t1.forceDir(dir);
                t2.forceDir(dir);
                t.add(t1);
                t.add(t2);
            }
        }   
    }
    public Rectangle(Vector pos,Vector x,Vector y,int texture){
        t=new ArrayList<Triangle>();
        this.pos=pos;
        this.x=x;
        this.y=y;
        t.add(new Triangle(pos,pos.plus(x),pos.plus(y),texture));
        t.add(new Triangle(pos.plus(x).plus(y),pos.plus(y),pos.plus(x),texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(1.0-u1)),(int)((h-1)*(1.0-u2))})));
    }
    public Rectangle(Vector pos,Vector x,Vector y,Vector dir,int texture){
        t=new ArrayList<Triangle>();
        this.pos=pos;
        this.x=x;
        this.y=y;
        Triangle t1=new Triangle(pos,pos.plus(x),pos.plus(y),texture);
        t1.forceDir(dir);
        Triangle t2=new Triangle(pos.plus(x).plus(y),pos.plus(y),pos.plus(x),texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(1.0-u1)),(int)((h-1)*(1.0-u2))}));
        t2.forceDir(dir);
        t.add(t1);
        t.add(t2);
    }
    public Rectangle(Vector pos,Vector x,Vector y,int texture,int detail){
        t=new ArrayList<Triangle>();
        this.pos=pos;
        this.x=x;
        this.y=y;
        Vector xDetail=this.x.multiply(1.0/detail);
        Vector yDetail=this.y.multiply(1.0/detail);
        for(int i=0;i<detail;i++){
            for(int j=0;j<detail;j++){
                final int iFinal=i;
                final int jFinal=j;
                Vector posDetail=pos.plus(xDetail.multiply(i*1.0)).plus(yDetail.multiply(j*1.0));
                t.add(new Triangle(posDetail,posDetail.plus(xDetail),posDetail.plus(yDetail),texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(iFinal+u1)/detail),(int)((h-1)*(jFinal+u2)/detail)})));
                t.add(new Triangle(posDetail.plus(xDetail).plus(yDetail),posDetail.plus(yDetail),posDetail.plus(xDetail),texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(iFinal+1.0-u1)/detail),(int)((h-1)*(jFinal+1.0-u2)/detail)})));
            }
        }   
    }
    public Rectangle(Vector pos,Vector x,Vector y,Vector dir,int texture,int detail){
        t=new ArrayList<Triangle>();
        this.r=r;
        this.g=g;
        this.b=b;
        this.pos=pos;
        this.x=x;
        this.y=y;
        Vector xDetail=this.x.multiply(1.0/detail);
        Vector yDetail=this.y.multiply(1.0/detail);
        for(int i=0;i<detail;i++){
            for(int j=0;j<detail;j++){
                final int iFinal=i;
                final int jFinal=j;
                Vector posDetail=pos.plus(xDetail.multiply(i*1.0)).plus(yDetail.multiply(j*1.0));
                Triangle t1=new Triangle(posDetail,posDetail.plus(xDetail),posDetail.plus(yDetail),texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(iFinal+u1)/detail),(int)((h-1)*(jFinal+u2)/detail)}));
                Triangle t2=new Triangle(posDetail.plus(xDetail).plus(yDetail),posDetail.plus(yDetail),posDetail.plus(xDetail),texture,(u1,u2,w,h)->(new int[]{(int)((w-1)*(iFinal+1.0-u1)/detail),(int)((h-1)*(jFinal+1.0-u2)/detail)}));
                t1.forceDir(dir);
                t2.forceDir(dir);
                t.add(t1);
                t.add(t2);
            }
        }   
    }
    public ArrayList<Triangle>getTriangles(){
        return t;
    }
    public ArrayList<Sphere>getSpheres(){
        return new ArrayList<Sphere>();
    }
}
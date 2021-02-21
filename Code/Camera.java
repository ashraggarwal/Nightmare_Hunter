import java.awt.Color;
public class Camera{
    private Vector pos,dir,x,y,c;
    private double theta,raster,dMag,clip,horizon;
    private int width;
    public Camera(Vector pos/*,Vector dir*/,Vector c,Double theta,int width,double factor){
        this.pos=pos;
        //this.dir=dir;
        this.c=c;
        this.dMag=c.minus(pos).mag();
        this.dir=c.minus(pos).unit();
        this.x=new Vector(-dir.y(),dir.x(),0.0).unit().neg();
        this.y=dir.cross(this.x);
        this.theta=theta;
        this.width=width;
        this.raster=-width*0.5/Math.tan(theta);
        this.clip=-this.dMag*factor;
        updateHorizon();
    }
    public void updateHorizon(){
        this.horizon=y.multiply(dir.z()/y.z()).dot(y)*raster;
    }
    public Camera(Vector dir,Double dMag,Vector c,Double theta,int width,double factor){
        //this.dir=dir;
        this.c=c;
        this.dMag=dMag;
        this.dir=dir.unit();
        this.pos=this.c.minus(this.dir.multiply(this.dMag));
        this.x=new Vector(-this.dir.y(),this.dir.x(),0.0).unit().neg();
        this.y=this.dir.cross(this.x);
        this.theta=theta;
        this.width=width;
        this.raster=-width*0.5/Math.tan(theta);
        this.clip=-this.dMag*factor;
        updateHorizon();
        //System.out.println(this.pos);
    }
    public void rotateZ(double theta){
        Vector del=pos.minus(c);
        //System.out.println(del.mag());
        double dX=del.x()*Math.cos(theta)-del.y()*Math.sin(theta);
        double dY=del.x()*Math.sin(theta)+del.y()*Math.cos(theta);
        del.x(dX);
        del.y(dY);
        //dir.x(dir.x()*Math.cos(theta)-dir.y()*Math.sin(theta));
        //dir.y(dir.x()*Math.sin(theta)+dir.y()*Math.cos(theta));
        //System.out.println(del.mag());
        pos.x(c.x()+del.x());
        pos.y(c.y()+del.y());
        this.dir=c.minus(pos).unit();
        this.x=new Vector(-dir.y(),dir.x(),0.0).unit().neg();
        this.y=dir.cross(this.x);
        updateHorizon();
        //System.out.println(dir);
        //System.out.println(pos.minus(c).mag());
    }
    public void rotateL(double theta){
        Vector del=pos.minus(c);
        Vector delX=new Vector(del.x(),del.y(),0.0);
        double mag=delX.mag();
        delX.set(delX.unit());
        double dX=mag*Math.cos(theta)-del.z()*Math.sin(theta);
        double dZ=mag*Math.sin(theta)+del.z()*Math.cos(theta);
        del.set(delX.multiply(dX).plus(new Vector(0.0,0.0,dZ)));
        pos.set(c.plus(del));
        this.dir=c.minus(pos).unit();
        this.x=new Vector(-dir.y(),dir.x(),0.0).unit().neg();
        this.y=dir.cross(this.x);
        updateHorizon();
    }
    public Vector changeVars(Vector v){
        Vector del=v.minus(pos);
        return new Vector(x.dot(del),y.dot(del),-dir.dot(del));
    }
    public Vector returnVars(Vector v){
        Vector del=v.minus(pos);
        return new Vector(del.x()*x.x()+del.y()*y.x()-del.z()*dir.x(),del.x()*x.y()+del.y()*y.y()-del.z()*dir.y(),del.x()*x.z()+del.y()*y.z()-del.z()*dir.z());
    }
    public Vector returnVars2(Vector del){
        return new Vector(del.x()*x.x()+del.y()*y.x()-del.z()*dir.x(),del.x()*x.y()+del.y()*y.y()-del.z()*dir.y(),del.x()*x.z()+del.y()*y.z()-del.z()*dir.z());
    }
    public Vector perspective(Vector v){
        return new Vector(v.x()*raster/v.z(),v.y()*raster/v.z(),raster);
    }
    public double getTheta(){
        return theta;
    }
    public void move(Vector v){
        pos=pos.plus(v);
        c=c.plus(v);
    }
    public double getRaster(){
        return raster;
    }
    public void update(Vector head){
        this.c=head;
        this.pos=this.c.minus(this.dir.multiply(dMag));
    }
    public double getClip(){
        return clip;
    }
    public double getHorizon(){
        return horizon;
    }
    public Triangle[] getClippedTriangles(Triangle current,Vector[]vc){
        int num1=-1;
        int num2=-1;
        int num1B=-1;
        int num2B=-1;
        for(int i=0;i<vc.length;i++){
            //in front
            if(vc[i].z()<=clip){
                if(num1==-1){
                    num1=i;
                }else{
                    num2=i;
                }
            }else{
                if(num1B==-1){
                    num1B=i;
                }else{
                    num2B=i;
                }
            }
        }
        Vector norm=returnVars2(new Vector(0.0,0.0,-1.0));
        //System.out.println(norm);
        Vector point=returnVars2(new Vector(0.0,0.0,clip*1.001)).plus(pos);
        //System.out.println(point);
        Vector[]v=current.getV();
        if(num2==-1){
            //System.out.println(changeVars(v[0])+"; "+changeVars(v[1])+"; "+changeVars(v[2]));
            Vector v0=v[num1];
            Vector v1=v[num1B];
            Vector v2=v[num2B];
            Vector dir1=v1.minus(v0);
            Vector dir2=v2.minus(v0);
            v1=v0.minus(dir1.multiply(-norm.dot(point.minus(v0))/norm.dot(dir1)));
            v2=v0.minus(dir2.multiply(-norm.dot(point.minus(v0))/norm.dot(dir2)));
            Vector[]vNew=new Vector[3];
            vNew[num1]=v0;
            vNew[num1B]=v1;
            vNew[num2B]=v2;
            //System.out.println(changeVars(vNew[0])+"; "+changeVars(vNew[1])+"; "+changeVars(vNew[2]));
            //return new Triangle[]{new Triangle(vNew[0],vNew[1],vNew[2],new Color(255,0,0),current.getN())};
            return new Triangle[]{new Triangle(vNew[0],vNew[1],vNew[2],current)};
        }
        Vector v0=v[num1B];
        Vector v1=v[num1];
        Vector v2=v[num2];
        Vector dir1=v1.minus(v0);
        Vector dir2=v2.minus(v0);
        Vector p1=v0.minus(dir1.multiply(-norm.dot(point.minus(v0))/norm.dot(dir1)));
        Vector p2=v0.minus(dir2.multiply(-norm.dot(point.minus(v0))/norm.dot(dir2)));
        Vector[]vNew1=new Vector[3];
        Vector[]vNew2=new Vector[3];
        vNew1[num1B]=p1;
        vNew1[num1]=v1;
        vNew1[num2]=v2;
        vNew2[num1B]=p2;
        vNew2[num1]=p1;
        vNew2[num2]=v2;
        //System.out.println(changeVars(vNew1[2]).z());
        //return new Triangle[]{new Triangle(vNew1[0],vNew1[1],vNew1[2],new Color(255,0,255),current.getN()),new Triangle(vNew2[0],vNew2[1],vNew2[2],new Color(0,0,0),current.getN())};
        return new Triangle[]{new Triangle(vNew1[0],vNew1[1],vNew1[2],current),new Triangle(vNew2[0],vNew2[1],vNew2[2],current)};
    }
}
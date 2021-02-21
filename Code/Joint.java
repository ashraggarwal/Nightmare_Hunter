import java.util.ArrayList;
public class Joint extends Vector{
    private double size;
    private ArrayList<Vector>rotate;
    private boolean sphere;
    public Joint(Vector v,ArrayList<Vector>rotate,double size){
        super(v.x(),v.y(),v.z());
        this.rotate=rotate;
        for(int i=0;i<rotate.size();i++){
            rotate.set(i,rotate.get(i));
        }
        this.size=size;
        this.sphere=true;
    }
    public Joint(double x,double y,double z,double size){
        super(x,y,z);
        this.rotate=new ArrayList<Vector>();
        this.size=size;
        this.sphere=true;
    }
    public Joint(double x,double y,double z){
        super(x,y,z);
        this.rotate=new ArrayList<Vector>();
        this.sphere=false;
    }
    public Joint(Vector v,double size){
        super(v.x(),v.y(),v.z());
        this.rotate=new ArrayList<Vector>();
        this.size=size;
        this.sphere=true;
    }
    public Joint(Vector v){
        super(v.x(),v.y(),v.z());
        this.rotate=new ArrayList<Vector>();
        this.sphere=false;
    }
    public void addRotate(Vector rot){
        rotate.add(rot);
    }
    public ArrayList<Vector>getRotate(){
        return rotate;
    }
    public double getSize(){
        return size;
    }
    public Sphere getSphere(Vector pos,double scale,int r,int g,int b){
        if(this.sphere){
            return new Sphere(super.multiply(scale).plus(pos),size,r,g,b);
        }
        return null;
    }
    public boolean getSphere(){
        return sphere;
    }
    public void rotate(ArrayList<Joint>points,int i,double theta){
        if(i<rotate.size()){
            Vector v=new Vector(super.x(),super.y(),super.z());
            Vector axis=rotate.get(i).unit();
            Vector end=new Vector(Math.cos(theta),Math.sin(theta),0.0);
            for(int j=0;j<points.size();j++){
                Joint joint=points.get(j);
                if(!joint.equals(this)){
                    Vector temp=joint.minus(v);
                    //System.out.println(temp.mag());
                    double dist=temp.dot(axis);
                    Vector p=axis.multiply(dist);
                    Vector x=temp.minus(p);
                    Vector y=x.cross(axis);
                    joint.x(v.x()+p.x()+end.x()*x.x()+end.y()*y.x());
                    joint.y(v.y()+p.y()+end.x()*x.y()+end.y()*y.y());
                    joint.z(v.z()+p.z()+end.x()*x.z()+end.y()*y.z());
                    //System.out.println(joint.minus(v).mag());
                }
                ArrayList<Vector>r=joint.getRotate();
                for(int k=0;k<r.size();k++){
                    Vector vec=r.get(k);
                    double dist2=vec.dot(axis);
                    Vector p2=axis.multiply(dist2);
                    Vector x2=vec.minus(p2);
                    Vector y2=x2.cross(axis);
                    vec.x(p2.x()+end.x()*x2.x()+end.y()*y2.x());
                    vec.y(p2.y()+end.x()*x2.y()+end.y()*y2.y());
                    vec.z(p2.z()+end.x()*x2.z()+end.y()*y2.z());
                }
            }
        }
    }
    public void rotate(Joint[]points,int i,double theta){
        if(i<rotate.size()){
            Vector v=new Vector(super.x(),super.y(),super.z());
            Vector axis=rotate.get(i).unit();
            Vector end=new Vector(Math.cos(theta),Math.sin(theta),0.0);
            for(int j=0;j<points.length;j++){
                Joint joint=points[j];
                if(!joint.equals(this)){
                    Vector temp=joint.minus(v);
                    //System.out.println(temp.mag());
                    double dist=temp.dot(axis);
                    Vector p=axis.multiply(dist);
                    Vector x=temp.minus(p);
                    Vector y=x.cross(axis);
                    joint.x(v.x()+p.x()+end.x()*x.x()+end.y()*y.x());
                    joint.y(v.y()+p.y()+end.x()*x.y()+end.y()*y.y());
                    joint.z(v.z()+p.z()+end.x()*x.z()+end.y()*y.z());
                    //System.out.println(joint.minus(v).mag());
                }
                ArrayList<Vector>r=joint.getRotate();
                for(int k=0;k<r.size();k++){
                    Vector vec=r.get(k);
                    double dist2=vec.dot(axis);
                    Vector p2=axis.multiply(dist2);
                    Vector x2=vec.minus(p2);
                    Vector y2=x2.cross(axis);
                    vec.x(p2.x()+end.x()*x2.x()+end.y()*y2.x());
                    vec.y(p2.y()+end.x()*x2.y()+end.y()*y2.y());
                    vec.z(p2.z()+end.x()*x2.z()+end.y()*y2.z());
                }
            }
        }
    }
}
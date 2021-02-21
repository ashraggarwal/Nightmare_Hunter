import java.io.Serializable;
import java.util.ArrayList;
public class RectPrism implements Shape,Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Triangle>t;
    private ArrayList<Rectangle>rects;
    private int r,g,b;
    private Vector pos,x,y,z;
    private Vector[]nodes;
    private int[][]collisionNums;
    private boolean near;
    public RectPrism(Vector pos,Vector x,Vector y,Vector z){
        this.pos=pos;
        this.x=x;
        this.y=y;
        this.z=z;
        collisionNums=new int[][]{{0,1,2,5},{0,1,3,4},{0,2,3,1},{3,5,6,2},{2,4,5,0},{1,4,6,3}};
        generateNodes();
        t=new ArrayList<Triangle>();
        near=true;
    }
    public RectPrism(Vector pos,Vector x,Vector y,double depth){
        this.pos=pos;
        this.x=x;
        this.y=y;
        this.z=x.cross(y).unit().multiply(depth);
        collisionNums=new int[][]{{0,1,2,5},{0,1,3,4},{0,2,3,1},{3,5,6,2},{2,4,5,0},{1,4,6,3}};
        generateNodes();
        t=new ArrayList<Triangle>();
        near=true;
    }
    public RectPrism(Vector pos,Vector x,Vector y,Vector z,int r,int g,int b){
        this.pos=pos;
        this.x=x;
        this.y=y;
        this.z=z;
        this.r=r;
        this.g=g;
        this.b=b;
        collisionNums=new int[][]{{0,1,2,5},{0,1,3,4},{0,2,3,1},{3,5,6,2},{2,4,5,0},{1,4,6,3}};
        generateNodes();
        t=new ArrayList<Triangle>();
        generateRects();
        near=true;
    }
    public RectPrism(Vector pos,Vector x,Vector y,double depth,int r,int g,int b){
        this.pos=pos;
        this.x=x;
        this.y=y;
        this.z=x.cross(y).unit().multiply(depth);
        this.r=r;
        this.g=g;
        this.b=b;
        collisionNums=new int[][]{{0,1,2,5},{0,1,3,4},{0,2,3,1},{3,5,6,2},{2,4,5,0},{1,4,6,3}};
        generateNodes();
        t=new ArrayList<Triangle>();
        generateRects();
        near=true;
    }
    public RectPrism(Vector pos,Vector x,Vector y,Vector z,int r,int g,int b,int detail){
        this.pos=pos;
        this.x=x;
        this.y=y;
        this.z=z;
        this.r=r;
        this.g=g;
        this.b=b;
        collisionNums=new int[][]{{0,1,2,5},{0,1,3,4},{0,2,3,1},{3,5,6,2},{2,4,5,0},{1,4,6,3}};
        generateNodes();
        t=new ArrayList<Triangle>();
        generateRects(detail);
        near=true;
    }
    public RectPrism(Vector pos,Vector x,Vector y,double depth,int r,int g,int b,int detail){
        this.pos=pos;
        this.x=x;
        this.y=y;
        this.z=x.cross(y).unit().multiply(depth);
        this.r=r;
        this.g=g;
        this.b=b;
        collisionNums=new int[][]{{0,1,2,5},{0,1,3,4},{0,2,3,1},{3,5,6,2},{2,4,5,0},{1,4,6,3}};
        generateNodes();
        t=new ArrayList<Triangle>();
        generateRects(detail);
        near=true;
    }
    public void generateRects(int detail){
        rects=new ArrayList<Rectangle>();
        rects.add(new Rectangle(nodes[0],this.x,this.y,nodes[0].minus(nodes[8]),r,g,b,detail));
        rects.add(new Rectangle(nodes[0],this.x,this.z,nodes[0].minus(nodes[8]),r,g,b,detail));
        rects.add(new Rectangle(nodes[0],this.y,this.z,nodes[0].minus(nodes[8]),r,g,b,detail));
        rects.add(new Rectangle(nodes[3],this.x,this.y,nodes[3].minus(nodes[8]),r,g,b,detail));
        rects.add(new Rectangle(nodes[2],this.x,this.z,nodes[2].minus(nodes[8]),r,g,b,detail));
        rects.add(new Rectangle(nodes[1],this.y,this.z,nodes[1].minus(nodes[8]),r,g,b,detail));
        for(int i=0;i<rects.size();i++){
            t.addAll(rects.get(i).getTriangles());
        }
    }
    public void generateRects(){
        rects=new ArrayList<Rectangle>();
        rects.add(new Rectangle(nodes[0],this.x,this.y,nodes[0].minus(nodes[8]),r,g,b));
        rects.add(new Rectangle(nodes[0],this.x,this.z,nodes[0].minus(nodes[8]),r,g,b));
        rects.add(new Rectangle(nodes[0],this.y,this.z,nodes[0].minus(nodes[8]),r,g,b));
        rects.add(new Rectangle(nodes[3],this.x,this.y,nodes[3].minus(nodes[8]),r,g,b));
        rects.add(new Rectangle(nodes[2],this.x,this.z,nodes[2].minus(nodes[8]),r,g,b));
        rects.add(new Rectangle(nodes[1],this.y,this.z,nodes[1].minus(nodes[8]),r,g,b));
        for(int i=0;i<rects.size();i++){
            t.addAll(rects.get(i).getTriangles());
        }
    }
    public void setNear(boolean near){
        this.near=near;
    }
    public boolean getNear(){
        return near;
    }
    public void generateNodes(){
        nodes=new Vector[9];
        Vector v1=this.pos.plus(this.x);
        Vector v2=this.pos.plus(this.y);
        Vector v3=this.pos.plus(this.z);
        Vector v4=v1.plus(this.y);
        nodes[0]=this.pos;
        nodes[1]=v1;
        nodes[2]=v2;
        nodes[3]=v3;
        nodes[4]=v4;
        nodes[5]=v2.plus(this.z);
        nodes[6]=v3.plus(this.x);
        nodes[7]=v4.plus(this.z);
        nodes[8]=this.pos.plus(this.x.multiply(0.5)).plus(this.y.multiply(0.5)).plus(this.z.multiply(0.5));
    }
    public Vector[] getNodes(){
        return nodes;
    }
    public ArrayList<Triangle> getTriangles(){
        return t;
    }
    public boolean colliding(RectPrism r){
        if(!collision(r)||!r.collision(this)){
            return false;
        }
        return true;
    }
    public boolean collision(RectPrism r){
        Vector[]nodes2=r.getNodes();
        loop1:for(int i=0;i<collisionNums.length;i++){
            int[]current=collisionNums[i];
            Vector n=nodes[current[1]].minus(nodes[current[0]]).cross(nodes[current[2]].minus(nodes[current[0]]));
            double test=n.dot(nodes[current[3]].minus(nodes[current[0]]));
            for(int j=0;j<8;j++){
                double check=n.dot(nodes2[j].minus(nodes[current[0]]))*test;
                if(check>0){
                    continue loop1;
                }
            }
            return false;
        }
        return true;
    }
    public ArrayList<Sphere>getSpheres(){
        return new ArrayList<Sphere>();
    }
    public void move(Vector v){
        this.pos.set(this.pos.plus(v));
        generateNodes();
    }
    public void moveZ(double depth){
        this.pos.z(this.pos.z()+depth);
        generateNodes();
    }
}
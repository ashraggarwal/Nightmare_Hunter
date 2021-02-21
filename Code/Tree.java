import java.util.ArrayList;
public class Tree implements Obstacle,Trackable{
    private boolean near;
    private Cylinder stump;
    private Sphere s1,s2,s3,s4;
    private Rectangle farStump;
    private Triangle farBranch;
    private Vector base,top,branch1,branch2,branch3;
    private ArrayList<Sphere>spheres,farSpheres;
    private ArrayList<Triangle>farTriangles,triangles;
    private RectPrism box;
    private ArrayList<RectPrism>walls,farWalls;
    public Tree(Vector base,double size,double height,int woodTexture,int r,int g,int b){
        this.near=true;
        this.base=base.minus(new Vector(0.0,0.0,size*0.1));
        this.top=base.plus(new Vector(0.0,0.0,size*height));
        this.branch1=base.plus(new Vector(0.16,0.092,0.84*height).multiply(size));
        this.branch2=base.plus(new Vector(-0.16,0.092,0.84*height).multiply(size));
        this.branch3=base.plus(new Vector(0.0,-0.185,0.84*height).multiply(size));
        this.stump=new Cylinder(this.base,this.top,0.08*size,woodTexture);
        this.s1=new Sphere(this.top,0.2*size,r,g,b);
        this.s2=new Sphere(this.branch1,0.2*size,r,g,b);
        this.s3=new Sphere(this.branch2,0.2*size,r,g,b);
        this.s4=new Sphere(this.branch3,0.2*size,r,g,b);
        triangles=new ArrayList<Triangle>();
        spheres=new ArrayList<Sphere>();
        triangles.addAll(stump.getTriangles());
        spheres.add(this.s1);
        spheres.add(this.s2);
        spheres.add(this.s3);
        spheres.add(this.s4);
        walls=new ArrayList<RectPrism>();
        box=new RectPrism(this.base.minus(new Vector(0.08,0.08,0.0).multiply(size)),new Vector(0.16*size,0.0,0.0),new Vector(0.0,0.16*size,0.0),new Vector(0.0,0.0,(height+0.2)*size));
        walls.add(box);
        farTriangles=new ArrayList<Triangle>();
        farSpheres=new ArrayList<Sphere>();
        farWalls=new ArrayList<RectPrism>();
    }
    public Tree(double x,double y,Map m,double size,double height,int woodTexture,int r,int g,int b){
        this.near=true;
        this.base=new Vector(x,y,m.getZ(x,y));
        this.top=this.base.plus(new Vector(0.0,0.0,size*height));
        this.branch1=this.base.plus(new Vector(0.16,0.092,0.84*height).multiply(size));
        this.branch2=this.base.plus(new Vector(-0.16,0.092,0.84*height).multiply(size));
        this.branch3=this.base.plus(new Vector(0.0,-0.185,0.84*height).multiply(size));
        this.base=this.base.minus(new Vector(0.0,0.0,size*0.1));
        this.stump=new Cylinder(this.base,this.top,0.08*size,woodTexture);
        this.s1=new Sphere(this.top,0.2*size,r,g,b);
        this.s2=new Sphere(this.branch1,0.2*size,r,g,b);
        this.s3=new Sphere(this.branch2,0.2*size,r,g,b);
        this.s4=new Sphere(this.branch3,0.2*size,r,g,b);
        triangles=new ArrayList<Triangle>();
        spheres=new ArrayList<Sphere>();
        triangles.addAll(stump.getTriangles());
        spheres.add(this.s1);
        spheres.add(this.s2);
        spheres.add(this.s3);
        spheres.add(this.s4);
        walls=new ArrayList<RectPrism>();
        box=new RectPrism(this.base.minus(new Vector(0.08,0.08,0.0).multiply(size)),new Vector(0.16*size,0.0,0.0),new Vector(0.0,0.16*size,0.0),new Vector(0.0,0.0,(height+0.3)*size));
        walls.add(box);
        //farStump=new Rectangle(this.base.plus(new Vector(-0.36*size,0.0,0.0)),new Vector(0.72*size,0.0,0.0),new Vector(0.0,0.0,size*height*1.25),6);
        farTriangles=new ArrayList<Triangle>();
        farTriangles.addAll(farStump.getTriangles());
        farSpheres=new ArrayList<Sphere>();
        farWalls=new ArrayList<RectPrism>();
    }
    public ArrayList<Triangle>getTriangles(){
        if(near){
            return triangles;
        }
        return farTriangles;
    }
    public ArrayList<Sphere>getSpheres(){
        if(near){
            return spheres;
        }
        return farSpheres;
    }
    public ArrayList<RectPrism>getWalls(){
        if(near){
            return walls;
        }
        return farWalls;
    }
    public Vector getBase(){
        return base;
    }
    public void setNear(boolean near){
        this.near=near;
    }
    public Vector getPos(){
        return base;
    }
}
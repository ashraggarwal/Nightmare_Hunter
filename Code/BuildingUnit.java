import java.util.ArrayList;
public class BuildingUnit implements Obstacle{
    private Cylinder room;
    private ArrayList<Triangle>triangles;
    private ArrayList<RectPrism>walls;
    private ArrayList<Sphere>spheres;
    public BuildingUnit(Vector pos,Vector x,Vector y,double height,double roofSize,int exterior,int roof){
        double root2=Math.sqrt(2);
        triangles=new ArrayList<Triangle>();
        walls=new ArrayList<RectPrism>();
        spheres=new ArrayList<Sphere>();
        walls.add(new RectPrism(pos,x,y,1000*height*(1+roofSize)));
        Vector xRadius=x.multiply(0.5);
        Vector yRadius=y.multiply(0.5);
        Vector center=pos.plus(xRadius).plus(yRadius);
        room=new Cylinder(center,center.plus(new Vector(0.0,0.0,height)),xRadius.multiply(Math.sqrt(2)),yRadius.multiply(Math.sqrt(2)),exterior,4);
        Vector node0=pos.plus(new Vector(0.0,0.0,height));
        Vector node1=node0.plus(x);
        Vector node2=node0.plus(y);
        Vector node3=node1.plus(y);
        Vector node4=center.plus(new Vector(0.0,0.0,height*(1+roofSize)));
        triangles.add(new Triangle(node0,node1,node4,roof));
        triangles.add(new Triangle(node1,node3,node4,roof));
        triangles.add(new Triangle(node3,node2,node4,roof));
        triangles.add(new Triangle(node2,node0,node4,roof));
        triangles.addAll(room.getTriangles());
    }
    public BuildingUnit(Vector pos,Vector x,Vector y,double height,double roofSize,int exterior,int roof,Map m){
        double root2=Math.sqrt(2);
        triangles=new ArrayList<Triangle>();
        walls=new ArrayList<RectPrism>();
        spheres=new ArrayList<Sphere>();
        pos.z(shiftOnMap(pos,x,y,m));
        walls.add(new RectPrism(pos,x,y,1000*height*(1+roofSize)));
        Vector xRadius=x.multiply(0.5);
        Vector yRadius=y.multiply(0.5);
        Vector center=pos.plus(xRadius).plus(yRadius);
        room=new Cylinder(center,center.plus(new Vector(0.0,0.0,height)),xRadius.multiply(Math.sqrt(2)),yRadius.multiply(Math.sqrt(2)),exterior,4);
        Vector node0=pos.plus(new Vector(0.0,0.0,height));
        Vector node1=node0.plus(x);
        Vector node2=node0.plus(y);
        Vector node3=node1.plus(y);
        Vector node4=center.plus(new Vector(0.0,0.0,height*(1+roofSize)));
        triangles.add(new Triangle(node0,node1,node4,roof));
        triangles.add(new Triangle(node1,node3,node4,roof));
        triangles.add(new Triangle(node3,node2,node4,roof));
        triangles.add(new Triangle(node2,node0,node4,roof));
        triangles.addAll(room.getTriangles());
    }
    public static double shiftOnMap(Vector pos,Vector x,Vector y,Map m){
        //double[][]coords=new double[][]{{pos.x(),pos.y()},{pos.x()+x.x(),pos.y()+x.y()},{pos.x()+y.x(),pos.y()+y.y()},{pos.x()+x.x()+y.x(),pos.y()+x.y()+y.y()}};
        double[][]coords=new double[][]{{pos.x(),pos.y()},{pos.x()+x.x(),pos.y()+x.y()},{pos.x()+x.x()/2,pos.y()+x.y()/2},{pos.x()+y.x(),pos.y()+y.y()},{pos.x()+y.x()/2,pos.y()+y.y()/2},{pos.x()+x.x()+y.x(),pos.y()+x.y()+y.y()},{pos.x()+x.x()/2+y.x(),pos.y()+x.y()/2+y.y()},{pos.x()+x.x()+y.x()/2,pos.y()+x.y()+y.y()/2}};
        double minHeight=m.getZ(coords[0][0],coords[0][1]);
        for(int i=1;i<coords.length;i++){
            if(m.getZ(coords[i][0],coords[i][1])<minHeight){
                minHeight=m.getZ(coords[i][0],coords[i][1]);
            }
        }
        return minHeight;
    }
    public ArrayList<Triangle>getTriangles(){
        return triangles;
    }
    public ArrayList<Sphere>getSpheres(){
        return spheres;
    }
    public ArrayList<RectPrism>getWalls(){
        return walls;
    }
}
import java.util.ArrayList;
public class Building implements Obstacle{
    private ArrayList<Triangle>triangles;
    private ArrayList<BuildingUnit>units;
    private ArrayList<Sphere>spheres;
    private ArrayList<RectPrism>walls;
    public Building(Vector center,double size,double multiplier,double floorHeight,double roofSize,int floorsC,int floorsR,int floorsL,int floorsU,int floorsD,int wall,int roof,Map m){
        triangles=new ArrayList<Triangle>();
        walls=new ArrayList<RectPrism>();
        spheres=new ArrayList<Sphere>();
        units=new ArrayList<BuildingUnit>();
        Vector[][]nodes=new Vector[][]{{new Vector(-5.0/13,-5.0/13,0.0),new Vector(10.0/13,0.0,0.0),new Vector(0.0,10.0/13,0.0)},{new Vector(0.0,-7.0/13,0.0),new Vector(7.0/13,7.0/13,0.0),new Vector(-7.0/13,7.0/13,0.0)},{new Vector(1.0/13,-3.0/13,0.0),new Vector(12.0/13,0.0,0.0),new Vector(0.0,6.0/13,0.0)},{new Vector(-1.0,-3.0/13,0.0),new Vector(12.0/13,0.0,0.0),new Vector(0.0,6.0/13,0.0)},{new Vector(-3.0/13,1.0/13,0.0),new Vector(6.0/13,0.0,0.0),new Vector(0.0,12.0/13,0.0)},{new Vector(-3.0/13,-1.0,0.0),new Vector(6.0/13,0.0,0.0),new Vector(0.0,12.0/13,0.0)}};
        int[]floors=new int[]{floorsC,floorsC,floorsR,floorsL,floorsU,floorsD};
        for(int i=0;i<nodes.length;i++){
            for(int j=0;j<nodes[i].length;j++){
                nodes[i][j]=nodes[i][j].multiply(size);
            }            
            nodes[i][0].z(BuildingUnit.shiftOnMap(nodes[i][0].plus(center),nodes[i][1],nodes[i][2],m));
        }
        for(int i=0;i<nodes.length;i++){
            for(int j=0;j<floors[i];j++){
                units.add(new BuildingUnit(nodes[i][0].plus(center).plus(new Vector(0.0,0.0,floorHeight*size*j)),nodes[i][1],nodes[i][2],size*floorHeight,roofSize,wall,roof));
                for(int k=0;k<nodes[i].length;k++){
                    nodes[i][k]=nodes[i][k].multiply(multiplier);
                }
            }
        }
        for(int i=0;i<units.size();i++){
            triangles.addAll(units.get(i).getTriangles());
            walls.addAll(units.get(i).getWalls());
        }
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
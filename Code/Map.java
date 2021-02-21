import java.util.ArrayList;
public class Map implements Shape{
    private ArrayList<Triangle>t;
    private ArrayList<Triangle>t2;
    private ArrayList<Triangle>t5;
    private ArrayList<Triangle>t10;
    private ArrayList<Triangle>triangles;
    private ArrayList<Sphere>s;
    private Vector[][]map;
    private double frequency,amplitude,persistence,scale,exponent;
    private int octaves,width,height;
    private double[]biomeDivisions;
    private ArrayList<Tree>trees;
    public Map(){
        this.frequency=0.015;
        this.amplitude=2400.0;
        this.octaves=10;
        this.persistence=0.25;
        this.width=1000;
        this.height=1000;
        this.scale=20.0;
        this.exponent=1.2;
        /*this.frequency=0.03;
        this.amplitude=4000.0;
        this.octaves=10;
        this.persistence=0.25;
        this.width=1000;
        this.height=1000;
        this.scale=40.0;
        this.exponent=1.2;*/
        this.map=new Vector[width+1][height+1];
        //PerlinNoise p=new PerlinNoise();
        this.t=new ArrayList<Triangle>(width*height*2);
        this.t2=new ArrayList<Triangle>(width*height/2);
        this.t5=new ArrayList<Triangle>(width*height*2/25);
        this.t10=new ArrayList<Triangle>(width*height/50);
        this.triangles=new ArrayList<Triangle>();
        this.s=new ArrayList<Sphere>();
        for(int i=0;i<this.map.length;i++){
            for(int j=0;j<this.map[i].length;j++){
                double d=dLCont(i,j);
                //this.map[i][j]=new Vector(i*this.scale,j*this.scale,this.amplitude*(weight+Math.pow(/*p.octavePerlinHalf*/SimplexNoise.noiseOctaves(i*this.frequency,j*this.frequency/*,0.0*/,this.octaves,this.persistence),this.exponent)-weight*Math.pow(d,0.6))/(1+weight));
                double elevation=SimplexNoise.noiseOctaves(i*this.frequency,j*this.frequency/*,0.0*/,this.octaves,this.persistence);
                elevation=Math.pow(elevation,exponent);
                double lower=lower(d);
                double upper=upper(d);
                elevation=lower+elevation*(upper-lower);
                this.map[i][j]=new Vector(i*this.scale,j*this.scale,this.amplitude*elevation);                
                //s.add(new Sphere(map[i][j],scale/2,255,0,0));
            }
        }
        //System.out.println(map[1][1]);
        //System.out.println(1);
        biomeDivisions=new double[4];
        /*biomeDivisions[0]=0.1;
        biomeDivisions[1]=0.2;
        biomeDivisions[2]=0.5;
        biomeDivisions[3]=0.8;*/
        biomeDivisions[0]=0.12;
        biomeDivisions[1]=0.20;
        biomeDivisions[2]=0.50;
        biomeDivisions[3]=0.75;
        generate(t,1);
        //System.out.println(2);
        generate(t2,2);
        //System.out.println(3);
        generate(t5,5);
        //System.out.println(4);
        generate(t10,10);
        this.trees=new ArrayList<Tree>();
        generateTrees();
        //System.out.println("I didn't run out of memory somehow");
    }
    public int getBiome(Triangle triangle){
        double averageHeight=triangle.getAverageHeight();
        for(int i=0;i<biomeDivisions.length;i++){
            if(i==0){
                if(averageHeight<biomeDivisions[0]*amplitude){
                   return 0; 
                }
            }else if(i==2){
                double slope=triangle.getGrad().dot(triangle.getGrad().unit());
                double cutoff=0.7;
                if(averageHeight>=biomeDivisions[1]*amplitude&&averageHeight<biomeDivisions[2]*amplitude){
                    if(slope<cutoff){
                        return i;
                    }else{
                        return 8;
                    }
                }
            }else if(i==3){
                double slope=triangle.getGrad().dot(triangle.getGrad().unit());
                double cutoff=3.0;
                if(averageHeight>=biomeDivisions[2]*amplitude&&averageHeight<biomeDivisions[3]*amplitude){
                    if(slope<cutoff){
                        return i;
                    }else{
                        return 9;
                    }
                }
            }else{
                if(averageHeight>=biomeDivisions[i-1]*amplitude&&averageHeight<biomeDivisions[i]*amplitude){
                    return i;
                }
            }
        }
        return biomeDivisions.length;
    }
    public void checkTrees(ArrayList<Obstacle>obstacles){
        loop1:for(int k=0;k<trees.size();k++){
            for(int l=0;l<trees.get(k).getWalls().size();l++){
                for(int i=0;i<obstacles.size();i++){
                    for(int j=0;j<obstacles.get(i).getWalls().size();j++){
                        if(trees.get(k).getWalls().get(l).colliding(obstacles.get(i).getWalls().get(j))){
                            trees.remove(k);
                            k--;
                            continue loop1;
                        }
                    }
                }
            }
        }
    }
    private void addForest(double x,double y,int space,double size){
        for(int i=Math.max(0,(int)(x-size));i<=Math.min(width-1,(int)(x+size));i+=space){
            //double[]temp=treeNoise[i];
            for(int j=Math.max(0,(int)(y-size));j<=Math.min(width-1,(int)(y+size));j+=space){
                //temp[j]=SimplexNoise.noise(i*200*space*1.0/width,j*space*1.0/height);
                double xPos=scale*(i+space*SimplexNoise.noise(i*200*space*1.0/width,0.0));
                double yPos=scale*(j+space*SimplexNoise.noise(0.0,j*200*space*1.0/height));
                double z=getZ(xPos,yPos);
                if(biomeDivisions[1]*amplitude<=z&&biomeDivisions[2]*amplitude>z){
                    trees.add(new Tree(new Vector(x,y,z),1000.0,1.0,6,0,255,0));
                }
            }
        }
    }
    public void updateTrees(Person p){
        Vector pos=p.getPos();
        double cutoff=1000.0;
        for(int i=0;i<trees.size();i++){
            Tree current=trees.get(i);
            Vector dist=current.getBase().minus(pos);
            if(dist.dot(dist)<cutoff*cutoff){
                current.setNear(true);
            }else{
                current.setNear(false);
            }
        }
    }
    private void generateTrees(){
        int space=8;
        double minSize=100.0;
        double maxSize=500.0;
        //double[][]treeNoise=new double[width/space][height/space];
        for(int i=0;i<width;i+=space){
            //double[]temp=treeNoise[i];
            for(int j=0;j<height;j+=space){
                //temp[j]=SimplexNoise.noise(i*200*space*1.0/width,j*space*1.0/height);
                double x=scale*(i+space*SimplexNoise.noise(i*200*space*1.0/width,0.0));
                double y=scale*(j+space*SimplexNoise.noise(0.0,j*200*space*1.0/height));
                double z=getZ(x,y);
                double chance=SimplexNoise.noise(i*200*space*1.0/width,j*200*space*1.0/height);
                double bar=(getTriangle(x,y).getN().z()+z/amplitude)/2;
                if(biomeDivisions[1]*amplitude<=z&&biomeDivisions[2]*amplitude>z&&chance>bar){
                    double noise=(chance-bar)/(1.0-bar);
                    double size=minSize+(noise)*(maxSize-minSize);
                    trees.add(new Tree(new Vector(x,y,z),size,1.0,6,0,255,0));
                }
            }
        }
        /*int numForests=10;
        double minSize=10.0;
        double maxSize=20.0;
        int minSpace=5;
        int maxSpace=10;
        int shiftX=0;
        int shiftY=100;
        for(int i=0;i<numForests;i++){
            double x=scale*width*SimplexNoise.noise(shiftX*0.5,shiftY*0.5);
            double y=scale*height*SimplexNoise.noise(shiftY*0.5,shiftX*0.5);
            double z=getZ(x,y);
            if(biomeDivisions[1]*amplitude<=z&&biomeDivisions[2]*amplitude>z){
                trees.add(new Tree(new Vector(x,y,z),1000.0,1.0,6,0,255,0));
                addForest(x,y,(int)(minSpace+(maxSpace-minSpace)*SimplexNoise.noise(x,y)),minSize+(maxSize-minSize)*SimplexNoise.noise(y,x));
            }else{
                shiftX+=10;
                shiftY-=10;
                i--;
            }
        }*/
        //int num=3;
        /*for(int i=0;i<treeNoise.length;i++){
            double[]temp=treeNoise[i];
            loop1:for(int j=0;j<temp.length;j++){
                for(int k=Math.max(0,i-num);k<=Math.min(width/2-1,i+num);k++){
                    double[] temp2=treeNoise[k];
                    for(int l=Math.max(0,j-num);l<=Math.min(height/2-1,j+num);l++){
                        if(temp[j]<temp2[l]){
                            continue loop1;
                        }
                        double x=2*(i+SimplexNoise.noise(i*200/width,j*200/height));
                        double y=2*(j+SimplexNoise.noise(i*200/width,j*200/height));
                        double z=getZ(x,y);
                        if(biomeDivisions[1]<=z&&biomeDivisions[2]>z){
                            System.out.println(x+" "+y+" "+z);
                            trees.add(new Tree(new Vector(x,y,z),1000.0,1.0,6,0,255,0));
                            j+=num-1;
                            continue loop1;
                        }
                    }
                }
            }
        }*/
    }
    private void generate(ArrayList<Triangle>t,int constant){
        for(int i=0;i<map.length-1;i+=constant){
            for(int j=0;j<map[i].length-1;j+=constant){
                //t.add(new Triangle(map[i][j],map[i+1][j],map[i][j+1],0,255,0));
                //t.add(new Triangle(map[i][j+1],map[i+1][j],map[i+1][j+1],0,255,0));
                Vector p00=map[i][j];
                Vector p10=map[i+constant][j];
                Vector p01=map[i][j+constant];
                Vector p11=map[i+constant][j+constant];
                //t.add(new Triangle(p00,p10,p01,0,(int)(255*(p00.z()+p10.z()+p01.z())/(3*amplitude)),255-(int)(255*Math.pow((p00.z()+p10.z()+p01.z())/(3*amplitude),0.2))));
                //t.add(new Triangle(p11,p01,p10,0,(int)(255*(p11.z()+p10.z()+p01.z())/(3*amplitude)),255-(int)(255*Math.pow((p11.z()+p10.z()+p01.z())/(3*amplitude),0.2))));
                int texture1=0;
                int texture2=0;
                Triangle t1=new Triangle(p00,p10,p01,texture1);
                t1.setTexture(getBiome(t1));
                Triangle t2=new Triangle(p11,p01,p10,texture2,(u1,u2,w,h)->(new int[]{(int)((w-1)*(1.0-u1)),(int)((h-1)*(1.0-u2))}));
                t2.setTexture(getBiome(t2));
                t.add(t1);
                t.add(t2);
            }
        }
    }
    public double lower(double d){
        //return 0.3*(1.0-d);
        return 0.4*Math.log10(10-9*d);
    }
    public double upper(double d){
        double t=1.0-d;
        return t*t*t*(t*(6*t-15)+10);
    }
    public double dAbs(int i,int j){
        return Math.abs(i*1.0/this.width-1.0/2)+Math.abs(j*1.0/this.height-1.0/2);
    }
    public double dLInf(int i,int j){
        return 2*Math.max(Math.abs(i*1.0/this.width-1.0/2),Math.abs(j*1.0/this.height-1.0/2));
    }
    public double dL2(int i,int j){
        return Math.sqrt(2*Math.pow((i*1.0/this.width-1.0/2),2)+2*Math.pow((j*1.0/this.height-1.0/2),2));
    }
    public double dLp(int i,int j,double p){
        return Math.pow((Math.pow(Math.abs(i*2.0/this.width-1.0),p)+Math.pow(Math.abs(j*2.0/this.height-1.0),p))/2,1/p);
    }
    public double dLCont(int i,int j){
        if(dLInf(i,j)==1.0){
            return 1.0;
        }else{
            return dLp(i,j,1/(1-dLInf(i,j)));
        }
    }
    public Triangle getTriangle(double x,double y){
        if(x<0||y<0||x>=(width)*scale||y>=(height)*scale){
            return null;
        }
        int xPos=(int)(x/scale);
        int yPos=(int)(y/scale);
        //System.out.println(xPos+" "+yPos);
        double xPos2=x%scale;
        double yPos2=x%scale;
        if(xPos2+yPos2>scale){
            return t.get((yPos+(height)*xPos)*2+1);
        }
        return t.get((yPos+(height)*xPos)*2);
    }
    public void updateTriangles(Vector pos){
        //System.out.println("hello"); 
        /*double size1=0.05;
        double size2=0.1;
        double size5=0.20;*/
        double size1=0.1;
        double size2=0.2;
        double size5=0.3;
        int sx=(int)(size1*width/2);
        int sx2=(int)(size2*width/2);
        int sx5=(int)(size5*width/2);
        int sy=(int)(size1*height/2);
        int sy2=(int)(size2*height/2);
        int sy5=(int)(size5*height/2);
        sx2-=sx2%2;
        sx5-=sx5%5;
        sy2-=sy2%2;
        sy5-=sy5%5;
        int x=(int)(pos.x()/scale);
        int y=(int)(pos.y()/scale);
        int x110=x-sx-sx2-sx5;
        x110 -= x110 % 10;
        int x210 =x+sx+sx2+sx5;
        x210 += x210 % 10;
        int y110 = y - sy - sy2 - sy5;
        y110 -= y110 % 10;
        int y210 = y+sy+sy2+sy5;
        y210 += y210 % 10;
        int x15=x110+sx5;
        int x25=x210-sx5;
        int y15=y110+sy5;
        int y25=y210-sy5;
        int x12=x15+sx2;
        int x22=x25-sx2;
        int y12=y15+sy2;
        int y22=y25-sy2;
        x110=boundX(x110);
        x15=boundX(x15);
        x12=boundX(x12);
        x210=boundX(x210);
        x25=boundX(x25);
        x22=boundX(x22);
        y110=boundY(y110);
        y15=boundY(y15);
        y12=boundY(y12);
        y210=boundY(y210);
        y25=boundY(y25);
        y22=boundY(y22);
        triangles=new ArrayList<Triangle>(500000);
        //System.out.println(x+" "+y);
        //System.out.println(x12+" "+x22+" "+y12+" "+y22);
        //System.out.println(x15+" "+x25+" "+y15+" "+y25);
        //System.out.println(x110+" "+x210+" "+y110+" "+y210);
        for(int i=x12;i<x22;i++){
            for(int j=y12;j<y22;j++){
                triangles.add(getTriangle0(t,i,j,1));
                triangles.add(getTriangle1(t,i,j,1));
            }
        }
        addTriangles(x15,x25,y15,y25,x12,x22,y12,y22,2,t2);
        addTriangles(x110,x210,y110,y210,x15,x25,y15,y25,5,t5);
        addTriangles(0,width,0,height,x110,x210,y110,y210,10,t10);
    }
    public void addTriangles(int xOuter1,int xOuter2,int yOuter1,int yOuter2,int xInner1,int xInner2,int yInner1,int yInner2,int size,ArrayList<Triangle>t){
        for(int x=xOuter1;x<xOuter2;x+=size){
            for(int y=yOuter1;y<yInner1;y+=size){
                triangles.add(getTriangle0(t,x,y,size));
                triangles.add(getTriangle1(t,x,y,size));
            }
            for(int y=yInner2;y<yOuter2;y+=size){
                triangles.add(getTriangle0(t,x,y,size));
                triangles.add(getTriangle1(t,x,y,size));
            }
        }
        for(int y=yInner1;y<yInner2;y+=size){
            for(int x=xOuter1;x<xInner1;x+=size){
                triangles.add(getTriangle0(t,x,y,size));
                triangles.add(getTriangle1(t,x,y,size));
            }
            for(int x=xInner2;x<xOuter2;x+=size){
                triangles.add(getTriangle0(t,x,y,size));
                triangles.add(getTriangle1(t,x,y,size));
            }
        }
    }
    public Triangle getTriangle0(ArrayList<Triangle>t,int x,int y,int size){
        //System.out.println(x+" "+y+" "+width+" "+size);
        return t.get(((y/size)+(x/size)*(height/size))*2);
    }
    public Triangle getTriangle1(ArrayList<Triangle>t,int x,int y,int size){
        return t.get(((y/size)+(x/size)*(height/size))*2+1);
    }
    public int boundX(int x){
        if(x<0){
            return 0;
        }
        if(x>width){
            return width;
        }
        return x;
    }
    public int boundY(int y){
        if(y<0){
            return 0;
        }
        if(y>height){
            return height;
        }
        return y;
    }
    public ArrayList<Triangle>getTriangles(){
        return triangles;
    }
    public ArrayList<Sphere>getSpheres(){
        return s;
    }
    public double getZ(double x,double y){
        if(x<0||y<0||x>=(width)*scale||y>=(height)*scale){
            return 0.0;
        }
        int i=(int)(x/scale);
        int j=(int)(y/scale);
        //System.out.println(i+" "+j);
        double xPos=(x%scale)/scale;
        double yPos=(y%scale)/scale;
        //System.out.println(xPos+" "+yPos);
        if(xPos+yPos>1){
            //System.out.println(map[i+1][j].z()+" "+map[i][j+1].z()+" "+map[i+1][j+1].z());
            //System.out.println(map[i+1][j+1].z()*(xPos+yPos-1)+map[i+1][j].z()*(1-xPos)+map[i][j+1].z()*(1-yPos)+" "+1);
            return map[i+1][j+1].z()*(xPos+yPos-1)+map[i][j+1].z()*(1-xPos)+map[i+1][j].z()*(1-yPos);
        }
        //System.out.println(map[i][j].z()+" "+map[i+1][j].z()+" "+map[i][j+1].z());
        //System.out.println(map[i][j].z()*(1-xPos-yPos)+map[i+1][j].z()*xPos+map[i][j+1].z()*yPos+" "+2);
        return map[i][j].z()*(1-xPos-yPos)+map[i+1][j].z()*xPos+map[i][j+1].z()*yPos;
    }
    public ArrayList<Tree>getTrees(){
        return trees;
    }
}
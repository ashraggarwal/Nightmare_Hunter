import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Image;

public class Screen extends JPanel implements KeyListener, DataListener {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String hostName = "localhost";
	private int portNumber = 1024;
	private int myID;
	private int playerIndex = -1;
	private GameData gameData;
	private ArrayList<Triangle> trianglesS;
	private ArrayList<Sphere> spheresS;
	private ArrayList<Shape> shapes;
	private ArrayList<RectPrism> walls;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Monster> monsters;
	private Camera camera;
	private int width, height;
	private Coord[][] zBuffer;
	private Image currentImage;
	private BufferedImage wood, grass, water, beach, rock, snow, armor, bark, tree, cliff, cliff2, wall, roof, hilt,blade, monster;
	private BufferedImage[] textures;
	private Map map;
	private Player person;
	private boolean keyA, keyW, keyS, keyD, keyUp, keyDown, keyRight, keyLeft, keySpace, keyShift, key1,keyEnter;
	private final int constant = 2;
	private Vector light;
	private int time, dayLength;
	private DataCheck dataCheck;
	private ArrayList<Player> players;
	private Queue<boolean[]>actions;
	private double[]damage,tempDamage;
	private Container<Monster> currentMonster=new Container<Monster>();
	public Screen() {
		actions=new Queue<boolean[]>();
		myID = -1;
		playerIndex = -1;
		monsters=new ArrayList<Monster>();
		try {
			makeConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.time = 0;
		this.dayLength = 10000;
		this.light = new Vector(0.0, 0.0, 1.0).unit();
		keyA = false;
		keyW = false;
		keyS = false;
		keyD = false;
		keyDown = false;
		keyUp = false;
		keyLeft = false;
		keyRight = false;
		keyShift = false;
		key1 = false;
		keyEnter=false;
		width = 1200;
		height = 800;
		zBuffer = new Coord[(width)][(height)];
		trianglesS = new ArrayList<Triangle>();
		spheresS = new ArrayList<Sphere>();
		shapes = new ArrayList<Shape>();
		walls = new ArrayList<RectPrism>();
		obstacles = new ArrayList<Obstacle>();
		//monsters = new ArrayList<Monster>();
		// obstacles.add(new BuildingUnit(new Vector(10000.0,10000.0,2000.0),new
		// Vector(10000.0,0.0,0.0),new Vector(0.0,10000.0,0.0),5000.0,0.3,10,11));
		currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// triangles.add(new Triangle(new Vector(200.0,200.0,0.0),new
		// Vector(600.0,400.0,0.0),new Vector(200.0,400.0,0.0),255,0,0));
		// triangles.add(new Triangle(new Vector(200.0,200.0,-100.0),new
		// Vector(600.0,400.0,-100.0),new Vector(200.0,400.0,-100.0),0,0,255));
		// person=new Player(new Vector(10.0,10.0,12.664454236780799),20.0,0,0,255,1);
		person=players.get(playerIndex);
		//person=new Player(new Vector(0.0,0.0,0.0),20.0,0,0,255,5,1);
		//walls.add(new RectPrism(new Vector(50.0,50.0,0.0),new Vector(50.0,0.0,0.0),new Vector(0.0,50.0,0.0),new Vector(0.0,0.0,50.0),0,0,0,2));
		camera=new Camera(new Vector(0.0,2.0,-1.0),25.0,person.getHead(),Math.PI/4/*3*/,width,0.001);
		/*for(int i=0;i<50;i++){
			triangles.add(new Triangle(new Vector(-100.0,0.0+100.0*i,0.0),new Vector(-100.0,100.0+100.0*i,0.0),new Vector(100.0,0.0+100.0*i,0.0),40,240,0));
			triangles.add(new Triangle(new Vector(100.0,100.0+100.0*i,0.0),new Vector(-100.0,100.0+100.0*i,0.0),new Vector(100.0,0.0+100.0*i,0.0),40,240,0));
		}*/
		this.setLayout(null);
		addKeyListener(this);
		setFocusable(true);
		try {
            wood = ImageIO.read(getClass().getResource("wood.png"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            grass = ImageIO.read(getClass().getResource("grass4.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            water = ImageIO.read(getClass().getResource("water4.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            beach = ImageIO.read(getClass().getResource("beach.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            snow = ImageIO.read(getClass().getResource("snow2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            rock = ImageIO.read(getClass().getResource("rock3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            armor = ImageIO.read(getClass().getResource("armor.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            bark = ImageIO.read(getClass().getResource("bark.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            cliff = ImageIO.read(getClass().getResource("cliff.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            cliff2 = ImageIO.read(getClass().getResource("cliff2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            wall = ImageIO.read(getClass().getResource("wall2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            roof = ImageIO.read(getClass().getResource("roof2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            hilt = ImageIO.read(getClass().getResource("hilt.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            blade = ImageIO.read(getClass().getResource("blade2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		try {
            monster = ImageIO.read(getClass().getResource("skin4.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
		}
		generateTreeTexture();
		textures=new BufferedImage[]{water,beach,grass,rock,snow,armor,bark,tree,cliff,cliff2,wall,roof,hilt,blade,monster,wood};
		map=new Map();
		//public Building(Vector center,double size,double multiplier,double floorHeight,double roofSize,int floorsC,int floorsR,int floorsL,int floorsU,int floorsD,int wall,int roof,Map m){
		//obstacles.add(new Building(new Vector(4000.0,4000.0,0.0),500.0,0.8,0.5,0.5,4,2,3,1,2,10,11,map));
		obstacles.add(new Building(new Vector(4000.0,4000.0,0.0),500.0,0.8,0.5,0.5,10,4,8,9,6,10,11,map));
		for(int i=0;i<8;i++){
			obstacles.add(new Building(new Vector(3000.0+14000.0*SimplexNoise.noise(25*i,255-25*i),3000.0+14000.0*SimplexNoise.noise(255-25*i,25*i),0.0),500.0,0.8,0.5,0.5,(int)(10*SimplexNoise.noise(25*i*i,255-25*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i,255-25*i*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i*i,255-25*i*i*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i*i*i,255-25*i*i*i*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i*i*i*i,255-25*i*i*i*i*i*i)),10,11,map));
		}
		map.checkTrees(obstacles);
		//shapes.addAll(players);
		shapes.add(person);
		shapes.add(map);
		//monsters.add(new Monster(new Vector(-100.0,-100.0,0.0),100.0,1,1,0.3,0.4,0.5,255,0,0,14));
		/*for(int i=0;i<10;i++){
			monsters.add(new Monster(new Vector(100.0+200*i,-100.0,0.0),Math.random()*100.0+50.0,(int)(Math.random()*5),(int)(Math.random()*10+1),Math.random()*0.2+0.3,Math.random()*0.3+0.2,Math.random()*0.6+0.3,255,200,220,14));
		}*/
		//shapes.addAll(monsters);
		//shapes.add(new Monster(new Vector(100.0,-100.0,0.0),100.0,0,100,0.5,0.5,20.0,255,0,0,14));
		/*for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				obstacles.add(new Tree(2000.0+i*100.0,2020.0+j*100.0,map,100.0,1.0,6,0,255,0));

			}
		}*/
		obstacles.addAll(map.getTrees());
		//obstacles.add(new Tree(4000.0,4020.0,map,100.0,1.0,6,0,255,0));
		//obstacles.add(new Tree(10000.0,10000.0,map,5000.0,1.0,6,0,255,0));
		for(int i=0;i<obstacles.size();i++){
			walls.addAll(obstacles.get(i).getWalls());
		}
		shapes.addAll(walls);
		shapes.addAll(obstacles);
		//shapes.add(new Cylinder(new Vector(100.0,100.0,50.0),new Vector(100.0,200.0,50.0),50.0,255,0,0));
		//spheres.add(new Sphere(new Vector(100.0,600.0,0.0),100.0,255,0,0));
		//triangles.addAll(map.getTriangles());
		//spheres.addAll(map.getSpheres());
		dataCheck = new DataCheck();
		dataCheck.registerDataListener(this);
		dataCheck.doDataEvent();
	}
	public Dimension getPreferredSize() {
        return new Dimension(1200,800);
	}
	public void render(){
		//long startTime = System.currentTimeMillis();
		zBuffer=new Coord[(width)][(height)];
		map.updateTriangles(person.getPos());
		ArrayList<Triangle>triangles=new ArrayList<Triangle>();
		ArrayList<Sphere>spheres=new ArrayList<Sphere>();
		triangles.addAll(trianglesS);
		spheres.addAll(spheresS);
		for(int i=0;i<players.size();i++){
			if(i!=playerIndex){
				triangles.addAll(players.get(i).getTriangles());
				spheres.addAll(players.get(i).getSpheres());
			}
		}
		for(int i=0;i<monsters.size();i++){
			triangles.addAll(monsters.get(i).getTriangles());
			spheres.addAll(monsters.get(i).getSpheres());
		}
		for(int i=0;i<shapes.size();i++){
			triangles.addAll(shapes.get(i).getTriangles());
			spheres.addAll(shapes.get(i).getSpheres());
		}
		for(int i=0;i<spheres.size();i++){
			Sphere s=spheres.get(i);
			Vector c=camera.changeVars(s.getC());
			double mag=c.mag();
			double radius=s.getRadius();
			double ratio=radius/mag;
			if(c.z()>=0||ratio>1.0){
				continue;
			}
			double raster=camera.getRaster();
			//Vector cproj=camera.perspective(c);
			/*System.out.println(cproj.x()+" "+cproj.y());
			Vector d=new Vector(c.x(),c.y(),0.0).unit();
			Vector ch=c.unit();
			double theta=Math.asin(ratio);
			double alpha=Math.acos(ch.dot(d));
			double gamma=Math.PI-theta-alpha;
			double major=mag*raster*ratio/(c.z()*Math.sin(gamma));
			int xMin=(int)(cproj.x()-major);
			int xMax=(int)(cproj.x()+major);
			int yMin=(int)(cproj.y()-major);
			int yMax=(int)(cproj.y()+major);*/
			Vector[]bounds=s.getBounds();
			Vector[]boundsProj=new Vector[8];
			for(int j=0;j<bounds.length;j++){
				boundsProj[j]=camera.perspective(camera.changeVars(bounds[j]));
			}
			int xNum=(int)boundsProj[0].x();
			int yNum=(int)boundsProj[0].y();
			int xMin=xNum;
			int xMax=xNum;
			int yMin=yNum;			
			int yMax=yNum;
			for(int j=1;j<boundsProj.length;j++){
				Vector temp=boundsProj[j];
				int xCurrent=(int)temp.x();
				int yCurrent=(int)temp.y();
				if(xCurrent<xMin){
					xMin=xCurrent;
				}
				if(xCurrent>xMax){
					xMax=xCurrent;
				}
				if(yCurrent<yMin){
					yMin=yCurrent;
				}
				if(yCurrent>yMax){
					yMax=yCurrent;
				}
			}
			if(xMin<-width/2){
				xMin=-width/2;
			}
			if(yMin<-height/2){
				yMin=-height/2;
			}
			if(xMax>=width/2){
				xMax=width/2-1;
			}
			if(yMax>=height/2){
				yMax=height/2-1;
			}
			//System.out.println(xMin+" "+xMax+" "+yMin+" "+yMax);
			for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
				boolean in=false;
				loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
					Vector b=new Vector(x*1.0,y*1.0,raster).unit();
					double aq=b.dot(b);
					double bq=2*b.dot(c.neg());
					double cq=c.dot(c)-radius*radius;
					double discriminant=bq*bq-4*aq*cq;
					if(discriminant<0){
						if(in==true){
							break loop1;
						}
					}else{
						in=true;
						double t=(-bq-Math.sqrt(discriminant))/(2*aq);
						Vector loc=b.multiply(t);
						Vector n=camera.returnVars(loc).minus(camera.returnVars(c)).unit();
						Coord[] tempX = zBuffer[x+(width/2)];
						Coord tempCoord = tempX[y+(height)/2];
						double z=loc.z();
						if(tempCoord==null||tempCoord.getZ()<z){
							//tempX[y+(int)(height)/2]=new Coord(n,z,s.getR(),s.getG(),s.getB());
							double ambient=0.3;
							double temp=n.dot(light);
							temp=ambient+(1-ambient)*temp;
							//System.out.println(s.getR()*temp);
							tempX[y+(int)(height)/2]=new Coord(z,positive((int)(s.getR()*temp)),positive((int)(s.getG()*temp)),positive((int)(s.getB()*temp)));
							///tempX[y+(int)(height)/2]=new Coord(z,new Color(wood.getRGB((int)(v1*(wood.getWidth()-1)),(int)(v2*(wood.getHeight()-1))),true));
						}
					}
				}
			}
		}
		for(int j=0;j<triangles.size();j++){
			Triangle current=triangles.get(j);
			Vector[] tV=current.getV();
			Vector[] vc=new Vector[]{camera.changeVars(tV[0]),camera.changeVars(tV[1]),camera.changeVars(tV[2])};
			//Vector average=camera.changeVars(triangles.get(j).getAverage());
			//double direction=average.dot(triangles.get(j).getN());
			//maybe >0
			double clip=camera.getClip();
			if(vc[0].z()<=clip&&vc[1].z()<=clip&&vc[2].z()<=clip){
				Vector[] v=new Vector[]{camera.perspective(vc[0]),camera.perspective(vc[1]),camera.perspective(vc[2])};
				//g.setColor(new Color(triangles.get(j).getR(),triangles.get(j).getG(),triangles.get(j).getB(),triangles.get(j).getA()));
				//g.fillPolygon(new int[]{(int)(v[0].x()+width/2),(int)(v[1].x()+width/2),(int)(v[2].x()+width/2)},new int[]{(int)(-v[0].y()+height/2),(int)(-v[1].y()+height/2),(int)(-v[2].y()+height/2)},3);
				int xNum=(int)v[0].x();
				int yNum=(int)v[0].y();
				int xMin=xNum;
				int xMax=xNum;
				int yMin=yNum;
				int yMax=yNum;
				for(int i=0;i<v.length;i++){
					int xCurrent=(int)v[i].x();
					int yCurrent=(int)v[i].y();
					if(xCurrent<xMin){
						xMin=xCurrent;
					}
					if(xCurrent>xMax){
						xMax=xCurrent;
					}
					if(yCurrent<yMin){
						yMin=yCurrent;
					}
					if(yCurrent>yMax){
						yMax=yCurrent;
					}
				}
				if(xMin>(width/2)||xMax<(-width/2)||yMin>(height/2)||yMax<(-height/2)){
					continue;
				}
				Vector[] u=new Vector[3];
				u[0]=v[1].minus(v[0]);
				u[0].z(0.0);
				u[1]=v[2].minus(v[0]);
				u[1].z(0.0);
				if(xMin<-width/2){
					xMin=-width/2;
				}
				if(yMin<-height/2){
					yMin=-height/2;
				}
				if(xMax>=width/2){
					xMax=width/2-1;
				}
				if(yMax>=height/2){
					yMax=height/2-1;
				}
				for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
					boolean in=false;
					loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
						Coord bCurrent=zBuffer[x+(width/2)][y+height/2];
						if(bCurrent!=null){
							double zCurrent=bCurrent.getZ();
							if(vc[0].z()<zCurrent&&vc[1].z()<zCurrent&&vc[2].z()<zCurrent){
								continue;
							}
						}
						double sketch=0.2;
						u[2]=new Vector(x-sketch-v[0].x(),y-sketch-v[0].y(),0.0);
						double u0u0=u[0].dot(u[0]);
						double u1u1=u[1].dot(u[1]);
						double u0u1=u[0].dot(u[1]);
						double u2u0=u[2].dot(u[0]);
						double u2u1=u[2].dot(u[1]);
						double den=u0u0*u1u1-u0u1*u0u1;
						double v1=(u1u1*u2u0-u0u1*u2u1)/den;
						double v2=(u0u0*u2u1-u0u1*u2u0)/den;
						u[2]=new Vector(x+sketch-v[0].x(),y+sketch-v[0].y(),0.0);
						u2u0=u[2].dot(u[0]);
						u2u1=u[2].dot(u[1]);
						double v10=(u1u1*u2u0-u0u1*u2u1)/den;
						double v20=(u0u0*u2u1-u0u1*u2u0)/den;
						//Sketchy fix come back to this sometime
						if((v1>=0&&v2>=0&&(v1+v2)<=1)||(v10>=0&&v20>=0&&(v10+v20)<=1)){
							in=true;
							//double z=vc[0].z()+v1*(vc[1].z()-vc[0].z())+v2*(vc[2].z()-vc[0].z());
							double z=1/((1-v1-v2)/vc[0].z()+v1/vc[1].z()+v2/vc[2].z());
							Coord[] tempX = zBuffer[x+(width/2)];
							Coord tempCoord = tempX[y+(height)/2];
							if(tempCoord==null||tempCoord.getZ()<z){
								if(!current.hasTexture()){
									double temp=current.getN().dot(light);
									double ambient=0.3;
									temp=ambient+(1-ambient)*temp;
									tempX[y+(int)(height)/2]=new Coord(z,positive((int)(current.getR()*temp)),positive((int)(current.getG()*temp)),positive((int)(current.getB()*temp)));
								}else{
									double u1=v1*z/vc[1].z();
									double u2=v2*z/vc[2].z();
									BufferedImage texture=textures[current.getTexture()];
									/*if(u1>1.0){
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
									}*/
									//Color c=blend(u1,u2,new Color(255,0,0),new Color(0,255,0),new Color(0,0,255));
									//Color c=blend(u1,u2,new Color(65,152,10),new Color(19,133,16),new Color(19,109,21));
									int[]tempPosition=current.getXY(u1,u2,texture.getWidth(),texture.getHeight());
									//Color c=new Color(texture.getRGB(tempPosition[0],tempPosition[1]));
									//Color c=new Color(texture.getRGB(tempPosition[0]/constant,tempPosition[1]/constant));
									//Color c=new Color(texture.getRGB(tempPosition[0]-tempPosition[0]%(constant*2),tempPosition[1]-tempPosition[1]%(constant*2)));
									int xOnImage=tempPosition[0]-tempPosition[0]%constant;
									int yOnImage=tempPosition[1]-tempPosition[1]%constant;
									double temp=current.getN().dot(light);
									/*double frequency=0.1;
									double xNoise=SimplexNoise.noise(frequency*xOnImage+vc[0].x(),vc[0].y());
									double yNoise=SimplexNoise.noise(vc[0].x(),frequency*yOnImage+vc[0].y());
									Vector noise=new Vector(xNoise,yNoise,0.0);
									double temp=current.getN().plus(noise.multiply(3.0)).unit().dot(new Vector(0.0,0.0,1.0));*/
									// ambient=0.0;
									//if(l==0){
									double ambient=0.3;
									//}
									temp=ambient+(1-ambient)*temp;
									Color c=new Color(texture.getRGB(xOnImage,yOnImage));
									//if(current.getTexture()==7&&c.getRed()>200){
										//continue;
									//}
									tempX[y+(int)(height)/2]=new Coord(z,positive((int)(c.getRed()*temp)),positive((int)(c.getGreen()*temp)),positive((int)(c.getBlue()*temp)));	
									//tempX[y+(int)(height)/2]=new Coord(z,new Color(grass.getRGB((int)(u1*(grass.getWidth()-1)),(int)(u2*(grass.getHeight()-1))),true));
								}
							}
						}else if(in==true){
							break loop1;
						}
					}
				}
			}else if(vc[0].z()<=clip||vc[1].z()<=clip||vc[2].z()<=clip){
				//triangles.remove(j);
				
				//System.out.println(vc[0].z()+" "+vc[1].z()+" "+vc[2].z()+" "+clip);
				Triangle[]clippedTriangles=camera.getClippedTriangles(current,vc);
				triangles.remove(j);
				for(int l=clippedTriangles.length-1;l>=0;l--){
					triangles.add(j,clippedTriangles[l]);
				}
				j--;
				//System.out.println(j);
			}
		}
		//long endTime = System.currentTimeMillis();
		//long timeElapsed = endTime - startTime;
		//System.out.println("hello");
		//System.out.println("Execution time in milliseconds: " + timeElapsed);
	}
	public Image generateImage(){
		BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics g=bufferedImage.getGraphics();
		draw(g);
		return bufferedImage;
	}
	public void generateTreeTexture(){
		tree=new BufferedImage(36,60,BufferedImage.TYPE_INT_RGB);
		Graphics g=tree.getGraphics();
		g.setColor(new Color(86,47,14));
		g.fillRect(18,50,8,50);
		g.setColor(new Color(0,255,0));
		g.fillOval(8,60,20,20);
		g.fillOval(0,52,20,20);
		g.fillOval(16,52,20,20);
	}
	public void drawImage(Graphics g){
		g.drawImage(currentImage,0,0,this);
	}
	public void draw(Graphics g){
		double horizon=camera.getHorizon();
		/*for(int i=0;i<zBuffer.length;i+=constant){
			Coord[]temp=zBuffer[i];
			for(int j=0;j<temp.length;j+=constant){
				Coord temp2=temp[j];
				if(temp2==null){
					if(j>height/2+horizon){
						g.setColor(new Color(0,0,255));
					}else{
						g.setColor(new Color(135,206,235));
					}
					g.fillRect(i,j,constant,constant);
				}else{
					g.setColor(temp2.getColor());
					g.fillRect(i,j,constant,constant);
				}
			}
		}*/
		g.setColor(new Color(125,206,235));
		g.fillRect(0,0,width,(int)(height/2+horizon));
		//g.setColor(new Color(128,132,135));
		//g.fillRect(0,(int)(height/2+horizon),width,(int)(height/2-horizon));
		//g.setColor(new Color(0,0,255));
		g.setColor(new Color(33,132,218));
		g.fillRect(0,(int)(height/2+horizon),width,(int)(height/2-horizon));
		for(int i=0;i<zBuffer.length;i+=constant){
			Coord[]temp=zBuffer[i];
			for(int j=0;j<temp.length;j+=constant){
				Coord temp2=temp[j];
				if(temp2!=null){
					g.setColor(temp2.getColor());
					g.fillRect(i,j,constant,constant);
				}
			}
		}
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,width/4,height/20);
		double ratioPlayer=person.getHealthRatio();
		if(ratioPlayer>0.6){
			g.setColor(new Color(0,255,0));
		}else if(ratioPlayer>0.3){
			g.setColor(new Color(255,255,0));
		}else{
			g.setColor(new Color(255,0,0));
		}
		g.drawRect(width/40,height/160,width/4-width/20,height/20-height/80);
		g.fillRect(width/40,height/160,(int)((width/4-width/20)*ratioPlayer),height/20-height/80);
		if(currentMonster.get()!=null&&currentMonster.get().getHealthRatio()>=0){
			g.setColor(new Color(0,0,0));
			g.fillRect(width-width/4,0,width/4,height/20);
			double ratio=currentMonster.get().getHealthRatio();
			if(ratio>0.6){
				g.setColor(new Color(0,255,0));
			}else if(ratio>0.3){
				g.setColor(new Color(255,255,0));
			}else{
				g.setColor(new Color(255,0,0));
			}
			g.drawRect(width-(width/40+width/4-width/20),height/160,width/4-width/20,height/20-height/80);
			g.fillRect(width-(width/40+width/4-width/20),height/160,(int)((width/4-width/20)*ratio),height/20-height/80);
		}
		if(keyEnter==false){
			g.setColor(new Color(0,0,0,200));
			g.fillRect(width/10,height/10,width*4/5,height*4/5+height/40);
			g.setColor(new Color(255,255,255));
			g.drawString("Nightmare Hunter",width/8,height/10+height/20);
			g.drawString("Explore a vast territory and hunt down all the nightmare creatures you encounter",width/8,height/10+2*height/20);
			g.drawString("Your health is displayed on the left and your enemy's health is displayed on the right",width/8,height/10+3*height/20);
			g.drawString("Controls: ",width/8,height/10+4*height/20);
			g.drawString("Up Arrow = Move Forward",width/8,height/10+5*height/20);
			g.drawString("Down Arrow = Move Backwards",width/8,height/10+6*height/20);
			g.drawString("Left Arrow = Turn Left",width/8,height/10+7*height/20);
			g.drawString("Right Arrow = Turn Right",width/8,height/10+8*height/20);
			g.drawString("Shift = Sprint",width/8,height/10+9*height/20);
			g.drawString("W = Rotate Camera Up",width/8,height/10+10*height/20);
			g.drawString("S = Rotate Camera Down",width/8,height/10+11*height/20);
			g.drawString("A = Rotate Camera Left",width/8,height/10+12*height/20);
			g.drawString("D = Rotate Camera Right",width/8,height/10+13*height/20);
			g.drawString("1 = Attack",width/8,height/10+14*height/20);
			g.drawString("Space = Jump",width/8,height/10+15*height/20);
			g.drawString("Press Enter To Exit Menu",width/8,height/10+16*height/20);
		}
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g);	
		/*if( gameData != null ){		
			ArrayList<Player> playerList = gameData.getPlayerList();
			for(int i=0; i<playerList.size(); i++){
				int x = playerList.get(i).getX();
				int y = playerList.get(i).getY();
				int id = playerList.get(i).getID();			
				g.setColor(Color.BLACK);
				g.drawString(""+id,x,y);			
				if( id == myID ){
					g.setColor(Color.RED);
					g.fillOval(x,y,30,30);
				} else {
					g.setColor(Color.BLUE);
					g.fillOval(x,y,30,30);
				}			
			}
			for(int i=0;i<gameData.getWallList().size();i++){
                g.setColor(new Color(0,0,0));
                g.fillRect(gameData.getWallList().get(i).getX(),gameData.getWallList().get(i).getY(),gameData.getWallList().get(i).getW(),gameData.getWallList().get(i).getH());
            }		
		}*/
		//g.drawString("My Fantasy Reality",500,400);
		/*Thread t1=new Thread(()->render());
		t1.start();
		try{
			t1.join();
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}
		Thread t2=new Thread(()->draw(g));
		t2.start();
		try{
			t2.join();
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}*/
		//render();
		//draw(g);	
		drawImage(g);
		//this.animate();
	}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==87){
			//w
			keyW=true;
			//camera.move(new Vector(0.0,50.0,0.0));	
		}
		else if (e.getKeyCode()==83){
			//s
			keyS=true;
			//camera.move(new Vector(0.0,-50.0,0.0));	
		}
		else if (e.getKeyCode()==68){
			//d
			keyD=true;
			//camera.move(new Vector(50.0,0.0,0.0));	
		}
		else if (e.getKeyCode()==65){
			//a
			keyA=true;
			//camera.move(new Vector(-50.0,0.0,0.0));	
		}else if(e.getKeyCode()==37){
			//left
			keyLeft=true;
			//camera.rotateZ(-0.2);
		}else if(e.getKeyCode()==38){
			//up
			keyUp=true;
			//camera.rotateL(0.2);
		}else if(e.getKeyCode()==39){
			//right
			keyRight=true;
			//camera.rotateZ(0.2);
		}else if(e.getKeyCode()==40){
			//down
			keyDown=true;
			//camera.rotateL(-0.2);
		}else if(e.getKeyCode()==32){
			keySpace=true;
		}else if(e.getKeyCode()==16){
			keyShift=true;
		}else if(e.getKeyCode()==49){
			key1=true;
		}else if(e.getKeyCode()==10){
			keyEnter=true;
		}
		
		/*if( gameData != null ){
			ArrayList<Player> playerList = gameData.getPlayerList();
			if( playerIndex != -1 ){
				if (e.getKeyCode()==87){
					//w
					playerList.get(playerIndex).moveUp();	
				}
				else if (e.getKeyCode()==83){
					//s
					playerList.get(playerIndex).moveDown();
				}
                else if (e.getKeyCode()==68){
					//s
					playerList.get(playerIndex).moveRight();
				}
                else if (e.getKeyCode()==65){
					//s
					playerList.get(playerIndex).moveLeft();
				}
			}
		}
		try{
			out.reset();
			out.writeObject(gameData);
		} catch (IOException ex) {
            System.out.println(ex);
        }*/
	}
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==87){
			//w
			keyW=false;
			//camera.move(new Vector(0.0,50.0,0.0));	
		}
		else if (e.getKeyCode()==83){
			//s
			keyS=false;
			//camera.move(new Vector(0.0,-50.0,0.0));	
		}
		else if (e.getKeyCode()==68){
			//d
			keyD=false;
			//camera.move(new Vector(50.0,0.0,0.0));	
		}
		else if (e.getKeyCode()==65){
			//a
			keyA=false;
			//camera.move(new Vector(-50.0,0.0,0.0));	
		}else if(e.getKeyCode()==37){
			//left
			keyLeft=false;
			//camera.rotateZ(-0.2);
		}else if(e.getKeyCode()==38){
			//up
			keyUp=false;
			//camera.rotateL(0.2);
		}else if(e.getKeyCode()==39){
			//right
			keyRight=false;
			//camera.rotateZ(0.2);
		}else if(e.getKeyCode()==40){
			//down
			keyDown=false;
			//camera.rotateL(-0.2);
		}else if(e.getKeyCode()==32){
			keySpace=false;
		}else if(e.getKeyCode()==16){
			keyShift=false;
		}else if(e.getKeyCode()==49){
			key1=false;
		}
	}
	public void keyTyped(KeyEvent e) {}
	@SuppressWarnings("unchecked") 
	public void makeConnection() throws IOException{
		Socket serverSocket = new Socket(InetAddress.getByName(hostName), portNumber);
		out = new ObjectOutputStream(serverSocket.getOutputStream());
		in = new ObjectInputStream(serverSocket.getInputStream());
		try {
			out.writeObject("Hello from new player");
			while(myID == -1){
				System.out.println("waiting for id");
				Object obj = in.readObject(); 
				if( obj instanceof Integer ){
					System.out.println(obj);
					myID = (Integer) obj;
					System.out.println("my id is " + myID);
				}
			}
			readData();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
			System.out.println(e);
            System.err.println("Couldn't get I/O for the connection to " +hostName);
            System.exit(1);
        } catch (ClassNotFoundException e){
			 System.err.println(e);
			 System.exit(1);
		} 
	}
	public synchronized void readData() throws IOException,ClassNotFoundException{
		this.gameData=(GameData) in.readObject();
		//System.out.println(this.gameData.getMonsters().get(0).getPos());
		players=this.gameData.getPlayerList();
		monsters=this.gameData.getMonsters();
		if(damage!=null){
			tempDamage=damage;
		}else{
			tempDamage=new double[monsters.size()];
		}
		damage=new double[monsters.size()];
		/*int frontNum=(Integer)in.readObject();
		int startSequenceNum=actions.frontNum();
		for(int i=startSequenceNum;i<frontNum;i++){
			System.out.println(i);
			actions.remove();
		}
		Iterator<boolean[]>iterator=actions.iterator();
		while(iterator.hasNext()){
			//System.out.println(iterator.hasNext());
			gameData.updatePlayers(myID,iterator.next(),map,walls);
		}
		//System.out.println("I made it");
		//System.out.println(monsters.get(0).getPos());
		//System.out.println(monsters.size());*/
		for(int i=0; i<players.size(); i++){
			int id = players.get(i).getID();
			if( id == myID ){
				playerIndex = i;
				break;
			}
		}
		//person=players.get(playerIndex);
	}
	@SuppressWarnings("unchecked") 
	public void receiveData() throws IOException{
		try {
			readData();
			out.reset();
			out.writeObject(person);
			out.reset();
			out.writeObject(playerIndex);
			out.reset();
			out.writeObject(tempDamage);
	    } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +hostName);
            System.exit(1);
        } catch (ClassNotFoundException e){
			 System.err.println(e);
			 System.exit(1);
		} 
	}
	public int positive(int data){
		if(data<0){
			return 0;
		}
		return data;
	}
	public void animate() throws IOException{
		while(true){
			try{
				Thread.sleep(50);
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}
			/*out.reset();
			out.writeObject(person);
			out.reset();
			out.writeObject(playerIndex);*/
			/*out.reset();
			out.writeObject(new boolean[]{keyLeft,keyUp,keyRight,keyDown,keySpace,keyShift,key1});
			actions.add(new boolean[]{keyLeft,keyUp,keyRight,keyDown,keySpace,keyShift,key1});*/
			if(keyW){
				camera.rotateL(0.1);
			}
			if(keyS){
				camera.rotateL(-0.1);
			}
			if(keyD){
				camera.rotateZ(0.1);
			}
			if(keyA){
				camera.rotateZ(-0.1);
			}
			double speed=0.0;
			if(keyLeft){
				person.turn(-0.1,camera,walls);
			}
			if(keyUp){
				speed=10.0;
			}
			if(keyRight){
				person.turn(0.1,camera,walls);
			}
			if(keyDown){
				speed=-10.0;
			}
			if(keySpace){
				person.jump();
			}
			if(keyShift){
				speed*=2;
			}
			if(key1){
				person.startAttackAnimation();
				//speed=0.0;
			}
			person.setSpeed(speed);
			person.moveOnVector(map,5,walls);
			for(int i=0;i<players.size();i++){
				if(i!=playerIndex)
					players.get(i).moveOnVector(map,5,walls);
			}
			person.hitMonster(monsters,damage,currentMonster);
			camera.update(person.getHead());
			map.updateTrees(person);
			for(int i=0;i<monsters.size();i++){
				monsters.get(i).autoMove(5.0, map, 5, walls);
				monsters.get(i).hitPlayer(person);
			}
			render();
			currentImage=generateImage();
			time++;
			//sync lighting later
			double lightX=Math.cos(2*Math.PI/(dayLength)*time+Math.PI/2);
			double lightZ=Math.sin(2*Math.PI/(dayLength)*time+Math.PI/2);
			if(lightZ<0){
				lightX=-lightX;
				lightZ=-lightZ;
			}
			light=new Vector(lightX,0.0,lightZ);
			repaint();
		}
	}
	public Color blend(double v1,double v2,Color c0,Color c1,Color c2){
		int red=(int)(c0.getRed()*(1-v1-v2)+c1.getRed()*v1+c2.getRed()*v2);
		if(red<0){
			red=0;
		}
		if(red>255){
			red=255;
		}
		int green=(int)(c0.getGreen()*(1-v1-v2)+c1.getGreen()*v1+c2.getGreen()*v2);
		if(green<0){
			green=0;
		}
		if(green>255){
			green=255;
		}
		int blue=(int)(c0.getBlue()*(1-v1-v2)+c1.getBlue()*v1+c2.getBlue()*v2);
		if(blue<0){
			blue=0;
		}
		if(blue>255){
			blue=255;
		}
		return new Color(red,green,blue);
	}
	public Color average(Color[]c){
		int r=0;
		int g=0;
		int b=0;
		int len=c.length;
		for(int i=0;i<len;i++){
			r+=c[i].getRed();
			g+=c[i].getGreen();
			b+=c[i].getBlue();
		}
		return new Color(r/len,g/len,b/len);
	}
	public void render3(){
		//long startTime = System.currentTimeMillis();
		//long totalTime=0;
		zBuffer=new Coord[(width)][(height)];
		map.updateTriangles(person.getPos());
		for(int l=-1;l<shapes.size();l++){
			ArrayList<Triangle>triangles;
			ArrayList<Sphere>spheres;
			if(l==-1){
				triangles=trianglesS;
				spheres=spheresS;
			}else{
				triangles=shapes.get(l).getTriangles();
				spheres=shapes.get(l).getSpheres();
			}
			for(int j=0;j<triangles.size();j++){
				Triangle current=triangles.get(j);
				Vector[] tV=current.getV();
				//long time1 = System.currentTimeMillis();
				Vector[] vc=new Vector[]{camera.changeVars(tV[0]),camera.changeVars(tV[1]),camera.changeVars(tV[2])};
				//long time2 = System.currentTimeMillis();
				//Vector average=camera.changeVars(triangles.get(j).getAverage());
				//double direction=average.dot(camera.changeVars(triangles.get(j).getN()));
				//maybe >0
				//totalTime+=time2-time1;
				if(vc[0].z()>=0||vc[1].z()>=0||vc[2].z()>=0/*||direction<0*/){
					continue;
				}
				Vector[] v=new Vector[]{camera.perspective(vc[0]),camera.perspective(vc[1]),camera.perspective(vc[2])};
				//g.setColor(new Color(triangles.get(j).getR(),triangles.get(j).getG(),triangles.get(j).getB(),triangles.get(j).getA()));
				//g.fillPolygon(new int[]{(int)(v[0].x()+width/2),(int)(v[1].x()+width/2),(int)(v[2].x()+width/2)},new int[]{(int)(-v[0].y()+height/2),(int)(-v[1].y()+height/2),(int)(-v[2].y()+height/2)},3);
				int xNum=(int)v[0].x();
				int yNum=(int)v[0].y();
				int xMin=xNum;
				int xMax=xNum;
				int yMin=yNum;
				int yMax=yNum;
				for(int i=0;i<v.length;i++){
					int xCurrent=(int)v[i].x();
					int yCurrent=(int)v[i].y();
					if(xCurrent<xMin){
						xMin=xCurrent;
					}
					if(xCurrent>xMax){
						xMax=xCurrent;
					}
					if(yCurrent<yMin){
						yMin=yCurrent;
					}
					if(yCurrent>yMax){
						yMax=yCurrent;
					}
				}
				if(xMin>(width/2)||xMax<(-width/2)||yMin>(height/2)||yMax<(-height/2)){
					continue;
				}
				Vector[] u=new Vector[3];
				u[0]=v[1].minus(v[0]);
				u[0].z(0.0);
				u[1]=v[2].minus(v[0]);
				u[1].z(0.0);
				if(xMin<-width/2){
					xMin=-width/2;
				}
				if(yMin<-height/2){
					yMin=-height/2;
				}
				if(xMax>=width/2){
					xMax=width/2-1;
				}
				if(yMax>=height/2){
					yMax=height/2-1;
				}
				for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
					boolean in=false;
					loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
						Coord bCurrent=zBuffer[x+(width/2)][y+height/2];
						if(bCurrent!=null){
							double zCurrent=bCurrent.getZ();
							if(vc[0].z()<zCurrent&&vc[1].z()<zCurrent&&vc[2].z()<zCurrent){
								continue;
							}
						}
						//0.1
						double sketch=0.2;
						u[2]=new Vector(x-sketch-v[0].x(),y-sketch-v[0].y(),0.0);
						double den=u[0].dot(u[0])*u[1].dot(u[1])-Math.pow(u[0].dot(u[1]),2);
						double v1=(u[1].dot(u[1])*u[2].dot(u[0])-u[1].dot(u[0])*u[2].dot(u[1]))/den;
						double v2=(u[0].dot(u[0])*u[2].dot(u[1])-u[1].dot(u[0])*u[2].dot(u[0]))/den;
						u[2]=new Vector(x+sketch-v[0].x(),y+sketch-v[0].y(),0.0);
						double v10=(u[1].dot(u[1])*u[2].dot(u[0])-u[1].dot(u[0])*u[2].dot(u[1]))/den;
						double v20=(u[0].dot(u[0])*u[2].dot(u[1])-u[1].dot(u[0])*u[2].dot(u[0]))/den;
						//Sketchy fix come back to this sometime
						if((v1>=0&&v2>=0&&(v1+v2)<=1)||(v10>=0&&v20>=0&&(v10+v20)<=1)){
							in=true;
							//double z=vc[0].z()+v1*(vc[1].z()-vc[0].z())+v2*(vc[2].z()-vc[0].z());
							double z=1/((1-v1-v2)/vc[0].z()+v1/vc[1].z()+v2/vc[2].z());
							//x3/z3=l1x1/z1+l2x2/z2;
							Coord[] tempX = zBuffer[x+(width/2)];
							Coord tempCoord = tempX[y+(height)/2];
							if(tempCoord==null||tempCoord.getZ()<z){
								//tempX[y+(int)(height)/2]=new Coord(z,triangles.get(j).getR(),triangles.get(j).getG(),triangles.get(j).getB());
								if(!current.hasTexture()){
									double temp=current.getN().dot(light);
									double ambient=0.3;
									temp=ambient+(1-ambient)*temp;
									tempX[y+(int)(height)/2]=new Coord(z,positive((int)(current.getR()*temp)),positive((int)(current.getG()*temp)),positive((int)(current.getB()*temp)));
								}else{
									double u1=v1*z/vc[1].z();
									double u2=v2*z/vc[2].z();
									BufferedImage texture=textures[current.getTexture()];
									/*if(u1>1.0){
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
									}*/
									//Color c=blend(u1,u2,new Color(255,0,0),new Color(0,255,0),new Color(0,0,255));
									//Color c=blend(u1,u2,new Color(65,152,10),new Color(19,133,16),new Color(19,109,21));
									int[]tempPosition=current.getXY(u1,u2,texture.getWidth(),texture.getHeight());
									//Color c=new Color(texture.getRGB(tempPosition[0],tempPosition[1]));
									//Color c=new Color(texture.getRGB(tempPosition[0]/constant,tempPosition[1]/constant));
									//Color c=new Color(texture.getRGB(tempPosition[0]-tempPosition[0]%(constant*2),tempPosition[1]-tempPosition[1]%(constant*2)));
									int xOnImage=tempPosition[0]-tempPosition[0]%constant;
									int yOnImage=tempPosition[1]-tempPosition[1]%constant;
									double temp=current.getN().dot(light);
									/*double frequency=0.1;
									double xNoise=SimplexNoise.noise(frequency*xOnImage+vc[0].x(),vc[0].y());
									double yNoise=SimplexNoise.noise(vc[0].x(),frequency*yOnImage+vc[0].y());
									Vector noise=new Vector(xNoise,yNoise,0.0);
									double temp=current.getN().plus(noise.multiply(3.0)).unit().dot(new Vector(0.0,0.0,1.0));*/
									// ambient=0.0;
									//if(l==0){
									double ambient=0.3;
									//}
									temp=ambient+(1-ambient)*temp;
									Color c=new Color(texture.getRGB(xOnImage,yOnImage));
									//if(current.getTexture()==7&&c.getRed()>200){
										//continue;
									//}
									tempX[y+(int)(height)/2]=new Coord(z,positive((int)(c.getRed()*temp)),positive((int)(c.getGreen()*temp)),positive((int)(c.getBlue()*temp)));	
									//tempX[y+(int)(height)/2]=new Coord(z,new Color(grass.getRGB((int)(u1*(grass.getWidth()-1)),(int)(u2*(grass.getHeight()-1))),true));
								}
							}
						}else if(in==true){
							break loop1;
						}
					}
				}
			}
			for(int i=0;i<spheres.size();i++){
				Sphere s=spheres.get(i);
				Vector c=camera.changeVars(s.getC());
				double mag=c.mag();
				double radius=s.getRadius();
				double ratio=radius/mag;
				if(c.z()>=0||ratio>1.0){
					continue;
				}
				double raster=camera.getRaster();
				//Vector cproj=camera.perspective(c);
				/*System.out.println(cproj.x()+" "+cproj.y());
				Vector d=new Vector(c.x(),c.y(),0.0).unit();
				Vector ch=c.unit();
				double theta=Math.asin(ratio);
				double alpha=Math.acos(ch.dot(d));
				double gamma=Math.PI-theta-alpha;
				double major=mag*raster*ratio/(c.z()*Math.sin(gamma));
				int xMin=(int)(cproj.x()-major);
				int xMax=(int)(cproj.x()+major);
				int yMin=(int)(cproj.y()-major);
				int yMax=(int)(cproj.y()+major);*/
				Vector[]bounds=s.getBounds();
				Vector[]boundsProj=new Vector[8];
				for(int j=0;j<bounds.length;j++){
					boundsProj[j]=camera.perspective(camera.changeVars(bounds[j]));
				}
				int xNum=(int)boundsProj[0].x();
				int yNum=(int)boundsProj[0].y();
				int xMin=xNum;
				int xMax=xNum;
				int yMin=yNum;			
				int yMax=yNum;
				for(int j=1;j<boundsProj.length;j++){
					Vector temp=boundsProj[j];
					int xCurrent=(int)temp.x();
					int yCurrent=(int)temp.y();
					if(xCurrent<xMin){
						xMin=xCurrent;
					}
					if(xCurrent>xMax){
						xMax=xCurrent;
					}
					if(yCurrent<yMin){
						yMin=yCurrent;
					}
					if(yCurrent>yMax){
						yMax=yCurrent;
					}
				}
				if(xMin<-width/2){
					xMin=-width/2;
				}
				if(yMin<-height/2){
					yMin=-height/2;
				}
				if(xMax>=width/2){
					xMax=width/2-1;
				}
				if(yMax>=height/2){
					yMax=height/2-1;
				}
				//System.out.println(xMin+" "+xMax+" "+yMin+" "+yMax);
				for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
					boolean in=false;
					loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
						Vector b=new Vector(x*1.0,y*1.0,raster).unit();
						double aq=b.dot(b);
						double bq=2*b.dot(c.neg());
						double cq=c.dot(c)-radius*radius;
						double discriminant=bq*bq-4*aq*cq;
						if(discriminant<0){
							if(in==true){
								break loop1;
							}
						}else{
							in=true;
							double t=(-bq-Math.sqrt(discriminant))/(2*aq);
							Vector loc=b.multiply(t);
							Vector n=camera.returnVars(loc).minus(camera.returnVars(c)).unit();
							Coord[] tempX = zBuffer[x+(width/2)];
							Coord tempCoord = tempX[y+(height)/2];
							double z=loc.z();
							if(tempCoord==null||tempCoord.getZ()<z){
								//tempX[y+(int)(height)/2]=new Coord(n,z,s.getR(),s.getG(),s.getB());
								double ambient=0.3;
								double temp=n.dot(light);
								temp=ambient+(1-ambient)*temp;
								//System.out.println(s.getR()*temp);
								tempX[y+(int)(height)/2]=new Coord(z,positive((int)(s.getR()*temp)),positive((int)(s.getG()*temp)),positive((int)(s.getB()*temp)));
								///tempX[y+(int)(height)/2]=new Coord(z,new Color(wood.getRGB((int)(v1*(wood.getWidth()-1)),(int)(v2*(wood.getHeight()-1))),true));
							}
						}
					}
				}
			}
		}
		//long endTime = System.currentTimeMillis();
		//long timeElapsed = endTime - startTime;
		//System.out.println("Execution time in milliseconds: " + timeElapsed);
		//System.out.println(totalTime);
	}
	public void render2(){
		//long startTime = System.currentTimeMillis();
		//long totalTime=0;
		zBuffer=new Coord[(width)][(height)];
		map.updateTriangles(person.getPos());
		for(int l=-1;l<shapes.size();l++){
			ArrayList<Triangle>triangles;
			ArrayList<Sphere>spheres;
			if(l==-1){
				triangles=trianglesS;
				spheres=spheresS;
			}else{
				triangles=shapes.get(l).getTriangles();
				spheres=shapes.get(l).getSpheres();
			}
			for(int j=0;j<triangles.size();j++){
				Triangle current=triangles.get(j);
				Vector[] tV=current.getV();
				//long time1 = System.currentTimeMillis();
				Vector[] vc=new Vector[]{camera.changeVars(tV[0]),camera.changeVars(tV[1]),camera.changeVars(tV[2])};
				//long time2 = System.currentTimeMillis();
				//Vector average=camera.changeVars(triangles.get(j).getAverage());
				//double direction=average.dot(camera.changeVars(triangles.get(j).getN()));
				//maybe >0
				//totalTime+=time2-time1;
				if(vc[0].z()<0&&vc[1].z()<0&&vc[2].z()<0/*||direction<0*/){
					Vector[] v=new Vector[]{camera.perspective(vc[0]),camera.perspective(vc[1]),camera.perspective(vc[2])};
					//g.setColor(new Color(triangles.get(j).getR(),triangles.get(j).getG(),triangles.get(j).getB(),triangles.get(j).getA()));
					//g.fillPolygon(new int[]{(int)(v[0].x()+width/2),(int)(v[1].x()+width/2),(int)(v[2].x()+width/2)},new int[]{(int)(-v[0].y()+height/2),(int)(-v[1].y()+height/2),(int)(-v[2].y()+height/2)},3);
					int xNum=(int)v[0].x();
					int yNum=(int)v[0].y();
					int xMin=xNum;
					int xMax=xNum;
					int yMin=yNum;
					int yMax=yNum;
					for(int i=0;i<v.length;i++){
						int xCurrent=(int)v[i].x();
						int yCurrent=(int)v[i].y();
						if(xCurrent<xMin){
							xMin=xCurrent;
						}
						if(xCurrent>xMax){
							xMax=xCurrent;
						}
						if(yCurrent<yMin){
							yMin=yCurrent;
						}
						if(yCurrent>yMax){
							yMax=yCurrent;
						}
					}
					if(xMin>(width/2)||xMax<(-width/2)||yMin>(height/2)||yMax<(-height/2)){
						continue;
					}
					Vector[] u=new Vector[3];
					u[0]=v[1].minus(v[0]);
					u[0].z(0.0);
					u[1]=v[2].minus(v[0]);
					u[1].z(0.0);
					if(xMin<-width/2){
						xMin=-width/2;
					}
					if(yMin<-height/2){
						yMin=-height/2;
					}
					if(xMax>=width/2){
						xMax=width/2-1;
					}
					if(yMax>=height/2){
						yMax=height/2-1;
					}
					for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
						boolean in=false;
						loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
							Coord bCurrent=zBuffer[x+(width/2)][y+height/2];
							if(bCurrent!=null){
								double zCurrent=bCurrent.getZ();
								if(vc[0].z()<zCurrent&&vc[1].z()<zCurrent&&vc[2].z()<zCurrent){
									continue;
								}
							}
							//0.1
							double sketch=0.2;
							u[2]=new Vector(x-sketch-v[0].x(),y-sketch-v[0].y(),0.0);
							double den=u[0].dot(u[0])*u[1].dot(u[1])-Math.pow(u[0].dot(u[1]),2);
							double v1=(u[1].dot(u[1])*u[2].dot(u[0])-u[1].dot(u[0])*u[2].dot(u[1]))/den;
							double v2=(u[0].dot(u[0])*u[2].dot(u[1])-u[1].dot(u[0])*u[2].dot(u[0]))/den;
							u[2]=new Vector(x+sketch-v[0].x(),y+sketch-v[0].y(),0.0);
							double v10=(u[1].dot(u[1])*u[2].dot(u[0])-u[1].dot(u[0])*u[2].dot(u[1]))/den;
							double v20=(u[0].dot(u[0])*u[2].dot(u[1])-u[1].dot(u[0])*u[2].dot(u[0]))/den;
							//Sketchy fix come back to this sometime
							if((v1>=0&&v2>=0&&(v1+v2)<=1)||(v10>=0&&v20>=0&&(v10+v20)<=1)){
								in=true;
								//double z=vc[0].z()+v1*(vc[1].z()-vc[0].z())+v2*(vc[2].z()-vc[0].z());
								double z=1/((1-v1-v2)/vc[0].z()+v1/vc[1].z()+v2/vc[2].z());
								//x3/z3=l1x1/z1+l2x2/z2;
								Coord[] tempX = zBuffer[x+(width/2)];
								Coord tempCoord = tempX[y+(height)/2];
								if(tempCoord==null||tempCoord.getZ()<z){
									//tempX[y+(int)(height)/2]=new Coord(z,triangles.get(j).getR(),triangles.get(j).getG(),triangles.get(j).getB());
									if(!current.hasTexture()){
										double temp=current.getN().dot(light);
										double ambient=0.3;
										temp=ambient+(1-ambient)*temp;
										tempX[y+(int)(height)/2]=new Coord(z,positive((int)(current.getR()*temp)),positive((int)(current.getG()*temp)),positive((int)(current.getB()*temp)));
									}else{
										double u1=v1*z/vc[1].z();
										double u2=v2*z/vc[2].z();
										BufferedImage texture=textures[current.getTexture()];
										/*if(u1>1.0){
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
										}*/
										//Color c=blend(u1,u2,new Color(255,0,0),new Color(0,255,0),new Color(0,0,255));
										//Color c=blend(u1,u2,new Color(65,152,10),new Color(19,133,16),new Color(19,109,21));
										int[]tempPosition=current.getXY(u1,u2,texture.getWidth(),texture.getHeight());
										//Color c=new Color(texture.getRGB(tempPosition[0],tempPosition[1]));
										//Color c=new Color(texture.getRGB(tempPosition[0]/constant,tempPosition[1]/constant));
										//Color c=new Color(texture.getRGB(tempPosition[0]-tempPosition[0]%(constant*2),tempPosition[1]-tempPosition[1]%(constant*2)));
										int xOnImage=tempPosition[0]-tempPosition[0]%constant;
										int yOnImage=tempPosition[1]-tempPosition[1]%constant;
										double temp=current.getN().dot(light);
										/*double frequency=0.1;
										double xNoise=SimplexNoise.noise(frequency*xOnImage+vc[0].x(),vc[0].y());
										double yNoise=SimplexNoise.noise(vc[0].x(),frequency*yOnImage+vc[0].y());
										Vector noise=new Vector(xNoise,yNoise,0.0);
										double temp=current.getN().plus(noise.multiply(3.0)).unit().dot(new Vector(0.0,0.0,1.0));*/
										// ambient=0.0;
										//if(l==0){
										double ambient=0.3;
										//}
										temp=ambient+(1-ambient)*temp;
										Color c=new Color(texture.getRGB(xOnImage,yOnImage));
										//if(current.getTexture()==7&&c.getRed()>200){
											//continue;
										//}
										tempX[y+(int)(height)/2]=new Coord(z,positive((int)(c.getRed()*temp)),positive((int)(c.getGreen()*temp)),positive((int)(c.getBlue()*temp)));	
										//tempX[y+(int)(height)/2]=new Coord(z,new Color(grass.getRGB((int)(u1*(grass.getWidth()-1)),(int)(u2*(grass.getHeight()-1))),true));
									}
								}
							}else if(in==true){
								break loop1;
							}
						}
					}
				}else if(vc[0].z()<0||vc[1].z()<0||vc[2].z()<0){
					boolean a=false;
					ArrayList<Vector>v=new ArrayList<Vector>(2);
					int front1=-1;
					int front2=-1;
					int back1=-1;
					int back2=-1;
					if(vc[0].z()<0){
						a=true;
						Vector v0=camera.perspective(vc[0]);
						v.add(v0);
						front1=0;
						if(v0.x()>(width/2)&&v0.x()<(-width/2)&&v0.y()>(height/2)&&v0.y()<(-height/2)){
							a=true;
						}
					}else{
						back1=0;
					}
					if(vc[1].z()<0){
						a=true;
						Vector v1=camera.perspective(vc[1]);
						v.add(v1);
						if(front1==-1){
							front1=1;
						}else{
							front2=1;
						}
						if(v1.x()>(width/2)&&v1.x()<(-width/2)&&v1.y()>(height/2)&&v1.y()<(-height/2)){
							a=true;
						}
					}else{
						if(back1==-1){
							back1=1;
						}else{
							back2=1;
						}
					}
					if(vc[2].z()<0){
						a=true;
						Vector v2=camera.perspective(vc[2]);
						v.add(v2);
						if(front1==-1){
							front1=2;
						}else{
							front2=2;
						}
						if(v2.x()>(width/2)&&v2.x()<(-width/2)&&v2.y()>(height/2)&&v2.y()<(-height/2)){
							a=true;
						}
					}else{
						if(back1==-1){
							back1=2;
						}else{
							back2=2;
						}
					}
					if(a){
						if(v.size()==2){
							double raster=camera.getRaster();
							Vector back=vc[back1];
							Vector del1=vc[front1].minus(back);
							Vector del2=vc[front2].minus(back);
							double clip=camera.getClip();
							double t1=(clip-back.z())/del1.z();
							double t2=(clip-back.z())/del2.z();
							Vector coord1=back.plus(del1.multiply(t1));
							Vector coord2=back.plus(del2.multiply(t2));
							v.add(coord1);
							v.add(coord2);
							Vector current0=v.get(0);
							int xNum=(int)current0.x();
							int yNum=(int)current0.y();
							int xMin=xNum;
							int xMax=xNum;
							int yMin=yNum;
							int yMax=yNum;
							for(int i=0;i<v.size();i++){
								Vector currentI=v.get(i);
								int xCurrent=(int)currentI.x();
								int yCurrent=(int)currentI.y();
								if(xCurrent<xMin){
									xMin=xCurrent;
								}
								if(xCurrent>xMax){
									xMax=xCurrent;
								}
								if(yCurrent<yMin){
									yMin=yCurrent;
								}
								if(yCurrent>yMax){
									yMax=yCurrent;
								}
							}
							if(xMin<-width/2){
								xMin=-width/2;
							}
							if(yMin<-height/2){
								yMin=-height/2;
							}
							if(xMax>=width/2){
								xMax=width/2-1;
							}
							if(yMax>=height/2){
								yMax=height/2-1;
							}
							for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
								boolean in=false;
								loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
									Vector norm=current.getN();
									double numer=norm.dot(vc[0]);
									//System.out.println(norm[0]+", "+norm[1]+", "+norm[2]);
									double denom=norm.dot(new Vector(x*1.0,y*1.0,raster));
									double r=numer/denom;
									Vector q=new Vector(r*x,r*y,raster*(r-1));
									Coord[]tempX=zBuffer[x+(int)(width/2)];
									Coord tempC=tempX[y+(int)(height/2)];
									double z=q.z();
									if(tempC!=null&&tempC.getZ()>=z){
										continue;
									}
									Vector w=q.minus(vc[0]);
									Vector u1=vc[1].minus(vc[0]);
									Vector v1=vc[2].minus(vc[0]);
									double uv=u1.dot(v1);
									double uu=u1.dot(u1);
									double vv=v1.dot(v1);
									double wv=w.dot(v1);
									double wu=w.dot(u1);
									double den=uv*uv-uu*vv;
									double s=(uv*wv-vv*wu)/den;
									double t=(uv*wu-uu*wv)/den;	
									if(s>=0&&t>=0&&(s+t)<=1){
										in=true;
										if(!current.hasTexture()){
											double temp=current.getN().dot(light);
											double ambient=0.3;
											temp=ambient+(1-ambient)*temp;
											tempX[y+(int)(height)/2]=new Coord(z,positive((int)(current.getR()*temp)),positive((int)(current.getG()*temp)),positive((int)(current.getB()*temp)));
										}else{
											BufferedImage texture=textures[current.getTexture()];
											/*if(u1>1.0){
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
											}*/
											Color c=blend(s,t,new Color(255,0,0),new Color(0,255,0),new Color(0,0,255));
											//Color c=blend(u1,u2,new Color(65,152,10),new Color(19,133,16),new Color(19,109,21));
											int[]tempPosition=current.getXY(s,t,texture.getWidth(),texture.getHeight());
											//Color c=new Color(texture.getRGB(tempPosition[0],tempPosition[1]));
											//Color c=new Color(texture.getRGB(tempPosition[0]/constant,tempPosition[1]/constant));
											//Color c=new Color(texture.getRGB(tempPosition[0]-tempPosition[0]%(constant*2),tempPosition[1]-tempPosition[1]%(constant*2)));
											int xOnImage=tempPosition[0]-tempPosition[0]%constant;
											int yOnImage=tempPosition[1]-tempPosition[1]%constant;
											double temp=current.getN().dot(light);
											/*double frequency=0.1;
											double xNoise=SimplexNoise.noise(frequency*xOnImage+vc[0].x(),vc[0].y());
											double yNoise=SimplexNoise.noise(vc[0].x(),frequency*yOnImage+vc[0].y());
											Vector noise=new Vector(xNoise,yNoise,0.0);
											double temp=current.getN().plus(noise.multiply(3.0)).unit().dot(new Vector(0.0,0.0,1.0));*/
											// ambient=0.0;
											//if(l==0){
											double ambient=0.3;
											//}
											temp=ambient+(1-ambient)*temp;
											//Color c=new Color(texture.getRGB(xOnImage,yOnImage));
											//if(current.getTexture()==7&&c.getRed()>200){
												//continue;
											//}
											tempX[y+(int)(height)/2]=new Coord(z,positive((int)(c.getRed()*temp)),positive((int)(c.getGreen()*temp)),positive((int)(c.getBlue()*temp)));	
											//tempX[y+(int)(height)/2]=new Coord(z,new Color(grass.getRGB((int)(u1*(grass.getWidth()-1)),(int)(u2*(grass.getHeight()-1))),true));
										}
									}else if(in==true){
										break;
									}
								}
							}
						}else if(v.size()==1){
							double raster=camera.getRaster();
							Vector front=vc[front1];
							double clip=camera.getClip();
							Vector del1=vc[back1].minus(front);
							Vector del2=vc[back2].minus(front);
							double t1=(clip-front.z())/del1.z();
							double t2=(clip-front.z())/del2.z();
							Vector coord1=front.plus(del1.multiply(t1));
							Vector coord2=front.plus(del2.multiply(t2));
							v.add(coord1);
							v.add(coord2);
							Vector current0=v.get(0);
							int xNum=(int)current0.x();
							int yNum=(int)current0.y();
							int xMin=xNum;
							int xMax=xNum;
							int yMin=yNum;
							int yMax=yNum;
							for(int i=0;i<v.size();i++){
								Vector currentI=v.get(i);
								int xCurrent=(int)currentI.x();
								int yCurrent=(int)currentI.y();
								if(xCurrent<xMin){
									xMin=xCurrent;
								}
								if(xCurrent>xMax){
									xMax=xCurrent;
								}
								if(yCurrent<yMin){
									yMin=yCurrent;
								}
								if(yCurrent>yMax){
									yMax=yCurrent;
								}
							}
							if(xMin<-width/2){
								xMin=-width/2;
							}
							if(yMin<-height/2){
								yMin=-height/2;
							}
							if(xMax>=width/2){
								xMax=width/2-1;
							}
							if(yMax>=height/2){
								yMax=height/2-1;
							}
							for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
								boolean in=false;
								loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
									Vector norm=current.getN();
									double numer=norm.dot(vc[0]);
									//System.out.println(norm[0]+", "+norm[1]+", "+norm[2]);
									double denom=norm.dot(new Vector(x*1.0,y*1.0,raster));
									double r=numer/denom;
									Vector q=new Vector(r*x,r*y,raster*(r-1));
									Coord[]tempX=zBuffer[x+(int)(width/2)];
									Coord tempC=tempX[y+(int)(height/2)];
									double z=q.z();
									if(tempC!=null&&tempC.getZ()>=z){
										continue;
									}
									Vector w=q.minus(vc[0]);
									Vector u1=vc[1].minus(vc[0]);
									Vector v1=vc[2].minus(vc[0]);
									double uv=u1.dot(v1);
									double uu=u1.dot(u1);
									double vv=v1.dot(v1);
									double wv=w.dot(v1);
									double wu=w.dot(u1);
									double den=uv*uv-uu*vv;
									double s=(uv*wv-vv*wu)/den;
									double t=(uv*wu-uu*wv)/den;	
									System.out.println(s+" "+t);
									if(s>=0&&t>=0&&(s+t)<=1){
										in=true;
										if(!current.hasTexture()){
											double temp=current.getN().dot(light);
											double ambient=0.3;
											temp=ambient+(1-ambient)*temp;
											tempX[y+(int)(height)/2]=new Coord(z,positive((int)(current.getR()*temp)),positive((int)(current.getG()*temp)),positive((int)(current.getB()*temp)));
										}else{
											BufferedImage texture=textures[current.getTexture()];
											/*if(u1>1.0){
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
											}*/
											Color c=blend(s,t,new Color(255,0,0),new Color(0,255,0),new Color(0,0,255));
											//Color c=blend(u1,u2,new Color(65,152,10),new Color(19,133,16),new Color(19,109,21));
											int[]tempPosition=current.getXY(s,t,texture.getWidth(),texture.getHeight());
											//Color c=new Color(texture.getRGB(tempPosition[0],tempPosition[1]));
											//Color c=new Color(texture.getRGB(tempPosition[0]/constant,tempPosition[1]/constant));
											//Color c=new Color(texture.getRGB(tempPosition[0]-tempPosition[0]%(constant*2),tempPosition[1]-tempPosition[1]%(constant*2)));
											int xOnImage=tempPosition[0]-tempPosition[0]%constant;
											int yOnImage=tempPosition[1]-tempPosition[1]%constant;
											double temp=current.getN().dot(light);
											/*double frequency=0.1;
											double xNoise=SimplexNoise.noise(frequency*xOnImage+vc[0].x(),vc[0].y());
											double yNoise=SimplexNoise.noise(vc[0].x(),frequency*yOnImage+vc[0].y());
											Vector noise=new Vector(xNoise,yNoise,0.0);
											double temp=current.getN().plus(noise.multiply(3.0)).unit().dot(new Vector(0.0,0.0,1.0));*/
											// ambient=0.0;
											//if(l==0){
											double ambient=0.3;
											//}
											temp=ambient+(1-ambient)*temp;
											//Color c=new Color(texture.getRGB(xOnImage,yOnImage));
											//if(current.getTexture()==7&&c.getRed()>200){
												//continue;
											//}
											tempX[y+(int)(height)/2]=new Coord(z,positive((int)(c.getRed()*temp)),positive((int)(c.getGreen()*temp)),positive((int)(c.getBlue()*temp)));	
											//tempX[y+(int)(height)/2]=new Coord(z,new Color(grass.getRGB((int)(u1*(grass.getWidth()-1)),(int)(u2*(grass.getHeight()-1))),true));
										}
									}else if(in==true){
										break;
									}
								}
							}
						}
					}
				}
			}
			for(int i=0;i<spheres.size();i++){
				Sphere s=spheres.get(i);
				Vector c=camera.changeVars(s.getC());
				double mag=c.mag();
				double radius=s.getRadius();
				double ratio=radius/mag;
				if(c.z()>=0||ratio>1.0){
					continue;
				}
				double raster=camera.getRaster();
				//Vector cproj=camera.perspective(c);
				/*System.out.println(cproj.x()+" "+cproj.y());
				Vector d=new Vector(c.x(),c.y(),0.0).unit();
				Vector ch=c.unit();
				double theta=Math.asin(ratio);
				double alpha=Math.acos(ch.dot(d));
				double gamma=Math.PI-theta-alpha;
				double major=mag*raster*ratio/(c.z()*Math.sin(gamma));
				int xMin=(int)(cproj.x()-major);
				int xMax=(int)(cproj.x()+major);
				int yMin=(int)(cproj.y()-major);
				int yMax=(int)(cproj.y()+major);*/
				Vector[]bounds=s.getBounds();
				Vector[]boundsProj=new Vector[8];
				for(int j=0;j<bounds.length;j++){
					boundsProj[j]=camera.perspective(camera.changeVars(bounds[j]));
				}
				int xNum=(int)boundsProj[0].x();
				int yNum=(int)boundsProj[0].y();
				int xMin=xNum;
				int xMax=xNum;
				int yMin=yNum;			
				int yMax=yNum;
				for(int j=1;j<boundsProj.length;j++){
					Vector temp=boundsProj[j];
					int xCurrent=(int)temp.x();
					int yCurrent=(int)temp.y();
					if(xCurrent<xMin){
						xMin=xCurrent;
					}
					if(xCurrent>xMax){
						xMax=xCurrent;
					}
					if(yCurrent<yMin){
						yMin=yCurrent;
					}
					if(yCurrent>yMax){
						yMax=yCurrent;
					}
				}
				if(xMin<-width/2){
					xMin=-width/2;
				}
				if(yMin<-height/2){
					yMin=-height/2;
				}
				if(xMax>=width/2){
					xMax=width/2-1;
				}
				if(yMax>=height/2){
					yMax=height/2-1;
				}
				//System.out.println(xMin+" "+xMax+" "+yMin+" "+yMax);
				for(int x=xMin-xMin%constant;x<=xMax;x+=constant){
					boolean in=false;
					loop1:for(int y=yMin-yMin%constant;y<=yMax;y+=constant){
						Vector b=new Vector(x*1.0,y*1.0,raster).unit();
						double aq=b.dot(b);
						double bq=2*b.dot(c.neg());
						double cq=c.dot(c)-radius*radius;
						double discriminant=bq*bq-4*aq*cq;
						if(discriminant<0){
							if(in==true){
								break loop1;
							}
						}else{
							in=true;
							double t=(-bq-Math.sqrt(discriminant))/(2*aq);
							Vector loc=b.multiply(t);
							Vector n=camera.returnVars(loc).minus(camera.returnVars(c)).unit();
							Coord[] tempX = zBuffer[x+(width/2)];
							Coord tempCoord = tempX[y+(height)/2];
							double z=loc.z();
							if(tempCoord==null||tempCoord.getZ()<z){
								//tempX[y+(int)(height)/2]=new Coord(n,z,s.getR(),s.getG(),s.getB());
								double ambient=0.3;
								double temp=n.dot(light);
								temp=ambient+(1-ambient)*temp;
								//System.out.println(s.getR()*temp);
								tempX[y+(int)(height)/2]=new Coord(z,positive((int)(s.getR()*temp)),positive((int)(s.getG()*temp)),positive((int)(s.getB()*temp)));
								///tempX[y+(int)(height)/2]=new Coord(z,new Color(wood.getRGB((int)(v1*(wood.getWidth()-1)),(int)(v2*(wood.getHeight()-1))),true));
							}
						}
					}
				}
			}
		}
		//long endTime = System.currentTimeMillis();
		//long timeElapsed = endTime - startTime;
		//System.out.println("Execution time in milliseconds: " + timeElapsed);
		//System.out.println(totalTime);
	}
}
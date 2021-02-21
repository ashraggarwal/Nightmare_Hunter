import java.io.Serializable;
import java.util.ArrayList;
public class Player extends Person implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private boolean onMap;
	private boolean inAir;
	private double jumpPower;
	private double v;
	private double speed;
	private double originalHealth=1000;
	private double health=originalHealth,attackPower=50.0;
	private Vector spawn;
	public Player(Vector pos,Double scale,int r,int g,int b,int id){
		super(pos,scale,r,g,b);
		this.spawn=new Vector(pos.x(),pos.y(),pos.z());
		this.id=id;
		this.onMap=true;
		this.inAir=false;
		this.jumpPower=20;
		this.v=0;
	}
	public Player(Vector pos,Double scale,int r,int g,int b,int texture,int id){
		super(pos,scale,r,g,b,texture);
		this.spawn=new Vector(pos.x(),pos.y(),pos.z());
		this.id=id;
		this.onMap=true;
		this.inAir=false;
		this.jumpPower=20;
		this.v=0;
	}
	public int getID(){
		return id;
	}
	public double getHealthRatio(){
		return health/originalHealth;
	}
	public void takeDamage(double attackPower){
		health-=attackPower;
		if(health<0){
			health=originalHealth;
			setPosition(spawn);
		}
	}
	public void turn(double theta,Camera c,ArrayList<RectPrism>walls){
		if(super.turn(theta,walls)){
			c.rotateZ(-theta);
		}
	}
	public void setSpeed(double speed){
		this.speed=speed;
	}
	public void hitMonster(ArrayList<Monster>monsters,double[]damage,Container<Monster>currentMonster){
		super.hitMonster(monsters,damage,currentMonster,attackPower);
	}
	public void move(Camera c,Map m,int num,ArrayList<RectPrism>walls){
		//I'm lazy and this move function could cause quantum tunneling...
		super.setInAir(inAir);
		double gravity=5;
		double airMoveFactor=0.5;
		if(onMap){
			if(speed!=0.0){
				super.moveOnMap(speed,m,num,walls);
				super.doWalkingAnimation();
			}
		}else if(inAir){
			if(speed!=0.0){
			 super.move(speed*airMoveFactor,num,walls);
			}
			super.moveZ(v);
			this.v-=gravity;
			double mapZ=super.getMapZ(m);
			if(mapZ>super.getZ()){
				super.setZ(mapZ);
				this.inAir=false;
				this.onMap=true;
				this.v=0;
			}
		}
		if(speed==0.0){
			super.endWalk();
		}
		super.attackAnimation();
		c.update(super.getHead());
		super.create();
	}
	public void moveOnVector(Map m,int num,ArrayList<RectPrism>walls){
		//I'm lazy and this move function could cause quantum tunneling...
		super.setInAir(inAir);
		double gravity=5;
		double airMoveFactor=0.5;
		if(onMap){
			if(speed!=0.0){
				super.moveOnMap(speed,m,num,walls);
				super.doWalkingAnimation();
			}
		}else if(inAir){
			if(speed!=0.0){
			 super.move(speed*airMoveFactor,num,walls);
			}
			super.moveZ(v);
			this.v-=gravity;
			double mapZ=super.getMapZ(m);
			if(mapZ>super.getZ()){
				super.setZ(mapZ);
				this.inAir=false;
				this.onMap=true;
				this.v=0;
			}
		}
		if(speed==0.0){
			super.endWalk();
		}
		super.attackAnimation();
		super.create();
	}
	public void jump(){
		if(inAir==false){
			inAir=true;
			onMap=false;
			this.v=jumpPower;
		}
	}
	public String toString(){
		return "I am a sprite.";
	}
	
}
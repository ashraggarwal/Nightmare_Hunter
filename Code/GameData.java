import java.util.ArrayList;
import java.io.Serializable;
public class GameData implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Player> playerList;
	private ArrayList<Monster>monsters;
    //private ArrayList<Wall>wallList;
	public GameData(){	
		playerList = new ArrayList<Player>();
		monsters=new ArrayList<Monster>();
        //wallList=new ArrayList<Wall>();
	}
	/*public ArrayList<Wall>getWallList(){
        return wallList;
    }*/
	public void addNewPlayer(Player p){
		playerList.add(p);
		System.out.println("player list size " +playerList.size());
	}
	/*public void addWall(Wall w){
        wallList.add(w);
    }*/
	public void removePlayer(int id){
		for(int i=0; i<playerList.size(); i++){
			if( id == playerList.get(i).getID() ){
				playerList.remove(i);
				i--;
			}
		}
	}
	/*public void updatePlayers(int myID,boolean[]action,Map map,ArrayList<RectPrism>walls){
		int playerIndex=-1;
		for(int i=0; i<playerList.size(); i++){
			int id = playerList.get(i).getID();
			if( id == myID ){
				playerIndex = i;
				break;
			}
		}
		if(playerIndex!=-1){
			Player person=playerList.get(playerIndex);
			double speed=0.0;
			boolean keyLeft=action[0];
			boolean keyUp=action[1];
			boolean keyRight=action[2];
			boolean keyDown=action[3];
			boolean keySpace=action[4];
			boolean keyShift=action[5];
			boolean key1=action[6];
			//could mess up camera
			if(keyLeft){
				person.turn(-0.1,walls);
			}
			if(keyUp){
				speed=10.0;
			}
			if(keyRight){
				person.turn(0.1,walls);
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
		}
	}*/
	public void updateMonsterHealth(double[]damage){
		int a=0;
		for(int i=0;i<monsters.size();i++){
			if((i+a)<damage.length)
				//System.out.println(damage[i+a]);
			if(i<monsters.size()&&(i+a)<damage.length&&!monsters.get(i).takeDamage(damage[i+a])){
				monsters.remove(i);
				i--;
				a++;
			}

		}
	}
	public void updateGame(Map map,ArrayList<RectPrism>walls){
        if(this.monsters.size()<50){
			this.monsters.add(new Monster(new Vector(Math.random()*16000+2000,Math.random()*16000+2000,0.0),/*Math.random()*50.0+50.0*/Math.random()*50.0+25.0,(int)(Math.random()*5),(int)(Math.random()*10+1),Math.random()*0.2+0.3,Math.random()*0.3+0.2,Math.random()*0.6+0.3,255,200,220,14));
		}
		if(this.playerList.size()>0){
			//System.out.println(playerList.get(0).getPos());
			for(int i=0;i<this.monsters.size();i++){
				int index=-1;
				double cutoff=2000.0;
				double minDist=cutoff*cutoff;
				for(int j=0;j<this.playerList.size();j++){
					double temp=this.monsters.get(i).getSquareDistance(playerList.get(j));
					if(temp<minDist){
						index=j;
						minDist=temp;
					}
				}
				if(index!=-1){
					this.monsters.get(i).setTracking(this.playerList.get(index));
				}else{
					this.monsters.get(i).setTracking(null);
				}
				if(this.monsters.get(i).getTracking()==null){
					this.monsters.get(i).setTracking(/*playerList*/map.getTrees().get((int)(Math.random()*map.getTrees().size())));
					//monsters.get(i).setTracking(()->(playerList.get((int)(Math.random()*playerList.size())).getPos()));
				}
			}
			//System.out.println(monsters.get(0).getPos());
		}
		int num=12;
		for(int i=0;i<this.monsters.size();i++){
			for(int j=0;j<num;j++){
				this.monsters.get(i).autoMove(5.0, map, 5, walls);
			}
		}
    }
	public ArrayList<Player> getPlayerList(){
		return playerList;
	}
	public ArrayList<Monster>getMonsters(){
		return this.monsters;
	}
}
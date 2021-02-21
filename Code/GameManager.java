import java.util.ArrayList;
public class GameManager implements Runnable{

	private GameData gameData;
	private DLList<ServerThread> serverThreads;
	private Map map;
	private ArrayList<RectPrism>walls;
	private ArrayList<Obstacle>obstacles;
	private int id = 0;
	public GameManager(){
		map=new Map();
		obstacles=new ArrayList<Obstacle>();
		walls=new ArrayList<RectPrism>();
		obstacles.add(new Building(new Vector(4000.0,4000.0,0.0),500.0,0.8,0.5,0.5,10,4,8,9,6,10,11,map));
		for(int i=0;i<8;i++){
			obstacles.add(new Building(new Vector(3000.0+14000.0*SimplexNoise.noise(25*i,255-25*i),3000.0+14000.0*SimplexNoise.noise(255-25*i,25*i),0.0),500.0,0.8,0.5,0.5,(int)(10*SimplexNoise.noise(25*i*i,255-25*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i,255-25*i*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i*i,255-25*i*i*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i*i*i,255-25*i*i*i*i*i)),(int)(10*SimplexNoise.noise(25*i*i*i*i*i*i,255-25*i*i*i*i*i*i)),10,11,map));
		}
		map.checkTrees(obstacles);
		obstacles.addAll(map.getTrees());
		for(int i=0;i<obstacles.size();i++){
			walls.addAll(obstacles.get(i).getWalls());
		}
		serverThreads = new DLList<ServerThread>();
		gameData = new GameData();
		Thread controlThread=new Thread(new ControlThread(this));
		controlThread.start();
		//gameData.addWall(new Wall(300,300,200,200));		
	}
	
	
	public void addThread(ServerThread st){
		serverThreads.add(st);
		System.out.println("server threads size "+serverThreads.size());
	}
	public synchronized void updateGameData(){
		getGameData().updateGame(map,walls);
		/*if(serverThreads.size()>0){
			int minSize=serverThreads.get(0).getActions().size();
			for(int i=0;i<serverThreads.size();i++){
				int temp=serverThreads.get(i).getActions().size();
				if(temp<minSize){
					minSize=temp;
				}
			}
			for(int j=0;j<serverThreads.size();j++){
				for(int i=0;i<minSize;i++){
					getGameData().updatePlayers(serverThreads.get(j).getID(),serverThreads.get(j).getActions().remove(),map,walls);
				}
			}
		}*/
	}
	public synchronized GameData getGameData(){
		return gameData;
	}
	public synchronized void broadCastGameData(){
		for(int i=0; i<serverThreads.size(); i++){
			serverThreads.get(i).sendGameData(getGameData());
		}
	}
	
	
	public void setGameData(GameData gameData){
		this.gameData = gameData;
	}

	
	public void remove(ServerThread st){
		serverThreads.remove(st);
	}
	
	public synchronized int addNewPlayer(){
		id++;
		gameData.addNewPlayer(new Player(new Vector(2000.0,2400.0,map.getZ(2000.0,2400.0)),20.0,0,0,255,5,id));
		/*int x = (int)(Math.random()*600+100);
		int y = (int)(Math.random()*300+100);
		gameData.addNewPlayer(new Player(x,y,id));*/
		return id;
	}
	

	
	public void run(){
		while(true){
			try{
				Thread.sleep(100); //millisecond
			} catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void removePlayer(int id){
		
		gameData.removePlayer(id);
		
	}
	
	
	
	
	
}
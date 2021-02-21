import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerThread implements Runnable{
	
	private Socket clientSocket;
	private ObjectOutputStream out;
	private GameManager gm;
	private int myID;
	private boolean allow;
	//private Queue<boolean[]>actions;
	public ServerThread(Socket clientSocket, GameManager gm){
		this.clientSocket = clientSocket;
		this.gm = gm;
		myID = -1;
		allow=false;
		//actions=new Queue<boolean[]>();
	}
	public int getID(){
		return myID;
	}
	
	public void sendGameData(GameData gameData){
		if( out != null&&allow ){
			try{
				//System.out.print("Sending GameData to player: " + myID );
				//System.out.println(gameData.getMonsters().get(0).getPos());
				out.reset();
				out.writeObject(gameData);
				//out.reset();
				//Integer frontNum=getActions().frontNum();
				//out.writeObject(frontNum);
			} catch (IOException ex){
				gm.removePlayer(myID);
				gm.remove(this);
				//System.out.println(ex.getMessage());
			}
			
		}
	}
	/*public synchronized Queue<boolean[]>getActions(){
		return actions;
	}*/
	@SuppressWarnings("unchecked")
	public void run(){
		
		
		System.out.println(Thread.currentThread().getName() + ": connection opened.");
		
		try{
			
			
			
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			
			
			
			String message = (String) in.readObject();
			System.out.println(message + ": " +Thread.currentThread().getName() + " Connection Successful. " );
			
			
			//add new player
			myID = gm.addNewPlayer();
			//System.out.println(myID);
			out.writeObject(Integer.valueOf(myID));
			allow=true;
			//send gameData to all clients
			gm.broadCastGameData();
			
			while(true){
				//System.out.println(" Waiting for gameData...");
				Player tempPlayer = (Player) in.readObject();
				Integer position=(Integer)in.readObject();
				double[]damage=(double[])in.readObject();
				if(position<gm.getGameData().getPlayerList().size()){
					gm.getGameData().getPlayerList().set(position,tempPlayer);
				}
				gm.getGameData().updateMonsterHealth(damage);
				//System.out.println(" Done reading gameData...");
				//gm.broadCastGameData();
				//boolean[]action=(boolean[])in.readObject();
				//getActions().add(action);
			}
			
			
			
			
			
		} catch (IOException ex){
			System.out.println("Connection closed");
			
			gm.removePlayer(myID);
			gm.broadCastGameData();
			
            System.out.println(ex.getMessage());
		} catch (ClassNotFoundException ex){
			System.out.println(ex);
		}
		
	}
	
}
public class ControlThread implements Runnable{
    private GameManager gm;
    public ControlThread(GameManager gm){
        this.gm=gm;
    }
    public void run(){
        while(true){
			//System.out.println( "running" );
			gm.updateGameData();
			gm.broadCastGameData();
			try{
				Thread.sleep(50); //millisecond
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
		}
    }
}
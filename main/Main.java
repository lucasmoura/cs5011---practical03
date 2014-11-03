package main;


import util.GameOutput;
import artificial_intelligence.LogicalAgent;
import game.Map;
import game.Player;

public class Main 
{
	
	private static void pressAnyKeyToContinue()
	 { 
	        System.out.println("Press any key to continue...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	 }
	
	public static void main(String[] args)
	{

		GameOutput.getInstance().init();
		Map.getInstance().initMap();
		Map.getInstance().createTunnels();
		Map.getInstance().generateMap();
		Map.getInstance().drawMap();
		//Map.getInstance().printMap();
		//pressAnyKeyToContinue();
		
		//Map.getInstance().generateMap();
		
		Player player = new LogicalAgent(1);
		int status =LogicalAgent.CONTINUE;
		
		String playerStart = "\n\nPlayer starting at position: "+((LogicalAgent) player).getPosition()+"\n";
		GameOutput.getInstance().writeToFile(playerStart);
		System.out.print(playerStart);
		
		while(status == LogicalAgent.CONTINUE)
		{
			status = player.move();
			Map.getInstance().drawMap();
			//pressAnyKeyToContinue();
		}	
		
		String playerStatus = "";
		
		switch(((LogicalAgent) player).getStatus())
		{
			case 0:
				playerStatus = "alive";
				break;
			
			case 1:
				playerStatus = "dead";
				break;
			
			case 4:
				playerStatus = "victorious";
				break;
			
		}
		
		
		System.out.println("Game finished with player status: "+playerStatus);
		GameOutput.getInstance().writeToFile("\nGame finished with player status: "+playerStatus+"\n");
		GameOutput.getInstance().close();
		
	}

}

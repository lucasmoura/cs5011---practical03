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

		
		//Map.getInstance().initMap();
		Map.getInstance().initMapWithFile();
		//Map.getInstance().generateMap();
		Map.getInstance().createTunnels();
		
		int numVictories =0;
		int totalKill = 0;
		int loopValue = 100;
		int totalBatEncounters = 0;
		
		for(int i =0; i<loopValue; i++)
		{
			//Map.getInstance().generateMap();
			Map.getInstance().initMapWithFile();
			GameOutput.getInstance().init();
			Map.getInstance().drawMap();
			
			//Map.getInstance().generateMap();
			
			Player player = new LogicalAgent(1);
			int status =LogicalAgent.CONTINUE;
			
			String playerStart = "\n\nPlayer starting at position: "
					+((LogicalAgent) player).getPosition()+"\n";
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
					numVictories++;
					break;
				
			}
			
			//pressAnyKeyToContinue();
			totalKill += (((LogicalAgent) player).hasKillWumpus() == true? 1: 0);
			totalBatEncounters += (((LogicalAgent) player).hasBatAttack() == true? 1: 0);
			
//			System.out.println((((LogicalAgent) player).hasKillWumpus() == true? 1: 0));
//			pressAnyKeyToContinue();
			
			
			System.out.println("Game finished with player status: "+playerStatus);
			GameOutput.getInstance().writeToFile("\nGame finished with player status: "+playerStatus+"\n");
			GameOutput.getInstance().close();
			
		}
		
		System.out.println();
		System.out.println(".........Result for running the game "+loopValue + " times..........");
		System.out.println("Number of victories: "+numVictories);
		System.out.println("Number of defeats: "+(100-numVictories));
		System.out.println("Killed wumpus in exactly: "+totalKill + (totalKill==1?" time":" times"));
		System.out.println("Bat attacked the player in exactly "+totalBatEncounters +(totalBatEncounters==1?" time":" times") );
		
	}

}

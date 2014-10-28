package main;


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
		Map.getInstance().initMapWithFile();
		Map.getInstance().createTunnels();
		//Map.getInstance().printMap();
		
		Player player = new LogicalAgent(1);
		int status =LogicalAgent.CONTINUE;
		
		System.out.println("Player starting position: "+((LogicalAgent) player).getPosition());
		
		while(status == LogicalAgent.CONTINUE)
		{
			status = player.move();
			//pressAnyKeyToContinue();
		}	
		
		
		
		System.out.println("Game finished with player status: "+((LogicalAgent) player).getStatus());
		
	}

}

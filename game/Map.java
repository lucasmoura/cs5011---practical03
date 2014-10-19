package game;

public class Map 
{
	private Graph gameMap;
	
	public Map()
	{
		gameMap = new Graph();
	}
	
	public void initMap()
	{
		gameMap.addVertex(1);
		gameMap.addVertex(2);
		gameMap.addVertex(3);
		gameMap.addVertex(4);
		gameMap.addVertex(5);
		gameMap.addVertex(6);
		gameMap.addVertex(7);
		gameMap.addVertex(8);
		gameMap.addVertex(9);
		gameMap.addVertex(10);
		gameMap.addVertex(11);
		gameMap.addVertex(12);
		gameMap.addVertex(13);
		gameMap.addVertex(14);
		gameMap.addVertex(15);
		gameMap.addVertex(16);
		gameMap.addVertex(17);
		gameMap.addVertex(18);
		gameMap.addVertex(19);
		gameMap.addVertex(20);
	}
	
	private void initCave1()
	{
		//Cave 01
		gameMap.addEdge(1,11);
		gameMap.addEdge(1, 3);
		gameMap.addEdge(1, 2);	
	}
	
	private void initCave2()
	{
		//Cave 02
		gameMap.addEdge(2, 1);
		gameMap.addEdge(2, 5);
		gameMap.addEdge(2, 16);	
	}
	
	private void initCave3()
	{
		
	}
	
	public void createTunnels()
	{
		initCave1();
		initCave2();
		initCave3();

		
		//Cave 03
		gameMap.addEdge(3, 1);
		gameMap.addEdge(3, 6);
		gameMap.addEdge(3, 4);
		
		//Cave 04
		gameMap.addEdge(4, 3);
		gameMap.addEdge(4, 8);
		gameMap.addEdge(4, 16);
		
		//Cave 05
		gameMap.addEdge(5, 4);
		gameMap.addEdge(5, 2);
		gameMap.addEdge(5, 10);
		
		//Cave 06
		gameMap.addEdge(6, 3);
		gameMap.addEdge(6, 7);
		gameMap.addEdge(6, 12);
		
		//Cave 07
		gameMap.addEdge(7, 6);
		gameMap.addEdge(7, 13);
		gameMap.addEdge(7, 8);
		
		//Cave 08
		gameMap.addEdge(8, 7);
		gameMap.addEdge(8, 4);
		gameMap.addEdge(8, 9);
		
		//Cave 09
		gameMap.addEdge(9, 8);
		gameMap.addEdge(9, 10);
		gameMap.addEdge(9, 14);
		
		//Cave 10
		gameMap.addEdge(10, 5);
		gameMap.addEdge(10, 9);
		gameMap.addEdge(10, 15);
		
		//Cave 11
		gameMap.addEdge(11, 1);
		gameMap.addEdge(11, 12);
		gameMap.addEdge(11,20);
		
		//Cave 12
		gameMap.addEdge(12, 11);
		gameMap.addEdge(12, 6);
		gameMap.addEdge(12, 17);
		
		//Cave 13
		gameMap.addEdge(13, 17);
		gameMap.addEdge(13, 7);
		gameMap.addEdge(13, 14);
		
		//Cave 14
		gameMap.addEdge(14, 13);
		gameMap.addEdge(14, 9);
		gameMap.addEdge(14, 18);
		
		//Cave 15
		gameMap.addEdge(15, 10);
		gameMap.addEdge(15, 18);
		gameMap.addEdge(15, 16);
		
		//Cave 16
		gameMap.addEdge(16, 2);
		gameMap.addEdge(16, 15);
		gameMap.addEdge(16, 20);
		
		//Cave 17
		gameMap.addEdge(17, 12);
		gameMap.addEdge(17, 13);
		gameMap.addEdge(17, 19);
		
		//Cave 18
		gameMap.addEdge(18, 19);
		gameMap.addEdge(18, 14);
		gameMap.addEdge(18, 15);
		
		//Cave 19
		gameMap.addEdge(19, 17);
		gameMap.addEdge(19, 20);
		gameMap.addEdge(19, 18);
		
		//Cave 20
		gameMap.addEdge(20, 11);
		gameMap.addEdge(20, 19);
		gameMap.addEdge(20, 16);
		
	}
	
	
	
}

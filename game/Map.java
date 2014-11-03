package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import util.GameOutput;
import util.Util;

public class Map 
{
	private Graph gameMap;
	private static Map instance = null;
	
	public static Map getInstance()
	{
		if(instance == null)
			instance = new Map();
		
		return instance;
	}
	
	private Map()
	{
		gameMap = new Graph();
	}
	
	private void createPath(int actualPosition, int destination)
	{
		Queue<Integer> path = new LinkedList<Integer>();
		HashMap<Integer, Integer> pathMap = new HashMap<Integer, Integer>();
		int[] stored = new int[21];
		Arrays.fill(stored, 0);
		
		path.add(actualPosition);
	
		
		while(!path.isEmpty())
		{
			Integer caveId = path.poll();
			stored[caveId] = 1;
			
			
			if(caveId == destination)
			{
				//System.out.println("Cave found");
				break;
			}
				
			
			ArrayList<Cave> adjacentCaves = getChamberEdges(caveId);
			
			for(int i =0; i<adjacentCaves.size(); i++)
			{
				if(stored[adjacentCaves.get(i).getId()] == 0)
				{
					path.add(adjacentCaves.get(i).getId());
					pathMap.put(adjacentCaves.get(i).getId(), caveId);
					stored[caveId] = 1;
				}
			}
		}
		
		Integer target = destination;
		
		while(target != null)
		{
			if(Map.getInstance().getCave(target).getType() == -1)
				Map.getInstance().getCave(target).setType(Cave.EMPTY);
			
			target = pathMap.get(target);
		}
		
	}
	
	public void generateMap()
	{
		int numCaves = 3;
		int numBats = 2;
		
		//System.out.println("Starting position: "+1);
		getCave(1).setType(Cave.EMPTY);
		
		Random rand = new Random();
		int max = 20;
		int min =1;
		int exit = -1;
		int cave = -1;
		int bat = -1;
		int wumpus = -1;
		
		do
		{
			exit = rand.nextInt((max - min) + 1) + min;
		}while(exit == 1);
		
		int treasure = -1;
		//System.out.println("Exit location: "+exit);
		
		getCave(exit).setType(Cave.EXIT);
		
		do
		{
			treasure = rand.nextInt((max - min) + 1) + min;
			
		}while(exit == treasure);
		
		//System.out.println("Treasure location: "+treasure);
		getCave(treasure).setType(Cave.TREASURE);
		
		//System.out.println("\nCreate treasure path\n");
		createPath(1, treasure);
		
		//System.out.println("\nCreate exit path\n");
		createPath(treasure, exit);

		
		boolean valid = false;
		
		for(int i =0; i<numBats; i++)
		{
			do
			{
				bat = rand.nextInt((max - min) + 1) + min;
				//System.out.println(cave);
				
				if(getCave(bat).getType() != -1)
					valid = false;
				else
					valid = true;
				
			}while(!valid);
			
			getCave(bat).setType(Cave.BAT);
		}
		
		valid = false;
		
		do
		{
			wumpus = rand.nextInt((max - min) + 1) + min;
			
			if(getCave(wumpus).getType() != -1)
				valid = false;
			else
				valid = true;
			
		}while(!valid);
		
		getCave(wumpus).setType(Cave.WUMPUS);
		
		
		
		for(int i =0; i<numCaves; i++)
		{
			
			do
			{
				cave = rand.nextInt((max - min) + 1) + min;
				//System.out.println(cave);
				
				if(getCave(cave).getType() != -1)
					valid = false;
				else
					valid = true;
				
			}while(!valid);
			
			getCave(cave).setType(Cave.PIT);
				
		}
		
		for(int i =1; i<=20; i++)
		{
			Cave c = getCave(i);
			
			if(c.getType() == -1)
				c.setType(Cave.EMPTY);
			
			//System.out.println("Cave id: "+c.getId() +", with type: "+c.getType());
			
		}
		
	}

	
	public void initMap()
	{
		gameMap.addVertex(1, -1);
		gameMap.addVertex(2, -1);
		gameMap.addVertex(3, -1);
		gameMap.addVertex(4, -1);
		gameMap.addVertex(5, -1);
		gameMap.addVertex(6, -1);
		gameMap.addVertex(7, -1);
		gameMap.addVertex(8, -1);
		gameMap.addVertex(9, -1);
		gameMap.addVertex(10, -1);
		gameMap.addVertex(11, -1);
		gameMap.addVertex(12, -1);
		gameMap.addVertex(13, -1);
		gameMap.addVertex(14, -1);
		gameMap.addVertex(15, -1);
		gameMap.addVertex(16, -1);
		gameMap.addVertex(17, -1);
		gameMap.addVertex(18, -1);
		gameMap.addVertex(19, -1);
		gameMap.addVertex(20, -1);
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
		//Cave 03
		gameMap.addEdge(3, 1);
		gameMap.addEdge(3, 6);
		gameMap.addEdge(3, 4);	
	}
	
	private void initCave4()
	{
		//Cave 04
		gameMap.addEdge(4, 3);
		gameMap.addEdge(4, 8);
		gameMap.addEdge(4, 5);	
	}
	
	private void initCave5()
	{
		//Cave 05
		gameMap.addEdge(5, 4);
		gameMap.addEdge(5, 2);
		gameMap.addEdge(5, 10);	
	}
	
	private void initCave6()
	{
		//Cave 06
		gameMap.addEdge(6, 3);
		gameMap.addEdge(6, 7);
		gameMap.addEdge(6, 12);	
	}

	private void initCave7()
	{
		//Cave 07
		gameMap.addEdge(7, 6);
		gameMap.addEdge(7, 13);
		gameMap.addEdge(7, 8);	
	}

	private void initCave8()
	{
		//Cave 08
		gameMap.addEdge(8, 7);
		gameMap.addEdge(8, 4);
		gameMap.addEdge(8, 9);
	}

	private void initCave9()
	{
		//Cave 09
		gameMap.addEdge(9, 8);
		gameMap.addEdge(9, 10);
		gameMap.addEdge(9, 14);
	}

	private void initCave10()
	{
		//Cave 10
		gameMap.addEdge(10, 5);
		gameMap.addEdge(10, 9);
		gameMap.addEdge(10, 15);
	}

	private void initCave11()
	{
		//Cave 11
		gameMap.addEdge(11, 1);
		gameMap.addEdge(11, 12);
		gameMap.addEdge(11, 20);
	}

	private void initCave12()
	{
		//Cave 12
		gameMap.addEdge(12, 11);
		gameMap.addEdge(12, 6);
		gameMap.addEdge(12, 17);
	}

	private void initCave13()
	{
		//Cave 13
		gameMap.addEdge(13, 17);
		gameMap.addEdge(13, 7);
		gameMap.addEdge(13, 14);
	}

	private void initCave14()
	{
		//Cave 14
		gameMap.addEdge(14, 13);
		gameMap.addEdge(14, 9);
		gameMap.addEdge(14, 18);	
	}

	private void initCave15()
	{
		//Cave 15
		gameMap.addEdge(15, 10);
		gameMap.addEdge(15, 18);
		gameMap.addEdge(15, 16);
	}

	private void initCave16()
	{
		//Cave 16
		gameMap.addEdge(16, 2);
		gameMap.addEdge(16, 15);
		gameMap.addEdge(16, 20);
	}

	private void initCave17()
	{
		//Cave 17
		gameMap.addEdge(17, 12);
		gameMap.addEdge(17, 13);
		gameMap.addEdge(17, 19);
	}

	private void initCave18()
	{
		//Cave 18
		gameMap.addEdge(18, 19);
		gameMap.addEdge(18, 14);
		gameMap.addEdge(18, 15);
	}

	private void initCave19()
	{
		//Cave 19
		gameMap.addEdge(19, 17);
		gameMap.addEdge(19, 20);
		gameMap.addEdge(19, 18);
	}

	private void initCave20()
	{
		//Cave 20
		gameMap.addEdge(20, 11);
		gameMap.addEdge(20, 19);
		gameMap.addEdge(20, 16);
	}

	
	public void createTunnels()
	{
		initCave1();
		initCave2();
		initCave3();
		initCave4();
		initCave5();
		initCave6();
		initCave7();
		initCave8();
		initCave9();
		initCave10();
		initCave11();
		initCave12();
		initCave13();
		initCave14();
		initCave15();
		initCave16();
		initCave17();
		initCave18();
		initCave19();
		initCave20();
		
	}
	
	public ArrayList<Cave> getAdjacentCaves(int caveId)
	{
		return gameMap.getEdges(caveId);
	}
	
	public void printMap()
	{
		gameMap.printGraph();
	}
	
	public void initMapWithFile()
	{
		String filename = "/files/game.txt";
		BufferedReader reader = null;
		int id = 1;
		
		Path currentRelativePath = Paths.get("");
		filename = currentRelativePath.toAbsolutePath().toString()+filename;
		//System.out.println("Current relative path is: " + filename);
		
		try
		{
		    reader = new BufferedReader(new FileReader(filename));
		    String text = null;

		    while ((text = reader.readLine()) != null) 
		    {
		        gameMap.addVertex(id++, Integer.parseInt(text));
		        gameMap.getCave(id-1).setType(Integer.parseInt(text));
		    }
		} 
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();
		} 
		catch (IOException e)
		{
		    e.printStackTrace();
		} 
		finally 
		{
		    try 
		    {
		        if (reader != null)
		            reader.close();
		        
		    } 
		    catch (IOException e) 
		    {
		    }
		}

		
	}

	public ArrayList<Cave> getChamberEdges(int actualPosition) 
	{
		return gameMap.getEdges(actualPosition);
	}
	
	public Cave getCave(int caveId)
	{
		return gameMap.getCave(caveId);
	}
	
	public void clear()
	{
		gameMap.clear();
	}
	
	public void drawMap()
	{
		String map = " \n                                                     ,:.                                                    \n                                                    .."+getCave(20).getId()+"..                                                   \n                                                   ..."+Util.convertType(getCave(20).getType())+"...                                                   \n                                                 ...........                                                \n                                               *****  *  *****                                              \n                                            *****     **    ****                                            \n                                          *****       **      *****                                         \n                                        ****          **        *****                                       \n                                     *****            **           ****                                     \n                                   ****               **             *****                                  \n                                 ****                ...                ****                                \n                              *****                 .."+getCave(19).getId()+"..                 *****                             \n                            ****                   ..."+Util.convertType(getCave(19).getType())+"....                  *****                           \n                          ****                    ***   ***                    ****                         \n                       *****                    ***      ****                    *****                      \n                     ****                      ***         ***                     *****                    \n                  *****                      ***             ***                      ****                  \n                *****                       ***               ****                      *****               \n              ****                        ***                   ***                       *****             \n           *****                       *****                     ****                        ****           \n         *****                        *****                        *****                       *****        \n       ****                       ...."+getCave(17).getId()+"....                       ...."+getCave(18).getId()+".....                    *****      \n   ......                        ....."+Util.convertType(getCave(17).getType())+".....                      ....."+Util.convertType(getCave(18).getType())+"....                       ..."+getCave(16).getId()+".. \n  ..."+getCave(11).getId()+"..                   *****   ....  ***                   ***      *******                    ..."+Util.convertType(getCave(16).getType())+".. \n  ..."+Util.convertType(getCave(11).getType())+"..****               *****           *****             *****            *******           ****...... \n    **      ****....."+getCave(12).getId()+"...                  .."+getCave(13).getId()+"..************.."+getCave(14).getId()+"..              *****...."+getCave(15).getId()+"...***   **    \n     **         ....."+Util.convertType(getCave(12).getType())+"....                  ..."+Util.convertType(getCave(13).getType())+"..************..."+Util.convertType(getCave(14).getType())+"..                   ....."+Util.convertType(getCave(15).getType())+"....     **    \n     ***          .......                   **                ***                      ***           **     \n      **             ***                   ***                 **                     ***            **     \n       **             ***                  **                   **                   ***            **      \n       **              ***                **                    **                  ***            ***      \n        **              ***               **                     **                ***             **       \n        **               ***             **                      ***              ***             ***       \n         **               ***            **                       **             ***              **        \n         ***               ***        .."+getCave(7).getId()+"..                       .."+getCave(9).getId()+"..         ***              **         \n          **                ...      ..."+Util.convertType(getCave(7).getType())+"...                      .."+Util.convertType(getCave(9).getType())+"..        ***               **         \n          ***               .."+getCave(6).getId()+"..****........                   ......****..."+getCave(10).getId()+"...              **          \n           **               .."+Util.convertType(getCave(6).getType())+"..         ****               ****        ...."+Util.convertType(getCave(10).getType())+"....              **          \n            **               ***             ****          *****           .....               **           \n            **               ***               *****     ****               **                ***           \n             **               **                  ...."+getCave(8).getId()+"....                 **                **            \n             **               **                   ..."+Util.convertType(getCave(8).getType())+"...                  **               **             \n              **              **                    ....                    **               **             \n              ***             **                     **                     **              **              \n               **              **                    **                     **              **              \n               ***             **                    **                    **              **               \n                **             **                    ***                   **              **               \n                 **            **            *****..."+getCave(4).getId()+"...**                **             **                \n                 **            ***        *****......"+Util.convertType(getCave(4).getType())+".....******         **            ***                \n                  **          .."+getCave(3).getId()+"...********                  *******...."+getCave(5).getId()+"..           **                 \n                  **          .."+Util.convertType(getCave(3).getType())+"...                                   ..."+Util.convertType(getCave(5).getType())+"...          **                  \n                   **        ***                                           ****         **                  \n                   ***      ***                                              ***       **                   \n                    **     ***                                                 ***     **                   \n                    ***   ***                                                   ***   **                    \n                     **  **                                                      ***  **                    \n                      ....                                                       ......                     \n                     ..."+getCave(1).getId()+"...****************************************************..."+getCave(2).getId()+"...                    \n                     ..."+Util.convertType(getCave(1).getType())+"....***************************************************..."+Util.convertType(getCave(2).getType())+".... ";
		System.out.println(map);
		GameOutput.getInstance().writeToFile(map);
		System.out.println();
	}
	
	
	
}

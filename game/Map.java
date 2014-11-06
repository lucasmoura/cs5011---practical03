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
	
	/*
	 * Method used to generate a path of empty cave between two destinations. It used a breadth-first search algorithm to do so.
	 * @param actualPosition: The starting position of the path
	 * @param destination: The last location of the path
	 */
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
	
	/*
	 * Method used to randomly generate the map 
	 */
	public void generateMap()
	{
		Random rand = new Random();
		
		//Randomly initialize the number of pits and bats
		int numCaves = rand.nextInt(2)+1;
		int numBats = rand.nextInt(1)+1;
		
		getCave(1).setType(Cave.EMPTY);
		
		
		int max = 20;
		int min =1;
		int exit = -1;
		int cave = -1;
		int bat = -1;
		int wumpus = -1;
		
		//Generate the exit location
		do
		{
			exit = rand.nextInt((max - min) + 1) + min;
		}while(exit == 1);
		
		int treasure = -1;
		
		getCave(exit).setType(Cave.EXIT);
		
		//Generate the treasure location
		do
		{
			treasure = rand.nextInt((max - min) + 1) + min;
			
		}while(exit == treasure);
		
		getCave(treasure).setType(Cave.TREASURE);
		
		//Create a path between the starting position and the treasure
		createPath(1, treasure);
		//Create a path between the treasure and exit cave
		createPath(treasure, exit);

		/*
		 * The next piece of code generates the positions of bat, pits and wumpus
		 * The code basically picks random places in the map and checks to see if their type have not be generated yet, and if not, set the
		 * position containing the correspondent hazard.
		 */
		
		boolean valid = false;
		
		for(int i =0; i<numBats; i++)
		{
			do
			{
				bat = rand.nextInt((max - min) + 1) + min;
				
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
			
		}
		
	}

	/*
	 * Method used to create all the game map vertex
	 */
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
	
	/*
	 * Method used to create all the necessary edges for cave 01
	 */
	private void initCave1()
	{
		//Cave 01
		gameMap.addEdge(1,11);
		gameMap.addEdge(1, 3);
		gameMap.addEdge(1, 2);	
	}
	
	/*
	 * Method used to create all the necessary edges for cave 02
	 */
	private void initCave2()
	{
		//Cave 02
		gameMap.addEdge(2, 1);
		gameMap.addEdge(2, 5);
		gameMap.addEdge(2, 16);	
	}
	
	/*
	 * Method used to create all the necessary edges for cave 03
	 */
	private void initCave3()
	{
		//Cave 03
		gameMap.addEdge(3, 1);
		gameMap.addEdge(3, 6);
		gameMap.addEdge(3, 4);	
	}
	
	/*
	 * Method used to create all the necessary edges for cave 04
	 */
	private void initCave4()
	{
		//Cave 04
		gameMap.addEdge(4, 3);
		gameMap.addEdge(4, 8);
		gameMap.addEdge(4, 5);	
	}
	
	/*
	 * Method used to create all the necessary edges for cave 05
	 */
	private void initCave5()
	{
		//Cave 05
		gameMap.addEdge(5, 4);
		gameMap.addEdge(5, 2);
		gameMap.addEdge(5, 10);	
	}
	
	/*
	 * Method used to create all the necessary edges for cave 06
	 */
	private void initCave6()
	{
		//Cave 06
		gameMap.addEdge(6, 3);
		gameMap.addEdge(6, 7);
		gameMap.addEdge(6, 12);	
	}

	/*
	 * Method used to create all the necessary edges for cave 07
	 */
	private void initCave7()
	{
		//Cave 07
		gameMap.addEdge(7, 6);
		gameMap.addEdge(7, 13);
		gameMap.addEdge(7, 8);	
	}

	/*
	 * Method used to create all the necessary edges for cave 08
	 */
	private void initCave8()
	{
		//Cave 08
		gameMap.addEdge(8, 7);
		gameMap.addEdge(8, 4);
		gameMap.addEdge(8, 9);
	}

	/*
	 * Method used to create all the necessary edges for cave 09
	 */
	private void initCave9()
	{
		//Cave 09
		gameMap.addEdge(9, 8);
		gameMap.addEdge(9, 10);
		gameMap.addEdge(9, 14);
	}

	/*
	 * Method used to create all the necessary edges for cave 10
	 */
	private void initCave10()
	{
		//Cave 10
		gameMap.addEdge(10, 5);
		gameMap.addEdge(10, 9);
		gameMap.addEdge(10, 15);
	}

	/*
	 * Method used to create all the necessary edges for cave 11
	 */
	private void initCave11()
	{
		//Cave 11
		gameMap.addEdge(11, 1);
		gameMap.addEdge(11, 12);
		gameMap.addEdge(11, 20);
	}

	/*
	 * Method used to create all the necessary edges for cave 12
	 */
	private void initCave12()
	{
		//Cave 12
		gameMap.addEdge(12, 11);
		gameMap.addEdge(12, 6);
		gameMap.addEdge(12, 17);
	}

	/*
	 * Method used to create all the necessary edges for cave 13
	 */
	private void initCave13()
	{
		//Cave 13
		gameMap.addEdge(13, 17);
		gameMap.addEdge(13, 7);
		gameMap.addEdge(13, 14);
	}

	/*
	 * Method used to create all the necessary edges for cave 14
	 */
	private void initCave14()
	{
		//Cave 14
		gameMap.addEdge(14, 13);
		gameMap.addEdge(14, 9);
		gameMap.addEdge(14, 18);	
	}

	/*
	 * Method used to create all the necessary edges for cave 15
	 */
	private void initCave15()
	{
		//Cave 15
		gameMap.addEdge(15, 10);
		gameMap.addEdge(15, 18);
		gameMap.addEdge(15, 16);
	}

	/*
	 * Method used to create all the necessary edges for cave 16
	 */
	private void initCave16()
	{
		//Cave 16
		gameMap.addEdge(16, 2);
		gameMap.addEdge(16, 15);
		gameMap.addEdge(16, 20);
	}

	/*
	 * Method used to create all the necessary edges for cave 17
	 */
	private void initCave17()
	{
		//Cave 17
		gameMap.addEdge(17, 12);
		gameMap.addEdge(17, 13);
		gameMap.addEdge(17, 19);
	}

	/*
	 * Method used to create all the necessary edges for cave 18
	 */
	private void initCave18()
	{
		//Cave 18
		gameMap.addEdge(18, 19);
		gameMap.addEdge(18, 14);
		gameMap.addEdge(18, 15);
	}

	/*
	 * Method used to create all the necessary edges for cave 19
	 */
	private void initCave19()
	{
		//Cave 19
		gameMap.addEdge(19, 17);
		gameMap.addEdge(19, 20);
		gameMap.addEdge(19, 18);
	}

	/*
	 * Method used to create all the necessary edges for cave 20
	 */
	private void initCave20()
	{
		//Cave 20
		gameMap.addEdge(20, 11);
		gameMap.addEdge(20, 19);
		gameMap.addEdge(20, 16);
	}

	/*
	 * Method used to create all the edges for the game caves
	 */
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
	
	/*
	 * Method used to retrieve the adjacent caves of a specific cave
	 * @param caveId: The cave in each the edges will be retrieved
	 * @return: An ArrayList containing the edges for the specific cave id
	 */
	public ArrayList<Cave> getAdjacentCaves(int caveId)
	{
		return gameMap.getEdges(caveId);
	}
	
	/*
	 * Method used to print the graph structure
	 */
	public void printMap()
	{
		gameMap.printGraph();
	}
	
	/*
	 * Method used to init the cave types with a txt file
	 */
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

	/*
	 * Method used to retrieve the edges of a specific cave position
	 * @params actualPosition: the cave in which the edges will be returned
	 * @return: An ArrayList containing all the edges for the specific cave position
	 */
	public ArrayList<Cave> getChamberEdges(int actualPosition) 
	{
		return gameMap.getEdges(actualPosition);
	}
	
	/*
	 * Method used to get a cave from the game graph
	 * @param caveId: The id of the cave that will be retrieved
	 * @return: A Cave if the respective cave id
	 */
	public Cave getCave(int caveId)
	{
		return gameMap.getCave(caveId);
	}
	
	/*
	 * Method used to clear the game graph
	 */
	public void clear()
	{
		gameMap.clear();
	}
	
	/*
	 * Method used to draw the textual interface of the game
	 */
	public void drawMap()
	{
		String map = " \n                                                     ,:.                                                    \n                                                    .."+getCave(20).getId()+"..                                                   \n                                                   ..."+Util.convertType(20)+"...                                                   \n                                                 ...........                                                \n                                               *****  *  *****                                              \n                                            *****     **    ****                                            \n                                          *****       **      *****                                         \n                                        ****          **        *****                                       \n                                     *****            **           ****                                     \n                                   ****               **             *****                                  \n                                 ****                ...                ****                                \n                              *****                 .."+getCave(19).getId()+"..                 *****                             \n                            ****                   ..."+Util.convertType(19)+"....                  *****                           \n                          ****                    ***   ***                    ****                         \n                       *****                    ***      ****                    *****                      \n                     ****                      ***         ***                     *****                    \n                  *****                      ***             ***                      ****                  \n                *****                       ***               ****                      *****               \n              ****                        ***                   ***                       *****             \n           *****                       *****                     ****                        ****           \n         *****                        *****                        *****                       *****        \n       ****                       ...."+getCave(17).getId()+"....                       ...."+getCave(18).getId()+".....                    *****      \n   ......                        ....."+Util.convertType(17)+".....                      ....."+Util.convertType(18)+"....                       ..."+getCave(16).getId()+".. \n  ..."+getCave(11).getId()+"..                   *****   ....  ***                   ***      *******                    ..."+Util.convertType(16)+".. \n  ..."+Util.convertType(11)+"..****               *****           *****             *****            *******           ****...... \n    **      ****....."+getCave(12).getId()+"...                  .."+getCave(13).getId()+"..************.."+getCave(14).getId()+"..              *****...."+getCave(15).getId()+"...***   **    \n     **         ....."+Util.convertType(12)+"....                  ..."+Util.convertType(13)+"..************..."+Util.convertType(14)+"..                   ....."+Util.convertType(15)+"....     **    \n     ***          .......                   **                ***                      ***           **     \n      **             ***                   ***                 **                     ***            **     \n       **             ***                  **                   **                   ***            **      \n       **              ***                **                    **                  ***            ***      \n        **              ***               **                     **                ***             **       \n        **               ***             **                      ***              ***             ***       \n         **               ***            **                       **             ***              **        \n         ***               ***        .."+getCave(7).getId()+"..                       .."+getCave(9).getId()+"..         ***              **         \n          **                ...      ..."+Util.convertType(7)+"...                      .."+Util.convertType(9)+"..        ***               **         \n          ***               .."+getCave(6).getId()+"..****........                   ......****..."+getCave(10).getId()+"...              **          \n           **               .."+Util.convertType(6)+"..         ****               ****        ...."+Util.convertType(10)+"....              **          \n            **               ***             ****          *****           .....               **           \n            **               ***               *****     ****               **                ***           \n             **               **                  ...."+getCave(8).getId()+"....                 **                **            \n             **               **                   ..."+Util.convertType(8)+"...                  **               **             \n              **              **                    ....                    **               **             \n              ***             **                     **                     **              **              \n               **              **                    **                     **              **              \n               ***             **                    **                    **              **               \n                **             **                    ***                   **              **               \n                 **            **            *****..."+getCave(4).getId()+"...**                **             **                \n                 **            ***        *****......"+Util.convertType(4)+".....******         **            ***                \n                  **          .."+getCave(3).getId()+"...********                  *******...."+getCave(5).getId()+"..           **                 \n                  **          .."+Util.convertType(3)+"...                                   ..."+Util.convertType(5)+"...          **                  \n                   **        ***                                           ****         **                  \n                   ***      ***                                              ***       **                   \n                    **     ***                                                 ***     **                   \n                    ***   ***                                                   ***   **                    \n                     **  **                                                      ***  **                    \n                      ....                                                       ......                     \n                     ..."+getCave(1).getId()+"...****************************************************..."+getCave(2).getId()+"...                    \n                     ..."+Util.convertType(1)+"....***************************************************..."+Util.convertType(2)+".... ";
		System.out.println(map);
		GameOutput.getInstance().writeToFile(map);
		System.out.println();
	}
	
	
	
}

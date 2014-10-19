package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Map 
{
	private Graph gameMap;
	
	public Map()
	{
		gameMap = new Graph();
	}
	
	public void initMap()
	{
		gameMap.addVertex(1, 0);
		gameMap.addVertex(2, 0);
		gameMap.addVertex(3, 0);
		gameMap.addVertex(4, 0);
		gameMap.addVertex(5, 0);
		gameMap.addVertex(6, 0);
		gameMap.addVertex(7, 0);
		gameMap.addVertex(8, 0);
		gameMap.addVertex(9, 0);
		gameMap.addVertex(10, 0);
		gameMap.addVertex(11, 0);
		gameMap.addVertex(12, 0);
		gameMap.addVertex(13, 0);
		gameMap.addVertex(14, 0);
		gameMap.addVertex(15, 0);
		gameMap.addVertex(16, 0);
		gameMap.addVertex(17, 0);
		gameMap.addVertex(18, 0);
		gameMap.addVertex(19, 0);
		gameMap.addVertex(20, 0);
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
	
	public ArrayList<Cave> getCave(int cave)
	{
		return gameMap.getVertex(cave);
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
		System.out.println("Current relative path is: " + filename);
		
		try
		{
		    reader = new BufferedReader(new FileReader(filename));
		    String text = null;

		    while ((text = reader.readLine()) != null) 
		    {
		        gameMap.addVertex(id++, Integer.parseInt(text));
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
	
	
	
}

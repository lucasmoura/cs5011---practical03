package main;

import game.Map;

public class Main 
{
	
	public static void main(String[] args)
	{
		Map map = new Map();
		map.initMapWithFile();
		map.createTunnels();
		map.printMap();
		
	}

}

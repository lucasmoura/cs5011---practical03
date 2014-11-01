package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import game.Cave;
import game.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import artificial_intelligence.LogicalAgent;

public class LogicalAgentTest 
{
	
	private LogicalAgent agent;
	private Map map;
	
	
	@Before
	public void setUp() throws Exception 
	{
		map = Map.getInstance();
		map.initMap();
		map.createTunnels();
		
		agent = new LogicalAgent(1);
	}

	@After
	public void tearDown() throws Exception 
	{
		map.clear();
		agent = null;
	}

	@Test
	public void moveWumpusTest()
	{
		
		boolean expectedValue = false;
		map.getCave(2).setType(Cave.WUMPUS);
	
		int actualPosition = agent.moveWumpus();
		ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(2);
		
		for(Cave cave: adjacentCaves)
		{
			if(cave.getId() == actualPosition)
			{
				expectedValue = true;
				break;
			}
		}
		
		assertEquals(expectedValue, true);
		
		Map.getInstance().getCave(2).setType(Cave.EMPTY);
		
	}

}

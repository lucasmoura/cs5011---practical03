package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import game.Cave;
import game.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapTest 
{
	
	private Map map;
	
	@Before
	public void setUp()
	{
		map = Map.getInstance();
		map.initMap();
		map.createTunnels();
	}

	@Test
	public void cave1ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(1);
		int []expectedTunnels = {11, 3, 2};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave2ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(2);
		int []expectedTunnels = {1, 5, 16};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave3ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(3);
		int []expectedTunnels = {1, 6, 4};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave4ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(4);
		int []expectedTunnels = {3, 8, 5};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave5ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(5);
		int []expectedTunnels = {4, 2, 10};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave6ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(6);
		int []expectedTunnels = {3, 7, 12};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave7ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(7);
		int []expectedTunnels = {6, 13, 8};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave8ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(8);
		int []expectedTunnels = {7, 4, 9};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave9ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(9);
		int []expectedTunnels = {8, 10, 14};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave10ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(10);
		int []expectedTunnels = {5, 9, 15};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave11ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(11);
		int []expectedTunnels = {1, 12, 20};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave12ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(12);
		int []expectedTunnels = {11, 6, 17};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave13ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(13);
		int []expectedTunnels = {17, 7, 14};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave14ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(14);
		int []expectedTunnels = {13, 9, 18};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave15ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(15);
		int []expectedTunnels = {10, 18, 16};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave16ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(16);
		int []expectedTunnels = {2, 15, 20};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave17ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(17);
		int []expectedTunnels = {12, 13, 19};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave18ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(18);
		int []expectedTunnels = {19, 14, 15};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave19ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(19);
		int []expectedTunnels = {17, 20, 18};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}

	@Test
	public void cave20ConnectionsTest()
	{
		ArrayList<Cave> tunnels = map.getChamberEdges(20);
		int []expectedTunnels = {11, 19, 16};

		for(int i =0; i<tunnels.size(); i++)
			assertEquals(expectedTunnels[i], tunnels.get(i).getId());

	}
	
	@After
	public void tearDown()
	{
		map.clear();
	}


}

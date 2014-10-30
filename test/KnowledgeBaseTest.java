package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import game.Cave;
import game.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import artificial_intelligence.KnowledgeBase;
import artificial_intelligence.Premisse;

public class KnowledgeBaseTest 
{

	private Map map;
	private KnowledgeBase knowledgeBase;
	
	@Before
	public void setUp() throws Exception
	{
		map = Map.getInstance();
		map.initMap();
		map.createTunnels();
		
		knowledgeBase = new KnowledgeBase();
		
	}

	@After
	public void tearDown() throws Exception
	{
		Map.getInstance().getCave(11).setType(Cave.EMPTY);
		Map.getInstance().getCave(6).setType(Cave.EMPTY);
		Map.getInstance().getCave(3).setType(Cave.EMPTY);
		
		for(int i =0; i<21; i++)
			knowledgeBase.univisitCave(i);
		
		map.clear();
	}
	
	private void setVisitedNodes(int[] nodes)
	{
		for(int i =0; i<nodes.length; i++)
			knowledgeBase.setVisited(nodes[i]);
	}
	
	private void resetVisitedNodes(int[] nodes)
	{
		for(int i =0; i<nodes.length; i++)
			knowledgeBase.univisitCave(nodes[i]);
	}

	@Test
	public void createVisitedCavesTest() 
	{
		int actualPosition = 3;
		int[] expectedValues = {3, 1, 4, 11};
		int[] nodes = {11,1,4};
		int expectedSize = 4;
		setVisitedNodes(nodes);
		ArrayList<Cave> visitedCaves = knowledgeBase.
				createVisitedCaves(actualPosition);
		
		
		
		assertEquals(expectedSize, visitedCaves.size());
		for(int i =0; i<visitedCaves.size(); i++)
			assertEquals(expectedValues[i], visitedCaves.get(i).getId());
		
		resetVisitedNodes(nodes);
		
		actualPosition = 20;
		expectedSize = 5;
		int[] expectedValues2 = {20, 16, 2, 15, 18};
		int[] nodes2 = {20, 16, 15, 2, 18};
		
		setVisitedNodes(nodes2);
		
		visitedCaves.clear();
		visitedCaves = knowledgeBase.createVisitedCaves(20);
		
		assertEquals(expectedSize, visitedCaves.size());
		for(int i =0; i<visitedCaves.size(); i++)
			assertEquals(expectedValues2[i], visitedCaves.get(i).getId());
		
		resetVisitedNodes(nodes2);
		
	}
	
	@Test
	public void generatePremissesTest()
	{
		System.out.println("\ngeneratePremissesTest\n");
		
		int actualPosition = 3;
		int[] nodes = {3,11,1,4};
		setVisitedNodes(nodes);
		
		knowledgeBase.setBreeze(actualPosition, true);
		knowledgeBase.setEmpty(actualPosition, true);
		knowledgeBase.setBat(actualPosition, false);
		knowledgeBase.setPit(actualPosition, false);
		knowledgeBase.setWumpus(actualPosition, false);
		knowledgeBase.setSmell(actualPosition, false);
		knowledgeBase.setSound(actualPosition, false);
		
		knowledgeBase.setBreeze(1, false);
		knowledgeBase.setEmpty(1, true);
		knowledgeBase.setSmell(1, false);
		knowledgeBase.setSound(1, true);
		
		knowledgeBase.setBreeze(11, false);
		knowledgeBase.setEmpty(11, false);
		knowledgeBase.setSmell(11, false);
		knowledgeBase.setSound(11, false);
		knowledgeBase.setBat(11, true);
		
		knowledgeBase.setBreeze(4, false);
		knowledgeBase.setEmpty(4, true);
		knowledgeBase.setSmell(4, false);
		knowledgeBase.setSound(4, false);
		
		knowledgeBase.setVisited(1);
		knowledgeBase.setVisited(actualPosition);
		knowledgeBase.setVisited(11);
		knowledgeBase.setVisited(4);
		
		Map.getInstance().getCave(11).setType(Cave.BAT);
		Map.getInstance().getCave(6).setType(Cave.PIT);
		Map.getInstance().getCave(3).setType(Cave.EMPTY);
		
		ArrayList<Cave> visitedCaves = knowledgeBase.
				createVisitedCaves(actualPosition);
		ArrayList<Premisse> actualPremisse = knowledgeBase.generatePremisses(visitedCaves, actualPosition);
		
		ArrayList<Premisse> expectedPremisses = new ArrayList<Premisse>();
		Premisse premisse01 = new Premisse(6, 1);
		
		premisse01.setProbability(Cave.PIT);
		expectedPremisses.add(premisse01);
		
		int[] actualPremisse01 = actualPremisse.get(0).getProbability();
		int[] expectedPremisse01 = {Cave.PIT, -1, -1};
		int expectedLocation = 6;
		
		assertEquals(expectedLocation, actualPremisse.get(0).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse01[i], expectedPremisse01[i]);
		
		Premisse premisse02 = new Premisse(11, 1);
		premisse02.setProbability(Cave.BAT);
		int[] actualPremisse02 = actualPremisse.get(1).getProbability();
		int[] expectedPremisse02 = {Cave.BAT, -1, -1};
		expectedLocation = 11;
		
		assertEquals(expectedLocation, actualPremisse.get(1).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse02[i], expectedPremisse02[i]);
		
		Premisse premisse03 = new Premisse(2, 1);
		premisse03.setProbability(Cave.EMPTY);
		int[] actualPremisse03 = actualPremisse.get(2).getProbability();
		int[] expectedPremisse03 = premisse03.getProbability();
		expectedLocation = 2;
		
		assertEquals(expectedLocation, actualPremisse.get(2).getLocation());	
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse03[i], expectedPremisse03[i]);
		
	}
	
	@Test
	public void generatePremisses02Test()
	{
		System.out.println("generatePremisses02Test");
		
		int actualPosition = 1;
		int[] nodes = {1,11};
		setVisitedNodes(nodes);
		
		knowledgeBase.setBreeze(actualPosition, true);
		knowledgeBase.setEmpty(actualPosition, true);
		knowledgeBase.setSmell(actualPosition, false);
		knowledgeBase.setSound(actualPosition, true);
		
		knowledgeBase.setBreeze(11, false);
		knowledgeBase.setEmpty(11, false);
		knowledgeBase.setSmell(11, false);
		knowledgeBase.setSound(11, false);
		knowledgeBase.setBat(11, true);
		
		knowledgeBase.setVisited(1);
		knowledgeBase.setVisited(actualPosition);
		knowledgeBase.setVisited(11);
		knowledgeBase.setVisited(4);
		
		Map.getInstance().getCave(11).setType(Cave.BAT);
		Map.getInstance().getCave(3).setType(Cave.PIT);
		
		ArrayList<Cave> visitedCaves = knowledgeBase.
				createVisitedCaves(actualPosition);
		ArrayList<Premisse> actualPremisse = knowledgeBase.generatePremisses(visitedCaves, actualPosition);
		
		ArrayList<Premisse> expectedPremisses = new ArrayList<Premisse>();
		Premisse premisse01 = new Premisse(11, 1);
		
		premisse01.setProbability(Cave.BAT);
		expectedPremisses.add(premisse01);
		
		int[] actualPremisse01 = actualPremisse.get(0).getProbability();
		int[] expectedPremisse01 = premisse01.getProbability();
		int expectedLocation = 11;
		
		assertEquals(expectedLocation, actualPremisse.get(0).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse01[i], expectedPremisse01[i]);
		
		Premisse premisse02 = new Premisse(3, 1);
		premisse02.setProbability(Cave.PIT);
		premisse02.setProbability(Cave.EMPTY);
		int[] actualPremisse02 = actualPremisse.get(1).getProbability();
		int[] expectedPremisse02 = premisse02.getProbability();
		expectedLocation = 3;
		
		assertEquals(expectedLocation, actualPremisse.get(1).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse02[i], expectedPremisse02[i]);
		
		Premisse premisse03 = new Premisse(2, 1);
		premisse03.setProbability(Cave.PIT);
		premisse03.setProbability(Cave.EMPTY);
		int[] actualPremisse03 = actualPremisse.get(2).getProbability();
		int[] expectedPremisse03 = premisse03.getProbability();
		expectedLocation = 2;
		
		for(int i =0; i<3; i++)
			System.out.println("Teste: "+actualPremisse03[i]);
		
		assertEquals(expectedLocation, actualPremisse.get(2).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse03[i], expectedPremisse03[i]);
		
	}
	
	@Test
	public void makeInferenceTest()
	{
		
		System.out.println("makeInferenceTest");
		
		int actualPosition = 1;
		int[] nodes = {1,11,3};
		setVisitedNodes(nodes);
		
		knowledgeBase.setBreeze(actualPosition, true);
		knowledgeBase.setEmpty(actualPosition, true);
		knowledgeBase.setSmell(actualPosition, false);
		knowledgeBase.setSound(actualPosition, true);
		
		knowledgeBase.setBreeze(11, false);
		knowledgeBase.setEmpty(11, false);
		knowledgeBase.setSmell(11, false);
		knowledgeBase.setSound(11, false);
		knowledgeBase.setBat(11, true);
		
		knowledgeBase.setBreeze(3, false);
		knowledgeBase.setEmpty(3, true);
		knowledgeBase.setSmell(3, false);
		knowledgeBase.setSound(3, false);
		knowledgeBase.setBat(3, false);
		
		knowledgeBase.setVisited(actualPosition);
		knowledgeBase.setVisited(3);
		
		Map.getInstance().getCave(11).setType(Cave.BAT);
		Map.getInstance().getCave(3).setType(Cave.PIT);
		
		boolean expectedValue = true;
		boolean actualValue = false;
		
		int father = 1;
		int adjacentId = 2;
		int unknown = 1;
		int sensations[] = new int[3];
		sensations[0] = 1;
		sensations[1]=0;
		sensations[2] = 1;
		
		actualValue = knowledgeBase.makeInference(father, adjacentId, unknown, sensations);
		
		assertEquals(expectedValue, actualValue);
		assertEquals(expectedValue, knowledgeBase.isEmpty(2));
		
		
	}
	
	@Test
	public void orderPremissesTest()
	{
		
		System.out.println("\norderPremissesTest\n");
		
		ArrayList<Premisse> premisses = new ArrayList<Premisse>();
		
		Premisse premisse01 = new Premisse(1, 1);
		premisse01.setProbability(Cave.EMPTY);
		
		Premisse premisse02 = new Premisse(2, 1);
		premisse02.setProbability(Cave.WUMPUS);
		
		Premisse premisse03 = new Premisse(3, 1);
		premisse03.setProbability(Cave.WUMPUS);
		premisse03.setProbability(Cave.EMPTY);
		
		Premisse premisse04 = new Premisse(4, 1);
		premisse04.setProbability(Cave.BAT);
		premisse04.setProbability(Cave.EMPTY);
		
		Premisse premisse05 = new Premisse(5, 1);
		premisse05.setProbability(Cave.BAT);
		premisse05.setProbability(Cave.WUMPUS);
		
		Premisse premisse06 = new Premisse(6, 1);
		premisse06.setProbability(Cave.PIT);
		premisse06.setProbability(Cave.WUMPUS);
		
		Premisse premisse07 = new Premisse(7, 1);
		premisse07.setProbability(Cave.PIT);
		premisse07.setProbability(Cave.BAT);
		
		Premisse premisse08 = new Premisse(8, 1);
		premisse08.setProbability(Cave.PIT);
		premisse08.setProbability(Cave.EMPTY);
		
		Premisse premisse09 = new Premisse(9, 1);
		premisse09.setProbability(Cave.PIT);
		premisse09.setProbability(Cave.BAT);
		premisse09.setProbability(Cave.WUMPUS);
		
		//System.out.println(Arrays.toString(premisse09.getProbability()));
		
		Premisse premisse10 = new Premisse(10, 1);
		premisse10.setProbability(Cave.BAT);
		
		Premisse premisse11 = new Premisse(11, 1);
		premisse11.setProbability(Cave.PIT);
		
		premisses.add(premisse05);
		premisses.add(premisse10);
		premisses.add(premisse03);
		premisses.add(premisse07);
		premisses.add(premisse11);
		premisses.add(premisse01);
		premisses.add(premisse06);
		premisses.add(premisse04);
		premisses.add(premisse02);
		premisses.add(premisse08);
		premisses.add(premisse09);
		
		Premisse[] expectedPremisses = {premisse01, premisse02, premisse03, premisse04, premisse05, premisse06,
				premisse07, premisse08, premisse09, premisse10, premisse11};
		
		
		knowledgeBase.orderPremisses(premisses);
		
		for(int i =0; i<premisses.size(); i++)
		{
			System.out.println("Order: "+i);
			assertEquals(expectedPremisses[i], premisses.get(i));
		}	
		
	}
	
	@Test
	public void askTest()
	{
		System.out.println("\naskTest\n");
		
		int actualPosition = 3;
		int[] nodes = {3,11,1,4};
		setVisitedNodes(nodes);
		
		knowledgeBase.setBreeze(actualPosition, true);
		knowledgeBase.setEmpty(actualPosition, true);
		knowledgeBase.setBat(actualPosition, false);
		knowledgeBase.setPit(actualPosition, false);
		knowledgeBase.setWumpus(actualPosition, false);
		knowledgeBase.setSmell(actualPosition, false);
		knowledgeBase.setSound(actualPosition, false);
		
		knowledgeBase.setBreeze(1, false);
		knowledgeBase.setEmpty(1, true);
		knowledgeBase.setSmell(1, false);
		knowledgeBase.setSound(1, true);
		
		knowledgeBase.setBreeze(11, false);
		knowledgeBase.setEmpty(11, false);
		knowledgeBase.setSmell(11, false);
		knowledgeBase.setSound(11, false);
		knowledgeBase.setBat(11, true);
		
		knowledgeBase.setBreeze(4, false);
		knowledgeBase.setEmpty(4, true);
		knowledgeBase.setSmell(4, false);
		knowledgeBase.setSound(4, false);
		
		knowledgeBase.setVisited(1);
		knowledgeBase.setVisited(actualPosition);
		knowledgeBase.setVisited(11);
		knowledgeBase.setVisited(4);
		
		Map.getInstance().getCave(11).setType(Cave.BAT);
		Map.getInstance().getCave(6).setType(Cave.PIT);
		Map.getInstance().getCave(3).setType(Cave.EMPTY);
		
		ArrayList<Premisse> premisses = knowledgeBase.ask(actualPosition);
		Premisse premisse = premisses.get(0);
		
		int expectedFather = 1;
		int expectedLocation = 2;
		
		assertEquals(expectedLocation, premisse.getLocation());
		assertEquals(expectedFather, premisse.getFather());
		
		
	}
	
	@Test
	public void findFatherTest()
	{
		System.out.println("\nfindFatherTest\n");
		
		int actualPosition = 3;
		int[] nodes = {1, 3,4,11,12,17,13,14,9};
		
		System.out.println("\nfindFatherTest01\n");
		
		setVisitedNodes(nodes);
		
		int expectedValue = 4;
		int location = 8;
		int actualValue = knowledgeBase.findFather(actualPosition, location, 1);
		
		assertEquals(expectedValue, actualValue);
		
		resetVisitedNodes(nodes);
		
		int[] nodes2 = {19, 18, 14, 9, 8, 4, 3, 7, 6};
		actualPosition = 8;
		location = 19;
		
		System.out.println("\nfindFatherTest02\n");
		
		setVisitedNodes(nodes2);
		
		expectedValue = 9;
		actualValue = knowledgeBase.findFather(actualPosition, location, 1);
		
		assertEquals(expectedValue, actualValue);
		
		resetVisitedNodes(nodes2);
		
		int[] nodes3 = {3,11,1,4};
		actualPosition = 3;
		location = 2;
		
		setVisitedNodes(nodes3);
		
		expectedValue = 1;
		actualValue = knowledgeBase.findFather(actualPosition, location, 1);
		
		assertEquals(expectedValue, actualValue);
		
		resetVisitedNodes(nodes3);
		
		
		actualPosition = 3;
		location = 8;
		
		knowledgeBase.setPit(4, true);

		
		setVisitedNodes(nodes);
		
		expectedValue = 1;
		actualValue = knowledgeBase.findFather(actualPosition, location, 1);
		
		assertEquals(expectedValue, actualValue);
		
		resetVisitedNodes(nodes);
		
		knowledgeBase.setPit(4, false);
		
		
	}
	

}

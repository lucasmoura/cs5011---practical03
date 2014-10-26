package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
		ArrayList<Premisse> actualPremisse = knowledgeBase.generatePremisses(visitedCaves);
		
		ArrayList<Premisse> expectedPremisses = new ArrayList<Premisse>();
		Premisse premisse01 = new Premisse(6);
		
		premisse01.setProbability(Cave.PIT);
		expectedPremisses.add(premisse01);
		
		int[] actualPremisse01 = actualPremisse.get(0).getProbability();
		int[] expectedPremisse01 = {Cave.PIT, -1, -1};
		int expectedLocation = 6;
		
		assertEquals(expectedLocation, actualPremisse.get(0).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse01[i], expectedPremisse01[i]);
		
		Premisse premisse02 = new Premisse(11);
		premisse02.setProbability(Cave.BAT);
		int[] actualPremisse02 = actualPremisse.get(1).getProbability();
		int[] expectedPremisse02 = {Cave.BAT, -1, -1};
		expectedLocation = 11;
		
		assertEquals(expectedLocation, actualPremisse.get(1).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse02[i], expectedPremisse02[i]);
		
		Premisse premisse03 = new Premisse(2);
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
		ArrayList<Premisse> actualPremisse = knowledgeBase.generatePremisses(visitedCaves);
		
		ArrayList<Premisse> expectedPremisses = new ArrayList<Premisse>();
		Premisse premisse01 = new Premisse(11);
		
		premisse01.setProbability(Cave.BAT);
		expectedPremisses.add(premisse01);
		
		int[] actualPremisse01 = actualPremisse.get(0).getProbability();
		int[] expectedPremisse01 = premisse01.getProbability();
		int expectedLocation = 11;
		
		assertEquals(expectedLocation, actualPremisse.get(0).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse01[i], expectedPremisse01[i]);
		
		Premisse premisse02 = new Premisse(3);
		premisse02.setProbability(Cave.PIT);
		premisse02.setProbability(Cave.EMPTY);
		int[] actualPremisse02 = actualPremisse.get(1).getProbability();
		int[] expectedPremisse02 = premisse02.getProbability();
		expectedLocation = 3;
		
		assertEquals(expectedLocation, actualPremisse.get(1).getLocation());
		for(int i =0; i<3; i++)
			assertEquals(actualPremisse02[i], expectedPremisse02[i]);
		
		Premisse premisse03 = new Premisse(2);
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
		ArrayList<Premisse> premisses = new ArrayList<Premisse>();
		
		Premisse premisse01 = new Premisse(1);
		premisse01.setProbability(Cave.EMPTY);
		
		Premisse premisse02 = new Premisse(2);
		premisse02.setProbability(Cave.PIT);
		premisse02.setProbability(Cave.BAT);
		
		Premisse premisse03 = new Premisse(3);
		premisse03.setProbability(Cave.PIT);
		premisse03.setProbability(Cave.WUMPUS);
		
		Premisse premisse04 = new Premisse(4);
		premisse04.setProbability(Cave.PIT);
		premisse04.setProbability(Cave.EMPTY);
		
		Premisse premisse05 = new Premisse(5);
		premisse05.setProbability(Cave.EMPTY);
		
		Premisse premisse06 = new Premisse(6);
		premisse06.setProbability(Cave.PIT);
		premisse06.setProbability(Cave.BAT);
		premisse06.setProbability(Cave.WUMPUS);
		
		premisses.add(premisse05);
		premisses.add(premisse03);
		premisses.add(premisse01);
		premisses.add(premisse04);
		premisses.add(premisse02);
		
		Premisse[] expectedPremisses = {premisse01, premisse05, premisse04, premisse02, premisse03, premisse06};
		
		
		knowledgeBase.orderPremisses(premisses);
		
		for(int i =0; i<premisses.size(); i++)
			assertEquals(expectedPremisses[i], premisses.get(i));
		
	}
	

}

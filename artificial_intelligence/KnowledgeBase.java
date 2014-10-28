package artificial_intelligence;

import game.Cave;
import game.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

public class KnowledgeBase
{
	
	public class PremisseComparator implements Comparator<Premisse>
	{
	
		@Override
		public int compare(Premisse p1, Premisse p2)
		{
			int[] probability01 = p1.getProbability();
			int[] probability02 = p2.getProbability();
			
			if(probability01[0] == probability02[0])
			{
				
				if(probability01[1] == probability02[1])
				{
					if(probability01[2] == probability02[2])
					{
						if(p1.getLocation()>p2.getLocation())
							return 1;
						else if(p1.getLocation()<p2.getLocation())
							return -1;
						else
							return 0;
					}
					else
					{
						if(probability01[2]==Cave.EMPTY)
							return -1;
						else if(probability02[2] == Cave.EMPTY)
							return 1;
						else if(probability01[2]==Cave.WUMPUS)
							return 1;
						else if(probability02[2] == Cave.WUMPUS)
							return -1;
						else if(probability01[2]==Cave.BAT)
							return -1;
						else
							return 1;
					}
				}
				else if(probability01[1]==Cave.EMPTY)
					return -1;
				else if(probability02[1] == Cave.EMPTY)
					return 1;
				else if(probability01[1]==Cave.WUMPUS)
					return 1;
				else if(probability02[1] == Cave.WUMPUS)
					return -1;
				else if(probability01[1]==Cave.BAT)
					return -1;
				else
					return 1;
				
			}
			else
			{
				if(probability01[0]==Cave.EMPTY)
					return -1;
				else if(probability02[0] == Cave.EMPTY)
					return 1;
				else if(probability01[0]==Cave.WUMPUS)
					return 1;
				else if(probability02[0] == Cave.WUMPUS)
					return -1;
				else if(probability01[0]==Cave.BAT)
					return -1;
				else
					return 1;
			}
				
		}
	}	
		
	private CaveInfo[] caveInfo;
	
	public KnowledgeBase()
	{
		caveInfo = new CaveInfo[21];
		
		for(int i =0; i<caveInfo.length; i++)
			caveInfo[i] = new CaveInfo();
	}
	
	public boolean hasPit(int cave)
	{
		return caveInfo[cave].hasPit();
	}
	
	public ArrayList<Cave> createVisitedCaves(int actualPosition)
	{
		Stack<Cave> caves = new Stack<Cave>();
		ArrayList<Cave> visitedCaves = new ArrayList<Cave>();
		
		int[] storedCaves = new int[21];
		Arrays.fill(storedCaves, 0);
		
		Cave cave = Map.getInstance().getCave(actualPosition);
		
		caves.push(cave);
		visitedCaves.add(cave);
		storedCaves[cave.getId()] = 1;
		
		while(caves.isEmpty() == false)
		{
			Cave c = caves.peek();
			caves.pop();
			
			ArrayList<Cave> adjacentCaves =  Map.getInstance().
					getChamberEdges(c.getId());
			
			for(int i =0; i<adjacentCaves.size(); i++)
			{
				c = adjacentCaves.get(i);
				if(caveInfo[c.getId()].isVisited() == true
						&& storedCaves[c.getId()] == 0)
				{
					caves.push(c);
					visitedCaves.add(c);
					storedCaves[c.getId()] = 1;
				}	
			}
		}
		
		return visitedCaves;
		
	}
	
	public int findFather(int actualPosition, int location)
	{
		ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(actualPosition);
		Stack<Cave> cave = new Stack<Cave>();
		int father;
		boolean found = false;
		
		int[] storedCaves = new int[21];
		Arrays.fill(storedCaves, 0);
		
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			if(adjacentCaves.get(i).getId() == location)
				return actualPosition;
		}
		
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			Cave c = adjacentCaves.get(i);
			father = c.getId();
			storedCaves[c.getId()] = 1;
			cave.add(c);
			
			while(cave.isEmpty() == false)
			{
				c = cave.peek();
				cave.pop();
				
				if(c.getId() == location)
				{
				 found = true;
				 break;
				}
				
				ArrayList<Cave> adjacent = Map.getInstance().getChamberEdges(c.getId());
				
				for(Cave ca: adjacent)
				{
					if(ca.getId() == location)
					{
						found = true;
						break;
					}
					else if(ca.getId() == actualPosition)
						continue;
					else if(caveInfo[ca.getId()].isVisited()
							&& storedCaves[ca.getId()] == 0)
					{
						cave.add(ca);
						storedCaves[ca.getId()] = 1;
					}
				}
			}
			
			if(found)
				return father;
		}
		
		return -1;
	}
	
	public ArrayList<Premisse> generatePremisses(ArrayList<Cave> visitedCaves, int actualPosition)
	{
		ArrayList<Premisse> premisses = new ArrayList<Premisse>();
		
		for(int i =0; i<visitedCaves.size(); i++)
		{
			
			int id = visitedCaves.get(i).getId();
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(id);
			CaveInfo cave = caveInfo[id];
			
			if(cave.hasBat() || cave.hasPit())
				continue;
			
			boolean breeze = cave.feelBreeze();
			boolean sound = cave.feelSound();
			boolean smell = cave.feelSmell();
			
			int[] sensations = new int[3];
			Arrays.fill(sensations, 0);
			int empty = getNumEmptyCaves(breeze, sound, smell);
		    
			//System.out.println("Number of empty caves: "+empty);
			
			for(int j =0; j<adjacentCaves.size(); j++)
			{
				int adjacentId = adjacentCaves.get(j).getId();
				CaveInfo adjacentCave = caveInfo[adjacentId];
				int tempEmpty = empty;
				
				if(adjacentCave.isVisited() == true)
				{
					//System.out.println("Visisted cave: "+adjacentId);
					//System.out.println("Type: "+Map.getInstance().getCave(adjacentId).getType());
					Premisse premisse;
					Cave c = Map.getInstance().getCave(adjacentId);
					
					switch(c.getType())
					{
						case Cave.BAT:
							sound = false;
							//System.out.println("Add premisse with location: "+adjacentId);
							premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId));
							premisse.setProbability(Cave.BAT);
							premisses.add(premisse);
							break;
						
						case Cave.PIT:
							breeze = false;
							//System.out.println("Add premisse with location: "+adjacentId);
						    premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId));
						    premisse.setProbability(Cave.PIT);
						    premisses.add(premisse);
							break;
						
						case Cave.WUMPUS:
							smell = false;
							//System.out.println("Add premisse with location: "+adjacentId);
							premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId));
							premisse.setProbability(Cave.WUMPUS);
							premisses.add(premisse);
							break;
					}
					
					//System.out.println("Moving to next cave");
					continue;
				}
				
				//System.out.println("Adjacent cave: "+adjacentId);
				Premisse premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId));
				int unknown = 3;
				
				for(int k = 0; k<adjacentCaves.size(); k++)
				{
					int caveId = adjacentCaves.get(k).getId();
					//System.out.println("Cave id: "+caveId);
					
					if(caveId == adjacentId)
						continue;
					
					if(caveInfo[caveId].hasPit())
					{
						breeze = false;
						unknown--;
						sensations[0] = 1;
						continue;
					}
					
					if(caveInfo[caveId].hasWumpus())
					{
						smell = false;
						sensations[1] = 1;
						unknown--;
						continue;
					}
					
					if(caveInfo[caveId].hasBat())
					{
						 sound = false;
						 sensations[2] = 1;
						 unknown--;
						 continue;
					}
					
					if(caveInfo[caveId].isEmpty())
					{
						unknown--;
						tempEmpty--;
					}	
					
				}	
				
				//System.out.println(makeInference(id, adjacentId, unknown, sensations));
				
				//System.out.println("Stop looking into adjacent nodes");
			
				if(adjacentCave.hasPit() == true)
				{
					premisse.setProbability(Cave.PIT);
					//System.out.println("Add premisse with location: "+adjacentId);
					premisses.add(premisse);
					continue;
				}
				else if(breeze == true)
					premisse.setProbability(Cave.PIT);
					
				if(adjacentCave.hasBat() == true)
				{
					premisse.setProbability(Cave.BAT);
					//System.out.println("Add premisse with location: "+adjacentId);
					premisses.add(premisse);
					continue;
				}
				else if(sound == true)
					premisse.setProbability(Cave.BAT);
				
				if(adjacentCave.hasWumpus() == true)
				{
					premisse.setProbability(Cave.WUMPUS);
					//System.out.println("Add premisse with location: "+adjacentId);
					premisses.add(premisse);
					continue;
				}
				else if(smell == true)
					premisse.setProbability(Cave.WUMPUS);
				
				//System.out.println("Total empty caves: "+tempEmpty);
				if(tempEmpty != 0)
				{
					//System.out.println("Entrou aqui");
					premisse.setProbability(Cave.EMPTY);
				}
				
				//System.out.println("Add premisse with location: "+adjacentId);
				premisses.add(premisse);
			}
			
				
		}
	
		return premisses;
	}
	
	public boolean makeInference(int father, int adjacentId, int unknown, int[] sensations) 
	{
		if(caveInfo[adjacentId].isVisited())
			return false;
		
		boolean breeze = caveInfo[father].feelBreeze();
		boolean sound = caveInfo[father].feelSound();
		boolean smell = caveInfo[father].feelSmell();
		
//		System.out.println("Father: "+father);
//		System.out.println("Inference: "+adjacentId);
//		
		if(breeze && sound && !smell && sensations[0] == 1 && sensations[2] == 1 && unknown == 1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && sound && !smell && sensations[0] == 0 && sensations[2] == 1 && unknown == 0)
		{
			caveInfo[adjacentId].setPit(true);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && sound && !smell && sensations[0] == 1 && sensations[2] == 0 && unknown == 0)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(true);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && !sound && smell && sensations[0] == 1 && sensations[1] == 1 && unknown == 1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && !sound && smell && sensations[0] == 0 && sensations[1] == 1 && unknown == 0)
		{
			caveInfo[adjacentId].setPit(true);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && !sound && smell && sensations[0] == 1 && sensations[1] == 0 && unknown == 0)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(true);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(!breeze && sound && smell && sensations[2] == 1 && sensations[1] == 1 && unknown == 1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(!breeze && sound && smell && sensations[2] == 0 && sensations[1] == 1 && unknown == 0)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(true);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(!breeze && sound && smell && sensations[2] == 1 && sensations[1] == 0 && unknown == 0)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(true);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && sensations[0]==1 && unknown ==1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && sensations[0]==0 && unknown ==1)
		{
			caveInfo[adjacentId].setPit(true);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(smell && sensations[1]==1 && unknown ==1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(smell && sensations[1]==0 && unknown ==1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(true);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(sound && sensations[2]==1 && unknown ==1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(sound && sensations[2]==0 && unknown ==1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(true);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		
		return false;
		
	}
	
	public void orderPremisses(ArrayList<Premisse> premisses)
	{
		Collections.sort(premisses, new PremisseComparator());
	}

	public int getNumEmptyCaves(boolean breeze, boolean sound, boolean smell)
	{
		int empty = 3;
		
		empty += (breeze == true)?-1:0;
		empty += (sound == true)?-1:0;
		empty += (smell == true)?-1:0;
		
		return empty;
	}

	public Premisse ask(int actualPosition)
	{
		ArrayList<Cave> visitedCaves = createVisitedCaves(actualPosition);
		ArrayList<Premisse> premisses = generatePremisses(visitedCaves, actualPosition);
		orderPremisses(premisses);
		printPremisses(premisses);
		
		Premisse bestPremisse =  null;
		
		if(premisses.isEmpty() == false)
			bestPremisse = premisses.get(0);
		
		return bestPremisse;
	}
	
	private void printPremisses(ArrayList<Premisse> premisses)
	{
		
		System.out.println();
		
		for(int i =0; i<premisses.size(); i++)
		{
			System.out.println("Premisse: "+i);
			System.out.println("Father: "+premisses.get(i).getFather());
			System.out.println("Location: "+premisses.get(i).getLocation());
			System.out.println("Probability: "+Arrays.toString(premisses.get(i).getProbability()));
			System.out.println();
		}
		
	}
	
	public boolean hasWumpus(int cave)
	{
		return caveInfo[cave].hasWumpus();
	}
	
	public boolean hasBat(int cave)
	{
		return caveInfo[cave].hasBat();
	}
	
	public boolean isEmpty(int cave)
	{
		return caveInfo[cave].isEmpty();
	}
	
	public boolean isVisited(int cave)
	{
		return caveInfo[cave].isVisited();
	}
	
	public boolean feelBreeze(int cave)
	{
		return caveInfo[cave].feelBreeze();
	}
	
	public boolean feelSmell(int cave)
	{
		return caveInfo[cave].feelSmell();
	}
	
	public boolean feelSound(int cave)
	{
		return caveInfo[cave].feelSound();
	}
	
	public void setPit(int cave, boolean value)
	{
		caveInfo[cave].setPit(value);
	}
	
	public void setBat(int cave, boolean value)
	{
		caveInfo[cave].setBat(value);
	}
	
	public void setWumpus(int cave, boolean value)
	{
		caveInfo[cave].setWumpus(value);
	}
	
	public void setEmpty(int cave, boolean value)
	{
		caveInfo[cave].setEmpty(value);
	}
	
	public void setVisited(int cave)
	{
		caveInfo[cave].setVisited(true);
	}
	
	public void setBreeze(int cave, boolean value)
	{
		caveInfo[cave].setBreeze(value);
	}
	
	public void setSmell(int cave, boolean value)
	{
		caveInfo[cave].setSmell(value);
	}
	
	public void setSound(int cave, boolean value)
	{
		caveInfo[cave].setSound(value);
	}
	
	public void univisitCave(int cave)
	{
		caveInfo[cave].setVisited(false);
	}
	
}

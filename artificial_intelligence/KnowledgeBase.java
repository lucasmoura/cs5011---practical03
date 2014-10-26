package artificial_intelligence;

import game.Cave;
import game.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class KnowledgeBase
{
	
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
			
//			System.out.println("Cave id: "+c.getId());
//			System.out.println("Adjacent size: "+adjacentCaves.size());
//			System.out.println("marked: "+storedCaves[c.getId()]);
			
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
	
	public ArrayList<Premisse> generatePremisses(ArrayList<Cave> visitedCaves)
	{
		ArrayList<Premisse> premisses = new ArrayList<Premisse>();
		
		for(int i =0; i<visitedCaves.size(); i++)
		{
			
			int id = visitedCaves.get(i).getId();
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(id);
			CaveInfo cave = caveInfo[id];
			
			if(cave.hasBat())
				continue;
			
			boolean breeze = cave.feelBreeze();
			boolean sound = cave.feelSound();
			boolean smell = cave.feelSmell();
			
			int[] sensations = new int[3];
			Arrays.fill(sensations, 0);
			int empty = getNumEmptyCaves(breeze, sound, smell);
		    
			System.out.println("Number of empty caves: "+empty);
			
			for(int j =0; j<adjacentCaves.size(); j++)
			{
				int adjacentId = adjacentCaves.get(j).getId();
				CaveInfo adjacentCave = caveInfo[adjacentId];
				int tempEmpty = empty;
				
				if(adjacentCave.isVisited() == true)
				{
					System.out.println("Visisted cave: "+adjacentId);
					Premisse premisse;
					
					switch(Map.getInstance().getCave(adjacentId).getType())
					{
						case Cave.BAT:
							sound = false;
							premisse = new Premisse(adjacentId);
							premisse.setProbability(Cave.BAT);
							premisses.add(premisse);
							break;
						
						case Cave.PIT:
							breeze = false;
						    premisse = new Premisse(adjacentId);
						    premisse.setProbability(Cave.PIT);
						    premisses.add(premisse);
							break;
						
						case Cave.WUMPUS:
							smell = false;
							premisse = new Premisse(adjacentId);
							premisse.setProbability(Cave.WUMPUS);
							premisses.add(premisse);
							break;
					}
					
					System.out.println("Moving to next cave");
					continue;
				}
				
				System.out.println("Adjacent cave: "+adjacentId);
				Premisse premisse = new Premisse(adjacentId);
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
				
				System.out.println(makeInference(id, adjacentId, unknown, sensations));
				
				System.out.println("Stop looking into adjacent nodes");
			
				if(adjacentCave.hasPit() == true)
				{
					premisse.setProbability(Cave.PIT);
					premisses.add(premisse);
					continue;
				}
				else if(breeze == true)
					premisse.setProbability(Cave.PIT);
					
				if(adjacentCave.hasBat() == true)
				{
					premisse.setProbability(Cave.BAT);
					premisses.add(premisse);
					continue;
				}
				else if(sound == true)
					premisse.setProbability(Cave.BAT);
				
				if(adjacentCave.hasWumpus() == true)
				{
					premisse.setProbability(Cave.WUMPUS);
					premisses.add(premisse);
					continue;
				}
				else if(smell == true)
					premisse.setProbability(Cave.WUMPUS);
				
				System.out.println("Total empty caves: "+tempEmpty);
				if(tempEmpty != 0)
				{
					System.out.println("Entrou aqui");
					premisse.setProbability(Cave.EMPTY);
				}
				
				premisses.add(premisse);
			}
			
				
		}
		
		return premisses;
	}
	
	private boolean makeInference(int father, int adjacentId, int unknown, int[] sensations) 
	{
		if(caveInfo[adjacentId].isVisited())
			return false;
		
		boolean breeze = caveInfo[father].feelBreeze();
		boolean sound = caveInfo[father].feelSound();
		boolean smell = caveInfo[father].feelSmell();
		
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

	public int getNumEmptyCaves(boolean breeze, boolean sound, boolean smell)
	{
		int empty = 3;
		
		empty += (breeze == true)?-1:0;
		empty += (sound == true)?-1:0;
		empty += (smell == true)?-1:0;
		
		return empty;
	}

	public int ask(int actualPosition)
	{
		ArrayList<Cave> visitedCaves = createVisitedCaves(actualPosition);
		ArrayList<Premisse> premisses = generatePremisses(visitedCaves);
		
		return 0;
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

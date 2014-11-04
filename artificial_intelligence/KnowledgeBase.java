package artificial_intelligence;

import game.Cave;
import game.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import util.GameOutput;

public class KnowledgeBase
{
	
	public class PremisseComparator implements Comparator<Premisse>
	{		
		@Override
		public int compare(Premisse p1, Premisse p2)
		{
			int[] probability01 = p1.getProbability();
			int[] probability02 = p2.getProbability();
			
			if(probability01[0] == Cave.EMPTY && probability01[1] == -1)
				return -1;
			else if(probability02[0] == Cave.EMPTY && probability02[1] == -1)
				return 1;
			else if(probability01[0] == Cave.WUMPUS && probability01[1] == -1)
				return -1;
			else if(probability02[0] == Cave.WUMPUS && probability02[1] == -1)
				return 1;
			else if(probability01[0] == Cave.WUMPUS && probability01[1] == Cave.EMPTY)
				return -1;
			else if(probability02[0] == Cave.WUMPUS && probability02[1] == Cave.EMPTY)
				return 1;
			else if(probability01[0] == Cave.BAT && probability01[1] == Cave.EMPTY)
				return -1;
			else if(probability02[0] == Cave.BAT && probability02[1] == Cave.EMPTY)
				return 1;
			else if(probability01[0] == Cave.BAT && probability01[1] == Cave.WUMPUS)
				return -1;
			else if(probability02[0] == Cave.BAT && probability02[1] == Cave.WUMPUS)
				return 1;
			else if(probability01[0] == Cave.PIT && probability01[1] == Cave.WUMPUS)
				return -1;
			else if(probability02[0] == Cave.PIT && probability02[1] == Cave.WUMPUS)
				return 1;
			else if(probability01[0] == Cave.PIT && probability01[1] == Cave.BAT &&
					probability01[2] == -1)
				return -1;
			else if(probability02[0] == Cave.PIT && probability02[1] == Cave.BAT &&
					probability02[2] == -1)
				return 1;
			else if(probability01[0] == Cave.PIT && probability01[1] == Cave.EMPTY)
				return -1;
			else if(probability02[0] == Cave.PIT && probability02[1] == Cave.EMPTY)
				return 1;
			else if(probability01[0] == Cave.PIT && probability01[1] == Cave.BAT &&
					probability01[2] == Cave.WUMPUS)
				return -1;
			else if(probability02[0] == Cave.PIT && probability02[1] == Cave.BAT &&
					probability02[2] == Cave.WUMPUS)
				return 1;
			else if(probability01[0] == Cave.BAT && probability01[1] == -1)
				return -1;
			else if(probability02[0] == Cave.BAT && probability02[1] == -1)
				return 1;
			else if(probability01[0] == Cave.PIT && probability01[1] == -1)
				return -1;
			else if(probability02[0] == Cave.PIT && probability02[1] == -1)
				return 1;
			else
				return -1;
			
		}	
	}	
		
	private CaveInfo[] caveInfo;
	private int exitCave;
	private boolean hasTreasure;
	private HashMap<Integer, Integer> exitPath;
	private boolean hasExitPath;
	
	public KnowledgeBase()
	{
		exitCave = -1;
		hasTreasure = hasExitPath = false;
		exitPath = new HashMap<Integer, Integer>();
		
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
	
	public int goToExit(int actualPosition)
	{
		if(hasExitPath)
		{
			Integer target = getExitCave();
			hasExitPath = true;
			
			while (target != null)
			{
			  Integer temp = target;
			  System.out.println(target);
			  target = exitPath.get(target);
			  
			  if(target == actualPosition)
				  return temp;
			  
			}	
			
		}
		
		Queue<Integer> caves = new LinkedList<Integer>();
		caves.add(actualPosition);
		int find = 0;
		
		int[] storedCaves = new int[21];
		Arrays.fill(storedCaves, 0);
		
		while(!caves.isEmpty())
		{
			Integer caveId = caves.poll();
			storedCaves[caveId] = 1;
			
			//System.out.println("Cave visited = "+caveId);
			
			if(caveId == getExitCave())
			{
				//System.out.println("Cave found!! for id: "+caveId);
				find = 1;
				break;
			}	
			
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(caveId);
			
			for(int i =0; i<adjacentCaves.size(); i++)
			{
				int id = adjacentCaves.get(i).getId();
				//System.out.println("Cave "+id+" is visited? "+caveInfo[id].isVisited());
		
				if(caveInfo[id].hasBat() ||
				   caveInfo[id].hasPit() ||	
				   caveInfo[id].hasWumpus() ||
				   (caveInfo[id].isVisited() == false)||
				   storedCaves[id] == 1)
				{
					continue;
				}
				else
				{
					caves.add(id);
					exitPath.put(id, caveId);
				}	
			}
		}
	
		
		if(find == 1)
		{
			Integer target = getExitCave();
			hasExitPath = true;
			
			while (target != null)
			{
			  Integer temp = target;
			  //System.out.println(target);
			  target = exitPath.get(target);
			  
			  if(target == actualPosition)
				  return temp;
			  
			}	
				
		}
		
		return 0;
		
		
		
	}
	
	public int[] bfs(int actualPosition, int location, int father)
	{
		int find, size;
		
		find = size = 0;
		
		if(actualPosition == location)
			find = size = 0;
		else if(caveInfo[actualPosition].hasBat() ||
		   caveInfo[actualPosition].hasPit() ||
		   caveInfo[actualPosition].hasWumpus())
		{
			int[] answer = {-1, size};
			return answer;
		}
		
		Queue<Integer> caves = new LinkedList<Integer>();
		HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
		
		caves.add(actualPosition);
		parentMap.put(actualPosition, father);
		
		int[] storedCaves = new int[21];
		Arrays.fill(storedCaves, 0);
		
		//System.out.println("Actual Position: "+actualPosition + ", Location: "+location);
	
		while(!caves.isEmpty())
		{
			Integer caveId = caves.poll();
			storedCaves[caveId] = 1;
			
			//System.out.println("Cave visited = "+caveId);
			
			if(caveId == location)
			{
				//System.out.println("Cave found!! for id: "+caveId);
				find = 1;
				break;
			}	
			
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(caveId);
			
			for(int i =0; i<adjacentCaves.size(); i++)
			{
				int id = adjacentCaves.get(i).getId();
				//System.out.println("Cave "+id+" is visited? "+caveInfo[id].isVisited());
		
				if(id == location)
				{
					caves.add(id);
					parentMap.put(id, caveId);
				}
				else if(caveInfo[id].hasBat() ||
				   caveInfo[id].hasPit() ||	
				   caveInfo[id].hasWumpus() ||
				   (caveInfo[id].isVisited() == false && id != location)||
				   id == father || 
				   storedCaves[id] == 1)
				{
					
					continue;
				}
				else
				{
					caves.add(id);
					parentMap.put(id, caveId);
				}	
			}
		}
	
		
		if(find == 1)
		{
			Integer target = location;
			
			while (target != null)
			{
			  
			  size++;
			  target = parentMap.get(target);
			  
			}			  
		}
		
		//System.out.println("Path size: "+size);
		int[] value = {find, size};
		
		return value;
	}
	
	public int findFather(int actualPosition, int location, int premisseGenerator)
	{
		
		int size;
		size = 100;
		int father = -1;
		boolean valid = false;
		
		ArrayList<Cave> adjacentCave = Map.getInstance().getAdjacentCaves(actualPosition);
		int []answer = new int[2];
		
		if(premisseGenerator == actualPosition)
			valid = true;
		
		for(int i =0; i<adjacentCave.size(); i++)
		{
			int id = adjacentCave.get(i).getId();
			//System.out.println("Cave id: "+id);
			
			if(caveInfo[id].isVisited() == true || valid)
				answer = bfs(id, location, actualPosition);
			
//			if(actualPosition == 8)
//				System.out.println("For location: " + location +
//					" caveId: "+id+"\nAswer (found: "+answer[0]+", size: "+answer[1]+")\n");
			
			if(answer[0] ==1)
			{
				if(answer[1]<size)
				{
					size = answer[1];
					father = id;
				}
			}
		}
	
		if(father == location)
			father = actualPosition;
		
		return father;
	}

	
	public ArrayList<Premisse> generatePremisses(ArrayList<Cave> visitedCaves, int actualPosition)
	{
		ArrayList<Premisse> premisses = new ArrayList<Premisse>();
		System.out.println("Actual Position: "+actualPosition);
		
		for(int i =0; i<visitedCaves.size(); i++)
		{
			
			int id = visitedCaves.get(i).getId();
			//System.out.println("Cave creating premisse: "+id);
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(id);
			CaveInfo cave = caveInfo[id];
			
			if(cave.hasBat() || cave.hasPit() || cave.hasWumpus())
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
							premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
							premisse.setGenerator(id);
							premisse.setProbability(Cave.BAT);
							premisses.add(premisse);
							break;
						
						case Cave.PIT:
							breeze = false;
							//System.out.println("Add premisse with location: "+adjacentId);
						    premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
						    premisse.setGenerator(id);
						    premisse.setProbability(Cave.PIT);
						    premisses.add(premisse);
							break;
						
						case Cave.WUMPUS:
							smell = false;
							//System.out.println("Add premisse with location: "+adjacentId);
							premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
							premisse.setGenerator(id);
							premisse.setProbability(Cave.WUMPUS);
							premisses.add(premisse);
							break;
					}
					
					//System.out.println("Moving to next cave");
					continue;
				}
				
				//System.out.println("Adjacent cave: "+adjacentId);
				Premisse premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
				premisse.setGenerator(id);
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
				
				makeInference(id, adjacentId, unknown, sensations);
				
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
//		
		if(breeze && sound && !smell && sensations[0] == 1 && sensations[2] == 1 && unknown == 1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			//caveInfo[adjacentId].setVisited(true);
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
			//caveInfo[adjacentId].setVisited(true);
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
		else if(breeze && !sound && smell && sensations[0] == 1 && sensations[1] == 0 && unknown == 1)
		{
			System.out.println("WUMPUS found: "+adjacentId);
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
			//caveInfo[adjacentId].setVisited(true);
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
			System.out.println("WUMPUS found: "+adjacentId);
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
			//caveInfo[adjacentId].setVisited(true);
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
			//caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(smell && sensations[1]==0 && unknown ==1)
		{
			System.out.println("WUMPUS found: "+adjacentId);
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
			//caveInfo[adjacentId].setVisited(true);
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

	public ArrayList<Premisse> ask(int actualPosition)
	{
		ArrayList<Cave> visitedCaves = createVisitedCaves(actualPosition);
		System.out.print("\nVisited cave: ");
		GameOutput.getInstance().writeToFile("\nVisited caves: ");
		
		for(int i =0; i<visitedCaves.size(); i++)
		{
			Cave c = visitedCaves.get(i);
			System.out.print(c.getId());
			GameOutput.getInstance().writeToFile(String.valueOf(c.getId()));
			
			if(i != visitedCaves.size() -1)
			{
				System.out.print(", ");
				GameOutput.getInstance().writeToFile(", ");
			}	
			
		}
		
		System.out.println();
		GameOutput.getInstance().writeToFile("\n");
		
		ArrayList<Premisse> premisses = generatePremisses(visitedCaves, actualPosition);
		ArrayList<Premisse> bestPremisses = new ArrayList<Premisse>();
		orderPremisses(premisses);
		printPremisses(premisses);
		
		Premisse [] removePremisses = new Premisse[premisses.size()];
		int index = 0;
		
		for(Premisse p : premisses)
		{
			if(p.getFather() == -1)
				removePremisses[index++] = p;
		}
		
		for(int i =0; i<removePremisses.length; i++)
			premisses.remove(removePremisses[i]);
		
		bestPremisses.add(premisses.get(0));
		
		for(int i =1; i<premisses.size(); i++)
		{
			int[] probability01 = premisses.get(i-1).getProbability();
			int[] probability02 = premisses.get(i).getProbability();
			
			if(probability01[0] == probability02[0] && probability01[1] == probability02[1] &&
					probability01[2] == probability02[2])
			{
				bestPremisses.add(premisses.get(i));
			}	
			else
				break;
		}
		
//		Premisse bestPremisse =  null;
//		
//		if(premisses.isEmpty() == false)
//			bestPremisse = premisses.get(0);
		//System.out.println("Best premisses size: "+bestPremisses.size());
		return bestPremisses;
	}
	
	private void printPremisses(ArrayList<Premisse> premisses)
	{
		
		System.out.println();
		GameOutput.getInstance().writeToFile("\nDisplaying generated Premisses: \n\n");
		
		for(int i =0; i<premisses.size(); i++)
		{
			System.out.println("Premisse: "+i);
			GameOutput.getInstance().writeToFile("Premisse: "+i+"\n");
			System.out.println("Generator: "+premisses.get(i).getGenerator());
			GameOutput.getInstance().writeToFile("Generator: "+premisses.get(i).getGenerator()+"\n");
			System.out.println("Father: "+premisses.get(i).getFather());
			GameOutput.getInstance().writeToFile("Father: "+premisses.get(i).getFather()+"\n");
			System.out.println("Location: "+premisses.get(i).getLocation());
			GameOutput.getInstance().writeToFile("Location: "+premisses.get(i).getLocation()+"\n");
			System.out.println("Probability: "+Arrays.toString(premisses.get(i).getProbability()));
			GameOutput.getInstance().writeToFile("Probability: "+Arrays.toString(premisses.get(i).getProbability())+"\n");
			System.out.println();
			GameOutput.getInstance().writeToFile("\n");
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
	
	public void setTreasure(boolean value)
	{
		hasTreasure = true;
	}
	
	public boolean getTreasure()
	{
		return hasTreasure;
	}
	
	public void setExit(int caveId)
	{
		exitCave = caveId;
	}
	
	public int getExitCave()
	{
		return exitCave;
	}
	
}

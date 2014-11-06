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
	
	/* 
	 * Class used to parse the premises generated by the knowledge database
	 */
	public class PremisseComparator implements Comparator<Premisse>
	{		
		/*
		 * Method used by parse to sort the ArrayList of premises
		 */
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
		
	// Array of information produced by the logical agent about the caves
	private CaveInfo[] caveInfo;
	//Boolean used to know if the logical agent has already found the exit cave
	private int exitCave;
	//Boolean used to know if the logical agent has already found the treasure
	private boolean hasTreasure;
	//Data strucuture used to store the exit path from the treaseure location to the exit cave
	private HashMap<Integer, Integer> exitPath;
	//Boolean used to know if an exit path has already been created
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
	
	/* Method used to create generate the visited caves by the logical agent. This method used a depth-first search algorithm to locate all visited caves
	 * @params actualPosition: The actual position of the logical agent in the map
	 * @return An ArrayList containing all the visited caves by the logical agent
	 */
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
	
	
	/* Method used for the to move the logical agent to the exit cave. This method is only used if the logical agent already knows the exit cave location
	 * To find the exit path, this method use the breadth-first search algorithm to locate the shortest path between the player actual position and the
	 * exit cave
	 * @params actualPosition: The actual position of the logical agent in the map
	 * @return the position in the exit path the logical agent is supposed to move
	 */
	public int goToExit(int actualPosition)
	{
		
		/*
		 * If the exit path has already been generated, there is not point to go through the dfs algorithm again, it is only necessary to find the
		 * next location the logical agent is supposed to move in order to reach the exit
		 */
		if(hasExitPath)
		{
			Integer target = getExitCave();
			hasExitPath = true;
			
			while (target != null)
			{
			  Integer temp = target;
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
			
			
			if(caveId == getExitCave())
			{
				find = 1;
				break;
			}	
			
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(caveId);
			
			for(int i =0; i<adjacentCaves.size(); i++)
			{
				int id = adjacentCaves.get(i).getId();

				/*
				 * Logic used to see if a cave can be considered a node in the path or not. If the cave has any hazard or have not been visited, it
				 * is not considered a valid node in the exit path
				 */
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
	
		/*
		 * If a path is found, generate the exitPath data structure in order to avoid running the bfs algorithm more than one time to find the exit.
		 */
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
	
	/*
	 * A custom implementation of the breadth-first search algorithm
	 * @param actualPosition: The location of one of the adjacent nodes of the player current positon. For example, if the logical agent is at
	 * position 01, the possible values for actual position are 11, 3 and 2.
	 * @param location: The location the bfs algorithm is trying to find
	 * @param father: The logical agent current position
	 * @return : Return an int array with two positions, one used to know if a path has been found and the size of the path
	 */
	public int[] bfs(int actualPosition, int location, int father)
	{
		int find, size;
		
		find = size = 0;
		
		if(actualPosition == location)
			find = size = 0;
		//if the cave has any hazard, the path cannot start with that cave, therefore the path is invalid
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
		
	
		while(!caves.isEmpty())
		{
			Integer caveId = caves.poll();
			storedCaves[caveId] = 1;
			
			
			if(caveId == location)
			{
				find = 1;
				break;
			}	
			
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(caveId);
			
			for(int i =0; i<adjacentCaves.size(); i++)
			{
				int id = adjacentCaves.get(i).getId();
		
				if(id == location)
				{
					caves.add(id);
					parentMap.put(id, caveId);
				}
				/*
				 * Logic used to see with a cave cannot be used to generate a path. If a cave has any hazard, or is not visited and is not the location
				 * being looked for or if the cave id is the same one as the father or if the cave has already been explored, the cave cannot be considered as a path
				 */
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
	
		/*
		 * If a path is found, calculate the size of the path
		 */
		if(find == 1)
		{
			Integer target = location;
			
			while (target != null)
			{
			  
			  size++;
			  target = parentMap.get(target);
			  
			}			  
		}
		
		int[] value = {find, size};
		
		return value;
	}
	
	/*
	 * Method used to find a cave who is the father of the premise. In other words, generate the best position the logical agent should move in
	 * order to go to the premise location.
	 * @param actualPosition: The logical agent actual position
	 * @param location: The location the premise represents
	 * @param premisseGenerator: The visited cave that is generating the premise
	 * @return the cave id representing the best father for the premise
	 */
	public int findFather(int actualPosition, int location, int premisseGenerator)
	{
		
		int size;
		size = 100;
		int father = -1;
		boolean valid = false;
		
		ArrayList<Cave> adjacentCave = Map.getInstance().getAdjacentCaves(actualPosition);
		int []answer = new int[2];
		
		//Used to guarantee the case that the location is a non visited cave adjacent to the logical agent actual position
		if(premisseGenerator == actualPosition)
			valid = true;
		
		for(int i =0; i<adjacentCave.size(); i++)
		{
			int id = adjacentCave.get(i).getId();
			
			/*
			 * Explore the path for each of the adjacent nodes of the logical agent current position in order to find the best father
			 * for the premise
			 */
			if(caveInfo[id].isVisited() == true || valid)
				answer = bfs(id, location, actualPosition);
			
			if(answer[0] ==1)
			{
				if(answer[1]<size)
				{
					size = answer[1];
					father = id;
				}
			}
		}
	
		//If the father of the premise its the location of the premise, that means that the player actual position is the actual father of the premise
		if(father == location)
			father = actualPosition;
		
		return father;
	}

	/*
	 * Method used to generate premises for every non visited cave or hazard adjacent to an already visited cave
	 * @param visitedCave: An ArrayList containing the visited caves by the logical agent
	 * @param actualPosition: The logical agent actual position
	 * @return : An ArrayList of all the premises generated
	 */
	public ArrayList<Premisse> generatePremisses(ArrayList<Cave> visitedCaves, int actualPosition)
	{
		ArrayList<Premisse> premisses = new ArrayList<Premisse>();
		GameOutput.getInstance().writeToFile("\nActual Position: "+actualPosition+"\n");
		
		for(int i =0; i<visitedCaves.size(); i++)
		{
			
			int id = visitedCaves.get(i).getId();
			ArrayList<Cave> adjacentCaves = Map.getInstance().getChamberEdges(id);
			CaveInfo cave = caveInfo[id];
			
			//If the visited cave is a hazard, no premise can be generated from them
			if(cave.hasBat() || cave.hasPit() || cave.hasWumpus())
				continue;
			
			/*
			 * Extracts the percepts felt by the logical agent and stored in the knowledge database
			 */
			boolean breeze = cave.feelBreeze();
			boolean sound = cave.feelSound();
			boolean smell = cave.feelSmell();
			
			//Int array used to verify the correspondence of the percepts with the knowledge of the adjacent caves
			int[] sensations = new int[3];
			Arrays.fill(sensations, 0);
			int empty = getNumEmptyCaves(breeze, sound, smell);
		    
			/*
			 * For every visited cave, explores its adjacent caves in order to find a non visited cave to generate a premise
			 */
			for(int j =0; j<adjacentCaves.size(); j++)
			{
				int adjacentId = adjacentCaves.get(j).getId();
				CaveInfo adjacentCave = caveInfo[adjacentId];
				int tempEmpty = empty;
				
				/*
				 * If the cave has already been visited, create a premise only if that cave is a hazard to the player
				 * This was done in order for the player to find the wumpus position or a bat position if he needs to
				 */
				if(adjacentCave.isVisited() == true)
				{
					Premisse premisse;
					Cave c = Map.getInstance().getCave(adjacentId);
					
					/*
					 * If any cave with hazard has already been marked as visited, the percepts of the world are also updated. For example,
					 * if the logical agent felt breeze and a pit has been marked as visited, the knowledge base infer that the breeze comes
					 * from that marked pit, and update the boolean variable accordingly
					 */
					
					switch(c.getType())
					{
						case Cave.BAT:
							sound = false;
							premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
							premisse.setGenerator(id);
							premisse.setProbability(Cave.BAT);
							premisses.add(premisse);
							break;
						
						case Cave.PIT:
							breeze = false;
						    premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
						    premisse.setGenerator(id);
						    premisse.setProbability(Cave.PIT);
						    premisses.add(premisse);
							break;
						
						case Cave.WUMPUS:
							smell = false;
							premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
							premisse.setGenerator(id);
							premisse.setProbability(Cave.WUMPUS);
							premisses.add(premisse);
							break;
					}
					
					continue;
				}
				
				//If a non visited cave is found, create a premise for it
				Premisse premisse = new Premisse(adjacentId, findFather(actualPosition, adjacentId, id));
				premisse.setGenerator(id);
				
				/*
				 * This piece of code is used to create the variable used to make inference. It loops through all adjacent caves to the visited cave
				 * and looks at the information about all of them. For example, if there is one cave that its type is unknown, and the other two types
				 * are empty and pit, and the logical agent only feel breeze at the current position, therefore it can be infered that the unkown cave
				 * is empty. This piece of code set the number of unknown caves and the sensations related to the current visited cave
				 */
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
				
				
				//Method used to create inference with the knowledge the logical agent currently has
				makeInference(id, adjacentId, unknown, sensations);
				

				/*
				 * This next piece of code is used to set if the probabilities of the premise. If the inference has stated something about
				 * the cave current type, the first if acts upon it. However, if no inference was made, the percepts of the world are added to the
				 * premise probability. This pattern is repeated for the pit, bat and wumpus respectively.
				 */
				
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

				//If there is a possible empty cave nearby, given the world percepts, actualize the premise probability
				if(tempEmpty != 0)
				{
					premisse.setProbability(Cave.EMPTY);
				}
				
				premisses.add(premisse);
			}
			
				
		}
	
		return premisses;
	}
	
	/*
	 * Method used to generate inference over the the currently informations found on the knowledge base
	 * @param father: the visited cave generating the premise
	 * @param adjacentId: the current cave the inference is being done
	 * @param unknown: The number of unknown adjacent caves for the current visited cave
	 * @param sensations: the actualized percepts about the current visited cave
	 * @return a boolean variable that is true if a inference was generated and false otherwise
	 */
	public boolean makeInference(int father, int adjacentId, int unknown, int[] sensations) 
	{
		if(caveInfo[adjacentId].isVisited())
			return false;
		
		boolean breeze = caveInfo[father].feelBreeze();
		boolean sound = caveInfo[father].feelSound();
		boolean smell = caveInfo[father].feelSmell();
		
		/*
		 * This method looks thorough all the stimated possibilities that coould lead to a inference. For example,
		 * if the logical agent felt breeze and soung and there still one unknown cave. However, the caves containing a 
		 * pit and a bat has already been discovered, therefore the left cave can only be empty.
		 */
	
		if(breeze && sound && !smell && sensations[0] == 1 && sensations[2] == 1 && unknown == 1)
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(true);
			return true;
		}
		else if(breeze && sound && !smell && sensations[0] == 0 && sensations[2] == 1 && unknown == 1)//
		{
			caveInfo[adjacentId].setPit(true);
			caveInfo[adjacentId].setBat(false);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(breeze && sound && !smell && sensations[0] == 1 && sensations[2] == 0 && unknown == 1)//
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
			return true;
		}
		else if(breeze && !sound && smell && sensations[0] == 0 && sensations[1] == 1 && unknown == 1)//
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
			return true;
		}
		else if(!breeze && sound && smell && sensations[2] == 0 && sensations[1] == 1 && unknown == 1)//
		{
			caveInfo[adjacentId].setPit(false);
			caveInfo[adjacentId].setBat(true);
			caveInfo[adjacentId].setWumpus(false);
			caveInfo[adjacentId].setEmpty(false);
			caveInfo[adjacentId].setVisited(true);
			return true;
		}
		else if(!breeze && sound && smell && sensations[2] == 1 && sensations[1] == 0 && unknown == 1)//
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
	
	/*
	 * Method used to order the premises according to the rules stablished on the class PremisseComparator
	 * @param premisses: The ArrayList containing all the generated premises
	 */
	public void orderPremisses(ArrayList<Premisse> premisses)
	{
		Collections.sort(premisses, new PremisseComparator());
	}

	/*
	 * Method used to count the number of empty caves given the actual percepts of the world
	 * @param breeze: If the cave has breeze associated with it
	 * @param sound: If the cave has sound associated with it
	 * @param smell: If the cave has smell associated with it
	 * @return :  The number of empty caves found
	 */
	public int getNumEmptyCaves(boolean breeze, boolean sound, boolean smell)
	{
		int empty = 3;
		
		empty += (breeze == true)?-1:0;
		empty += (sound == true)?-1:0;
		empty += (smell == true)?-1:0;
		
		return empty;
	}

	/*
	 * Method used to generate the best premises for the logical agent
	 * @param actualPosition: The player actual position
	 * @return : An ArrayList containing all the best premises found
	 */
	public ArrayList<Premisse> ask(int actualPosition)
	{
		//Retrieve the caves visited by the logical agent
		ArrayList<Cave> visitedCaves = createVisitedCaves(actualPosition);
		System.out.print("\nVisited cave: ");
		GameOutput.getInstance().writeToFile("\nVisited caves: ");
		
		//Write to the game output the visited caves by the player
		for(int i =0; i<visitedCaves.size(); i++)
		{
			Cave c = visitedCaves.get(i);
			GameOutput.getInstance().writeToFile(String.valueOf(c.getId()));
			
			if(i != visitedCaves.size() -1)
				GameOutput.getInstance().writeToFile(", ");
			
		}
		
		GameOutput.getInstance().writeToFile("\n");
		
		//Retrieve all the possible premises for the logical agent visited caves
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
		
		//Remove all the premises with father value equals to -1. That means that there is not possible path from the logical agent current position
		//to the premise location
		for(int i =0; i<removePremisses.length; i++)
			premisses.remove(removePremisses[i]);
		
		/*
		 * This next piece of code is used to select premises with equal probability values. These premises are the ones considered the
		 * best ones
		 */
		
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
		
		return bestPremisses;
	}
	
	/*
	 * Method used to print the premises
	 * @param premisses: The premises generated by the knowledge base
	 */
	private void printPremisses(ArrayList<Premisse> premisses)
	{
		
		System.out.println();
		GameOutput.getInstance().writeToFile("\nDisplaying generated Premisses: \n\n");
		
		for(int i =0; i<premisses.size(); i++)
		{
			GameOutput.getInstance().writeToFile("Premisse: "+i+"\n");
			GameOutput.getInstance().writeToFile("Generator: "+premisses.get(i).getGenerator()+"\n");
			GameOutput.getInstance().writeToFile("Father: "+premisses.get(i).getFather()+"\n");
			GameOutput.getInstance().writeToFile("Location: "+premisses.get(i).getLocation()+"\n");
			GameOutput.getInstance().writeToFile("Probability: "+Arrays.toString(premisses.get(i).getProbability())+"\n");
			GameOutput.getInstance().writeToFile("\n");
		}
		
	}
	
	/*
	 * Method used to verify if a particular cave has a wumpus
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if there is or not a wumpus at that position
	 */
	public boolean hasWumpus(int cave)
	{
		return caveInfo[cave].hasWumpus();
	}
	
	/*
	 * Method used to verify if a particular cave has a bat
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if there is or not a bat at that position
	 */
	public boolean hasBat(int cave)
	{
		return caveInfo[cave].hasBat();
	}
	
	/*
	 * Method used to verify if a particular cave is empty
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if the cave is empty or not
	 */
	public boolean isEmpty(int cave)
	{
		return caveInfo[cave].isEmpty();
	}
	
	/*
	 * Method used to verify if a particular cave has already been visited
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if the cave is visited or not
	 */
	public boolean isVisited(int cave)
	{
		return caveInfo[cave].isVisited();
	}
	
	/*
	 * Method used to verify if a particular cave has a breeze associated with it
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if there is or not a breeze at that position
	 */
	public boolean feelBreeze(int cave)
	{
		return caveInfo[cave].feelBreeze();
	}
	
	/*
	 * Method used to verify if a particular cave has a smell associated with it
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if there is or not a smell at that position
	 */
	public boolean feelSmell(int cave)
	{
		return caveInfo[cave].feelSmell();
	}
	
	/*
	 * Method used to verify if a particular cave has a sound associated with it
	 * @param cave:  The cave id to look for
	 * @return: a boolean variable stating if there is or not a sound at that position
	 */
	public boolean feelSound(int cave)
	{
		return caveInfo[cave].feelSound();
	}
	
	/* Method used to set a pit on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has a pit or not
	 */
	public void setPit(int cave, boolean value)
	{
		caveInfo[cave].setPit(value);
	}
	
	/* Method used to set a bat on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has a bat or not
	 */
	public void setBat(int cave, boolean value)
	{
		caveInfo[cave].setBat(value);
	}
	
	/* Method used to set a wumpus on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has a wumpus or not
	 */
	public void setWumpus(int cave, boolean value)
	{
		caveInfo[cave].setWumpus(value);
	}
	
	/* Method used to set an empty location on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has an empty location or not
	 */
	public void setEmpty(int cave, boolean value)
	{
		caveInfo[cave].setEmpty(value);
	}
	
	/* Method used to set a specific cave as visited
	 * @param cave: he cave id to look for
	 */
	public void setVisited(int cave)
	{
		caveInfo[cave].setVisited(true);
	}
	
	/* Method used to set a breeze on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has a breeze or not
	 */
	public void setBreeze(int cave, boolean value)
	{
		caveInfo[cave].setBreeze(value);
	}
	
	/* Method used to set a smell on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has a smell or not
	 */
	public void setSmell(int cave, boolean value)
	{
		caveInfo[cave].setSmell(value);
	}
	
	/* Method used to set a sound on a specific cave
	 * @param cave: he cave id to look for
	 * @param value: If the cave has a sound or not
	 */
	public void setSound(int cave, boolean value)
	{
		caveInfo[cave].setSound(value);
	}
	
	/* Method used to set a specific cave as non visited, used for test purposes
	 * @param cave: he cave id to look for
	 */
	public void univisitCave(int cave)
	{
		caveInfo[cave].setVisited(false);
	}

	/* Method used to set a specific cave as containing the treasure
	 * @param cave: he cave id to look for
	 */
	public void setTreasure(boolean value)
	{
		hasTreasure = true;
	}
	
	/* Method used to see if the logical agent has already collected the treasure
	 * return : a boolean variable stating if the player has already collected the treasure or not
	 */
	public boolean getTreasure()
	{
		return hasTreasure;
	}
	
	/*
	 * Method used to set a specific cave as the exit position
	 * @param: The cave that contains the exit
	 */
	public void setExit(int caveId)
	{
		exitCave = caveId;
	}
	
	/*
	 * Method used to retrive the exit cave location
	 * return: the exit cave location
	 */
	public int getExitCave()
	{
		return exitCave;
	}
	
}

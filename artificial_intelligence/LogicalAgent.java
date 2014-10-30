package artificial_intelligence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import game.Cave;
import game.Map;
import game.Player;

public class LogicalAgent implements Player
{
	
	private final int ALIVE = 0;
	private final int DEAD = 1;
	private final int BAT = 2;
	private final int WON = 3;
	
	public static final int GAMEOVER = -1;
	public static final int CONTINUE = 0;
	public static final int VICTORY = 1;
	
	private int pastPosition;
	private int position;
	private int status;
	private boolean breeze;
	private boolean smell;
	private boolean squish;
	
	private KnowledgeBase knowledgeBase;
	
	public LogicalAgent(int position)
	{
		this.position = position;
		this.pastPosition = -1;
		status = ALIVE;
		breeze = smell = squish = false;
		knowledgeBase = new KnowledgeBase();
	}

	@Override
	public int pickUpTreasure() 
	{
		return 0;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public int getStatus()
	{
		return status;
	}

	@Override
	public int move() 
	{
		
		ArrayList<Cave> adjacentCaves = Map.getInstance().getAdjacentCaves(position);
		int[] caves = new int[3];
		
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			caves[i] = adjacentCaves.get(i).getId();
			if(adjacentCaves.get(i).hasBat())
				squish = true;
			else if(adjacentCaves.get(i).hasPit())
				breeze = true;
			else if(adjacentCaves.get(i).hasWumpus())
				smell = true;
			
		}
		
		checkPositionStatus();
		breeze = smell = squish = false;
		
		if(status == DEAD)
			return GAMEOVER;
		else if(status == BAT)
		{
			System.out.println("Player has encountered a Bat!!");
			position = generateNewPosition();
			System.out.println("Player new position is: "+position);
			return CONTINUE;
		}
		else if(status == WON)
			return VICTORY;
		
		ArrayList<Premisse> premisses = knowledgeBase.ask(position);
		ArrayList<Premisse> advancePremisses = new ArrayList<Premisse>();
		Premisse bestPremisse = null;
		
		if(premisses.size() == 1)
			bestPremisse = premisses.get(0);
		
		
		else if(premisses.get(0).getProbability()[0] == Cave.EMPTY)
		{
			for(int i =0; i<premisses.size(); i++)
			{
				if(premisses.get(i).getFather() == pastPosition)
					continue;
				else
				{
					bestPremisse = premisses.get(i);
					break;
				}
			}
		}
		else
		{
			System.out.println("Entrou aqui "+premisses.size());
			System.out.println("Cave 0: "+caves[0]);
			System.out.println("Cave 1: "+caves[1]);
			System.out.println("Cave 2: "+caves[2]);
			
			for(int i =0; i<premisses.size(); i++)
			{
				if(premisses.get(i).getLocation() == caves[0] || 
				   premisses.get(i).getLocation() == caves[1] ||
				   premisses.get(i).getLocation() == caves[2])
				{
					advancePremisses.add(premisses.get(i));
				}
			}
			
			System.out.println("Advance size: "+advancePremisses.size());
			Random rand = new Random();
			
			if(advancePremisses.size() > 0)
				bestPremisse = advancePremisses.get(rand.nextInt(advancePremisses.size()));
		}
		
		if(bestPremisse==null)
		{
			Random rand = new Random();
			bestPremisse = premisses.get(rand.nextInt(premisses.size()));
		}
		
		int value = checkForWumpus(bestPremisse);
		
		if(value == -1)
		{
			status = DEAD;
			return GAMEOVER;
		}
		else if(value == 0 || value == -2)
			return CONTINUE;
		
		boolean validPosition = false;
		
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			if(bestPremisse.getLocation() == adjacentCaves.get(i).getId())
			{
				pastPosition = position;
				position = bestPremisse.getLocation();
				validPosition = true;
				break;
			}	
				
		}
		
		if(!validPosition)
		{
			pastPosition = position;
			position = bestPremisse.getFather();
		}
		
		System.out.println("Player has moved to the following position: "+position);
		
		return CONTINUE;
		
//		ArrayList<Cave> visited = new ArrayList<Cave>();
//		
//		for(int i =0; i<adjacentCaves.size(); i++)
//		{
//			int caveIndex = adjacentCaves.get(i).getId();
//			
//			if(adjacentCaves.get(i).hasBat())
//				squish = true;
//			else if(adjacentCaves.get(i).hasPit())
//				breeze = true;
//			else if(adjacentCaves.get(i).hasWumpus())
//				smell = true;
//			
//			if(knowledgeBase.isVisited(caveIndex) == true)
//				visited.add(Map.getInstance().getCave(caveIndex));
//		}
//		
//		checkPositionStatus();
//		
//		if(status == DEAD)
//			return GAMEOVER;
//		else if(status == BAT)
//		{
//			System.out.println("Player has encountered a Bat!!");
//			position = generateNewPosition();
//			System.out.println("Player new position is: "+position);
//			return CONTINUE;
//		}
//		else if(status == WON)
//			return VICTORY;
//		
//		position = createBestMove(adjacentCaves, visited);
//		
//		System.out.println("Player has moved to the following position: "+position);
//		breeze = smell = squish = false;
//		
//		return CONTINUE;
	}

	private void checkPositionStatus()
	{
		Cave cave  = Map.getInstance().getCave(position);
		
		switch(cave.getType())
		{
			case Cave.EMPTY:
				status = ALIVE;
				knowledgeBase.setVisited(cave.getId());
				knowledgeBase.setEmpty(cave.getId(), true);
				knowledgeBase.setBat(cave.getId(), false);
				knowledgeBase.setPit(cave.getId(), false);
				knowledgeBase.setWumpus(cave.getId(), false);
				knowledgeBase.setBreeze(cave.getId(), breeze);
				knowledgeBase.setSmell(cave.getId(), smell);
				knowledgeBase.setSound(cave.getId(), squish);
				break;
		
			case Cave.PIT:
			case Cave.WUMPUS:
				status = DEAD;
				break;
			
			case Cave.TREASURE:
				status = WON;
				break;
			
			case Cave.BAT:
				status = BAT;
				knowledgeBase.setVisited(cave.getId());
				knowledgeBase.setEmpty(cave.getId(), false);
				knowledgeBase.setBat(cave.getId(), true);
				knowledgeBase.setPit(cave.getId(), false);
				knowledgeBase.setWumpus(cave.getId(), false);
				knowledgeBase.setBreeze(cave.getId(), breeze);
				knowledgeBase.setSmell(cave.getId(), smell);
				knowledgeBase.setSound(cave.getId(), squish);
				break;
		}
		
	}
	
	public int generateNewPosition()
	{
		Random rand = new Random();
		boolean valid = false;
		int position;
		
		do
		{
			position = rand.nextInt(20)+1;
			int id =Map.getInstance().getCave(position).getType();
			
			if( id== Cave.PIT || id == Cave.WUMPUS || id == Cave.BAT)
				valid = false;
			else
				valid = true;
			
		}while(valid == false);
		
		return position;
	}

	@Override
	public int shootArrow(int caveId) 
	{
		Cave cave = Map.getInstance().getCave(caveId);
		
		if(cave.getType() == Cave.WUMPUS)
		{
			System.out.println("Player has killed the wumpus");
			knowledgeBase.setVisited(cave.getId());
			knowledgeBase.setEmpty(cave.getId(), true);
			knowledgeBase.setBat(cave.getId(), false);
			knowledgeBase.setPit(cave.getId(), false);
			knowledgeBase.setWumpus(cave.getId(), false);
			knowledgeBase.setSmell(position, false);
			cave.setType(Cave.EMPTY);
			return 0;
		}	
		else
		{
			if(moveWumpus(cave) == position)
				return -1;
			
		}	
		
		return -2;
	}
	
	public int checkForWumpus(Premisse premisse)
	{
		if(premisse==null)
			return -3;
		
		int[] probability = premisse.getProbability();
		
		if(probability[0] == Cave.WUMPUS )
		{
			return shootArrow(premisse.getLocation());
		}
		else if(probability[0] == Cave.PIT && probability[1] == Cave.WUMPUS)
		{
			int value = shootArrow(premisse.getLocation());
			
			if(value == -2)
			{
				knowledgeBase.setVisited(premisse.getLocation());
				knowledgeBase.setEmpty(premisse.getLocation(), false);
				knowledgeBase.setBat(premisse.getLocation(), false);
				knowledgeBase.setPit(premisse.getLocation(), true);
				knowledgeBase.setWumpus(premisse.getLocation(), false);
				return -2;
			}
			else
				return value;
		}
		
		return -4;
	}
	
	public int moveWumpus(Cave cave)
	{
		Random rand = new Random();
		int move = rand.nextInt(3);
		
		ArrayList<Cave> movements = Map.getInstance().getChamberEdges(cave.getId());
		movements.get(move).setType(Cave.WUMPUS);
		
		cave.setType(Cave.EMPTY);
				
		return movements.get(move).getId();
	}

//	if(availableMovements.size() == 3)
//	{	
//		movement = rand.nextInt(3);
//		return availableMovements.get(movement).getId();
//	}
//	else if(availableMovements.size() == 2)
//	{
//		movement = rand.nextInt(2);
//		
//		if(breeze == true)
//		{
//			ArrayList<Cave> pastCave = Map.getInstance().getChamberEdges(visitedCaves.get(0).getId());
//			boolean back = true;
//			
//			for(int i =0; i<pastCave.size(); i++)
//			{
//				if(pastCave.get(i).hasPit())
//					back = false;
//			}
//			
//			if(back)
//				return visitedCaves.get(0).getId();
//			else if(smell == true)
//			{
//				Cave cave = availableMovements.get(movement);
//				return attackWumpus(movement, cave);
//			}
//			else
//				return availableMovements.get(movement).getId();
//				
//		}
//		else if(smell == true)
//		{
//			Cave cave = availableMovements.get(movement);
//			return attackWumpus(movement, cave);
//		}
//		else if(breeze == true && squish == true)
//			return availableMovements.get(movement).getId();
//		else if(smell == true && squish == true)
//			return availableMovements.get(movement).getId();
//		else
//			return availableMovements.get(movement).getId();
//			
//	}
//	else
//		return availableMovements.get(0).getId();
	
}

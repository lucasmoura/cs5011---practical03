package artificial_intelligence;

import java.util.ArrayList;
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
	
	private int position;
	private int status;
	private boolean breeze;
	private boolean smell;
	private boolean squish;
	
	private KnowledgeBase knowledgeBase;
	
	public LogicalAgent(int position)
	{
		this.position = position;
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
		ArrayList<Cave> visited = new ArrayList<Cave>();
		
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			int caveIndex = adjacentCaves.get(i).getId();
			
			if(adjacentCaves.get(i).hasBat())
				squish = true;
			else if(adjacentCaves.get(i).hasPit())
				breeze = true;
			else if(adjacentCaves.get(i).hasWumpus())
				smell = true;
			
			if(knowledgeBase.isVisited(caveIndex) == true)
				visited.add(Map.getInstance().getCave(caveIndex));
		}
		
		checkPositionStatus();
		
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
		
		position = createBestMove(adjacentCaves, visited);
		
		System.out.println("Player has moved to the following position: "+position);
		breeze = smell = squish = false;
		
		return CONTINUE;
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
	
	private int createBestMove
		(ArrayList<Cave> adjacentCaves, ArrayList<Cave> visited)
	{
		
		Random rand = new Random();
		int movement;
		
		if(visited.size() == 0)
		{
			movement = rand.nextInt(3)+1;
			return adjacentCaves.get(movement).getId();
		}
		
		
		if(breeze == true || smell == true)
		{
			
		}
		
		return 1;
	}
	
	public int generateNewPosition()
	{
		Random rand = new Random();
		boolean valid = false;
		int position;
		
		do
		{
			position = rand.nextInt(20)+1;
			
			if(Map.getInstance().getCave(position).getType() == Cave.PIT)
				valid = false;
			else
				valid = true;
			
		}while(valid == false);
		
		return position;
	}

	@Override
	public boolean shootArrow(Cave cave) 
	{
		if(cave.getType() == Cave.WUMPUS)
		{
			System.out.println("Player has succesfully killed the Wumpus");
			return true;
		}	
		else
		{
			moveWumpus(cave);
			return false;
		}	
	}
	
	public int attackWumpus(int movement, Cave cave)
	{
		boolean kill = shootArrow(cave);
		
		if(kill == false)
			return position;
		else
			return cave.getId();
	}
	
	public void moveWumpus(Cave cave)
	{
		Random rand = new Random();
		int move = rand.nextInt(3);
		
		ArrayList<Cave> movements = Map.getInstance().getChamberEdges(cave.getId());
		movements.get(move).setType(Cave.WUMPUS);
		
		cave.setType(Cave.EMPTY);
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

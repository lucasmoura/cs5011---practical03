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
	
	private HashMap<Integer, Boolean> visitedCaves;
	
	public LogicalAgent(int position)
	{
		this.position = position;
		status = ALIVE;
		breeze = smell = squish = false;
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
		
		ArrayList<Cave> adjacentCaves =
				Map.getInstance().getChamberEdges(position);
		
		ArrayList<Cave> availableMovements = new ArrayList<Cave>();
		ArrayList<Cave> visitedCaves = new ArrayList<Cave>();
		
		breeze = smell = squish = false;
		
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			if(adjacentCaves.get(i).hasBat())
				squish = true;
			else if(adjacentCaves.get(i).hasPit())
				breeze = true;
			else if(adjacentCaves.get(i).hasWumpus())
				smell = true;
			
			if(adjacentCaves.get(i).isVisited() == false)
				availableMovements.add(adjacentCaves.get(i));
			else
				visitedCaves.add(adjacentCaves.get(i));
		}
		
		position = createBestMove(availableMovements, visitedCaves);
		
		System.out.println("Player has moved to the following position: "+position);
		
		return CONTINUE;
	}

	private void checkPositionStatus()
	{
		Cave cave  = Map.getInstance().getCave(position);
		
		switch(cave.getType())
		{
			case Cave.EMPTY:
				status = ALIVE;
				cave.markAsVisited();
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
				break;
		}
		
		breeze = smell = squish = false;
		
	}
	
	private int createBestMove
		(ArrayList<Cave> availableMovements, ArrayList<Cave> visitedCaves)
	{
		
		Random rand = new Random();
		int movement;
		
		if(availableMovements.size() == 3)
		{	
			movement = rand.nextInt(3);
			return availableMovements.get(movement).getId();
		}
		else if(availableMovements.size() == 2)
		{
			movement = rand.nextInt(2);
			
			if(breeze == true)
			{
				ArrayList<Cave> pastCave = Map.getInstance().getChamberEdges(visitedCaves.get(0).getId());
				boolean back = true;
				
				for(int i =0; i<pastCave.size(); i++)
				{
					if(pastCave.get(i).hasPit())
						back = false;
				}
				
				if(back)
					return visitedCaves.get(0).getId();
				else if(smell == true)
				{
					Cave cave = availableMovements.get(movement);
					return attackWumpus(movement, cave);
				}
				else
					return availableMovements.get(movement).getId();
					
			}
			else if(smell == true)
			{
				Cave cave = availableMovements.get(movement);
				return attackWumpus(movement, cave);
			}
			else if(breeze == true && squish == true)
				return availableMovements.get(movement).getId();
			else if(smell == true && squish == true)
				return availableMovements.get(movement).getId();
			else
				return availableMovements.get(movement).getId();
				
		}
		else
			return availableMovements.get(0).getId();
		
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

	
}

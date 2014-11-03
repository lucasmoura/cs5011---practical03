package artificial_intelligence;

import java.util.ArrayList;
import java.util.Random;

import util.GameOutput;

import game.Cave;
import game.Map;
import game.Player;

public class LogicalAgent implements Player
{
	
	private final int ALIVE = 0;
	private final int DEAD = 1;
	private final int BAT = 2;
	private final int TREASURE = 3;
	private final int WON = 4;
	
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
	private boolean searchExit;
	
	public LogicalAgent(int position)
	{
		this.position = position;
		this.pastPosition = -1;
		status = ALIVE;
		searchExit = breeze = smell = squish = false;
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
		{
			System.out.println("The player has died...");
			GameOutput.getInstance().writeToFile("\nThe player has died...\n");
			return GAMEOVER;
		}
		else if(status == WON)
		{
			GameOutput.getInstance().writeToFile("\nThe player has won the game \\o/ !!!!n");
			return VICTORY;
		}
		else if(status == BAT)
		{
			System.out.println("\nPlayer has encountered a Bat!!!\n");
			GameOutput.getInstance().writeToFile("\nPlayer has encountered a Bat!!!\n");
			
			position = generateNewPosition();
			System.out.println("Player new position is: "+position);
			GameOutput.getInstance().writeToFile("\nPlayer new position is: "+position+"\n");
			
			return CONTINUE;
		}
		else if(status == TREASURE && !searchExit)
		{
			System.out.println("Player has collected the Treasure!!!");
			GameOutput.getInstance().writeToFile("\nPlayer has collected the Treasure!!!\n");
			GameOutput.getInstance().writeToFile("\nPlayer will start looking for the exit\n");
			searchExit = true;
			status = ALIVE;
			return CONTINUE;
		}
		
		if(searchExit && knowledgeBase.getExitCave() != -1)
		{
			System.out.println("Player is moving to the exit..");
			position = knowledgeBase.goToExit(position);
			GameOutput.getInstance().writeToFile("\nPlayer is moving to the exit: "+position+ "\n");
			
			if(position == knowledgeBase.getExitCave())
			{
				status = WON;
				System.out.println("Player has reached the exit!");
				return VICTORY;
			}
			else
			{
				status = ALIVE;
				return CONTINUE;
			}
		}
		
		ArrayList<Premisse> premisses = knowledgeBase.ask(position);
		ArrayList<Premisse> advancePremisses = new ArrayList<Premisse>();
		Premisse bestPremisse = null;
		
		if(premisses.size() == 1)
			bestPremisse = premisses.get(0);
		
		
		else if(premisses.get(0).getProbability()[0] == Cave.EMPTY)
		{
			for(int i =0; i<premisses.size(); i++)
			{
				if(premisses.get(i).getFather() == pastPosition || premisses.get(i).getLocation() == pastPosition)
					continue;
				else if(premisses.get(i).getFather() == caves[0] || premisses.get(i).getLocation() == caves[0] || 
						premisses.get(i).getFather() == caves[1] || premisses.get(i).getLocation() == caves[1] ||
						premisses.get(i).getFather() == caves[2] || premisses.get(i).getLocation() == caves[2])
					
				{
					advancePremisses.add(premisses.get(i));
				}
			}
			
			if(advancePremisses.size()==0)
			{
				GameOutput.getInstance().writeToFile("Entrou aqui");
				GameOutput.getInstance().writeToFile("\nPast Position: "+pastPosition+"\n");
				bestPremisse = premisses.get(0);
			}
			else
			{
				Random rand = new Random();
				bestPremisse = advancePremisses.get(rand.nextInt(advancePremisses.size()));
			}
		}
		else
		{
			
			for(int i =0; i<premisses.size(); i++)
			{
				if(premisses.get(i).getLocation() == caves[0] || 
				   premisses.get(i).getLocation() == caves[1] ||
				   premisses.get(i).getLocation() == caves[2])
				{
					advancePremisses.add(premisses.get(i));
				}
			}
			
			//System.out.println("Advance size: "+advancePremisses.size());
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
			if(bestPremisse.getLocation() == adjacentCaves.get(i).getId() &&
					bestPremisse.getLocation() != pastPosition)
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
		GameOutput.getInstance().writeToFile("\nPlayer has moved to the following position: "+position+"\n");
		
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
				
				if(cave.isExit() && knowledgeBase.getTreasure() == true)
					status = WON;
				else if(cave.isExit())
					knowledgeBase.setExit(cave.getId());
				
				break;
		
			case Cave.PIT:
			case Cave.WUMPUS:
				status = DEAD;
				break;
			
			case Cave.TREASURE:
				knowledgeBase.setTreasure(true);
				knowledgeBase.setVisited(cave.getId());
				knowledgeBase.setEmpty(cave.getId(), true);
				knowledgeBase.setBat(cave.getId(), false);
				knowledgeBase.setPit(cave.getId(), false);
				knowledgeBase.setWumpus(cave.getId(), false);
				knowledgeBase.setBreeze(cave.getId(), breeze);
				knowledgeBase.setSmell(cave.getId(), smell);
				knowledgeBase.setSound(cave.getId(), squish);
				status = TREASURE;
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
		
		System.out.println("Player will attack the wumpus at position: "+cave.getId());
		GameOutput.getInstance().writeToFile("\nPlayer will attack the wumpus at position: "+cave.getId()+"\n");
		
		if(cave.getType() == Cave.WUMPUS)
		{
			System.out.println("Player has killed the wumpus");
			GameOutput.getInstance().writeToFile("Player has killed the wumpus\n");
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
			System.out.println("Player has missed the wumpus :(");
			GameOutput.getInstance().writeToFile("Player has missed the wumpus :(\n");
			if(moveWumpus() == position)
				return -1;
			
		}	
		
		return -2;
	}
	
	public int checkForWumpus(Premisse premisse)
	{
		if(premisse==null)
			return -3;
		
		int[] probability = premisse.getProbability();
		
		if(premisse.getFather() != position)
			return -3;
		
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
			{	
				return value;
			}
		}
		
		return -4;
	}
	
	public int moveWumpus()
	{
		Random rand = new Random();
		int move = rand.nextInt(3);
		int wumpusPosition = -1;
		
		ArrayList<Cave> movements = Map.getInstance().getChamberEdges(position);
		
		for(Cave cave: movements)
		{
			if(cave.getType() == Cave.WUMPUS)
			{
				wumpusPosition = cave.getId();
				break;
			}	
		}
		
		if(wumpusPosition != -1)
			movements = Map.getInstance().getChamberEdges(wumpusPosition);
		
		movements.get(move).setType(Cave.WUMPUS);
		knowledgeBase.setSmell(position, false);
		
		Map.getInstance().getCave(wumpusPosition).setType(Cave.EMPTY);
		
		System.out.println("Wumpus has moved to the following position: "+movements.get(move).getId());
		GameOutput.getInstance().writeToFile("\nWumpus has moved to the following position: "
				+movements.get(move).getId()+"\n");
				
		return movements.get(move).getId();
	}
	
}

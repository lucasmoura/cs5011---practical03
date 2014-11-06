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
	
	//Logical agent past position
	private int pastPosition;
	//Logical agent current positiom
	private int position;
	//Logical agent status
	private int status;
	//Boolean used to represent if  the logical agent is perceiving a breeze
	private boolean breeze;
	//Boolean used to represent if  the logical agent is perceiving a smell
	private boolean smell;
	//Boolean used to represent if  the logical agent is perceiving a sound
	private boolean sound;
	//Boolean used to represent if the logical agent has killed the wumpus
	private boolean killWumpus;
	//Boolean used to represent if the logical agent has stumbled upon a bat
	private boolean batAttack;
	
	//The logical agent knowledge database
	private KnowledgeBase knowledgeBase;
	//Boolean used to represent if the player is currently searching for an exit
	private boolean searchExit;
	
	public LogicalAgent(int position)
	{
		this.position = position;
		this.pastPosition = -1;
		status = ALIVE;
		batAttack = killWumpus = searchExit = breeze = smell = sound = false;
		knowledgeBase = new KnowledgeBase();
	}
	
	/*
	 * Method used to retrieve the logical agent current position
	 * @return : The logical agent current position
	 */
	public int getPosition()
	{
		return position;
	}
	
	/*
	 * Method used to retrieve the logical agent current status
	 * @return: The logical agent current status
	 */
	public int getStatus()
	{
		return status;
	}

	/*
	 * Method used to move the logical agent position through the game world. This method is also responsible for the actions the player can do in the
	 * world, such as shooting an arrow or pick up the treasure
	 * @return: The player status after he has moved.
	 */
	@Override
	public int move() 
	{
		
		ArrayList<Cave> adjacentCaves = Map.getInstance().getAdjacentCaves(position);
		int[] caves = new int[3];
		
		/*
		 * Generate the logical agent actual percepts on the cave he is currently standing
		 */
		for(int i =0; i<adjacentCaves.size(); i++)
		{
			caves[i] = adjacentCaves.get(i).getId();
			if(adjacentCaves.get(i).hasBat())
			{
				sound = true;
				GameOutput.getInstance().writeToFile("\nThe player" +
						" has listend to a rustling sound coming from a nearby cave!\n");
			}
			else if(adjacentCaves.get(i).hasPit())
			{
				breeze = true;
				GameOutput.getInstance().writeToFile("\nThe player" +
						" has felt a breeze coming from a nearby cave!\n");
			}	
			else if(adjacentCaves.get(i).hasWumpus())
			{
				smell = true;
				GameOutput.getInstance().writeToFile("\nThe player" +
						" has smelled a terrible stench coming from a nearby cave!\n");
			}
		}
		
		//Check it position status and updates the knowledge base if possible
		checkPositionStatus();
		breeze = smell = sound = false;
		
		/*
		 * Check the player status in order to see what action he can do
		 */
		if(status == DEAD)
		{
			GameOutput.getInstance().writeToFile("\nThe player has died...\n");
			return GAMEOVER;
		}
		else if(status == WON)
		{
			GameOutput.getInstance().writeToFile("\nThe player has won the game \\o/ !!!!n");
			return VICTORY;
		}
		/*
		 * If the logical agent encounters a bat, he will be transported to a random empty cave in the game
		 */
		else if(status == BAT)
		{
			GameOutput.getInstance().writeToFile("\nPlayer has encountered a Bat!!!\n");
			
			position = generateNewPosition();
			GameOutput.getInstance().writeToFile("\nPlayer new position is: "+position+"\n");
			
			return CONTINUE;
		}
		/*
		 * If the logical agent has found the treasure, the logical agent will start looking for the exit cave
		 */
		else if(status == TREASURE && !searchExit)
		{
			GameOutput.getInstance().writeToFile("\nPlayer has collected the Treasure!!!\n");
			GameOutput.getInstance().writeToFile("\nPlayer will start looking for the exit\n");
			searchExit = true;
			status = ALIVE;
			return CONTINUE;
		}
		
		/*
		 * If the logical agent is looking for the exit and it has already found the exit cave, it will generate a path
		 * to the exit cave and follow it.
		 */
		if(searchExit && knowledgeBase.getExitCave() != -1)
		{
			position = knowledgeBase.goToExit(position);
			GameOutput.getInstance().writeToFile("\nPlayer is moving to the exit: "+position+ "\n");
			
			if(position == knowledgeBase.getExitCave())
			{
				status = WON;
				GameOutput.getInstance().writeToFile("\nPlayer has reached the exit!\n");
				return VICTORY;
			}
			else
			{
				status = ALIVE;
				return CONTINUE;
			}
		}
		
		//Get the best premises from the knowledge database
		ArrayList<Premisse> premisses = knowledgeBase.ask(position);
		ArrayList<Premisse> advancePremisses = new ArrayList<Premisse>();
		Premisse bestPremisse = null;
		
		if(premisses.size() == 1)
			bestPremisse = premisses.get(0);
		
		/*
		 * if the premises are all of empty caves, try locating the most near one. If that is not possible randomly pick an
		 * empty cave. This has the same behaviour for other caves, with the exeption that the past position is not checked.
		 */
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
			
			Random rand = new Random();
			
			if(advancePremisses.size() > 0)
				bestPremisse = advancePremisses.get(rand.nextInt(advancePremisses.size()));
		}
		
		if(bestPremisse==null)
		{
			Random rand = new Random();
			bestPremisse = premisses.get(rand.nextInt(premisses.size()));
		}
		
		//After selecting the best premise, check to see if there is a wumpus in its probability and acts accordinly
		int value = checkForWumpus(bestPremisse);
		
		//check the consequences of the premise in case there is a wumpus in its probability
		if(value == -1)
		{
			status = DEAD;
			return GAMEOVER;
		}
		else if(value == 0 || value == -2)
			return CONTINUE;
		
		boolean validPosition = false;
		
		//If the best premise location is one of the nearby adjacent caves of the logical agent current position and the location is different
		//from the logical agent past position, move to the premise location
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
		
		//If the location of the premise cannot be reached, move to its father
		if(!validPosition)
		{
			pastPosition = position;
			position = bestPremisse.getFather();
		}
		
		GameOutput.getInstance().writeToFile("\nPlayer has moved to the following position: "+position+"\n");
		
		return CONTINUE;
		
	}

	/*
	 * Method used to update both player status and knowledge base
	 */
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
				knowledgeBase.setSound(cave.getId(), sound);
			
				//If the player has already found the treasure and has located the exit cave, the logical agent has won the game
				if(cave.isExit() && knowledgeBase.getTreasure() == true)
					status = WON;
				else if(cave.isExit())
				{
					GameOutput.getInstance().writeToFile("\nPlayer has found the exit cave, but not the treasure....yet\n");
					knowledgeBase.setExit(cave.getId());
				}
				
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
				knowledgeBase.setSound(cave.getId(), sound);
				status = TREASURE;
				break;
			
			case Cave.BAT:
				status = BAT;
				batAttack = true;
				knowledgeBase.setVisited(cave.getId());
				knowledgeBase.setEmpty(cave.getId(), false);
				knowledgeBase.setBat(cave.getId(), true);
				knowledgeBase.setPit(cave.getId(), false);
				knowledgeBase.setWumpus(cave.getId(), false);
				knowledgeBase.setBreeze(cave.getId(), breeze);
				knowledgeBase.setSmell(cave.getId(), smell);
				knowledgeBase.setSound(cave.getId(), sound);
				break;
		}
		
	}
	
	/*
	 * Method used in case the logica agent enters a cave containing a bat
	 * @return: the random empty cave position the bat has dropped the logical
	 */
	public int generateNewPosition()
	{
		Random rand = new Random();
		boolean valid = false;
		int position;
		
		//Guarantees that the logical agent has been dropped in a empty cave
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

	/*
	 * Method used for the logical agent to shoot an arrow at a cave
	 * @param caveId; The cave id the player will shoot the arrow
	 * @return : an int value showing if the player has killed the wumpus or has died in the process or if just missed the wumpus
	 */
	@Override
	public int shootArrow(int caveId) 
	{
		
		Cave cave = Map.getInstance().getCave(caveId);
		
		GameOutput.getInstance().writeToFile("\nPlayer will attack the wumpus at position: "+cave.getId()+"\n");
		
		//If the player kills the wumpus, the knowledge base is actualized and the actual world is actualized too, since the wumpus is no more
		if(cave.getType() == Cave.WUMPUS)
		{
			GameOutput.getInstance().writeToFile("\nPlayer has killed the wumpus\n");
			killWumpus = true;
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
			GameOutput.getInstance().writeToFile("Player has missed the wumpus :(\n");
			//If the wumpus move to the logical agent position, the agent will die
			if(moveWumpus() == position)
				return -1;
			
		}	
		
		return -2;
	}
	
	/*
	 * Method used to check if the premise has a wumpus probability and act accordingly
	 * @param premisse: The premise the logical agent will use to move
	 * @return : an int value that states an invalid premise or that the player has killed the wumpus or died in the process or will continue
	 * in the game or has missed the wumpus.
	 */
	public int checkForWumpus(Premisse premisse)
	{
		/*
		 * The player will only try to shoot the wumpus for the following probabilites:
		 * (3, -1, -1)
		 * (3, 0, -1)
		 * (1, 2, 3)
		 * (1, 3, -1)
		 */
		
		if(premisse==null)
			return -3;
		
		int[] probability = premisse.getProbability();
		
		if(premisse.getFather() != position)
			return -3;
		
		if(probability[0] == Cave.WUMPUS || probability[2] == Cave.WUMPUS )
		{
			return shootArrow(premisse.getLocation());
		}
		else if(probability[0] == Cave.PIT && probability[1] == Cave.WUMPUS)
		{
			int value = shootArrow(premisse.getLocation());
			
			//If the player miss the wumpus, it can infer that the cave he shooted at contains a cave and update the knowledge base accordingly
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
	
	/*
	 * Method used to move the wumpus in case the logical agent miss its shoot
	 * @return: The wumpus new position
	 */
	public int moveWumpus()
	{
		Random rand = new Random();
		int move = rand.nextInt(3);
		int wumpusPosition = -1;
		boolean valid = false;
		
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
		
		do
		{
			//Guarantess that the wumpus will only move to an empty cave
			if(movements.get(move).getType() == Cave.BAT || movements.get(move).getType() == Cave.PIT)
				move = rand.nextInt(3);
			else
				valid = true;
			
		}
		while(!valid);
		
		movements.get(move).setType(Cave.WUMPUS);
			
		knowledgeBase.setSmell(position, false);
		
		Map.getInstance().getCave(wumpusPosition).setType(Cave.EMPTY);
		
		GameOutput.getInstance().writeToFile("\nWumpus has moved to the following position: "
				+movements.get(move).getId()+"\n");
				
		return movements.get(move).getId();
	}

	/*
	 * Method used to know if the player has killed the wumpus
	 * @return: A boolean variable if the player has already killed the wumpus or not
	 */
	public boolean hasKillWumpus()
	{
		return killWumpus;
	}

	/*
	 * Method used to know if the player has entered on the same cave where a bat resides
	 * @return: A boolean variable if the player has entered on the same cave where a bat resides
	 */
	public boolean hasBatAttack()
	{
		return batAttack;
	}

	
	
	
}

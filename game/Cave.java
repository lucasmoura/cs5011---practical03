package game;

public class Cave
{
	public static final int EMPTY = 0;
	public static final int PIT = 1;
	public static final int BAT = 2;
	public static final int WUMPUS = 3;
	public static final int TREASURE = 4;
	
	private int type;
	private int id;
	
	public Cave(int id, int type)
	{
		this.id = id;
		this.type = type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getId()
	{
		return id;
	}
	
	public boolean hasPit()
	{
		return type==PIT?true:false;
	}
	
	public boolean hasWumpus()
	{
		return type==WUMPUS?true:false;
	}
	
	public boolean hasBat()
	{
		return type==BAT?true:false;
	}
	
	public boolean hasTreasure()
	{
		return type==TREASURE?true:false;
	}

	
}

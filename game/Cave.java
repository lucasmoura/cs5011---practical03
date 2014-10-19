package game;

public class Cave
{
	int type;
	int id;
	
	public Cave(int id, int type)
	{
		this.id = id;
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
	
}

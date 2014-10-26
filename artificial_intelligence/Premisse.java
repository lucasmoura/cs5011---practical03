package artificial_intelligence;

import java.util.Arrays;

public class Premisse
{
	private int[] probability;
	private int location;
	
	public Premisse(int location)
	{
		this.location = location;
		probability = new int[3];
		Arrays.fill(probability, -1);
	}
	
	public boolean setProbability(int value)
	{
		for(int i =0; i<probability.length; i++)
		{
			if(probability[i] == -1)
			{
				probability[i] = value;
				return true;
			}	
		}
		
		return false;
	}

	public int getLocation()
	{
		return location;
	}

	public void setLocation(int location) 
	{
		this.location = location;
	}

	public int[] getProbability()
	{
		return probability;
	}

	public void setProbability(int[] probability) 
	{
		this.probability = probability;
	}
	
	
	
}

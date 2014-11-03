package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GameOutput 
{
	
	private static GameOutput instance = null;
	private BufferedWriter writer;
	
	private GameOutput()
	{
		writer = null;
	}
	
	public void init()
	{
		
		
		try
		{
			FileWriter fileWriter = new FileWriter("game_output.txt");
		    writer = new BufferedWriter(fileWriter);
		}
		catch ( IOException e)
		{
		}
		finally
		{
		    
		}
	}
	
	public static GameOutput getInstance()
	{
		if(instance == null)
			instance = new GameOutput();
		
		return instance;
	}
	
	public void writeToFile(String text)
	{
		try
		{
			writer.write(text);		
		}
		catch(IOException e)
		{
			
		}
	}
	
	public void close()
	{
		if(writer != null)
		{
			try 
			{
				writer.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}	
	}

}

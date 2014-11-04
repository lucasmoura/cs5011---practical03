package util;

import game.Map;

public class Util 
{
	
	public static String convertType(int id)
	{
		if(Map.getInstance().getCave(id).isExit())
			return "X";
		
		int type = Map.getInstance().getCave(id).getType();
		
			switch(type)
			{
				case 0:
					return "E";
				case 1:
					return "P";
				case 2:
					return "B";
				case 3:
					return "W";
				case 4:
					return "T";
			}
			
			return null;
	}

}

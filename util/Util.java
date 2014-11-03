package util;

public class Util 
{
	
	public static String convertType(int type)
	{
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
				case 5:
					return "X";
			}
			
			return null;
	}

}

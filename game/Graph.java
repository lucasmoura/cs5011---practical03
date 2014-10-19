package game;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph
{

	private HashMap< Integer, ArrayList<Cave>> vertexMap;
	private Cave[] caves;
	
	private static int VERTEX_ALREADY_IN_GRAPH = -2;
	private static int VERTEX_ADDED_TO_GRAPH = 0;
	private static int VERTEX_NOT_FOUND = -1;
	private static int EDGE_CREATED_SUCCESSFULLY = 0;
	
	public Graph()
	{
		vertexMap = new HashMap<Integer, ArrayList<Cave>>();
		caves = new Cave[21];
	}
	
	public void printGraph()
	{
		
		System.out.println("Printing graph structure...");
		
		for (Integer caveId: vertexMap.keySet()) 
		{
			
			System.out.println("Cave: "+ caveId);
			System.out.println("Type: "+caves[caveId].getType());
			System.out.println("Total Edges: "+vertexMap.get(caveId).size());
			
			for (Cave cave : vertexMap.get(caveId)) 
			{
				int id = cave.getId();
				System.out.println(caveId + "-->" + id);
			}
			
			System.out.println();
		}
	}
	
	public int getNumVertex()
	{
		return vertexMap.size();
	}
	
	public int addVertex(int id, int type)
	{
		
		if(vertexMap.get(id) != null)
			return VERTEX_ALREADY_IN_GRAPH;
		
		
		ArrayList<Cave> edge = new ArrayList<Cave>();
		
		caves[id] = new Cave(id, type);
		vertexMap.put(id, edge);
		
		
		return VERTEX_ADDED_TO_GRAPH;
		
	}
	
	public ArrayList<Cave> getVertex(int id)
	{
		
		return vertexMap.get(id);
	}
	
	public int addEdge(int from, int to)
	{
		
		ArrayList<Cave> edgeFrom = vertexMap.get(from);
		
		if(edgeFrom == null)
			return VERTEX_NOT_FOUND;
		
		edgeFrom.add(caves[to]);		
	
		return EDGE_CREATED_SUCCESSFULLY;
		
	}
	
	
	
}

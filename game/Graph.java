package game;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph
{

	private HashMap< Integer, ArrayList<Cave>> vertexMap;
	
	private static int INVALID_VERTEX_VALUE = -1;
	private static int VERTEX_ALREADY_IN_GRAPH = -2;
	private static int VERTEX_ADDED_TO_GRAPH = 0;
	private static int VERTEX_NOT_FOUND = -1;
	private static int EDGE_COST_UPGRADED = 1;
	private static int EDGE_CREATED_SUCCESSFULLY = 0;
	private static int EDGE_NOT_UPGRADED = -1;
	
	public Graph()
	{
		vertexMap = new HashMap<Integer, ArrayList<Cave>>();
	}
	
	public void printGraph()
	{
		
		System.out.println("Printing graph structure...");
		System.out.println(vertexMap.size());
		
		for (Integer musicId: vertexMap.keySet()) 
		{
			System.out.println("Entrou aqui");
			
			System.out.println("Music: "+ musicId);
			System.out.println("Total Edges: "+vertexMap.get(musicId).size());
			
			for (Cave cave : vertexMap.get(musicId)) 
			{
				//Long id = edge.getMusicId();
				//Integer cost = edge.getCost();
				
				//System.out.println(musicId + "-->" + id + ", Cost= "+cost );
			}
		}
	}
	
	public int getNumVertex()
	{
		return vertexMap.size();
	}
	
	public int addVertex(int id)
	{
		
		if(vertexMap.get(id) != null)
			return VERTEX_ALREADY_IN_GRAPH;
		
		
		ArrayList<Cave> edge = new ArrayList<Cave>();
		
		vertexMap.put(id, edge);
		
		return VERTEX_ADDED_TO_GRAPH;
		
	}
	
	public ArrayList<Cave> getVertex(int id)
	{
		
		ArrayList<Cave> edge =vertexMap.get(id);
		
		/*if(edge != null)
			Collections.sort(edge);*/
		
		return edge;
	}
	
	public int addEdge(int from, int to)
	{
		
		ArrayList<Cave> edgeFrom = vertexMap.get(from);
		ArrayList<Cave> edgeTo = vertexMap.get(to);
		
		if(edgeFrom == null)
			return VERTEX_NOT_FOUND;
		
		if(edgeTo == null)
			return VERTEX_NOT_FOUND;
		
		
		edgeFrom.add(new Cave());		
	
		return EDGE_CREATED_SUCCESSFULLY;
		
	}
	
	
	
}

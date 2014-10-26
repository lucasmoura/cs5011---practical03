package artificial_intelligence;

public class CaveInfo 
{
	
	private boolean hasPit;
	private boolean hasWumpus;
	private boolean hasBat;
	private boolean hasTreasure;
	private boolean isEmpty;
	private boolean visited;
	private boolean feelBreeze;
	private boolean feelSmell;
	private boolean feelSound;
	
	public CaveInfo()
	{
		hasBat = hasWumpus = hasPit = isEmpty = hasTreasure = false;
		feelBreeze = feelSmell = feelSound = visited = false;
	}

	public boolean hasPit() {
		return hasPit;
	}

	public void setPit(boolean hasPit) {
		this.hasPit = hasPit;
	}

	public boolean hasWumpus() {
		return hasWumpus;
	}

	public void setWumpus(boolean hasWumpus) {
		this.hasWumpus = hasWumpus;
	}

	public boolean hasBat() {
		return hasBat;
	}

	public boolean feelBreeze() {
		return feelBreeze;
	}

	public void setBreeze(boolean feelBreeze) 
	{
		this.feelBreeze = feelBreeze;
	}

	public boolean feelSmell()
	{
		return feelSmell;
	}

	public void setSmell(boolean feelSmell) {
		this.feelSmell = feelSmell;
	}

	public boolean feelSound() {
		return feelSound;
	}

	public void setSound(boolean feelSound)
	{
		this.feelSound = feelSound;
	}

	public void setBat(boolean hasBat) {
		this.hasBat = hasBat;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean hasTreasure()
	{
		return hasTreasure;
	}
	
	public void setTreasure(boolean treasure)
	{
		hasTreasure = treasure;
	}
	
}

public class Ship
{
	public char orientation;
	public int x,y;
	public boolean[] pile;

	public Ship(int x,int y,char o)
	{
		this.orientation = o;
		this.x = x;
		this.y = y;

		pile = new boolean[3];
		for(int i=0;i<3;i++)
			pile[i] = false;
	}

	public boolean isLost()
	{
		for(int i=0;i<3;i++)
		{
			if(pile[i] == false)
				return false;
		}
		return true;
	}

	public void addToPile(int x)
	{
		pile[x] = true;
	}
}
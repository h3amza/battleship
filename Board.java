public class Board
{
	public char[][] coordinates;
	public int height,width;
	public Ship[] ships = new Ship[3];

	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				coordinates[i][j] = '~';
			}
		}
	}

	public void clear()
	{
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				coordinates[i][j] = '~';
			}
		}
		for(int i=0;i<3;i++)
		{
			ships[i]=null;
		}
	}

	public void isHit(Ship ship){}

	public boolean setShip(Ship ship){return true;}

	public void attack(int row,int column){}

	public void drawBoard(){}

	public boolean isLost()
	{
		for(int i=0;i<3;i++)
		{
			if(ships[i].equals('X'))
				return false;
		}
		return true;
	}
}
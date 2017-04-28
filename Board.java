
public class Board 
{
	public char[][] Coords;
	private int height, width;
	
	public Board()
	{
		this.Coords = new char[5][5];
		this.width = 5;
		this.height = 5;
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				Coords[i][j] = '~';
			}
		}
	}
	
	public boolean isEmpty(int x, int y)
	{
		if(Coords[x][y] == '~')
			return true;
		else
			return false;
	}

	public boolean isOnBounds(Ship ship)
	{
		if(ship.orientation)
		{
			if((ship.x+ship.length)>5)
				return false;
		}
		else
		{
			if((ship.x+ship.length)>5)
				return false;
		}
		if(ship.x>=0 && ship.x<=4 && ship.y>=0 && ship.y<=4)
			return true;
		else
			return false;
	}
	public boolean setShip(Ship ship)
	{
		if(isOnBounds(ship))
		{
			int i;
			for(i=0;i<ship.length;i++)
			{
				if(ship.orientation)
				{
					Coords[ship.x][ship.y + i] = 'O';
				}
				else
				{
					Coords[ship.x + i][ship.y] = 'O';
				}
			}
			return true;	
		}
		else
			return false;
	}
	public boolean attack(int x, int y)
	{
		if(!(Coords[x][y]=='~'))
		{
			if(Coords[x][y] == 'O')
				Coords[x][y] = 'X';
			if(Coords[x][y] == '~')
				Coords[x][y] = '*';
		}
		else
		{
			return false;
		}
		return true;
	}

	public void printBoard()
	{
		System.out.println(" Y 0 1 2 3 4");
		System.out.println("X");
		for(int i=0;i<width;i++)
		{
			System.out.print((i)+"  ");
			for(int j=0;j<height;j++)
			{
				System.out.print(Coords[i][j]+" ");
			}
			System.out.println();
		}
	}
	public void markHit(int X, int Y)
	{
		Coords[X][Y] = 'X';
	}
	public void markMiss(int X, int Y)
	{
		Coords[X][Y] = '*';
	}
	
	public boolean lost()
	{
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(Coords[i][j] == 'O')
					return false;
			}
		}
		return true;
	}
}

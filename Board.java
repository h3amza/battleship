/*
* This class defines the game board and related function
*/

class Board
{
	private char[][] Coords; // game grid
	private final int height;
    private final int width; // can be used if dynamic grid selected going forward
	
	public Board()
	{
		this.Coords = new char[5][5]; // currently a 5x5 grid
		this.width = 5;
		this.height = 5;
		for(int i=0;i<5;i++) 
		{
			for(int j=0;j<5;j++)
			{
				Coords[i][j] = '~'; // fill with water on initialization
			}
		}
	}
	
	// check if grid empty or not
    private boolean isEmpty(int x, int y)
	{
        return Coords[x][y] == '~';
	}

	// given a ship object, check if ship is in bounds or not
    private boolean isOnBounds(Ship ship)
	{
		if(ship.orientation) // if horizontal, add length and check
		{
			if((ship.x+ship.length)>5)
				return false;
		}
		else
		{
			if((ship.x+ship.length)>5)
				return false;
		}
        return ship.x >= 0 && ship.x <= 4 && ship.y >= 0 && ship.y <= 4;
	}

	// given a ship object, set up ship on board
	// fill grid with O based on ship size and orientation
	public boolean setShip(Ship ship)
	{
		if(isOnBounds(ship)) // check if in bounds
		{
			int i;
			char[][] temp = Coords.clone();
			for(int x = 0; x < temp.length; x++) {
				Coords[x] = Coords[x].clone();
			}
			for(i=0;i<ship.length;i++)
			{
				if(ship.orientation)
				{
					if(isEmpty(ship.x,ship.y + i))
						temp[ship.x][ship.y + i] = 'O';
					else
						return false;
				}
				else
				{
					if(isEmpty(ship.x + i, ship.y))
						temp[ship.x + i][ship.y] = 'O';
					else
						return false;
				}
			}
			Coords = temp;
			return true;	
		}
		else
			return false;
	}

	// update grid value based on hit or miss
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

	// display board
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

	// marks enemy's board to be hit
	// used when enemy board is displayed
	public void markHit(int X, int Y)
	{
		Coords[X][Y] = 'X';
	}

	// same as mark hit but for miss
	public void markMiss(int X, int Y)
	{
		Coords[X][Y] = '*';
	}
	
	// if no Os then player has lost
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

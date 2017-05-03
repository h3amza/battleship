/*
* This class defines a Ship that is placed on the board
* Only useful as it is easier to implement as a separate class
* based on the usage of orientation and future works with 
* dynamic ship size
*/

public class Ship
{
	public final int length;
	public final int x;
	public final int y;
	public final boolean orientation;
	public Ship(int x, int y, boolean o)
	{
		this.x = x;
		this.y = y;
		this.orientation = o;
		this.length = 2;
	}
}

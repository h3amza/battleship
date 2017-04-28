import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.InterruptedException;

public class Client implements Runnable 
{

	public static Socket clientSocket = null;
	public static PrintStream outputStream = null;
	public static BufferedReader inputStream = null;
	public static BufferedReader input = null;

	public static Board myBoard, enemyBoard;
	public static String inputString;
	public static String response;
	public static boolean gameMode = false;
	public static boolean end = false;
	public static String enemyPlayer = "";
	public static boolean inGame = false;
	public static int playerTurn = 0;
	public static final int port = 7654;
	public static final String hostname = "localhost";

	public static void main(String[] args) 
	{
		myBoard = new Board();
		enemyBoard = new Board();
		
		try 
		{
			clientSocket = new Socket(hostname, port);
			input = new BufferedReader(new InputStreamReader(System.in));
			outputStream = new PrintStream(clientSocket.getOutputStream());
			inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("Something went wrong");
		}
		catch (IOException e) 
		{
			System.out.println("Something went wrong");
		}
		
		try 
		{
			System.out.println("PREPARE FOR BATTLESHIP");
			new Thread(new Client()).start();
			while (!end) 
			{
				if(gameMode==false)
				{
					inputString = input.readLine();
					
					gameMode = true;
					if(enemyPlayer.equals("Player2"))
						outputStream.println("|setup " + enemyPlayer);
				}
				else
				{
					for(int i=0;i<2;i++)
					{
						setupBoard();
					}
					System.out.println("\n\n\n\n\n");
					System.out.println("My Board:");
					System.out.println("");
					myBoard.printBoard();
					if(playerTurn==0)
						playerTurn=1;
					outputStream.println("|ready " + enemyPlayer);
					System.out.println("Press Enter to begin");
					input.readLine();

					while(inGame==false)
					{
						System.out.println("Wait!");
						try
						{
							Thread.sleep(1000);
						}
						catch(InterruptedException e)
						{
							System.out.println("Something went wrong");
						}
					}

					while(inGame)
					{
						System.out.println("\n\n\n\n\n");
						int X,Y;
						if(playerTurn==1) // P1 turn
						{
							attack();
						}
						if(playerTurn==2)
						{
							System.out.println("My Board:\n");
							myBoard.printBoard();
							System.out.println("Wait for the attack!");
						}
						input.readLine();
					}
				}
			}
			outputStream.close();
			inputStream.close();
			clientSocket.close();
		} 
		catch (IOException e) 
		{
			System.out.println("IOException:  " + e);
	    }
		
	  }
	
	public static void attack()
	{
		int X,Y;
		try
		{
			System.out.println("Enemy Board:\n");
			enemyBoard.printBoard();
			System.out.println("");

			System.out.println("Attack Coordinates:");
			System.out.print("X:");
			X = Integer.parseInt(input.readLine());
			
			System.out.print("Y:");
			Y = Integer.parseInt(input.readLine());
			
			outputStream.println("|attack " + enemyPlayer + " " + (Integer.toString(X)+Integer.toString(Y)));
		}
		catch(IOException e)
		{
			System.out.println("Something went wrong");
		}
	}
	public static void setupBoard()
	{
		int X,Y;
		String Orientation;
		Ship temp;
		System.out.println("Set up board..");
		System.out.println("You can set up 2 ships of size 2");
		System.out.println("~ is water, O is ship, * is missed attack, X is hit attack");
		System.out.println("Enter X and Y coordinates(0-4)");
		myBoard.printBoard();
		try
		{
			System.out.println("Give X value:");
			X = Integer.parseInt(input.readLine());
			
			System.out.println("Give Y value:");
			Y = Integer.parseInt(input.readLine());
			
			System.out.println("Do you want the Ship to be horizontal? (y/n)");
			Orientation = input.readLine();
			if(Orientation.equals("y"))
				temp = new Ship(X,Y,true);
			else
				temp = new Ship(X,Y,false);
			myBoard.setShip(temp);
		}
		catch(IOException e)
		{
			System.out.println("Something went wrong");
		}
	}

	  public void run() 
	  {
		  try 
		  {
			  while ((response = inputStream.readLine()) != null) 
			  {
				  if (response.startsWith("Challenge"))
				  {
					  enemyPlayer = "Player2";
				  }
				  if (response.contains("Game On"))
				  {
					  enemyPlayer = "Player1";
					  gameMode = true;
				  }
				  if (response.contains("Player Ready"))
				  {
					  if(playerTurn==0)
						  playerTurn=2;
					  inGame=true;
				  }
				  if (response.contains("hit"))
				  {
					  String[] attributes = response.split(" ",8);
					  String CoordX = String.valueOf(attributes[7].charAt(0));
					  String CoordY = String.valueOf(attributes[7].charAt(1));
					  response="You Hit users ship on Coords " + CoordX + " " + CoordY + "\nPress Enter and wait for attack!";
					  enemyBoard.markHit(Integer.parseInt(CoordX), Integer.parseInt(CoordY));
					  enemyBoard.attack(Integer.parseInt(CoordX), Integer.parseInt(CoordY));
					  System.out.println("\n\n\n\n\n");
					  System.out.println("Enemy Board:");
					  System.out.println("");
					  enemyBoard.printBoard();
					  playerTurn=2;
				  }
				  if (response.contains("miss"))
				  {
					  String[] attributes = response.split(" ",6);
					  String CoordX = String.valueOf(attributes[5].charAt(0));
					  String CoordY = String.valueOf(attributes[5].charAt(1));
					  response="You miss! \nPress Enter and wait for attack!";
					  enemyBoard.markMiss(Integer.parseInt(CoordX), Integer.parseInt(CoordY));
					  enemyBoard.attack(Integer.parseInt(CoordX), Integer.parseInt(CoordY));
					  System.out.println("\n\n\n\n\n");
					  System.out.println("Enemy Board:");
					  System.out.println("");
					  enemyBoard.printBoard();
					  playerTurn=2;
				  }
				  if (response.startsWith("--- You Win!"))
				  {
					  playerTurn=0;
					  gameMode=false;
					  inGame=false;
					  enemyPlayer="";
					  System.out.println("\n\n\n\n\n");
				  }
				  if (response.startsWith("--- Attack from User "))
				  {
					  String[] attributes = response.split(" ",8);
					  String CoordX = String.valueOf(attributes[7].charAt(0));
					  String CoordY = String.valueOf(attributes[7].charAt(1));
					  boolean isAttack = myBoard.attack(Integer.parseInt(CoordX), Integer.parseInt(CoordY));
					  if(isAttack)
					  {
						  outputStream.println("|hit " + enemyPlayer + " " + (CoordX+CoordY));
					  }
					  else
					  {
						  outputStream.println("|miss " + enemyPlayer + " " + (CoordX+CoordY));
					  }
					  System.out.println("\n\n\n\n\n");
					  System.out.println("My Board:\n");
					  myBoard.printBoard();
					  response = "--- Attack from User " + enemyPlayer + " on Coordinates " + CoordX + " " + CoordY;
					  System.out.println(response);
					  if(myBoard.lost())
					  {
						  response="You Lost!\nPress Enter to leave";
						  playerTurn=0;
						  gameMode=false;
						  inGame=false;
						  outputStream.println("|winner " + enemyPlayer);
					  }
					  else
					  {
						  response="Hit Enter to Attack!";
						  playerTurn=1;
					  }
				  }
				  System.out.println(response);
			  }
			  end = true;
		  } 
		  catch (IOException e) 
		  {
			  System.out.println("IOException:  " + e);
		  }
	  }
}
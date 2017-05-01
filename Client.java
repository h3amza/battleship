/*
* This class defines Client side of the code that creates 2 threads for each player
*/


import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.InterruptedException;

public class Client implements Runnable 
{

	// network related objects
	public static Socket clientSocket = null;
	public static PrintStream outputStream = null;
	public static BufferedReader inputStream = null;
	public static BufferedReader input = null;

	public static Board myBoard, enemyBoard; // p1 p2 boards
	public static String inputString; // string var for user input
	public static String response; // server response
	public static boolean gameMode = false; // setup mode or not
	public static boolean end = false; // game ended or not. main while loop
	public static String enemyPlayer = ""; // player name, always P2
	public static boolean inGame = false; // in game or not
	public static int playerTurn = 0; // toggle between players for turn checking
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
			System.out.println("PREPARE FOR BATTLESHIP"); // interaction with player
			new Thread(new Client()).start(); // start client thread
			while (!end) 
			{
				if(gameMode==false) // if not game mode, waiting for player2, set up game and begin
				{
					inputString = input.readLine();
					
					gameMode = true;
					if(enemyPlayer.equals("Player2"))
						outputStream.println("|setup " + enemyPlayer); // start game
				}
				else
				{
					for(int i=0;i<2;i++) // currently number of ships is 2 so run twice
					{
						setupBoard();
					}
					System.out.println("\n\n\n\n\n");
					System.out.println("My Board:");
					System.out.println("");
					myBoard.printBoard();	// print board
					if(playerTurn==0)
						playerTurn=1;
					outputStream.println("|ready " + enemyPlayer); // once boar set, send ready request
					System.out.println("Press Enter to begin");
					input.readLine();

					while(inGame==false) // not your turn, wait for other player
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
							attack(); // attack
						}
						if(playerTurn==2) // or wait to be attacked
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
	
	// get coordinates and attack enemy board
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
			
			outputStream.println("|attack " + enemyPlayer + " " + (Integer.toString(X)+Integer.toString(Y))); // send server request for attack
		}
		catch(IOException e)
		{
			System.out.println("Something went wrong");
		}
	}

	// get coordinates for each ship to set up on board
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

	// thread invokes this function
	  public void run() 
	  {
		  try 
		  {
			  while ((response = inputStream.readLine()) != null) // if getting a response from server
			  {
				  if (response.startsWith("Challenge")) // if this, set up game
				  {
					  enemyPlayer = "Player2";
				  }
				  if (response.contains("Game On")) // if this you are P2
				  {
					  enemyPlayer = "Player1";
					  gameMode = true;
				  }
				  if (response.contains("Player Ready")) // if ready, start game
				  {
					  if(playerTurn==0)
						  playerTurn=2;
					  inGame=true;
				  }
				  if (response.contains("hit"))	// if this, then you hit enemy ship
				  {
					  String[] attributes = response.split(" ",8);
					  String CoordX = String.valueOf(attributes[7].charAt(0));
					  String CoordY = String.valueOf(attributes[7].charAt(1));
					  response="You Hit users ship on Coords " + CoordX + " " + CoordY + "\nPress Enter and wait for attack!";
					  enemyBoard.markHit(Integer.parseInt(CoordX), Integer.parseInt(CoordY)); // mark enemy board for attack
					  enemyBoard.attack(Integer.parseInt(CoordX), Integer.parseInt(CoordY));
					  System.out.println("\n\n\n\n\n");
					  System.out.println("Enemy Board:");
					  System.out.println("");
					  enemyBoard.printBoard();
					  playerTurn=2; // toggle turn
				  }
				  if (response.contains("miss")) // if this, then you miss enemy ship
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
					  playerTurn=2; // toggle turn
				  } 
				  if (response.startsWith("--- You Win!")) // end of game
				  {
					  playerTurn=0;
					  gameMode=false;
					  inGame=false;
					  enemyPlayer="";
					  System.out.println("\n\n\n\n\n");
				  }
				  if (response.startsWith("--- Attack from User ")) // attack enemy 
				  {
					  String[] attributes = response.split(" ",8);
					  String CoordX = String.valueOf(attributes[7].charAt(0)); // x coord
					  String CoordY = String.valueOf(attributes[7].charAt(1)); // y coord
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
					  if(myBoard.lost()) // you lost, end game
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

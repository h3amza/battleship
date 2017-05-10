/*
* This class defines Client side of the code that creates 2 threads for each player
* Instead of creating a separate thread class with data to be passed around upon creation, 
* the threads are generated for this class. 
*/

import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.lang.InterruptedException;

public class Client implements Runnable
{
	// network related objects
	public static Socket clientSocket;
	public static PrintStream outputStream;
	public static BufferedReader inputStream;
	public static BufferedReader input;

	public static Board myBoard;
    public static Board enemyBoard; // p1 p2 boards

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
				if(!gameMode) // if not game mode, waiting for player2, set up game and begin
				{
                    input.readLine();
					
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

					while(!inGame) // not your turn, wait for other player
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
	    while(true) {
            int X, Y;
            try {
                System.out.println("Enemy Board:\n");
                enemyBoard.printBoard();
                System.out.println("");

                System.out.println("Attack Coordinates:");
                while(true) {
                    System.out.print("X:");
                    X = Integer.parseInt(input.readLine());
                    if (X > 4 || X < 0) {
                        System.out.println("X value provided is outside bounds of board, please try again");
                        continue;
                    }

                    break;
                }

                while (true) {
                    System.out.print("Y:");
                    Y = Integer.parseInt(input.readLine());
                    if (Y > 4 || Y < 0) {
                        System.out.println("Y value provided is outside bounds of board, please try again");
                        continue;
                    }
                    break;
                }

                if(!enemyBoard.isEmpty(X,Y))
				{
					System.out.println("You have already attacked that square, please try again");
					continue;
				}

                outputStream.println("|attack " + enemyPlayer + " " + (Integer.toString(X) + Integer.toString(Y))); // send server request for attack
                break;
            } catch (IOException e) {
                System.out.println("Something went wrong");
                break;
            } catch (NumberFormatException e)
            {
                System.out.println("Non-numeric input, please try again");
            }
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
		while(true) {
            System.out.println("Enter X and Y coordinates(0-4)");
            myBoard.printBoard();
            while (true) {
                try {
                    System.out.println("Give X value:");
                    X = Integer.parseInt(input.readLine());

                    System.out.println("Give Y value:");
                    Y = Integer.parseInt(input.readLine());
                    // check if valid input or in bounds
                    if (X < 0 || X > 4) {
                        System.out.println("Invalid X value, please try again");
                        continue;
                    }
                    if (Y < 0 || Y > 4) {
                        System.out.println("Invalid Y value, please try again");
                        continue;
                    }
                    break;

                } catch (Exception e) {
                    System.out.println("Something went wrong,please try again");
                }
            }

            while (true) {
                try {
                    System.out.println("Do you want the Ship to be horizontal? (y/n)");
                    Orientation = input.readLine().toLowerCase();

                    switch (Orientation) {
                        case "y":
                            temp = new Ship(X, Y, true);
                            break;
                        case "n":
                            temp = new Ship(X, Y, false);
                            break;
                        default:
                            System.out.println("Invalid input please try again\n");
                            continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Something went wrong,please try again\n");
                }

            }
            if(myBoard.setShip(temp))
                break;
            else
                System.out.println("Trying to place ship in spot that is already occupied, please try again");
        }
	}

	public void printEnemyBoard()
	{
		System.out.println("\n\n\n\n\n");
		System.out.println("Enemy Board:");
		System.out.println("");
		enemyBoard.printBoard();
	}
	// thread invokes this function
	public void run() 
	{
	  	try 
	  	{
	      	String response;
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
					  	playerTurn=2; // both players now connected
				  	inGame=true;
			  	}
			  	if (response.contains("hit"))	// if this, then you hit enemy ship
			  	{
				  	String[] responseArray = response.split(" ");
				  	int x = Character.getNumericValue(responseArray[5].charAt(0));
				  	int y = Character.getNumericValue(responseArray[5].charAt(1));
				  	response ="You hit ship on coordinates " + responseArray[5].charAt(0) + " " +responseArray[5].charAt(1) + "\nPress Enter and wait for attack!";
				  	enemyBoard.markHit(x, y); // mark enemy board for attack
				  	enemyBoard.attack(x, y);
				  	printEnemyBoard();
				  	playerTurn=2; // toggle turn
			  	}
			  	if (response.contains("miss")) // if this, then you miss enemy ship
			  	{
				  	String[] responseArray = response.split(" ");
				  	int x = Character.getNumericValue(responseArray[4].charAt(0));
				  	int y = Character.getNumericValue(responseArray[4].charAt(1));
				  	response ="You missed! \nPress Enter and wait for attack!";
				  	enemyBoard.markMiss(x,y);
				  	enemyBoard.attack(x,y);
				  	printEnemyBoard();
				  	playerTurn=2; // toggle turn
			  	} 
			  	if (response.contains("You Win!")) // end of game
			  	{
				  	System.out.println("\n\n\n\n\n");
				  	outputStream.println("end");
			  	}
			  	if (response.contains("Attack from")) // attack enemy 
			  	{
				  	String[] responseArray = response.split(" ");
				  	int x = Character.getNumericValue(responseArray[5].charAt(0)); // x coord
				  	int y = Character.getNumericValue(responseArray[5].charAt(1)); // y coord
				  	boolean isAttack = myBoard.attack(x, y);
				  	if(isAttack)
				  	{
					  	outputStream.println("|hit " + enemyPlayer + " " + responseArray[5].charAt(0) + responseArray[5].charAt(1));
				  	}
				  	else
				  	{
					  	outputStream.println("|miss " + enemyPlayer + " " + responseArray[5].charAt(0) + responseArray[5].charAt(1));
				  	}
				  	System.out.println("\n\n\n\n\n");
				  	System.out.println("My Board:\n");
				  	myBoard.printBoard();
				  	response = "Attack from " + enemyPlayer + " on coordinates " + responseArray[5].charAt(0) + " " + responseArray[5].charAt(1);
				  	System.out.println(response);
				  	if(myBoard.lost()) // you lost, end game
				  	{
					  	response ="You Lost!";
					  	outputStream.println("|winner " + enemyPlayer);
					  	outputStream.println("|end");
				  	}
				  	else
				  	{
					  	response ="Hit Enter to Attack!";
					  	playerTurn=1;
				  	}
			  	}
			  	if(response.contains("End"))
			  		System.exit(0);
			  	System.out.println(response);
		  	}
		  	end = true;
	  	} 
	  	catch (IOException e) 
	  	{
		  	System.out.println("IOException:  " + e);
		  	System.exit(-1);
	  	}

	}
}

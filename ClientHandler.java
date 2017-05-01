/*
* This class defines ClientHandler to get communicate between server and client
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

class ClientHandler extends Thread 
{
	// network related objects
	private String clientName = null;
	private BufferedReader inputStream = null;
	private PrintStream outputStream = null;
	private Socket clientSocket = null;
	private final ClientHandler[] handler;
	private boolean isSetName = false;
	public static int playerValue = 0;
	
	public ClientHandler(Socket clientSocket, ClientHandler[] handler) 
	{
		this.clientSocket = clientSocket;
	    this.handler = handler;
	}
	
	// send win notification
	public void win(String from, String to) 
	{
		boolean send = false;
		for (int i = 0; i < 2; i++) // only two threads, one for each player
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You Win! Hit enter to leave the game!");
			 	send = true;
			  	break;
			}
		}
	}
	
	// send attack notification
	public void attack(String from, String to, String Coords)
	{
		boolean send = false;
		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) // check if this player by name
			{
			 	handler[i].outputStream.println("--- Attack from User " + from + " on Coordinates " + Coords);
			 	send = true;
			  	break;
			}
		}
	}

	// send hit enemy ship notification
	public void hitResponse(String from, String to, String Coords)
	{
		boolean send = false;
		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You hit users ship on Coordinates " + Coords);
			 	send = true;
			  	break;
			}
		}
	}
	
	// send miss enemy ship notification
	public void missResponse(String from, String to, String Coords)
	{
		boolean send = false;
		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You miss on Coordinates " + Coords);
			 	send = true;
			  	break;
			}
		}
	}
	
	// send game setup notification
	public void game(String from, String to)
	{
		boolean send = false;
		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("Challenge from " + from + ". Press Enter to continue:");
			 	this.outputStream.println(to + " challenged");
			 	send = true;
			  	break;
			}
		}
		if(send == false)
		{
			this.outputStream.println("--- User " + to + " does not exist!");
		}
	}
	
	// send game beginning notification
	public void setup(String from, String to)
	{
		boolean send = false;
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("Game On! Press Enter to continue:");
			 	send = true;
			  	break;
			}
		}
	}
	
	// send board ready notification
	public void ready(String from, String to)
	{
		boolean send = false;
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("Player Ready");
			 	send = true;
			  	break;
			}
		}
	}
	
	public void run() 
	{	
	    try 
	    {
	    	inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	outputStream = new PrintStream(clientSocket.getOutputStream());
	    	String name;
    		name = "Player"+Integer.toString(++playerValue); // for first connection, P1. for second, P2
    		this.isSetName = true;
	    	
	    	outputStream.println("You are " + name); // specify which player the user is
	    	
    		for (int i = 0; i < 2; i++) 
    		{
    			if (handler[i] != null && handler[i] == this) 
    			{
    				clientName = name; // set current name to P1
    				break;
    			}
    		}
    		for (int i = 0; i < 2; i++) 
    		{
    			if (handler[i] != null && handler[i] != this)  // if second player arrives, set up game
    			{
    				if(handler[i].isSetName)
    				{
    					handler[i].outputStream.println(name + " connected..");
    					game("Player2","Player1");
    				}
    			}
    		}
    		Boolean sentinel = true;
	    	while (sentinel) // always true until broken -- needs fixing
	    	{
	    		String line = inputStream.readLine(); // read server response
	    		System.out.println(line);
	    		if (line.startsWith("|")) // if start with | then predefined term
	    		{
	    			String command;
	    			String[] serverString = line.substring(1).split(" ",2); // split string on spaces
	    			command = serverString[0].trim();
    				if (!command.isEmpty()) 
    				{
    					switch(command)
    					{
    						case "end": // break on end, currently not working
    							break;
    						case "winner": // send win notification
    							win(name, serverString[1]);
    							break;
    						case "setup": // set up game 
	    						setup(name, serverString[1]);
	    						break;
	    					case "ready": // player board now filled with ships
	    						ready(name, serverString[1]);
	    						break;	
    						case "attack": // player has attacked
    							if(serverString.length == 2)
	    						{
	    							serverString = serverString[1].split(" ",2);
	    							attack(name, serverString[0], serverString[1]);
	    						}
	    						break;
	    					case "hit": // in case of hit
	    						if(serverString.length == 2)
	    						{
	    							serverString = serverString[1].split(" ",2);
	    							hitResponse(name, serverString[0], serverString[1]);
	    						}
	    						break;
	    					case "miss": // in case of miss
	    						if(serverString.length == 2)
	    						{
	    							serverString = serverString[1].split(" ",2);
	    							missResponse(name, serverString[0], serverString[1]);
	    						}
	    						break;
	    					default:
	    						break;
    					}
    				}
    				else
    				{
    					this.outputStream.println("Something went wrong"); // this should not happen
    				}
	    		}
	    		else
	    		{
	    			this.outputStream.println("Something went wrong");
	    		}
	    	}
	    	inputStream.close();
	    	outputStream.close();
	    	clientSocket.close();
	    } 
	    catch (IOException e) 
	    {
	    	System.out.println("Something went wrong");
	    }
	}
}

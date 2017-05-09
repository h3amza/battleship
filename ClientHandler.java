/*
* This class defines ClientHandler to get communicate between server and client
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread
{
	// network related objects
	private String clientName = null;
    private PrintStream outputStream = null;
	private Socket clientSocket = null;
	private final ClientHandler[] handler;
	private boolean isSetName = false;
	private static int playerValue = 0;
	
	public ClientHandler(Socket clientSocket, ClientHandler[] handler)
	{
		this.clientSocket = clientSocket;
	    this.handler = handler;
	}

	private void end()
	{
		for (int i = 0; i < 2; i++) // only two threads, one for each player
		{
			handler[i].outputStream.println("--- End");
		}
	}
	// send win notification
    private void win(String to)
	{
		for (int i = 0; i < 2; i++) // only two threads, one for each player
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You Win! Hit enter to leave the game!");
                break;
			}
		}
	}
	
	// send attack notification
    private void attack(String from, String to, String Coords)
	{

		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) // check if this player by name
			{
			 	handler[i].outputStream.println("--- Attack from User " + from + " on Coordinates " + Coords);
                break;
			}
		}
	}

	// send hit enemy ship notification
    private void hitResponse(String to, String Coords)
	{

		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You hit users ship on Coordinates " + Coords);
                break;
			}
		}
	}
	
	// send miss enemy ship notification
    private void missResponse(String to, String Coords)
	{

		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You miss on Coordinates " + Coords);
                break;
			}
		}
	}
	
	// send game setup notification
    private void game(String from, String to)
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
		if(!send)
		{
			this.outputStream.println("--- User " + to + " does not exist!");
		}
	}
	
	// send game beginning notification
    private void setup(String to)
	{
		for (int i = 0; i < 2; i++)
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("Game On! Press Enter to continue:");
                break;
			}
		}
	}
	
	// send board ready notification
    private void ready(String to)
	{
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("Player Ready");
                break;
			}
		}
	}
	
	public void run() 
	{	
	    try 
	    {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
                                sentinel = false;
    							end();
    							break;
    						case "winner": // send win notification
    							win(serverString[1]);
    							break;
    						case "setup": // set up game 
	    						setup(serverString[1]);
	    						break;
	    					case "ready": // player board now filled with ships
	    						ready(serverString[1]);
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
	    							hitResponse(serverString[0], serverString[1]);
	    						}
	    						break;
	    					case "miss": // in case of miss
	    						if(serverString.length == 2)
	    						{
	    							serverString = serverString[1].split(" ",2);
	    							missResponse(serverString[0], serverString[1]);
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

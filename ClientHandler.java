/*
* This class defines ClientHandler to get communicate between server and client.
* It looks at the string sent to the server, parses it, and makes decisions based
* on the parsed string.
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread
{
	// network related objects
	public String clientName;
    public PrintStream outputStream;
	public Socket clientSocket;
	public final ClientHandler[] handler;
	public boolean isSetName = false;
	public static int playerValue = 0;
	
	public ClientHandler(Socket clientSocket, ClientHandler[] handler)
	{
		this.clientSocket = clientSocket;
	    this.handler = handler;
	}

	// send game setup notification
    public void game(String from, String to)
	{	
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("Challenge from " + from + ". Press Enter to continue:");
			 	this.outputStream.println(to + " challenged");
			  	break;
			}
		}
	}
	
	// send game beginning notification
    public void setup(String to)
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
    public void ready(String to)
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
	
	public void end()
	{
		for (int i = 0; i < 2; i++) // only two threads, one for each player
		{
			handler[i].outputStream.println("End");
		}
		System.exit(0);
	}

	// send win notification
    public void win(String to)
	{
		for (int i = 0; i < 2; i++) // only two threads, one for each player
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("You Win!");
                break;
			}
		}
	}
	
	// send attack notification
    public void attack(String from, String to, String coords)
	{

		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) // check if this player by name
			{
			 	handler[i].outputStream.println("Attack from " + from + " on coordinates " + coords);
                break;
			}
		}
	}

	// send hit enemy ship notification
    public void hitResponse(String to, String coords)
	{

		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("You hit ship on coordinates " + coords);
                break;
			}
		}
	}
	
	// send miss enemy ship notification
    public void missResponse(String to, String coords)
	{

		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("You missed on coordinates " + coords);
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
	    		System.out.println(line); // prints to server console
    			String[] serverString = line.split(" "); // split string on spaces
				if (!serverString[0].isEmpty()) 
				{
					switch(serverString[0])
					{
						case "|end": // break on end, currently not working
                            sentinel = false;
							end();
							break;
						case "|winner": // send win notification
							win(serverString[1]);
							break;
						case "|setup": // set up game 
    						setup(serverString[1]);
    						break;
    					case "|ready": // player board now filled with ships
    						ready(serverString[1]);
    						break;	
						case "|attack": // player has attacked
    						attack(name, serverString[1], serverString[2]);
    						break;
    					case "|hit": // in case of hit
    						hitResponse(serverString[1], serverString[2]);
    						break;
    					case "|miss": // in case of miss
    						missResponse(serverString[1], serverString[2]);
    						break;
    					default:
    						break;
					}
				}
				else
				{
					this.outputStream.println("Something went wrong 1"); // this should not happen
				}
    		
	    	}
	    	clientSocket.close();
	    } 
	    catch (IOException e) 
	    {
	    	System.out.println("Something went wrong");
	    	e.printStackTrace();
	    }
	}
}

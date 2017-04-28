import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

class ClientHandler extends Thread 
{
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
	
	public void win(String from, String to)
	{
		boolean send = false;
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- You Win! Hit enter to leave the game!");
			 	send = true;
			  	break;
			}
		}
	}
	
	public void attack(String from, String to, String Coords)
	{
		boolean send = false;
		
		for (int i = 0; i < 2; i++) 
		{
			if (handler[i].clientName.equals(to)) 
			{
			 	handler[i].outputStream.println("--- Attack from User " + from + " on Coordinates " + Coords);
			 	send = true;
			  	break;
			}
		}
	}
	
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
    		name = "Player"+Integer.toString(++playerValue);
    		this.isSetName = true;
	    	
	    	outputStream.println("You are " + name);
	    	
    		for (int i = 0; i < 2; i++) 
    		{
    			if (handler[i] != null && handler[i] == this) 
    			{
    				clientName = name;
    				break;
    			}
    		}
    		for (int i = 0; i < 2; i++) 
    		{
    			if (handler[i] != null && handler[i] != this) 
    			{
    				if(handler[i].isSetName)
    				{
    					handler[i].outputStream.println(name + " connected..");
    					game("Player2","Player1");
    				}
    			}
    		}
    		Boolean sentinel = true;
	    	while (sentinel) 
	    	{
	    		String line = inputStream.readLine();
	    		System.out.println(line);
	    		if (line.startsWith("|")) 
	    		{
	    			String command;
	    			String[] serverString = line.substring(1).split(" ",2);
	    			command = serverString[0].trim();
    				if (!command.isEmpty()) 
    				{
    					switch(command)
    					{
    						case "end":
    							break;
    						case "winner":
    							win(name, serverString[1]);
    							break;
    						case "setup":
	    						setup(name, serverString[1]);
	    						break;
	    					case "ready":
	    						ready(name, serverString[1]);
	    						break;	
    						case "attack":
    							if(serverString.length == 2)
	    						{
	    							serverString = serverString[1].split(" ",2);
	    							attack(name, serverString[0], serverString[1]);
	    						}
	    						break;
	    					case "hit":
	    						if(serverString.length == 2)
	    						{
	    							serverString = serverString[1].split(" ",2);
	    							hitResponse(name, serverString[0], serverString[1]);
	    						}
	    						break;
	    					case "miss":
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
    					this.outputStream.println("Something went wrong");
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
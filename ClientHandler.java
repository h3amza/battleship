/*
 * A combination of client handler and client listener. 
 * Defines all functions to play the game and also displays data to client screen.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler extends Thread 
{

    private String clientName = null;
    private BufferedReader inputStream = null;
    private PrintStream outputStream = null;
    private Socket clientSocket = null;
    private final ClientHandler[] handlers;
    private int maxClientConnections;

    public ClientHandler(Socket connectionSock, ClientHandler[] handlers) 
    {
        this.clientSocket = connectionSock;
        this.handlers = handlers;
        maxClientConnections = handlers.length;
    }
    
    public void sendWin(String fromClient, String toClient)
    {
        boolean send = false;
        synchronized (this)
        {
            for(int i = 0; i < maxClientConnections; i++)
            {
                if(handlers[i] != null && handlers[i].clientName != null && handlers[i] != this && handlers[i].clientName.equals(toClient))
                {
                    handlers[i].outputStream.println("You Win!");
                    send = true;
                    break;
                }
            }
        }
        if(send == false)
        {
            this.outputStream.println("Client " + toClient + " does not exist!");
        }
    }

    public void sendAttackCoordinates(String fromName, String toName, String Coords)
    {
        boolean send = false;
        synchronized (this)
        {
            for (int i = 0; i < maxClientConnections; i++)
            {
                if (handlers[i] != null && handlers[i].clientName != null &&
                        handlers[i] != this && handlers[i].clientName.equals(toName))
                {
                    handlers[i].outputStream.println("*** Attack from User " + fromName + " on Coordinates " + Coords);
                    send = true;
                    break;
                }
            }
        }
        if(send == false)
        {
            this.outputStream.println("*** User " + toName + " does not exist!");
        }
    }

    public void sendResponseAttackYes(String fromName, String toName, String Coords)
    {
        boolean send = false;
        synchronized (this)
        {
            for (int i = 0; i < maxClientConnections; i++)
            {
                if (handlers[i] != null && handlers[i].clientName != null &&
                        handlers[i] != this && handlers[i].clientName.equals(toName))
                {
                    handlers[i].outputStream.println("*** You hit users ship on Coordinates " + Coords);
                    send = true;
                    break;
                }
            }
        }
        if(send == false)
        {
            this.outputStream.println("*** User " + toName + " does not exist!");
        }
    }

    public void sendResponseAttackNo(String fromName, String toName, String Coords)
    {
        boolean send = false;
        synchronized (this)
        {
            for (int i = 0; i < maxClientConnections; i++)
            {
                if (handlers[i] != null && handlers[i].clientName != null &&
                        handlers[i] != this && handlers[i].clientName.equals(toName))
                {
                    handlers[i].outputStream.println("*** You miss on Coordinates " + Coords);
                    send = true;
                    break;
                }
            }
        }
        if(send == false)
        {
            this.outputStream.println("*** User " + toName + " does not exist!");
        }
    }


    public void run()
    {
        try
        {
	    	/*
	    	 * Input and output streams for this client.
	    	 */
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new PrintStream(clientSocket.getOutputStream());
	        String name = "Client 1";


	    	/* Start the interaction. */
            while (true)
            {
                String line = inputStream.readLine();
                if (line.startsWith("/"))
                {
                    String command;
                    String[] attributes = line.substring(1).split(" ",2);
                    command = attributes[0].trim();
                    if (!command.isEmpty())
                    {
                        if(command.equals("quit"))
                        {
                            break;
                        }
                        else if(command.equals("youWin"))
                        {
                            if(attributes.length == 2 && !attributes[1].isEmpty())
                            {
                                sendWin(name, attributes[1]);
                            }
                        }
                        else if(command.equals("attack"))
                        {
                            if(attributes.length == 2)
                            {
                                attributes = attributes[1].split(" ",2);
                                if (attributes.length == 2 && !attributes[0].isEmpty() && !attributes[1].isEmpty())
                                {
                                    sendAttackCoordinates(name, attributes[0], attributes[1]);
                                }
                            }
                        }
                        else if(command.equals("responseAttackYes"))
                        {
                            if(attributes.length == 2)
                            {
                                attributes = attributes[1].split(" ",2);
                                if (attributes.length == 2 && !attributes[0].isEmpty() && !attributes[1].isEmpty())
                                {
                                    sendResponseAttackYes(name, attributes[0], attributes[1]);
                                }
                            }
                        }
                        else if(command.equals("responseAttackNo"))
                        {
                            if(attributes.length == 2)
                            {
                                attributes = attributes[1].split(" ",2);
                                if (attributes.length == 2 && !attributes[0].isEmpty() && !attributes[1].isEmpty())
                                {
                                    sendResponseAttackNo(name, attributes[0], attributes[1]);
                                }
                            }
                        }

                        else
                        {
                            this.outputStream.println("Error");
                        }
                    }
                    else
                    {
                        this.outputStream.println("Error");
                    }
                }
                else
                {
                    this.outputStream.println("Error");
                }
            }

            inputStream.close();
            outputStream.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
        }
    }
}

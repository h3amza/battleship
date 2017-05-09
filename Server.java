/*
* This class defines Server that starts Client handler threads and communicates responses
*/

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server
{
	private static ServerSocket serverSocket = null;
    private static final ClientHandler[] handler = new ClientHandler[2]; // one handler for each player
	private static final int port = 7654;


	public static void main(String args[]) 
	{
		
		try 
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Connection made");
		} 
		catch (IOException e) 
		{
			System.out.println("Something went wrong");
			e.printStackTrace();
			System.exit(-1);
		}

		while (true) 
		{
			try 
			{
                Socket clientSocket = serverSocket.accept();
				int i;
				for (i = 0; i < 2; i++) 
				{
					if (handler[i] == null) 
					{
						(handler[i] = new ClientHandler(clientSocket, handler)).start(); // create handler and start
						break;
					}
				}	
			}
			catch (IOException e) 
			{
				System.out.println("Something went wrong");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
